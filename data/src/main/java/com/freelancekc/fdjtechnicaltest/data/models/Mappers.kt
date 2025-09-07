package com.freelancekc.fdjtechnicaltest.data.models

import com.freelancekc.fdjtechnicaltest.domain.models.League
import com.freelancekc.fdjtechnicaltest.domain.models.Team

fun LeagueDto.toDomain(): League = League(
    id = id,
    name = name
)

fun TeamDto.toDomain(): Team = Team(
    id = id,
    name = name,
    imageUrl = badge
)

fun List<LeagueDto>.toLeagueDomain(): List<League> = map { it.toDomain() }

fun List<TeamDto>.toTeamDomain(): List<Team> = map { it.toDomain() }
