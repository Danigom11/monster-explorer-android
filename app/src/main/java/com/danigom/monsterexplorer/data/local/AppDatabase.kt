package com.danigom.monsterexplorer.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.danigom.monsterexplorer.data.local.dao.PokemonDao
import com.danigom.monsterexplorer.data.local.entities.PokemonEntity

@Database(entities = [PokemonEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pokemon_database"
                )
                    .fallbackToDestructiveMigration() // Permite recrear la BD cuando cambia el esquema
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

