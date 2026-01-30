package com.danigom.monsterexplorer.data.model

import com.squareup.moshi.Json

data class PokemonListResponse(
    @Json(name = "results")
    val results: List<PokemonResult>
)