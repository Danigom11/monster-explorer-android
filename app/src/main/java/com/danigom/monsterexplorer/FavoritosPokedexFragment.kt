package com.danigom.monsterexplorer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.danigom.monsterexplorer.databinding.FragmentFavoritosPokedexBinding
import com.danigom.monsterexplorer.ui.adapter.FavoritosAdapter
import com.danigom.monsterexplorer.ui.viewmodel.FavoritosViewModel
import com.danigom.monsterexplorer.ui.viewmodel.PokemonViewModelFactory

class FavoritosPokedexFragment : Fragment() {

    private var _binding: FragmentFavoritosPokedexBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: FavoritosAdapter

    // Usar ViewModelFactory
    private val viewModel: FavoritosViewModel by viewModels {
        val app = requireActivity().application as MonsterExplorerApp
        PokemonViewModelFactory(app.repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritosPokedexBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar RecyclerView con el Adapter
        adapter = FavoritosAdapter(emptyList()) { pokemon ->
            // Click en un Pokémon capturado - navegar a detalles
            val action = FavoritosPokedexFragmentDirections.actionFavoritesToDetail(
                pokemonName = pokemon.nombre,
                pokemonId = pokemon.pokemonId,
                latitude = pokemon.latitud.toFloat(),
                longitude = pokemon.longitud.toFloat(),
                yaCapturado = true  // Indicar que ya está capturado
            )
            findNavController().navigate(action)
        }

        binding.rvPokemonList.layoutManager = LinearLayoutManager(context)
        binding.rvPokemonList.adapter = adapter

        // Observar los Pokémon capturados
        viewModel.listaPokemon.observe(viewLifecycleOwner) { pokemonList ->
            adapter.updateData(pokemonList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

