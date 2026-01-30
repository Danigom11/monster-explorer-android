# APIs en Android Studio

## 1. ¿Qué es una API REST?
Una API REST es un conjunto de reglas que permite la comunicación entre sistemas. [cite_start]Utiliza métodos HTTP como GET y POST para intercambiar datos, facilitando así el acceso a recursos en línea[cite: 741, 742].
[cite_start]REST (Representational State Transfer) no es un lenguaje ni un framework, sino un conjunto de reglas y convenciones para que clientes y servidores se comuniquen de manera estándar[cite: 743].

**Verbos HTTP principales:**
* [cite_start]**GET:** Obtener datos (Ej. Traer la lista de usuarios)[cite: 752].
* [cite_start]**POST:** Enviar datos (Ej. Registrar un nuevo usuario)[cite: 752].
* [cite_start]**PUT:** Actualizar datos (Ej. Modificar información de un usuario)[cite: 752].
* [cite_start]**DELETE:** Eliminar datos (Ej. Borrar un usuario)[cite: 752].

## 2. Comprendiendo el JSON en APIs
El JSON es el idioma común entre App y API. [cite_start]Es ligero, legible y fácil de mapear a objetos Kotlin[cite: 754].

**Mapeo a Kotlin:**
[cite_start]Los nombres de propiedades en Kotlin deben coincidir con los del JSON o usar `@Json(name="...")` si usamos Moshi para serialización personalizada[cite: 767, 768].

## 3. La Pila Tecnológica
Herramientas clave para el desarrollo Android:
* [cite_start]**Retrofit 2:** Convierte endpoints HTTP en interfaces Kotlin, evitando escribir código repetitivo de manejo de conexiones y parsing[cite: 772].
* **Moshi:** Convertidor moderno hecho para Kotlin que transforma JSON en objetos. [cite_start]Maneja bien tipos nulos (`String?`) y evita errores silenciosos[cite: 775, 777].
* [cite_start]**Coroutines:** Fundamentales para realizar operaciones asíncronas sin bloquear el hilo principal (Main Thread), evitando que la app se congele (ANR)[cite: 779, 780, 781].
* [cite_start]**View Binding:** Asegura un manejo seguro y eficiente de las vistas en XML[cite: 789].

## 4. Arquitectura MVVM (Modelo-Vista-Modelo de Vista)
[cite_start]Define cómo fluye la información[cite: 792]:
1.  [cite_start]**Vista (Activity/Fragment):** El usuario interactúa (pulsa un botón) y avisa al ViewModel[cite: 792, 793, 794].
2.  [cite_start]**ViewModel:** Lanza una coroutine (Dispatcher.IO) y llama al Repositorio/Retrofit[cite: 795, 796, 797].
3.  [cite_start]**Modelo (Retrofit + Moshi):** Consulta la API y convierte el JSON en objetos Kotlin[cite: 798, 799, 800].
4.  **Retorno:** El ViewModel recibe la data y actualiza un `StateFlow` / `LiveData`. [cite_start]La Vista observa los cambios y muestra los datos[cite: 801, 802, 803, 804].

## 5. Configuración del Proyecto y Seguridad
### El Manifiesto (Permisos)
[cite_start]Es necesario declarar permisos en el `AndroidManifest.xml` fuera de la etiqueta `<application>`[cite: 875, 878, 880].
* [cite_start]**INTERNET:** Permiso normal necesario para conectar[cite: 878].
* [cite_start]**ACCESS_NETWORK_STATE:** Opcional pero recomendado para comprobar si hay conexión antes de intentar la llamada[cite: 881].

### Gradle y Dependencias
Se necesitan librerías para:
* [cite_start]**Retrofit:** El motor base[cite: 887].
* [cite_start]**Convertidor (Moshi Converter):** "Plugin" para transformar JSON a objetos automáticamente[cite: 888, 889].
* [cite_start]**Logging Interceptor:** Permite "interceptar" la llamada y ver en el Logcat qué se envía y recibe (fundamental para depurar)[cite: 891, 893].
* [cite_start]**Coroutines:** Librerías específicas (`lifecycle-viewmodel-ktx`) para lanzar tareas ligadas al ciclo de vida[cite: 895].

## 6. Capa de Datos y Networking
### Data Classes y DTO
* [cite_start]**DTO (Data Transfer Object):** Clases que son simples contenedores de datos sin lógica de negocio, usadas para transportar información del servidor a la app[cite: 905, 906].
* [cite_start]**Anotación @Json:** Resuelve diferencias de nomenclatura (ej. `user_name` en JSON vs `userName` en Kotlin) usando `@Json(name = "user_name")`[cite: 923, 924].

### Nulabilidad (Null Safety)
Es vital definir correctamente los tipos:
* [cite_start]Si el campo siempre viene: `val name: String`[cite: 930].
* [cite_start]Si puede no venir o ser null: `val name: String?`[cite: 931].
[cite_start]Esto evita excepciones y datos corruptos[cite: 932].

## 7. Implementación de Retrofit
### Interfaz ApiService
Define los endpoints y métodos HTTP usando anotaciones. [cite_start]Solo describimos **qué** queremos, no **cómo** se hace[cite: 964, 1008].

```kotlin
interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>
}


Patrón Singleton (Retrofit Client)
Se usa un object para tener una única instancia reutilizable y centralizada.

Configura la baseUrl.

Añade el MoshiConverterFactory.

Crea la implementación de ApiService.

8. Capa de Presentación y Gestión de Estados
Es fundamental no realizar llamadas API directamente desde la Activity para evitar duplicidades y bugs por el ciclo de vida. La lógica debe estar en el ViewModel.

Gestión de Estados (UiState)
Pensamos en estados, no solo en datos. Se suele usar una sealed class:


Loading: Cargando.


Success: Tengo datos.


Error: Algo ha fallado.

ViewModel y Coroutines
El ViewModel expone el estado mediante StateFlow y usa viewModelScope para lanzar coroutines.

Kotlin
class UserViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<List<User>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<User>>> = _uiState

    fun loadUsers() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val users = RetrofitClient.apiService.getUsers()
                _uiState.value = UiState.Success(users)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al cargar los datos")
            }
        }
    }
}


Rol de la Activity
La Activity solo observa el uiState y muestra los datos o errores correspondientes. No contiene lógica.

Resumen General
El flujo completo de una app bien estructurada es:

API REST (JSON) →

Retrofit + Moshi (Cliente y Conversión) →

ApiService (Endpoints) →

ViewModel (Lógica, Coroutines, Estados) →

Activity/UI (Vista reactiva que observa StateFlow/LiveData).