package com.freelancekc.fdjtechnicaltest.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamsResponse(
    @SerialName("teams")
    val teams: List<TeamDto>
)

@Serializable
data class TeamDto(
    @SerialName("idTeam")
    val id: String,
    @SerialName("strTeam")
    val name: String,
    @SerialName("strBadge")
    val badge: String? = null
)
