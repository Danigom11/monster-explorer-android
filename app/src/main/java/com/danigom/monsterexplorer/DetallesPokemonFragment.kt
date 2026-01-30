package com.danigom.monsterexplorer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.danigom.monsterexplorer.databinding.FragmentDetallesPokemonBinding

class DetallesPokemonFragment : Fragment() {

    private var _binding: FragmentDetallesPokemonBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetallesPokemonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCapturar.setOnClickListener {
            // Simular captura
            Toast.makeText(context, "¡Pokémon capturado!", Toast.LENGTH_SHORT).show()
            
            // Navegar a favoritos
            findNavController().navigate(R.id.action_detail_to_favorites)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}