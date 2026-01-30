package com.danigom.monsterexplorer.data.model

import com.squareup.moshi.Json

data class PokemonResult(
    @Json(name = "name")
    val name: String,
    @Json(name = "url")
    val url: String
)