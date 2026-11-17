package com.Meztido0.Dewgov.domain.repository

import com.Meztido0.Dewgov.domain.model.CrewMember
import com.Meztido0.Dewgov.domain.model.DistrictBlueprint
import com.Meztido0.Dewgov.domain.model.Dossier
import com.Meztido0.Dewgov.domain.model.EndingInfo
import com.Meztido0.Dewgov.domain.model.Mission
import com.Meztido0.Dewgov.domain.model.Upgrade

interface CityRepository {
    fun districts(): List<DistrictBlueprint>
    fun crew(): List<CrewMember>
    fun missions(): List<Mission>
    fun upgrades(): List<Upgrade>
    fun dossiers(): List<Dossier>
    fun endings(): List<EndingInfo>
}
