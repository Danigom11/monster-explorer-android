package com.danigom.monsterexplorer.data.repository

import com.danigom.monsterexplorer.data.remote.RetrofitClient

class PokemonRepository {
    suspend fun getPokemonList(limit: Int) =
        RetrofitClient.api.getPokemonList(limit)
}