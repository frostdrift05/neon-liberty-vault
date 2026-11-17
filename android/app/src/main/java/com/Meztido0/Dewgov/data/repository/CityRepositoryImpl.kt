package com.Meztido0.Dewgov.data.repository

import com.Meztido0.Dewgov.data.sample.SampleData
import com.Meztido0.Dewgov.domain.model.CrewMember
import com.Meztido0.Dewgov.domain.model.DistrictBlueprint
import com.Meztido0.Dewgov.domain.model.Dossier
import com.Meztido0.Dewgov.domain.model.EndingInfo
import com.Meztido0.Dewgov.domain.model.Mission
import com.Meztido0.Dewgov.domain.model.Upgrade
import com.Meztido0.Dewgov.domain.repository.CityRepository

class CityRepositoryImpl : CityRepository {
    override fun districts(): List<DistrictBlueprint> = SampleData.districts
    override fun crew(): List<CrewMember> = SampleData.crew
    override fun missions(): List<Mission> = SampleData.missions
    override fun upgrades(): List<Upgrade> = SampleData.upgrades
    override fun dossiers(): List<Dossier> = SampleData.dossiers
    override fun endings(): List<EndingInfo> = SampleData.endings
}
