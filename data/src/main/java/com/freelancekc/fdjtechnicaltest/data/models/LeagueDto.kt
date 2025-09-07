package com.freelancekc.fdjtechnicaltest.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LeaguesResponse(
    @SerialName("leagues")
    val leagues: List<LeagueDto>
)

@Serializable
data class LeagueDto(
    @SerialName("idLeague")
    val id: String,
    @SerialName("strLeague")
    val name: String
)
