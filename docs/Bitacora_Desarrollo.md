# Bitácora de desarrollo: aplicación móvil de registro de ventas

Este documento registra el avance del proyecto y las explicaciones realizadas
durante el aprendizaje. Se actualizará paso a paso.

## 1. Objetivo de la aplicación

La primera entrega se enfocará en el módulo de registro de ventas ficticias.
La aplicación deberá permitir registrar, consultar y modificar ventas mientras
no estén validadas.

El alcance indicado en los lineamientos incluye:

- Lista de ventas.
- Formulario para crear y modificar una venta.
- Validaciones visibles.
- Arquitectura MVVM.
- Estado de pantalla mediante `UiState` y `StateFlow`.
- Persistencia local con Room.
- Uso de la cámara con información ficticia.

No se implementará todo el caso completo desde el comienzo. Se avanzará por
partes pequeñas para comprender cada componente.

## 2. Forma de trabajo

- El código se explicará antes de aumentar la complejidad.
- Se utilizarán nombres y estructuras sencillas.
- No se realizarán commits automáticamente.
- Los commits se harán solamente cuando el estudiante lo indique.
- Las evidencias y los conceptos importantes se irán registrando aquí.

## 3. Primer concepto: pantalla Compose

Una función marcada con `@Composable` representa una parte de la interfaz de
usuario. Por ejemplo, `ListaVentasScreen` representa la pantalla donde luego
se mostrarán las ventas guardadas.

## 4. Segundo concepto: `VentaUiState`

`VentaUiState` es una clase que guarda temporalmente la información que el
usuario está escribiendo o viendo en una pantalla.

```kotlin
data class VentaUiState(
    val tipoServicio: String = "",
    val fechaOperacion: String = "",
    val cliente: String = "",
    val monto: String = "",
    val codigoAutorizacion: String = "",
    val observaciones: String = "",
    val estado: String = "Borrador"
)
```

Esta clase no es una base de datos. Sus propiedades son datos temporales en
memoria. Si la aplicación se cierra, estos datos se pierden.

### Diferencia entre los componentes

```text
UiState       = información actual del formulario
Entity        = estructura de una venta que se guardará
Dao           = operaciones para guardar y consultar
Room          = almacenamiento permanente de la aplicación
```

Más adelante, `VentaEntity` representará una venta guardada en Room y
`VentaDao` definirá las operaciones sobre esas ventas.

## 5. `VentaViewModel` y `StateFlow`

El `ViewModel` será el intermediario entre la pantalla y el estado. La pantalla
no cambiará directamente los datos: llamará funciones del `ViewModel`.

`StateFlow` permite que la interfaz reciba automáticamente los cambios del
estado.

La idea general será:

```text
Usuario escribe -> Screen llama al ViewModel -> ViewModel actualiza UiState
                -> StateFlow avisa a la Screen -> Screen se redibuja
```

Todavía no se guardarán datos en Room. En esta etapa solamente se aprenderá a
modificar el estado del formulario.

## 6. Próximo paso

Conectar el `VentaViewModel` con `VentaFormScreen` para que los campos del
formulario puedan leer y modificar el estado.

## 7. Repositorios usados como referencia

Se revisaron dos proyectos de referencia:

### Repositorio del profesor

Ayuda a comprender:

- Organización general del proyecto.
- `MainActivity` y navegación.
- Pantallas de login y menú.
- Uso básico de `UiState`, `ViewModel` y `Repository`.

### Repositorio `CasoSemestral_App_Moviles`

Ayuda a comprender las etapas más avanzadas:

- `VentaEntity`.
- `VentaDao`.
- `AppDatabase` con Room.
- `VentaRepository`.
- Lista y formularios de ventas.
- Integración de cámara y lectura de QR.

Estos proyectos serán referencias de aprendizaje, no código para copiar
completo. La aplicación propia se desarrollará gradualmente y con una
organización sencilla.

## 8. Conexión entre `VentaFormScreen` y `VentaViewModel`

La pantalla del formulario lee los valores desde `VentaUiState` y llama al
`VentaViewModel` cuando el usuario escribe.

La pantalla no modifica directamente el estado. Por ejemplo, al escribir el
cliente, la pantalla llama `onClienteChange`, y el `ViewModel` crea una copia
actualizada del estado.

En esta etapa los datos todavía viven en memoria. Room se agregará después,
cuando el formulario y sus validaciones básicas estén funcionando.

## 9. Explicación básica de `VentaViewModel`

`VentaViewModel` es la clase que coordina los cambios del formulario. La
pantalla no modificará directamente sus datos.

```kotlin
class VentaViewModel : ViewModel()
```

Esto indica que la clase es un `ViewModel` de Android. Su información puede
conservarse durante cambios de configuración, como una rotación de pantalla.

```kotlin
private val _uiState = MutableStateFlow(VentaUiState())
```

Se crea un estado inicial usando `VentaUiState()`. `MutableStateFlow` permite
que el `ViewModel` cambie ese estado. El prefijo `_` indica que es una
propiedad privada.

```kotlin
val uiState: StateFlow<VentaUiState> = _uiState.asStateFlow()
```

Se expone una versión que la pantalla puede observar, pero no modificar. Así
el `ViewModel` mantiene el control de los cambios.

```kotlin
_uiState.value = _uiState.value.copy(monto = valor)
```

`copy` crea una nueva versión del estado conservando los demás campos y
cambiando únicamente el monto.

Las funciones `onTipoServicioChange`, `onClienteChange` y las demás reciben lo
que el usuario escribe y actualizan el campo correspondiente.

## 10. Primera conexión entre pantalla y `ViewModel`

Se comenzará con un solo campo para comprobar el flujo completo. La pantalla
lee `tipoServicio` desde `uiState` y envía cada cambio al método
`onTipoServicioChange` del `ViewModel`.

Cuando esta conexión funcione, se agregarán los demás campos uno por uno.

## 11. Error de dependencia: `Unresolved reference: viewModel`

La función `viewModel()` pertenece a la librería
`androidx.lifecycle:lifecycle-viewmodel-compose`. Si el proyecto no incluye
esa dependencia, el código puede importar `ViewModel`, pero Compose no
reconoce la función que obtiene una instancia del `ViewModel`.

La solución es agregar la dependencia en el bloque `dependencies` del módulo
`app` y sincronizar Gradle. El aviso sobre una versión nueva de Gradle es
independiente de este error.

## 12. Segundo campo del formulario

Se agregó el campo `fechaOperacion` usando el mismo flujo que
`tipoServicio`. El valor se lee desde `uiState` y cada cambio se envía al
método `onFechaOperacionChange` del `VentaViewModel`.

## 13. Tercer campo del formulario

Se agregó el campo `cliente`. El texto escrito se envía mediante
`onClienteChange` y queda reflejado en `VentaUiState`.

## 14. Campo numérico: `monto`

El campo `monto` se mantiene como `String` mientras el usuario escribe, pero
se configura el teclado numérico mediante `KeyboardType.Number`. La validación
de que sea un número mayor que cero se agregará en una etapa posterior.

## 15. Campo `codigoAutorizacion`

El código de autorización se conecta al método `onCodigoAutorizacionChange` del
`ViewModel`. Más adelante se validará que sea obligatorio y que no esté
duplicado en los registros almacenados.

## 26. Comparación con el repositorio de referencia

El `VentaDao` del repositorio `CasoSemestral_App_Moviles` utiliza los mismos
elementos principales:

- `@Dao` para declarar el acceso a Room.
- `@Insert`, `@Update` y `@Delete`.
- Métodos `suspend` para operaciones de escritura.
- `@Query` para consultar datos.
- `Flow<List<VentaEntity>>` para mantener listas reactivas.

El `Dao` propio se simplifica para la primera entrega y agrega la búsqueda por
código de autorización, necesaria para controlar duplicados.

La versión revisada de `VentaDao.kt` fue aprobada e incorporada al workspace.

## 28. Repository de ventas

Se incorporó `VentaRepository` como intermediario entre el `ViewModel` y
`VentaDao`. Expone operaciones para guardar, consultar, actualizar, eliminar y
buscar códigos de autorización sin que la interfaz conozca directamente Room.

## 27. Base de datos Room de ventas

Se incorporó `AppDatabase` con una sola entidad, `VentaEntity`, y acceso a
través de `VentaDao`. La base se crea como un singleton para evitar múltiples
instancias innecesarias durante la ejecución de la aplicación.

## 16. Campo `observaciones`

El campo de observaciones completa la información básica del formulario. En
esta etapa se comporta como texto libre y se actualiza mediante
`onObservacionesChange`.

## 17. Mostrar la pantalla propia desde `MainActivity`

La aplicación seguía mostrando `Hello Android` porque `MainActivity` todavía
llamaba la función de ejemplo `Greeting`. Para mostrar la aplicación propia,
`setContent` debe llamar `VentaFormScreen` dentro del tema visual del proyecto.

## 18. Primera validación: tipo de servicio obligatorio

La validación se separa en tres responsabilidades:

- `VentaUiState` guarda el mensaje de error.
- `VentaViewModel` revisa si el campo está vacío.
- `VentaFormScreen` muestra visualmente el error.

Esta separación permite que la pantalla no contenga las reglas de negocio.

La primera validación fue comprobada dejando `tipoServicio` vacío y
presionando el botón. El mensaje apareció debajo del campo, confirmando el
flujo completo entre `UiState`, `ViewModel` y pantalla.

## 19. Validación de fecha de operación

La fecha de operación también será obligatoria. Se agregará un mensaje de
error independiente para no mezclar el estado de un campo con el de otro.

## 20. Validación del cliente ficticio

El campo `cliente` también será obligatorio. Se agregará
`clienteError` al `VentaUiState`, una función de validación en el
`ViewModel` y la presentación visual del mensaje en la pantalla.

## 21. Validación del monto

El monto se mantiene como texto mientras el usuario escribe, pero al validar
se transforma con `toDoubleOrNull()`. Así se pueden distinguir tres casos:

- Campo vacío.
- Texto que no representa un número.
- Número menor o igual a cero.

## 22. Validación del código de autorización

En esta etapa se valida que el código no esté vacío. La validación de códigos
duplicados depende de consultar los registros persistidos, por lo que se
implementará después de agregar Room y el `Dao`.

## 23. Validación centralizada del formulario

La pantalla no debería conocer cada regla de validación. En lugar de llamar
cinco métodos desde el botón, `VentaViewModel` ofrecerá un único método
`validarFormulario()` que coordinará todas las validaciones y devolverá si el
formulario es válido.

## 24. Próxima etapa: persistencia con Room

El siguiente bloque será guardar las ventas de forma permanente. Antes de
agregar Room se revisarán las versiones de Kotlin, Gradle y KSP del proyecto,
porque el compilador de Room debe ser compatible con ellas.

El orden será:

1. Configurar las dependencias de Room.
2. Crear `VentaEntity`.
3. Crear `VentaDao`.
4. Crear `AppDatabase`.
5. Conectar el `Repository` y el `ViewModel`.

La configuración revisada usa Kotlin `2.2.10`. Para Room se utilizará KSP
`2.2.10-2.0.2` y Room `2.8.5`, manteniendo el compilador de Room compatible
con Kotlin.

Durante esta configuración apareció un error TOML porque las tablas inline de
las dependencias se escribieron en varias líneas. La sintaxis correcta usa una
línea completa por definición.

## 25. Primera entidad de Room: `VentaEntity`

`VentaEntity` representa una fila de la tabla de ventas. A diferencia de
`VentaUiState`, que contiene texto temporal del formulario, la entidad contiene
los datos preparados para persistirse.

El monto se escribe como `String` en el formulario, pero se guardará como
`Double` en la entidad después de validarlo y convertirlo.

## 29. Conexión del `ViewModel` con Room

`VentaViewModel` mantiene su nombre y ahora utiliza `AndroidViewModel` para
obtener el contexto de la aplicación. Con ese contexto obtiene `AppDatabase`,
crea el `VentaRepository` y puede guardar una venta en Room.

El método `guardarVenta()` primero ejecuta las validaciones, revisa que el
código de autorización no esté repetido y luego transforma los textos del
formulario en un objeto `VentaEntity`. El estado también informa si la venta
se está guardando y muestra el resultado de la operación.

## 30. Conexión del formulario con el guardado

`VentaFormScreen` ahora llama a `guardarVenta()` cuando se presiona el botón.
También muestra los errores debajo de cada campo, desactiva el botón mientras
se guarda la información y presenta el mensaje final de la operación.

Se agregó desplazamiento vertical para que el formulario pueda utilizarse en
pantallas pequeñas sin ocultar sus campos.

## 31. Lista de ventas

Se completó `ListaVentasScreen` usando un `LazyColumn`. La pantalla observa
las ventas guardadas mediante el `Flow` entregado por `VentaViewModel` y Room.
Cada registro muestra el servicio, fecha, cliente, monto, código de
autorización y estado. Si todavía no existen registros, se informa al usuario
con un mensaje simple.

## 32. Navegación entre pantallas

Se agregó Navigation Compose para conectar `ListaVentasScreen` y
`VentaFormScreen`. La lista es la pantalla inicial, el botón `Nueva venta`
abre el formulario y `Volver a la lista` regresa a la pantalla anterior.

La navegación se definió en `MainActivity` mediante las rutas `lista` y
`formulario`. La dependencia se agregó al catálogo de versiones usando una
definición TOML en una sola línea.

## 33. Lector QR con CameraX

Se integró CameraX para mostrar la cámara trasera dentro del formulario y ML
Kit para analizar los cuadros de la cámara. El lector está configurado para
detectar únicamente códigos QR.

La aplicación solicita el permiso de cámara cuando el usuario presiona
`Escanear código QR`. Al detectar un código, su contenido se guarda en el
estado del formulario y luego se persiste en `VentaEntity` como respaldo de la
venta. La lista también indica si cada registro tiene respaldo QR.

