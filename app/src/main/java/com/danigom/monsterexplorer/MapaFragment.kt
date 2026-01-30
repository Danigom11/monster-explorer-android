package com.danigom.monsterexplorer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.danigom.monsterexplorer.data.model.PokemonResult
import com.danigom.monsterexplorer.databinding.FragmentMapaBinding
import com.danigom.monsterexplorer.ui.viewmodel.MapViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import kotlin.random.Random

class MapaFragment : Fragment() {

    private var _binding: FragmentMapaBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MapViewModel by viewModels()

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

        binding.fabFavoritos.setOnClickListener {
            findNavController().navigate(R.id.action_map_to_favorites)
        }

        // Cargar datos
        viewModel.loadPokemon()
        viewModel.pokemonList.observe(viewLifecycleOwner) { pokemonList ->
            addPokemonMarkers(pokemonList)
        }
    }

    private fun addPokemonMarkers(pokemonList: List<PokemonResult>) {
        val center = GeoPoint(40.4168, -3.7038) // Madrid
        pokemonList.forEach { pokemon ->
            val position = randomGeoPoint(center)
            addPokemonMarker(pokemon.name, position)
        }
        binding.map.invalidate()
    }

    private fun addPokemonMarker(name: String, position: GeoPoint) {
        val marker = Marker(binding.map)
        marker.position = position
        marker.title = name
        marker.setOnMarkerClickListener { marker, mapView ->
            marker.showInfoWindow()
            // Aquí podríamos navegar al detalle pasando datos
             findNavController().navigate(R.id.action_map_to_detail)
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