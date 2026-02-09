package com.danigom.monsterexplorer.data.remote

import com.danigom.monsterexplorer.data.model.PokemonListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PokeApiService {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int = 0
    ): PokemonListResponse
}