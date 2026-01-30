# Estructura de proyecto API

## Lista resumida de archivos base
**Archivos mínimos para una app base con API**
1. ApiService.kt
2. RetrofitClient.kt
3. ApiConstants.kt
4. User.kt (modelo de ejemplo)
5. UserRepository.kt
6. UserViewModel.kt
7. MainActivity.kt o MainFragment.kt
8. Resource.kt (o similar)

## Estructura base recomendada (MVVM + Retrofit)

### Paquete raíz
`com.example.baseapp`

### data
Contiene todo lo relacionado con datos y API.

**data.remote**
Archivos relacionados con Retrofit y la API.
1. `ApiService.kt`: Define los endpoints (@GET, @POST, etc.)
2. `RetrofitClient.kt`: Configura Retrofit (baseUrl, converter, client)
3. `ApiConstants.kt` (opcional pero recomendado): URLs base, timeouts, headers comunes

**data.model**
Modelos de datos que vienen de la API.
4. `User.kt` (ejemplo): Data class que representa una respuesta JSON

**data.repository**
Capa intermedia entre la API y el ViewModel.
5. `UserRepository.kt`:
   * Llama a ApiService
   * Maneja la lógica de acceso a datos

### ui
Contiene la lógica de interfaz.

**ui.viewmodel**
6. `UserViewModel.kt`:
   * Expone datos a la UI
   * Usa coroutines y LiveData / StateFlow

**ui.view** (opcional si usan Activities o Fragments)
7. `MainActivity.kt / MainFragment.kt`:
   * Observa el ViewModel
   * Muestra datos en pantalla

### utils (opcional pero muy útil)
8. `Resource.kt`: Manejo de estados: Loading, Success, Error
9. `NetworkResult.kt` (alternativa moderna): Resultado genérico de llamadas API