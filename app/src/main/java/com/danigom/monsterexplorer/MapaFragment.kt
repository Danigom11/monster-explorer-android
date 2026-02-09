package com.danigom.monsterexplorer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.danigom.monsterexplorer.data.local.entities.PokemonEntity
import com.danigom.monsterexplorer.data.model.PokemonResult
import com.danigom.monsterexplorer.databinding.FragmentMapaBinding
import com.danigom.monsterexplorer.ui.viewmodel.MapViewModel
import com.danigom.monsterexplorer.ui.viewmodel.PokemonViewModelFactory
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import kotlin.random.Random

class MapaFragment : Fragment() {

    private var _binding: FragmentMapaBinding? = null
    private val binding get() = _binding!!

    // Usar ViewModelFactory con el repositorio
    private val viewModel: MapViewModel by viewModels {
        val app = requireActivity().application as MonsterExplorerApp
        PokemonViewModelFactory(app.repository)
    }

    // Lista de pokémon capturados
    private var pokemonCapturadosIds = mutableSetOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar mapa
        val map = binding.map
        map.setMultiTouchControls(true)
        val startPoint = GeoPoint(40.4168, -3.7038) // Madrid
        map.controller.setZoom(15.0)
        map.controller.setCenter(startPoint)

        // Botón de favoritos
        binding.fabFavoritos.setOnClickListener {
            findNavController().navigate(R.id.action_map_to_favorites)
        }

        // Botón de actualizar
        binding.fabActualizar.setOnClickListener {
            viewModel.loadPokemon()
            Toast.makeText(context, "Cargando nuevos Pokémon...", Toast.LENGTH_SHORT).show()
        }

        // Observar pokémon capturados
        viewModel.pokemonCapturados.observe(viewLifecycleOwner) { capturados ->
            pokemonCapturadosIds = capturados.map { it.pokemonId }.toMutableSet()
            // Recargar marcadores cuando cambian los capturados
            viewModel.pokemonList.value?.let { pokemonList ->
                addPokemonMarkers(pokemonList)
            }
        }

        // Cargar datos de pokémon disponibles
        viewModel.pokemonList.observe(viewLifecycleOwner) { pokemonList ->
            addPokemonMarkers(pokemonList)
        }

        // Cargar pokémon inicialmente
        viewModel.loadPokemon()
    }

    private fun addPokemonMarkers(pokemonList: List<PokemonResult>) {
        val center = GeoPoint(40.4168, -3.7038) // Madrid
        // Limpiar marcadores previos
        binding.map.overlays.clear()

        // Filtrar pokémon ya capturados
        val pokemonDisponibles = pokemonList.filter { pokemon ->
            val idStr = pokemon.url.split("/").filter { it.isNotEmpty() }.last()
            val pokemonId = idStr.toIntOrNull() ?: 0
            pokemonId !in pokemonCapturadosIds
        }

        pokemonDisponibles.forEach { pokemon ->
            val position = randomGeoPoint(center)
            addPokemonMarker(pokemon, position)
        }
        binding.map.invalidate()
    }

    private fun addPokemonMarker(pokemon: PokemonResult, position: GeoPoint) {
        val marker = Marker(binding.map)
        marker.position = position
        marker.title = pokemon.name.replaceFirstChar { it.uppercase() }

        marker.setOnMarkerClickListener { m, _ ->
            // Extraer el ID del Pokémon de la URL
            val idStr = pokemon.url.split("/").filter { it.isNotEmpty() }.last()
            val pokemonId = idStr.toIntOrNull() ?: 0

            // Navegar a la pantalla de detalles con los datos del Pokémon
            val action = MapaFragmentDirections.actionMapToDetail(
                pokemonName = pokemon.name,
                pokemonId = pokemonId,
                latitude = m.position.latitude.toFloat(),
                longitude = m.position.longitude.toFloat()
            )
            findNavController().navigate(action)
            true
        }
        binding.map.overlays.add(marker)
    }

    private fun randomGeoPoint(center: GeoPoint): GeoPoint {
        val offset = 0.005
        val lat = center.latitude + Random.nextDouble(-offset, offset)
        val lon = center.longitude + Random.nextDouble(-offset, offset)
        return GeoPoint(lat, lon)
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

