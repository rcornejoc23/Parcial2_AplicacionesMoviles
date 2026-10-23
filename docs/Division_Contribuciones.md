# División de contribuciones

Este documento registra cómo se distribuyen los cambios del proyecto entre
Renato Cornejo y Eliot Ibacache.

## Identidades Git

| Integrante | Correo |
|---|---|
| Renato Cornejo | re.cornejoc@duocuc.cl |
| Eliot Ibacache | eli.ibacache@duoc.cl |

## Forma de trabajo

- El código se explica antes de incorporarlo al workspace.
- Renato y Eliot revisan o aprueban los cambios antes de registrarlos.
- Los commits se crean solamente cuando corresponda dentro del avance.
- Cada commit se atribuye a uno de los dos integrantes.
- La división puede alternarse; no es necesario mantener un orden fijo.
- Cada entrada incluirá autor, fecha, mensaje, archivos y propósito.
- No se atribuirán cambios que no hayan sido aprobados o revisados.

## Registro

| Nº | Fecha | Autor | Commit | Mensaje | Archivos principales | Propósito |
|---:|---|---|---|---|---|---|
| 1 | 2026-10-23 | Renato Cornejo | `7c0580a` | `feat: inicializa aplicacion de registro de ventas` | Proyecto Android, Compose, pantallas iniciales y bitácora | Dejar funcionando la base del proyecto y el formulario inicial. |
| 2 | 2026-10-23 | Eliot Ibacache | `20d74a6` | `feat: agrega persistencia local de ventas` | `VentaEntity`, `VentaDao`, `AppDatabase` | Preparar la persistencia local de ventas con Room. |
| 3 | 2026-10-23 | Eliot Ibacache | `2eb53f0` | `feat: agrega repositorio de ventas` | `VentaRepository`, bitácora | Separar el acceso a Room de la lógica de presentación. |
| 4 | 2026-10-23 | Renato Cornejo | `66f69f6` | `feat: conecta el viewmodel con room` | `VentaUiState`, `VentaViewModel` | Conectar el formulario con el repositorio y guardar ventas validadas. |
| 5 | 2026-10-23 | Eliot Ibacache | `4f31df6` | `feat: conecta formulario con guardado de ventas` | `VentaFormScreen` | Conectar el botón con el guardado y mostrar el resultado al usuario. |
| 6 | 2026-10-23 | Renato Cornejo | `feb10e5` | `feat: agrega lista de ventas persistidas` | `ListaVentasScreen`, `VentaViewModel` | Mostrar los registros guardados mediante `Flow`. |
| 7 | 2026-10-23 | Eliot Ibacache | `053690e` | `feat: agrega navegacion entre pantallas` | `MainActivity`, pantallas de ventas y configuración de navegación | Conectar la lista y el formulario mediante rutas de navegación. |
| 8 | 2026-10-23 | Renato Cornejo | `fb16398` | `feat: integra lector qr con camerax` | CameraX, ML Kit, `QrScanner`, formulario y persistencia | Leer códigos QR y guardar su contenido como respaldo de la venta. |

