package com.danigom.monsterexplorer.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danigom.monsterexplorer.data.repository.PokemonRepository
import kotlinx.coroutines.launch

class PokemonViewModel(private val repository: PokemonRepository) : ViewModel() {
    fun loadPokemon() {
        viewModelScope.launch {
            try {
                val response = repository.getPokemonList(20)
                response.results.forEach {
                    Log.d("POKEMON", it.name)
                }
            } catch (e: Exception) {
                Log.e("POKEMON_ERROR", e.message ?: "Error desconocido")
            }
        }
    }
}
