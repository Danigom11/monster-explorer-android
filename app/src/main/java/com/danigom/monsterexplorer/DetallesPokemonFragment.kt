package com.danigom.monsterexplorer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.danigom.monsterexplorer.databinding.FragmentDetallesPokemonBinding
import com.danigom.monsterexplorer.ui.viewmodel.MapViewModel
import com.danigom.monsterexplorer.ui.viewmodel.PokemonViewModelFactory
import com.danigom.monsterexplorer.data.local.entities.PokemonEntity

class DetallesPokemonFragment : Fragment() {

    private var _binding: FragmentDetallesPokemonBinding? = null
    private val binding get() = _binding!!

    // Recibir argumentos de navegación
    private val args: DetallesPokemonFragmentArgs by navArgs()

    // ViewModel para capturar
    private val viewModel: MapViewModel by viewModels {
        val app = requireActivity().application as MonsterExplorerApp
        PokemonViewModelFactory(app.repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetallesPokemonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mostrar datos del Pokémon
        binding.tvPokemonName.text = args.pokemonName.replaceFirstChar { it.uppercase() }

        // Cargar imagen del Pokémon
        val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${args.pokemonId}.png"
        Glide.with(this)
            .load(imageUrl)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .into(binding.ivPokemonImage)

        // Mostrar información adicional (puedes personalizarlo)
        binding.tvPokemonType.text = "ID: #${args.pokemonId}"

        // Configurar el botón según si ya está capturado o no
        if (args.yaCapturado) {
            // Si ya está capturado, mostrar botón de liberar
            binding.btnCapturar.text = "Liberar Pokémon"
            binding.btnCapturar.setOnClickListener {
                val pokemon = PokemonEntity(
                    pokemonId = args.pokemonId,
                    nombre = args.pokemonName,
                    nivel = (1..100).random(),
                    fechaCaptura = System.currentTimeMillis(),
                    latitud = args.latitude.toDouble(),
                    longitud = args.longitude.toDouble()
                )

                // Liberar el Pokémon
                viewModel.liberarPokemon(pokemon)

                Toast.makeText(context, "${args.pokemonName} liberado", Toast.LENGTH_SHORT).show()

                // Volver atrás (a la Pokédex) sin agregar al historial
                findNavController().popBackStack()
            }
        } else {
            // Si no está capturado, mostrar botón de capturar
            binding.btnCapturar.text = "Capturar Pokémon"
            binding.btnCapturar.setOnClickListener {
                // Crear entidad y capturar en Room
                val pokemon = PokemonEntity(
                    pokemonId = args.pokemonId,
                    nombre = args.pokemonName,
                    nivel = (1..100).random(),
                    fechaCaptura = System.currentTimeMillis(),
                    latitud = args.latitude.toDouble(),
                    longitud = args.longitude.toDouble()
                )

                // Guardar usando el ViewModel
                viewModel.capturarPokemonEntity(pokemon)

                Toast.makeText(context, "¡${args.pokemonName} capturado!", Toast.LENGTH_SHORT).show()

                // Navegar de vuelta al mapa
                findNavController().navigate(R.id.action_detail_to_map)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}