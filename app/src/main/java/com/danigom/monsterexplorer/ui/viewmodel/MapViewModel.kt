package com.danigom.monsterexplorer.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.danigom.monsterexplorer.data.local.entities.PokemonEntity
import com.danigom.monsterexplorer.data.model.PokemonResult
import com.danigom.monsterexplorer.data.repository.PokemonRepository
import kotlinx.coroutines.launch

class MapViewModel(private val repository: PokemonRepository) : ViewModel() {
    private val _pokemonList = MutableLiveData<List<PokemonResult>>()
    val pokemonList: LiveData<List<PokemonResult>> = _pokemonList

    // Obtener los pokémon capturados desde Room
    val pokemonCapturados: LiveData<List<PokemonEntity>> =
        repository.pokemonCapturados.asLiveData()

    fun loadPokemon() {
        viewModelScope.launch {
            try {
                // Generar offset aleatorio entre 0 y 800 (hay más de 1000 pokémon en la API)
                val randomOffset = (0..800).random()
                val response = repository.getPokemonList(20, randomOffset)
                _pokemonList.value = response.results
            } catch (e: Exception) {
                Log.e("MAP_VM", e.message ?: "Error cargando Pokémon")
            }
        }
    }

    // Función para capturar Pokémon y guardar en Room
    fun capturarPokemon(pokemon: PokemonResult, lat: Double, lon: Double) {
        viewModelScope.launch {
            // Extraer el ID de la URL
            val idStr = pokemon.url.split("/").filter { it.isNotEmpty() }.last()
            val pokemonId = idStr.toIntOrNull() ?: 0

            val nuevoPokemon = PokemonEntity(
                pokemonId = pokemonId,
                nombre = pokemon.name,
                nivel = (1..100).random(),
                fechaCaptura = System.currentTimeMillis(),
                latitud = lat,
                longitud = lon
            )

            repository.capturarPokemon(nuevoPokemon)
            Log.d("MAP_VM", "${pokemon.name} capturado en Room!")
        }
    }

    // Función para capturar directamente con PokemonEntity (desde DetallesPokemonFragment)
    fun capturarPokemonEntity(pokemon: PokemonEntity) {
        viewModelScope.launch {
            repository.capturarPokemon(pokemon)
            Log.d("MAP_VM", "${pokemon.nombre} capturado en Room!")
        }
    }

    // Función para liberar/eliminar un Pokémon capturado
    fun liberarPokemon(pokemon: PokemonEntity) {
        viewModelScope.launch {
            repository.liberarPokemon(pokemon)
            Log.d("MAP_VM", "${pokemon.nombre} liberado!")
        }
    }
}
