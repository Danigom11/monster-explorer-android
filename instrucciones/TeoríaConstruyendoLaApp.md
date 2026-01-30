# FRAGMENT, RECYCLER VIEW Y OSMDROID
## Construyendo una app

### FRAGMENT
Un Fragment es una parte reutilizable de la interfaz de usuario de una app Android. Representa una pantalla completa o una sección de una pantalla, y siempre vive dentro de una Activity. Podemos pensar en un Fragment como una pantalla independiente, pero que necesita una Activity para existir.

**En esta práctica:**
* El mapa es un Fragment
* La Pokédex (favoritos) es otro Fragment
* La pantalla de detalles es otro Fragment

**Cada Fragment se encarga de:**
* Inflar su layout XML
* Gestionar la interacción del usuario
* Mostrar los datos que le corresponden

#### ¿Cómo funciona un Fragment?
Un Fragment tiene un ciclo de vida, es decir, una serie de momentos importantes:
* Cuando se crea
* Cuando se muestra
* Cuando deja de ser visible
* Cuando se destruye

Esto permite que Android:
* Cambie de pantalla sin cerrar la app
* Reutilice componentes
* Gestione mejor la memoria

**Normalmente, un Fragment:**
1. Carga su layout XML
2. Configura sus vistas (botones, listas, textos)
3. Obtiene datos (normalmente desde un ViewModel)
4. Responde a acciones del usuario

#### Componentes habituales de un Fragment
Un Fragment suele estar formado por:
* **Un archivo XML:** Define cómo se ve la pantalla (TextView, RecyclerView, botones, etc.)
* **Una clase Fragment (.kt):** Controla la lógica de la pantalla
* **Un ViewModel (opcional pero recomendado):** Guarda los datos y el estado de la pantalla. Esta separación ayuda a que el código sea más claro, más fácil de mantener y no mezcle lógica con diseño.

---

### RECYCLER VIEW
Un RecyclerView es un componente que sirve para mostrar listas o colecciones de datos de forma eficiente.

**Ejemplos:**
* Lista de Pokémon capturados
* Lista de Pokémon cercanos
* Lista de evoluciones

**La idea clave:**
El RecyclerView no sabe qué datos muestra ni cómo son; solo sabe que tiene que mostrar una lista. Por eso necesita otros componentes que le expliquen qué hacer.

#### ¿Cómo funciona un RecyclerView?
El RecyclerView funciona reciclando vistas:
* No crea una vista nueva para cada elemento.
* Reutiliza las vistas que ya no se ven en pantalla.
* Esto mejora muchísimo el rendimiento.

Para poder funcionar, el RecyclerView necesita tres cosas principales:
1. Un layout de item
2. Un Adapter
3. Un Layout Manager

**1. Un layout de item (item.xml)**
Es el diseño de una sola fila o elemento de la lista. Por ejemplo: Imagen del Pokémon y Nombre del Pokémon. Este XML define cómo se ve un Pokémon en la lista, no la lista entera.

**2. Un Adapter**
El Adapter es el intermediario entre los datos (lista de Pokémon) y la interfaz (item.xml).
Su responsabilidad es:
* Crear los items
* Rellenarlos con datos
* Decidir cuántos hay

Se puede pensar en el Adapter como el traductor entre los datos y la pantalla.

**3. Un LayoutManager**
El LayoutManager decide cómo se colocan los elementos:
* En vertical (ej. Lista para la Pokédex)
* En horizontal
* En cuadrícula (ej. Grid para mostrar Pokémon en miniatura)

---

### OSMDROID
OSMDroid es una librería que permite usar mapas basados en OpenStreetMap en aplicaciones Android. Es una alternativa libre a Google Maps.

**Ideal para:**
* Prácticas
* Proyectos educativos
* Apps sin dependencias de Google

**En esta práctica, OSMDroid se usa para:**
* Mostrar el mapa
* Colocar marcadores de Pokémon
* Simular movimiento del jugador

#### ¿Cómo funciona OSMDroid?
OSMDroid se basa en tres ideas clave:

**1. El mapa (MapView)**
El MapView es el componente visual (el equivalente al "contenedor" del mapa).
* Muestra el mapa
* Permite hacer zoom
* Permite mover la cámara

**2. El controlador del mapa**
Permite separar lo que se ve de cómo se controla.
* Cambiar el zoom
* Mover el mapa a una posición concreta
* Centrar la vista en el jugador

**3. Overlays (capas)**
Los overlays son elementos que se colocan encima del mapa.
* Marcadores (Pokémon, jugador)
* Líneas
* Zonas

Cada Pokémon en el mapa suele ser un overlay con una posición (latitud y longitud).