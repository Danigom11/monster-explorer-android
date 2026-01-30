# Práctica MonsterExplorer - Parte 1

En esta primera parte vamos a configurar Retrofit con Moshi, conectarnos a PokeAPI y realizar la primera llamada funcional desde la app.

### 1. Crear el proyecto
1. Abre Android Studio
2. Nuevo proyecto → Empty Views Activity
3. Configura:
    * **Name:** MonsterExplorer
    * **Package name:** com.example.monsterexplorer
    * **Language:** Kotlin
    * **Minimum SDK:** API 26 o superior (recomendado)
4. Finaliza y espera a que Gradle termine

### 2. Activar ViewBinding
1. Abre el archivo `build.gradle.kts (Module : app)`
2. Dentro del bloque `android {}` añade:
```kotlin
android {
    buildFeatures {
        viewBinding = true
    }
}
Sincroniza el proyecto (Sync Now)

3. Añadir permisos de Internet
Abre AndroidManifest.xml

Añade antes de <application>:

XML
<uses-permission android:name="android.permission.INTERNET" />
Sin este permiso Retrofit no funcionará.

4. Añadir librerías necesarias
Abrimos build.gradle.kts (Module : app) y añadimos en dependencies {}:

Kotlin
// Retrofit
implementation("com.squareup.retrofit2:retrofit:2.11.0")
// Moshi
implementation("com.squareup.moshi:moshi:1.15.1")
implementation("com.squareup.moshi:moshi-kotlin:1.15.1")
// Retrofit + Moshi
implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
// Logging (muy importante para prácticas)
implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
// ViewModel + Coroutines
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
implementation("androidx.activity:activity-ktx:1.8.2")
Sincroniza Gradle.

5. Estructura de carpetas
Crea exactamente esta estructura (si no existe):

com.example.monsterexplorer

data

model

remote

repository

ui

viewmodel

utils

6. Crear modelos de datos (PokeAPI)
Vamos a consumir este endpoint: https://pokeapi.co/api/v2/pokemon?limit=20

6.1 Modelo de respuesta
data/model/PokemonListResponse.kt

Kotlin
package com.example.monsterexplorer.data.model

import com.squareup.moshi.Json

data class PokemonListResponse(
    @Json(name = "results")
    val results: List<PokemonResult>
)
data/model/PokemonResult.kt

Kotlin
package com.example.monsterexplorer.data.model

import com.squareup.moshi.Json

data class PokemonResult(
    @Json(name = "name")
    val name: String,
    @Json(name = "url")
    val url: String
)
Moshi se encargará automáticamente del mapeo JSON.

7. Crear la interfaz de la API
data/remote/PokeApiService.kt

Kotlin
package com.example.monsterexplorer.data.remote

import com.example.monsterexplorer.data.model.PokemonListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PokeApiService {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int
    ): PokemonListResponse
}
8. Configurar Retrofit
data/remote/RetrofitClient.kt

Kotlin
package com.example.monsterexplorer.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {
    private const val BASE_URL = "[https://pokeapi.co/api/v2/](https://pokeapi.co/api/v2/)"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val api: PokeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(PokeApiService::class.java)
    }
}
9. Crear el repositorio
data/repository/PokemonRepository.kt

Kotlin
package com.example.monsterexplorer.data.repository

import com.example.monsterexplorer.data.remote.RetrofitClient

class PokemonRepository {
    suspend fun getPokemonList(limit: Int) = 
        RetrofitClient.api.getPokemonList(limit)
}
El repositorio aísla la fuente de datos.

10. ViewModel
ui/viewmodel/PokemonViewModel.kt

Kotlin
package com.example.monsterexplorer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.monsterexplorer.data.repository.PokemonRepository
import kotlinx.coroutines.launch
import android.util.Log

class PokemonViewModel : ViewModel() {
    private val repository = PokemonRepository()

    fun loadPokemon() {
        viewModelScope.launch {
            try {
                val response = repository.getPokemonList(20)
                response.results.forEach {
                    Log.d("POKEMON", it.name)
                }
            } catch (e: Exception) {
                Log.e("POKEMON_ERROR", e.message ?: "Error desconocido")
            }
        }
    }
}
11. MainActivity con ViewBinding
MainActivity.kt

Kotlin
package com.example.monsterexplorer

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.monsterexplorer.databinding.ActivityMainBinding
import com.example.monsterexplorer.ui.viewmodel.PokemonViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: PokemonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.loadPokemon()
    }
}