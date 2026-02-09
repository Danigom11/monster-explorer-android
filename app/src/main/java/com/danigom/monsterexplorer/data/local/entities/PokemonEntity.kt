package com.danigom.monsterexplorer.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_capturados")
data class PokemonEntity(
    @PrimaryKey
    val pokemonId: Int, // ID original de la PokeAPI - ahora es clave primaria para evitar duplicados
    val nombre: String,
    val nivel: Int,
    val fechaCaptura: Long, // Timestamp
    val latitud: Double,
    val longitud: Double
)

