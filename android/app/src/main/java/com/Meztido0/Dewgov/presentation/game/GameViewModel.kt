package com.Meztido0.Dewgov.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.core.config.GameConfig
import com.Meztido0.Dewgov.core.format.formatChips
import com.Meztido0.Dewgov.core.format.formatStreak
import com.Meztido0.Dewgov.domain.model.CrewMember
import com.Meztido0.Dewgov.domain.model.District
import com.Meztido0.Dewgov.domain.model.EndingCause
import com.Meztido0.Dewgov.domain.model.JobOutcome
import com.Meztido0.Dewgov.domain.model.Mission
import com.Meztido0.Dewgov.domain.model.NightRun
import com.Meztido0.Dewgov.domain.model.Reputation
import com.Meztido0.Dewgov.domain.model.UpgradeIds
import com.Meztido0.Dewgov.domain.repository.CityRepository
import com.Meztido0.Dewgov.domain.repository.EvidenceRepository
import com.Meztido0.Dewgov.domain.repository.ProgressRepository
import com.Meztido0.Dewgov.domain.repository.UpgradeRepository
import com.Meztido0.Dewgov.domain.usecase.ApplyMissionChoiceUseCase
import com.Meztido0.Dewgov.domain.usecase.GenerateNightUseCase
import com.Meztido0.Dewgov.domain.usecase.NightSetup
import com.Meztido0.Dewgov.domain.usecase.PatrolSweepUseCase
import com.Meztido0.Dewgov.domain.usecase.ResolveEndingUseCase
import com.Meztido0.Dewgov.domain.usecase.ResolveJobUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel(
    private val cityRepository: CityRepository,
    private val progressRepository: ProgressRepository,
    private val upgradeRepository: UpgradeRepository,
    private val evidenceRepository: EvidenceRepository,
    private val generateNight: GenerateNightUseCase,
    private val resolveJob: ResolveJobUseCase,
    private val patrolSweep: PatrolSweepUseCase,
    private val applyMissionChoice: ApplyMissionChoiceUseCase,
    private val resolveEnding: ResolveEndingUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val eventChannel = Channel<GameEvent>(Channel.BUFFERED)
    val events: Flow<GameEvent> = eventChannel.receiveAsFlow()

    private var token = -1
    private var run: NightRun? = null
    private var rng: Random = Random(1)
    private var loopJob: Job? = null
    private var band: Reputation = Reputation.UNALIGNED
    private var armoredTrunk = false
    private var vaultCodeKnown = false
    private var selectedIndex = -1
    private var selectedCrewId = -1

    fun beginNight(newToken: Int) {
        if (token == newToken) return
        token = newToken
        loopJob?.cancel()
        startNight()
    }

    fun pause() {
        loopJob?.cancel()
    }

    private fun startNight() {
        val progress = progressRepository.load()
        val owned = upgradeRepository.owned()
        band = progress.band
        armoredTrunk = owned.contains(UpgradeIds.ARMORED_TRUNK)
        vaultCodeKnown = evidenceRepository.pins().size >= GameConfig.VAULT_CODE_PINS
        val setup = NightSetup(
            nightIndex = progress.nightsRun,
            moves = GameConfig.NIGHT_MOVES + if (owned.contains(UpgradeIds.NITROUS_INJECTOR)) 1 else 0,
            heatCap = if (owned.contains(UpgradeIds.CHROME_PLATING)) {
                GameConfig.HEAT_CAP_PLATED
            } else {
                GameConfig.HEAT_CAP_BASE
            },
            decodeCharges = GameConfig.DECODE_CHARGES +
                if (owned.contains(UpgradeIds.SIGNAL_SCRAMBLER)) 1 else 0,
            banked = 0,
            missionIds = cityRepository.missions().map { it.id }
        )
        val night = generateNight(cityRepository.districts(), cityRepository.crew(), setup)
        rng = Random(night.seed)
        val swept = patrolSweep(night.districts, night.currentIndex, 0, rng)
        run = night.copy(districts = swept)
        selectedIndex = -1
        selectedCrewId = -1
        publish(autoSelect = true, phase = GamePhase.PLANNING, resolve = null)
    }

    fun selectDistrict(index: Int) {
        val night = run ?: return
        if (_state.value.phase != GamePhase.PLANNING) return
        if (index !in reachable(night)) return
        selectedIndex = index
        selectedCrewId = pickCrew(night, index)
        publish(autoSelect = false, phase = GamePhase.PLANNING, resolve = null)
    }

    fun selectCrew(id: Int) {
        val night = run ?: return
        if (_state.value.phase != GamePhase.PLANNING) return
        val member = night.crew.firstOrNull { it.id == id } ?: return
        if (!canLead(night, member)) return
        selectedCrewId = id
        publish(autoSelect = false, phase = GamePhase.PLANNING, resolve = null)
    }

    fun decode() {
        val night = run ?: return
        if (!_state.value.canDecode) return
        val target = selectedIndex
        run = night.copy(
            decodeCharges = night.decodeCharges - 1,
            districts = night.districts.map { if (it.index == target) it.copy(decoded = true) else it }
        )
        publish(autoSelect = false, phase = GamePhase.PLANNING, resolve = null)
    }

    fun bank() {
        val night = run ?: return
        if (!_state.value.canBank) return
        val amount = night.take
        val banked = night.copy(
            banked = night.banked + amount,
            take = 0,
            heat = night.heat + GameConfig.BANK_HEAT_COST,
            movesLeft = night.movesLeft - 1
        )
        run = banked
        publish(
            autoSelect = false,
            phase = GamePhase.RESOLVING,
            resolve = ResolveUi(JobOutcome.CLEAN, formatChips(amount), banking = true)
        )
        loopJob?.cancel()
        loopJob = viewModelScope.launch {
            delay(GameConfig.RESOLVE_CARD_MS)
            afterMove()
        }
    }

    fun go() {
        val night = run ?: return
        if (!_state.value.canGo) return
        val target = night.districts.firstOrNull { it.index == selectedIndex } ?: return
        val lead = night.crew.firstOrNull { it.id == selectedCrewId } ?: return
        publish(autoSelect = false, phase = GamePhase.RESOLVING, resolve = null)
        loopJob?.cancel()
        loopJob = viewModelScope.launch {
            delay(GameConfig.RESOLVE_MOVE_MS)
            val roll = rng.nextInt(100)
            val outcome = resolveJob(
                district = target,
                crew = lead,
                heat = night.heat,
                streakX10 = night.streakX10,
                band = band,
                armoredTrunk = armoredTrunk,
                roll = roll
            )
            val nerveCost = GameConfig.NERVE_LEAD_COST +
                if (outcome.outcome == JobOutcome.BURNED) GameConfig.NERVE_BURN_EXTRA else 0
            val updatedCrew = night.crew.map { member ->
                if (member.id == lead.id) {
                    member.copy(nerve = (member.nerve - nerveCost).coerceAtLeast(0))
                } else {
                    member.copy(
                        nerve = (member.nerve + GameConfig.NERVE_BENCH_GAIN)
                            .coerceAtMost(GameConfig.NERVE_MAX)
                    )
                }
            }
            val nextStreak = outcome.streakX10
            val moved = night.copy(
                currentIndex = target.index,
                take = night.take + outcome.gain,
                heat = night.heat + outcome.heatGain,
                streakX10 = nextStreak,
                bestStreakX10 = if (nextStreak > night.bestStreakX10) nextStreak else night.bestStreakX10,
                movesLeft = night.movesLeft - 1,
                districtsHit = night.districtsHit +
                    if (outcome.outcome == JobOutcome.BURNED) 0 else 1,
                newPins = if (outcome.pinId >= 0 && !night.newPins.contains(outcome.pinId)) {
                    night.newPins + outcome.pinId
                } else {
                    night.newPins
                },
                crew = updatedCrew,
                districts = night.districts.map {
                    if (it.index == target.index) it.copy(visited = true, decoded = true) else it
                }
            )
            run = moved
            publish(
                autoSelect = false,
                phase = GamePhase.RESOLVING,
                resolve = ResolveUi(outcome.outcome, formatChips(outcome.gain))
            )
            delay(GameConfig.RESOLVE_CARD_MS)
            if (moved.heat >= moved.heatCap) {
                endNight(EndingCause.BUST)
                return@launch
            }
            if (target.isVault) {
                endNight(EndingCause.BREACH)
                return@launch
            }
            val mission = cityRepository.missions().firstOrNull { it.id == target.missionId }
            if (mission != null && outcome.outcome != JobOutcome.BURNED) {
                publish(autoSelect = false, phase = GamePhase.MISSION, resolve = null, mission = mission)
                return@launch
            }
            afterMove()
        }
    }

    fun chooseMission(first: Boolean) {
        val night = run ?: return
        val mission = _state.value.mission ?: return
        val choice = if (first) mission.choiceA else mission.choiceB
        val updated = applyMissionChoice(night, choice)
        run = updated
        loopJob?.cancel()
        loopJob = viewModelScope.launch {
            if (updated.heat >= updated.heatCap) {
                endNight(EndingCause.BUST)
            } else {
                afterMove()
            }
        }
    }

    fun openAbort() {
        if (_state.value.phase != GamePhase.PLANNING) return
        _state.update { it.copy(abortVisible = true) }
    }

    fun dismissAbort() {
        _state.update { it.copy(abortVisible = false) }
    }

    fun walkAway() {
        _state.update { it.copy(abortVisible = false) }
        loopJob?.cancel()
        loopJob = viewModelScope.launch { endNight(EndingCause.WALK_AWAY) }
    }

    private suspend fun afterMove() {
        val night = run ?: return
        if (night.movesLeft <= 0) {
            endNight(EndingCause.OUT_OF_MOVES)
            return
        }
        val swept = patrolSweep(night.districts, night.currentIndex, night.heat, rng)
        run = night.copy(districts = swept)
        selectedIndex = -1
        selectedCrewId = -1
        publish(autoSelect = true, phase = GamePhase.PLANNING, resolve = null)
    }

    private suspend fun endNight(cause: EndingCause) {
        val night = run ?: return
        val progress = progressRepository.load()
        val reputationAfter = (progress.reputation + night.repDelta).coerceIn(-6, 6)
        val result = resolveEnding(night, cause, reputationAfter, vaultCodeKnown)
        evidenceRepository.unlock(result.newPins)
        val pinsAfter = evidenceRepository.pins()
        progressRepository.save(
            progress.copy(
                bankedChips = progress.bankedChips + result.banked,
                bestNight = if (result.banked > progress.bestNight) result.banked else progress.bestNight,
                nightsRun = progress.nightsRun + 1,
                reputation = reputationAfter,
                endingsSeen = progress.endingsSeen + result.endingId,
                vaultCodeKnown = pinsAfter.size >= GameConfig.VAULT_CODE_PINS
            )
        )
        _state.update {
            it.copy(
                phase = GamePhase.ENDING,
                mission = null,
                abortVisible = false,
                endingTintBusted = cause == EndingCause.BUST
            )
        }
        delay(GameConfig.ENDING_FLASH_MS)
        eventChannel.send(GameEvent.NightOver(result))
    }

    private fun reachable(night: NightRun): Set<Int> =
        GenerateNightUseCase.neighboursOf(night.currentIndex)
            .filter { !night.districts[it].lockedByPatrol }
            .toSet()

    private fun canLead(night: NightRun, member: CrewMember): Boolean {
        if (member.nerve >= GameConfig.NERVE_MIN_TO_LEAD) return true
        return night.crew.none { it.nerve >= GameConfig.NERVE_MIN_TO_LEAD }
    }

    private fun pickDistrict(night: NightRun, options: Set<Int>): Int {
        if (options.isEmpty()) return -1
        val plain = options.filter { !night.districts[it].isVault }
        val pool = if (plain.isEmpty()) options.toList() else plain
        return pool.maxByOrNull { index ->
            val district = night.districts[index]
            val revisit = if (district.visited) district.payout / 2 else district.payout
            revisit - district.risk * 70
        } ?: pool.first()
    }

    private fun pickCrew(night: NightRun, districtIndex: Int): Int {
        val target = night.districts.firstOrNull { it.index == districtIndex }
        val available = night.crew.filter { canLead(night, it) }
        if (available.isEmpty()) return -1
        val matching = available.filter { target != null && it.jobType == target.jobType }
        val pool = if (matching.isEmpty()) available else matching
        return (pool.maxByOrNull { it.nerve } ?: pool.first()).id
    }

    private fun publish(
        autoSelect: Boolean,
        phase: GamePhase,
        resolve: ResolveUi?,
        mission: Mission? = null
    ) {
        val night = run ?: return
        val options = reachable(night)
        if (autoSelect || selectedIndex !in options) {
            selectedIndex = pickDistrict(night, options)
        }
        if (autoSelect || night.crew.none { it.id == selectedCrewId && canLead(night, it) }) {
            selectedCrewId = pickCrew(night, selectedIndex)
        }
        val selected = night.districts.firstOrNull { it.index == selectedIndex }
        val current = night.districts.firstOrNull { it.index == night.currentIndex }
        val nodes = night.districts.map { district -> toNode(district, night, options) }
        val crewSlots = night.crew.map { member ->
            CrewSlot(
                id = member.id,
                nameRes = member.nameRes,
                jobType = member.jobType,
                nerve = member.nerve,
                enabled = canLead(night, member) && phase == GamePhase.PLANNING,
                selected = member.id == selectedCrewId,
                matchesTarget = selected != null && selected.jobType == member.jobType
            )
        }
        val planning = phase == GamePhase.PLANNING
        val usedMoves = night.movesTotal - night.movesLeft
        val heatShown = night.heat.coerceIn(0, night.heatCap)
        val hint = when {
            phase == GamePhase.RESOLVING -> R.string.hint_resolving
            selected != null && selected.isVault -> R.string.hint_vault
            current != null && current.isFence && night.take > 0 -> R.string.hint_fence
            else -> R.string.hint_plan
        }
        _state.update {
            it.copy(
                ready = true,
                phase = phase,
                seed = night.seed,
                nodes = nodes,
                crew = crewSlots,
                currentIndex = night.currentIndex,
                selectedIndex = selectedIndex,
                movesLeft = night.movesLeft,
                movesTotal = night.movesTotal,
                moveNumber = (usedMoves + 1).coerceAtMost(night.movesTotal),
                clockHour = (GameConfig.NIGHT_START_HOUR + usedMoves) % 24,
                heat = heatShown,
                heatCap = night.heatCap,
                takeText = formatChips(night.take),
                takeValue = night.take,
                bankedText = formatChips(night.banked),
                streakText = formatStreak(night.streakX10),
                streakX10 = night.streakX10,
                decodeCharges = night.decodeCharges,
                canDecode = planning && night.decodeCharges > 0 &&
                    selected != null && !selected.decoded,
                canGo = planning && selectedIndex >= 0 && selectedCrewId >= 0,
                canBank = planning && current != null && current.isFence &&
                    night.take > 0 && night.movesLeft > 0,
                hintRes = hint,
                resolve = resolve,
                mission = mission,
                endingTintBusted = false
            )
        }
    }

    private fun toNode(district: District, night: NightRun, options: Set<Int>): NodeUi {
        val nodeState = when {
            district.index == night.currentIndex -> NodeState.CURRENT
            district.index == selectedIndex -> NodeState.SELECTED
            district.lockedByPatrol -> NodeState.LOCKED
            district.index in options -> NodeState.REACHABLE
            district.visited -> NodeState.VISITED
            else -> NodeState.IDLE
        }
        return NodeUi(
            index = district.index,
            nameRes = district.nameRes,
            row = district.row,
            col = district.col,
            jobType = district.jobType,
            isVault = district.isVault,
            isFence = district.isFence,
            risk = district.risk,
            riskLow = (district.risk - 1).coerceAtLeast(1),
            riskHigh = (district.risk + 1).coerceAtMost(5),
            payout = if (district.visited) district.payout / 2 else district.payout,
            decoded = district.decoded,
            hasMission = district.missionId >= 0 && district.decoded,
            patrolTarget = district.patrolTarget,
            state = nodeState
        )
    }
}
