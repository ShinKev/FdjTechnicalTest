package com.freelancekc.fdjtechnicaltest.data.api

import com.freelancekc.fdjtechnicaltest.data.models.LeaguesResponse
import com.freelancekc.fdjtechnicaltest.data.models.TeamsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SportsApiService {

    @GET("all_leagues.php")
    suspend fun getAllLeagues(): LeaguesResponse

    @GET("search_all_teams.php")
    suspend fun getTeamsByLeague(@Query("l") leagueName: String): TeamsResponse

}
