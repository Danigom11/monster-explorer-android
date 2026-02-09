package com.danigom.monsterexplorer.data.repository

import com.danigom.monsterexplorer.data.local.dao.PokemonDao
import com.danigom.monsterexplorer.data.local.entities.PokemonEntity
import com.danigom.monsterexplorer.data.remote.RetrofitClient
import kotlinx.coroutines.flow.Flow

class PokemonRepository(private val pokemonDao: PokemonDao) {
    // API remota
    suspend fun getPokemonList(limit: Int, offset: Int = 0) =
        RetrofitClient.api.getPokemonList(limit, offset)

    // Base de datos local
    suspend fun capturarPokemon(pokemon: PokemonEntity) {
        pokemonDao.insertarPokemon(pokemon)
    }

    val pokemonCapturados: Flow<List<PokemonEntity>> = pokemonDao.obtenerTodosLosPokemon()

    suspend fun liberarPokemon(pokemon: PokemonEntity) {
        pokemonDao.liberarPokemon(pokemon)
    }
}

