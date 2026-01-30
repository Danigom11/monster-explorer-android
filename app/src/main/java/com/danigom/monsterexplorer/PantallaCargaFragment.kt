package com.danigom.monsterexplorer

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.danigom.monsterexplorer.databinding.FragmentPantallaCargaBinding

class PantallaCargaFragment : Fragment() {

    private var _binding: FragmentPantallaCargaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPantallaCargaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Simular carga y navegar al mapa después de 3 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.action_loading_to_map)
        }, 3000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}