package com.danigom.monsterexplorer.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danigom.monsterexplorer.data.model.PokemonResult
import com.danigom.monsterexplorer.data.repository.PokemonRepository
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {
    private val repository = PokemonRepository()
    private val _pokemonList = MutableLiveData<List<PokemonResult>>()
    val pokemonList: LiveData<List<PokemonResult>> = _pokemonList

    fun loadPokemon() {
        viewModelScope.launch {
            try {
                val response = repository.getPokemonList(10)
                _pokemonList.value = response.results
            } catch (e: Exception) {
                Log.e("MAP_VM", e.message ?: "Error cargando Pokémon")
            }
        }
    }
}
