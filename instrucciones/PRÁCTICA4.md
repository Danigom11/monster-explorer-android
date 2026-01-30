# [cite_start]PRÁCTICA 4 [cite: 195]

## [cite_start]FASE 3: MAPAS CON OSMDROID [cite: 196]
[cite_start]OSMDroid es una librería open-source que permite usar mapas de OpenStreetMap en Android → El mapa es una View normal, no un servicio externo opaco. [cite: 197]

[cite_start]**Diferencia fundamental con Google Maps** [cite: 198]

| Característica | [cite_start]Google Maps [cite: 199] | [cite_start]OSMDroid [cite: 200] |
| :--- | :--- | :--- |
| Tipo | [cite_start]Servicio externo [cite: 201] | [cite_start]Librería local [cite: 201] |
| API Key | [cite_start]Sí [cite: 202] | [cite_start]No [cite: 203] |
| Callbacks | [cite_start]Sí [cite: 204] | [cite_start]No [cite: 205] |
| Fragment interno | [cite_start]Sí [cite: 206] | [cite_start]No [cite: 206] |
| MapView normal | [cite_start]No [cite: 207] | [cite_start]Sí [cite: 208] |

### [cite_start]PASO 3.1 - Añadir la dependencia de OSMDroid [cite: 209]
[cite_start]`build.gradle.kts (Module :app)` [cite: 211]
[cite_start]Dentro de `dependencies {}` añade: [cite: 212]
```kotlin
[cite_start]implementation("org.osmdroid:osmdroid-android:6.1.18") [cite: 213]
PASO 3.2 - Añadir permisos necesarios 

OSMDroid descarga mapas desde internet. AndroidManifest.xml (FUERA de <application>) 

XML
<uses-permission android:name="android.permission.INTERNET" /> [cite: 217]
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /> [cite: 218]
PASO 3.3 - Configurar OSMDroid correctamente (OBLIGATORIO) 

Este paso es crítico y suele olvidarse. ¿Por qué hay que configurar OSMDroid? OpenStreetMap bloquea apps que: 

No se identifican 

Abusan del servicio 

OSMDroid exige un user-agent. 

PASO 3.3.1 - Crear clase Application 

Nueva clase: MonsterExplorerApp.kt 

Kotlin
class MonsterExplorerApp: Application() { [cite: 229]
    override fun onCreate() { [cite: 230]
        super.onCreate() [cite: 231]
        Configuration.getInstance().userAgentValue = packageName [cite: 234]
    } [cite: 232]
} [cite: 233]

Qué hace este código: 

Se ejecuta al arrancar la app 

Define cómo se identifica la app en OpenStreetMap 

Usa el nombre del paquete como identificador 

PASO 3.3.2 - Registrar la Application en el Manifest 


AndroidManifest.xml Dentro de <application>: 

XML
<application
    android:name=".MonsterExplorerApp"
> [cite: 243, 244, 245]
Sin este paso: 

El mapa puede no cargar 

O cargarse de forma intermitente 

PASO 3.4 - Preparar el XML del MapFragment 

Ahora vamos a mostrar el mapa en pantalla. fragment_map.xml 

XML
<FrameLayout
    xmlns:android="[http://schemas.android.com/apk/res/android](http://schemas.android.com/apk/res/android)"
    android:layout_width="match_parent"
    [cite_start]android:layout_height="match_parent"> [cite: 252, 253, 254, 255]
    
    [cite: 256]
    <org.osmdroid.views.MapView
        android:id="@+id/map"
        android:layout_width="match_parent"
        [cite_start]android:layout_height="match_parent" /> [cite: 257, 258, 259, 260]
        
    [cite: 261]
    <com.google.android.material.floatingactionbutton.FloatingActionButton
        android:id="@+id/fabPokedex"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom|end"
        android:layout_margin="16dp"
        android:src="@drawable/ic_pokedex"
        [cite_start]android:contentDescription="Ir a Pokédex" /> [cite: 262, 263, 264, 265, 266, 267, 268, 269]
</FrameLayout> [cite: 270]
PASO 3.5 - Crear MapFragment.kt con ViewBinding 

Aquí unimos Fragment + MapView + ViewBinding. MapFragment.kt (versión mínima funcional) 

Kotlin
class MapFragment: Fragment() { [cite: 274]
    private var _binding: FragmentMapBinding? = null [cite: 275]
    private val binding get() = _binding!! [cite: 276]

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    [cite_start]): View { [cite: 277, 278, 279, 280, 281]
        _binding = FragmentMapBinding.inflate(inflater, container, false) [cite: 283]
        return binding.root [cite: 284]
    } [cite: 282]

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { [cite: 285]
        super.onViewCreated(view, savedInstanceState) [cite: 286]
        
        val map = binding.map [cite: 288]
        map.setMultiTouchControls(true) [cite: 289]
        val startPoint = GeoPoint(40.4168, -3.7038) // Madrid [cite: 290]
        map.controller.setZoom(15.0) [cite: 291]
        map.controller.setCenter(startPoint) [cite: 292]
    } [cite: 287]

    override fun onDestroyView() { [cite: 293]
        super.onDestroyView() [cite: 294]
        _binding = null [cite: 295]
    } [cite: 296]
} [cite: 297]
FASE 4 - COLOCAR POKÉMON EN EL MAPA (OSMDROID + RETROFIT) 

Un Pokémon en el mapa = Datos (API) + Posición (mapa) + Representación visual (marker) 


Arquitectura que vamos a usar: PokeAPI (Retrofit) ↓ Pokemon Repository ↓ MapViewModel ↓ MapFragment ↓ OSMDroid Marker 

PASO 4.1 - ViewModel para el mapa 

No vamos a meter Retrofit directamente en el Fragment: buena arquitectura desde el inicio. MapViewModel.kt Ubicación recomendada: ui/viewmodel/MapViewModel.kt 

Kotlin
class MapViewModel: ViewModel() { [cite: 316]
    private val repository = PokemonRepository() [cite: 317]
    private val _pokemonList = MutableLiveData<List<PokemonResult>>() [cite: 318]
    val pokemonList: LiveData<List<PokemonResult>> = _pokemonList [cite: 319]

    fun loadPokemon() { [cite: 320]
        viewModelScope.launch { [cite: 321]
            try { [cite: 321]
                val response = repository.getPokemonList(10) [cite: 325]
                _pokemonList.value = response.results [cite: 326]
            } catch (e: Exception) { [cite: 327]
                Log.e("MAP_VM", e.message ?: "Error cargando Pokémon") [cite: 328]
            } [cite: 322]
        } [cite: 323]
    } [cite: 324]
}
PASO 4.2 - Conectar el ViewModel al MapFragment 

Volvemos a MapFragment.kt. Añadir el ViewModel - Arriba del Fragment: 

Kotlin
private val viewModel: MapViewModel by viewModels() [cite: 334]

Llamar a la carga de Pokémon - Dentro de onViewCreated(): 

Kotlin
viewModel.loadPokemon() [cite: 337]
viewModel.pokemonList.observe(viewLifecycleOwner) { pokemonList -> [cite: 338]
    addPokemonMarkers(pokemonList) [cite: 339]
} [cite: 340]
PASO 4.3 - Crear marcadores de Pokémon (OSMDroid) 

Ahora convertimos Pokémon → Marcadores. Función para añadir un marcador: 

Kotlin
private fun addPokemonMarker(name: String, position: GeoPoint) { [cite: 344, 345, 346, 347]
    val marker = Marker(binding.map) [cite: 349]
    marker.position = position [cite: 350]
    marker.title = name [cite: 351]
    binding.map.overlays.add(marker) [cite: 352]
} [cite: 348]
PASO 4.4 - Generar posiciones aleatorias 

Simulamos que los Pokémon aparecen "por ahí". Función para posiciones aleatorias: 

Kotlin
private fun randomGeoPoint(center: GeoPoint): GeoPoint { [cite: 356]
    val offset = 0.005 [cite: 356]
    val lat = center.latitude + Random.nextDouble(-offset, offset) [cite: 358]
    val lon = center.longitude + Random.nextDouble(-offset, offset) [cite: 359]
    return GeoPoint(lat, lon) [cite: 360]
} [cite: 357]
PASO 4.5 - Añadir TODOS los Pokémon al mapa 

Ahora unimos todo. Función completa: 

Kotlin
private fun addPokemonMarkers(pokemonList: List<PokemonResult>) { [cite: 364]
    val center = GeoPoint(40.4168, -3.7038) // Madrid [cite: 365]
    pokemonList.forEach { pokemon -> [cite: 366]
        val position = randomGeoPoint(center) [cite: 367]
        addPokemonMarker(pokemon.name, position) [cite: 368]
    } [cite: 369]
    binding.map.invalidate() [cite: 371]
} [cite: 370]