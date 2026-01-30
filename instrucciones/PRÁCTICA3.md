# PRÁCTICA 3: FRAGMENTS Y API MAPS.

## CONCEPTOS TEÓRICOS:
* **Fragment:** un Fragment es un componente visual reutilizable de Android que representa una parte de la interfaz de usuario y que vive dentro de una Activity. Piensa en un Fragment como una pantalla o subpantalla, con su propio XML, su propio ciclo de vida y su propia lógica en Kotlin.
* **FragmentContainerView:** es el espacio dentro del marco donde vas colocando los fragments. No tiene UI propia, tan solo es un contenedor.
* **Navigation Component (NavHostFragment):** es el elemento que define qué fragment existen, cuál es el inicial y cómo se navega entre ellos.

## PRÁCTICA:

### 1. Activity_main.xml
En la Activity_main establecemos un FragmentContainerView, en el que colocaremos el componente de navegación (NavHostFragment) que convierte el xml en un contenedor.

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="[http://schemas.android.com/apk/res/android](http://schemas.android.com/apk/res/android)"
    xmlns:app="[http://schemas.android.com/apk/res-auto](http://schemas.android.com/apk/res-auto)"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    
    <androidx.fragment.app.FragmentContainerView
        android:id="@+id/nav_host_fragment"
        android:name="androidx.navigation.fragment.NavHostFragment"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:defaultNavHost="true"
        app:navGraph="@navigation/nav_graph"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />
        
</androidx.constraintlayout.widget.ConstraintLayout>
2. Navigation
Creamos una carpeta en "res" llamada Navigation y dentro de ella un componente de navegación (res/navigation/nav_graph.xml). Aquí es donde realmente se desarrollará la definición e interacción de los fragments.

XML
<?xml version="1.0" encoding="utf-8"?>
<navigation
    xmlns:android="[http://schemas.android.com/apk/res/android](http://schemas.android.com/apk/res/android)"
    xmlns:app="[http://schemas.android.com/apk/res-auto](http://schemas.android.com/apk/res-auto)"
    android:id="@+id/nav_graph"
    app:startDestination="@id/loadingFragment">
    
    <fragment
        android:id="@+id/loadingFragment"
        android:name="com.misclases.monsterexplorerprev.LoadingFragment"
        android:label="Loading">
        <action
            android:id="@+id/action_loading_to_map"
            app:destination="@id/mapFragment" />
    </fragment>
    
    <fragment
        android:id="@+id/mapFragment"
        android:name="com.misclases.monsterexplorerprev.MapFragment"
        android:label="Mapa">
        <action
            android:id="@+id/action_map_to_detail"
            app:destination="@id/pokemonDetailFragment" />
        <action
            android:id="@+id/action_map_to_favorites"
            app:destination="@id/favoritesFragment" />
    </fragment>
    
    <fragment
        android:id="@+id/pokemonDetailFragment"
        android:name="com.misclases.monsterexplorerprev.PokemonDetailFragment"
        android:label="Detalle Pokémon">
        <action
            android:id="@+id/action_detail_to_favorites"
            app:destination="@id/favoritesFragment" />
        <action
            android:id="@+id/action_detail_to_map"
            app:destination="@+id/mapFragment"/>
    </fragment>
    
    <fragment
        android:id="@+id/favoritesFragment"
        android:name="com.misclases.monsterexplorerprev.FavoritesFragment"
        android:label="Favoritos" />
        
</navigation>
3. Fragment UI
Los fragments se desarrollan igual que si fueran activities, teniendo en cuenta los requisitos de cada uno de ellos:

3.1 Fragment_loading
XML
<RelativeLayout
    xmlns:android="[http://schemas.android.com/apk/res/android](http://schemas.android.com/apk/res/android)"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/white">
    
    <ImageView
        android:id="@+id/imgLogo"
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:layout_centerInParent="true"
        android:src="@drawable/logo_pokemon"
        android:contentDescription="Logo de la app"/>
        
    <TextView
        android:id="@+id/tvAppName"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Monster Explorer"
        android:textSize="24sp"
        android:textStyle="bold"
        android:layout_below="@id/imgLogo"
        android:layout_centerHorizontal="true"
        android:layout_marginTop="16dp"/>
        
</RelativeLayout>
3.2 Fragment_map.xaml
XML
<FrameLayout
    xmlns:android="[http://schemas.android.com/apk/res/android](http://schemas.android.com/apk/res/android)"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    
    <com.google.android.material.floatingactionbutton.FloatingActionButton
        android:id="@+id/fabPokedex"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom|end"
        android:layout_margin="16dp"
        android:src="@drawable/ic_pokedex"
        android:contentDescription="Ir a Pokedex"/>
        
</FrameLayout>
3.3 fragment_pokemon_detail.xml
XML
<ScrollView ...>
    <LinearLayout ...>
        <ImageView android:id="@+id/imgPokemon" .../>
        <TextView android:id="@+id/tvPokemonName" .../>
        <TextView android:id="@+id/tvPokemonTypes" .../>
        <TextView android:id="@+id/tvPokemonDescription" .../>
        <Button android:id="@+id/btnFavorite" .../>
    </LinearLayout>
</ScrollView>
fragment_favorites.xml
XML
<LinearLayout xmlns:android="[http://schemas.android.com/apk/res/android](http://schemas.android.com/apk/res/android)"
    xmlns:app="[http://schemas.android.com/apk/res-auto](http://schemas.android.com/apk/res-auto)"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp">
    
    <TextView
        android:id="@+id/tvPokedexTitle"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Pokédex"
        android:textAlignment="center"
        android:textSize="24sp"
        android:textStyle="bold"
        android:paddingBottom="12dp"/>
        
    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/rvFavorites"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager" />
        
</LinearLayout>
3.4 pokemon_item.xml
Este xml no representa una pantalla completa, sino el diseño visual de un solo Pokémon dentro de una lista. Este archivo define cómo se verá cada elemento que el usuario observa en el recyclerview: su nombre, su imagen y su estado.

XML
<LinearLayout xmlns:android="[http://schemas.android.com/apk/res/android](http://schemas.android.com/apk/res/android)"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="horizontal"
    android:padding="8dp">
    
    <ImageView
        android:id="@+id/imgPokemon"
        android:layout_width="64dp"
        android:layout_height="64dp"
        android:contentDescription="Pokemon image" />
        
    <TextView
        android:id="@+id/tvPokemonName"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="12dp"
        android:textSize="18sp"
        android:textStyle="bold" />
        
</LinearLayout>