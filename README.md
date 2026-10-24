# Suela Urbana · Registro de ventas

Primera entrega de DSY1105 (caso 004D): módulo de **Registro de Ventas** con
MVVM, Room, navegación, validaciones, UiState + StateFlow y cámara (lectura de QR ficticio).

## Flujo de navegación

`MainActivity` → `AppNavigation` → **Login** → **Menú** → **ListaVentasScreen** → **VentaFormScreen**

- Usuario de prueba: `vendedor` / contraseña `1234` (credenciales ficticias).
- Desde la lista, al tocar una venta se abre el formulario en modo edición
  (solo si la venta no está en estado "Validada").

## Estructura

```
com.example.evparcial2/
├── data/
│   ├── local/
│   │   ├── VentaEntity.kt      Tabla "ventas" de Room
│   │   ├── VentaDao.kt         Consultas (insertar, actualizar, buscar, duplicados)
│   │   └── AppDatabase.kt      Base de datos Room (singleton)
│   └── repository/
│       └── VentaRepository.kt  Único punto de acceso a los datos para el ViewModel
├── ui/
│   ├── login/                  LoginScreen + LoginViewModel + LoginUiState
│   ├── menu/                   MenuScreen
│   ├── venta/
│   │   ├── ListaVentasScreen.kt
│   │   ├── VentaFormScreen.kt  Crear y editar venta
│   │   ├── VentaUiState.kt
│   │   ├── VentaViewModel.kt   Validaciones y lógica (la vista no accede a datos)
│   │   └── QrScanner.kt        CameraX + ML Kit
│   ├── navigation/
│   │   └── AppNavigation.kt
│   └── theme/                  Paleta "Suela Urbana"
└── MainActivity.kt
```

## Validaciones (en `VentaViewModel`)

| Campo | Regla |
|---|---|
| Tipo de servicio | Obligatorio |
| Fecha de operación | Obligatoria |
| Cliente | Obligatorio |
| Monto | Obligatorio, numérico y mayor que 0 |
| Código de autorización | Obligatorio y no duplicado (al editar se ignora la propia venta) |
| Estado | Una venta nueva inicia como "Borrador" |

Los errores se muestran en cada campo con `isError` y `supportingText`.

## Cómo probar cada criterio

1. **Persistencia (Room):** registrar una venta, cerrar la app por completo y volver a abrirla: la venta sigue en la lista.
2. **Reactividad:** al guardar o modificar, se vuelve a la lista y el cambio aparece sin recargar (Flow de Room).
3. **Edición:** tocar una venta, cambiar el monto y guardar.
4. **Código duplicado:** intentar registrar otra venta con un código existente: aparece el error en el campo.
5. **Cámara:** "Escanear código QR" → leer un QR ficticio. El contenido queda como respaldo de la venta
   y, si el código de autorización está vacío, se completa con el QR.

Solo se usan datos ficticios (clientes, montos, códigos y QR de prueba).
