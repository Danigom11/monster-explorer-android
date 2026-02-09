package com.danigom.monsterexplorer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.danigom.monsterexplorer.data.local.entities.PokemonEntity
import com.danigom.monsterexplorer.databinding.PokemonItemBinding

class FavoritosAdapter(
    private var pokemonList: List<PokemonEntity>,
    private val onItemClick: (PokemonEntity) -> Unit
) : RecyclerView.Adapter<FavoritosAdapter.PokemonViewHolder>() {

    inner class PokemonViewHolder(val binding: PokemonItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = PokemonItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = pokemonList[position]

        // Nombre con primera letra en mayúscula
        holder.binding.tvPokemonName.text = pokemon.nombre.replaceFirstChar { it.uppercase() }

        // URL de la imagen oficial de PokeAPI
        val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.pokemonId}.png"

        // Cargar imagen con Glide
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(android.R.drawable.ic_menu_report_image)
            .into(holder.binding.imgPokemon)

        // Click listener
        holder.itemView.setOnClickListener {
            onItemClick(pokemon)
        }
    }

    override fun getItemCount() = pokemonList.size

    fun updateData(newList: List<PokemonEntity>) {
        this.pokemonList = newList
        notifyDataSetChanged()
    }
}

