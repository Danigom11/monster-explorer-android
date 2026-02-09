package com.danigom.monsterexplorer.data.local.dao

import androidx.room.*
import com.danigom.monsterexplorer.data.local.entities.PokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarPokemon(pokemon: PokemonEntity)

    @Query("SELECT * FROM pokemon_capturados ORDER BY fechaCaptura DESC")
    fun obtenerTodosLosPokemon(): Flow<List<PokemonEntity>>

    @Delete
    suspend fun liberarPokemon(pokemon: PokemonEntity)
}

