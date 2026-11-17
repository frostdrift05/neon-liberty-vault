package com.Meztido0.Dewgov.data.sample

import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.domain.model.CrewMember
import com.Meztido0.Dewgov.domain.model.DistrictBlueprint
import com.Meztido0.Dewgov.domain.model.Dossier
import com.Meztido0.Dewgov.domain.model.EndingIds
import com.Meztido0.Dewgov.domain.model.EndingInfo
import com.Meztido0.Dewgov.domain.model.JobType
import com.Meztido0.Dewgov.domain.model.Mission
import com.Meztido0.Dewgov.domain.model.MissionChoice
import com.Meztido0.Dewgov.domain.model.Upgrade
import com.Meztido0.Dewgov.domain.model.UpgradeIds

object SampleData {

    val districts: List<DistrictBlueprint> = listOf(
        DistrictBlueprint(0, R.string.district_0, 0, 0, JobType.WIRES, false, false, 2, 4),
        DistrictBlueprint(1, R.string.district_1, 0, 1, JobType.MUSCLE, false, false, 3, 5),
        DistrictBlueprint(2, R.string.district_2, 0, 2, JobType.WORDS, false, false, 2, 4),
        DistrictBlueprint(3, R.string.district_3, 1, 0, JobType.WHEELS, false, false, 1, 3),
        DistrictBlueprint(4, R.string.district_4, 1, 1, JobType.VAULT, true, false, 5, 5),
        DistrictBlueprint(5, R.string.district_5, 1, 2, JobType.WIRES, false, false, 3, 5),
        DistrictBlueprint(6, R.string.district_6, 2, 0, JobType.WORDS, false, true, 1, 3),
        DistrictBlueprint(7, R.string.district_7, 2, 1, JobType.WHEELS, false, false, 1, 2),
        DistrictBlueprint(8, R.string.district_8, 2, 2, JobType.MUSCLE, false, true, 2, 4)
    )

    val crew: List<CrewMember> = listOf(
        CrewMember(0, R.string.crew_0, JobType.WHEELS, 100),
        CrewMember(1, R.string.crew_1, JobType.WIRES, 100),
        CrewMember(2, R.string.crew_2, JobType.MUSCLE, 100),
        CrewMember(3, R.string.crew_3, JobType.WORDS, 100)
    )

    val missions: List<Mission> = listOf(
        Mission(
            id = 0,
            titleRes = R.string.mission_0_title,
            bodyRes = R.string.mission_0_body,
            choiceA = MissionChoice(R.string.mission_0_a, 1100, 14, -2, 9),
            choiceB = MissionChoice(R.string.mission_0_b, 0, -10, 2, 10)
        ),
        Mission(
            id = 1,
            titleRes = R.string.mission_1_title,
            bodyRes = R.string.mission_1_body,
            choiceA = MissionChoice(R.string.mission_1_a, 0, -15, 2, 11),
            choiceB = MissionChoice(R.string.mission_1_b, 900, 8, -2, 12)
        ),
        Mission(
            id = 2,
            titleRes = R.string.mission_2_title,
            bodyRes = R.string.mission_2_body,
            choiceA = MissionChoice(R.string.mission_2_a, 400, 0, 3, 13),
            choiceB = MissionChoice(R.string.mission_2_b, 1200, 15, -3, 14)
        )
    )

    val upgrades: List<Upgrade> = listOf(
        Upgrade(UpgradeIds.CHROME_PLATING, R.string.upgrade_0_name, R.string.upgrade_0_effect, 1200),
        Upgrade(UpgradeIds.SIGNAL_SCRAMBLER, R.string.upgrade_1_name, R.string.upgrade_1_effect, 1500),
        Upgrade(UpgradeIds.NITROUS_INJECTOR, R.string.upgrade_2_name, R.string.upgrade_2_effect, 2000),
        Upgrade(UpgradeIds.ARMORED_TRUNK, R.string.upgrade_3_name, R.string.upgrade_3_effect, 2500)
    )

    val dossiers: List<Dossier> = listOf(
        Dossier(0, R.string.pin_0, R.string.hint_0, 9),
        Dossier(1, R.string.pin_1, R.string.hint_1, 13),
        Dossier(2, R.string.pin_2, R.string.hint_2, 4),
        Dossier(3, R.string.pin_3, R.string.hint_3, 7),
        Dossier(4, R.string.pin_4, R.string.hint_4, 2),
        Dossier(5, R.string.pin_5, R.string.hint_5, 11),
        Dossier(6, R.string.pin_6, R.string.hint_6, 8),
        Dossier(7, R.string.pin_7, R.string.hint_7, 3),
        Dossier(8, R.string.pin_8, R.string.hint_8, 6),
        Dossier(9, R.string.pin_9, R.string.hint_9, 10),
        Dossier(10, R.string.pin_10, R.string.hint_10, 0),
        Dossier(11, R.string.pin_11, R.string.hint_11, 12),
        Dossier(12, R.string.pin_12, R.string.hint_12, 5),
        Dossier(13, R.string.pin_13, R.string.hint_13, 14),
        Dossier(14, R.string.pin_14, R.string.hint_14, 1)
    )

    val endings: List<EndingInfo> = listOf(
        EndingInfo(EndingIds.BUSTED, R.string.ending_0_title, R.string.ending_0_text),
        EndingInfo(EndingIds.GHOST_EXIT, R.string.ending_1_title, R.string.ending_1_text),
        EndingInfo(EndingIds.CROWN_TAKE, R.string.ending_2_title, R.string.ending_2_text),
        EndingInfo(EndingIds.BURNED_DEAL, R.string.ending_3_title, R.string.ending_3_text),
        EndingInfo(EndingIds.EMPTY_VAULT, R.string.ending_4_title, R.string.ending_4_text)
    )
}
