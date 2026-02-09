package com.danigom.monsterexplorer.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.danigom.monsterexplorer.data.local.entities.PokemonEntity
import com.danigom.monsterexplorer.data.repository.PokemonRepository
import kotlinx.coroutines.launch

class FavoritosViewModel(private val repository: PokemonRepository) : ViewModel() {
    // Convertir Flow a LiveData
    val listaPokemon: LiveData<List<PokemonEntity>> = repository.pokemonCapturados.asLiveData()

    fun liberarPokemon(pokemon: PokemonEntity) {
        viewModelScope.launch {
            repository.liberarPokemon(pokemon)
        }
    }
}

