# Recetario de Cocina Manchego

Aplicación móvil desarrollada en **Android con Kotlin y Jetpack Compose** que presenta un catálogo interactivo de recetas tradicionales de Castilla-La Mancha. Este proyecto integra arquitectura **MVVM**, **Flujo Unidireccional de Datos (UDF)**, navegación entre pantallas, animaciones reactivas al estado, adaptabilidad a la orientación del dispositivo e internacionalización.

---

## Características principales

* **Listado interactivo de recetas:** Muestra 10 recetas tradicionales manchegas mediante `LazyColumn` con claves únicas (`key`), mostrando imagen, título, tiempo de preparación y dificultad.
* **Tarjetas expandibles animadas:** Al pulsar una receta se despliega su comarca y el acceso a la ficha completa (solo una abierta a la vez), combinando `animateColorAsState`, `animateContentSize` y `AnimatedVisibility`.
* **Recetas favoritas (Mejora 1):** Permite marcar y desmarcar recetas como favoritas tanto desde la tarjeta de la lista como desde la barra superior de la ficha, con animación de color y estado sincronizado en tiempo real.
* **Buscador por nombre (Mejora 2):** Filtrado instantáneo sin distinción entre mayúsculas y minúsculas mediante `OutlinedTextField`, botón de borrado rápido, aviso cuando no hay coincidencias y conservación del texto al girar el dispositivo (`rememberSaveable`).
* **Ficha de detalle adaptativa:** Responde dinámicamente a la orientación de la pantalla (`LocalConfiguration`), organizando el contenido en vertical (imagen arriba, datos debajo) o en horizontal (40 % imagen a la izquierda, 60 % datos a la derecha) reutilizando los mismos bloques de interfaz.
* **Internacionalización (i18n) y tema dinámico:** Interfaz externa localizada en **español** (por defecto) e **inglés**, y colores basados íntegramente en `MaterialTheme.colorScheme` con soporte para modo claro y modo oscuro.

---

## Arquitectura y estructura del proyecto

La aplicación sigue el patrón **MVVM (Model-View-ViewModel)** junto con **UDF**, separando responsabilidades en capas bien definidas:

```text
com.example.recetariomanchego/
├── data/
│   ├── Receta.kt              # Enum Dificultad y data class inmutable Receta
│   └── RecetasData.kt         # Fuente de datos simulada con las 10 recetas
├── ui/
│   ├── RecetasViewModel.kt    # Estado observable (StateFlow) y lógica de negocio
│   ├── ComponentesReceta.kt   # Bloques sin estado (TarjetaReceta, BloqueFoto, etc.)
│   ├── PantallasReceta.kt     # ListaRecetasScreen y FichaRecetaScreen (adaptativa)
│   └── theme/                 # Tema, tipografías y paleta Material Design 3
└── MainActivity.kt            # Punto de entrada y grafo de navegación (NavHost)
```

### Flujo de datos y navegación

* **`RecetasViewModel`** se instancia una única vez en `AppNavigation` (fuera del `NavHost`) para que la lista y la ficha compartan la misma instancia y sobrevivan a los cambios de configuración.
* **`NavHost` y `NavController`** gestionan las rutas centralizadas (`Screen.Lista` y `Screen.Ficha`), pasando el identificador de la receta como argumento entero (`NavType.IntType`).
* **Componentes sin estado (*Stateless*):** `TarjetaReceta`, `BloqueFoto`, `BloqueResumen`, `BloqueIngredientes` y `BloquePasos` reciben únicamente datos y lambdas, facilitando su reutilización y previsualización (`@Preview`).

---

## Tecnologías y dependencias

* **Lenguaje:** Kotlin
* **UI Toolkit:** Jetpack Compose con Material Design 3
* **SDK mínimo:** API 24 (Android 7.0)
* **Librerías principales:**
  * `androidx.navigation:navigation-compose`
  * `androidx.lifecycle:lifecycle-viewmodel-compose`
  * `androidx.lifecycle:lifecycle-runtime-compose`

---

## Instalación y ejecución

1. Clona o descomprime el repositorio del proyecto.
2. Ábrelo en **Android Studio** (recomendado Ladybug o superior).
3. Espera a que **Gradle** descargue las dependencias y sincronice el proyecto.
4. Ejecuta la aplicación en un emulador o dispositivo físico con Android 7.0 (API 24) o superior.

---

## Verificación y pruebas recomendadas

* **Navegación y estado:** Expandir una receta en la lista, entrar a su ficha, marcarla como favorita y volver atrás para comprobar que tanto el estado expandido como el favorito se conservan.
* **Rotación de pantalla (`Ctrl + F11`):** Rotar el emulador tanto en la lista (con búsqueda activa y tarjeta expandida) como en la ficha de detalle para verificar la persistencia del estado y el cambio a diseño horizontal.
* **Idioma y tema:** Cambiar el idioma del dispositivo/emulador a inglés y alternar entre modo claro y oscuro para validar las traducciones y el contraste de `MaterialTheme`.

---
Las respuestas a las preguntas estan en este recurso [Respuestas](./respuestas.md)