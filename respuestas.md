# Documento de Reflexión - Proyecto UT2: Recetario de Cocina Manchego

## Preguntas de reflexión

### 1. ¿Qué ocurre si giras el dispositivo estando en la ficha de una receta? ¿Se pierde el estado? ¿Por qué?
No se pierde el estado: sigue mostrándose la misma receta en la ficha y la interfaz adapta su diseño 
(cambiando entre disposición vertical u horizontal según la orientación). 
Esto ocurre porque, aunque al girar el dispositivo se destruye y recrea la `Activity` 
y la interfaz de usuario, el `RecetasViewModel` sobrevive a los cambios de 
configuración manteniendo los datos intactos, y el argumento de navegación 
(el `id` de la receta) se conserva en la pila de navegación (*back stack*).

### 2. ¿Por qué es importante reutilizar bloques de UI en lugar de duplicar código?
Reutilizar bloques evita repetir la misma estructura visual en distintos lugares 
(como en la ficha vertical, la ficha horizontal y la tarjeta de la lista), 
reduciendo errores y facilitando el mantenimiento, ya que si tenemos q cambiar cualquier 
cosa en el proyecto solo lo hacemos en un mismo sitio.

### 3. ¿Cómo se aplica UDF en este proyecto? Identifica los tres niveles: ViewModel, pantalla y bloques reutilizables.
En este proyecto el **Flujo Unidireccional de Datos (UDF)** 
funciona haciendo que el estado baje y los eventos suban en tres niveles:

* **ViewModel (`RecetasViewModel`):** Es la fuente única de verdad; guarda el estado de forma privada y mutable (`MutableStateFlow`) y lo expone como inmutable (`StateFlow`), ofreciendo métodos públicos (como `alternarExpandida` o `alternarFavorita`) para modificarlo.
* **Pantalla (`ListaRecetasScreen` y `FichaRecetaScreen`):** Observa el estado del `ViewModel` mediante `collectAsStateWithLifecycle()`, pasa los datos necesarios hacia abajo a sus hijos y delega los eventos hacia arriba llamando a los métodos del `ViewModel` o ejecutando la navegación.
* **Bloques reutilizables (`TarjetaReceta`, `BloqueFoto`, `BloqueResumen`, `BloqueIngredientes` y `BloquePasos`):** Son componentes sin estado que únicamente dibujan los datos que reciben por parámetro y notifican las acciones del usuario hacia arriba mediante lambdas (`onAlternar`, `onVerFicha`, etc.), sin conocer el `ViewModel` ni el `NavController`.

---

## Mejora 2: Respuesta sobre el buscador (3 líneas)

> **¿Por qué aquí el texto se guarda con `rememberSaveable` y no en el `ViewModel`? ¿Qué cambiaría si estuviera en el `ViewModel`?**

El texto del buscador es un estado local de la interfaz de lista que solo necesita sobrevivir a la rotación, por lo que `rememberSaveable` es suficiente sin añadir lógica innecesaria al `ViewModel`. Si se guardara en el `ViewModel`, el filtro se mantendría también al navegar hacia la ficha de una receta y volver atrás, además de poder gestionarse o compartirse desde el propio `ViewModel`.