package com.danigom.monsterexplorer

import android.app.Application
import com.danigom.monsterexplorer.data.local.AppDatabase
import com.danigom.monsterexplorer.data.repository.PokemonRepository
import org.osmdroid.config.Configuration

class MonsterExplorerApp: Application() {
    // Lazy initialization de Room y Repositorio
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { PokemonRepository(database.pokemonDao()) }

    override fun onCreate() {
        super.onCreate()
        Configuration.getInstance().userAgentValue = packageName
    }
}
