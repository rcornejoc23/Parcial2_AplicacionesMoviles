package com.example.evparcial2.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Modo claro: botones carbón, acentos infrarrojo, fondo hueso.
private val LightColorScheme = lightColorScheme(
    primary = Carbon,
    onPrimary = Blanco,
    primaryContainer = Asfalto,
    onPrimaryContainer = Blanco,

    secondary = Infrarrojo,
    onSecondary = Blanco,
    secondaryContainer = InfrarrojoSuave,
    onSecondaryContainer = Carbon,

    tertiary = VoltOscuro,
    onTertiary = Blanco,
    tertiaryContainer = Volt,
    onTertiaryContainer = Carbon,

    background = Hueso,
    onBackground = Carbon,
    surface = Hueso,
    onSurface = Carbon,
    surfaceVariant = Caja,
    onSurfaceVariant = Grafito,
    surfaceContainerLowest = Blanco,
    surfaceContainerLow = Blanco,
    surfaceContainer = Caja,
    surfaceContainerHigh = Caja,
    surfaceContainerHighest = Blanco,

    outline = Grafito,
    outlineVariant = CajaOscura,

    error = ErrorRojo,
    onError = Blanco
)

// Modo oscuro: botones volt sobre fondo noche.
private val DarkColorScheme = darkColorScheme(
    primary = Volt,
    onPrimary = Carbon,
    primaryContainer = VoltOscuro,
    onPrimaryContainer = VoltSuave,

    secondary = InfrarrojoClaro,
    onSecondary = Carbon,
    secondaryContainer = Infrarrojo,
    onSecondaryContainer = Blanco,

    tertiary = InfrarrojoClaro,
    onTertiary = Carbon,

    background = Noche,
    onBackground = Niebla,
    surface = Noche,
    onSurface = Niebla,
    surfaceVariant = NocheContenedor,
    onSurfaceVariant = NieblaTenue,
    surfaceContainerLowest = Noche,
    surfaceContainerLow = NocheSuperficie,
    surfaceContainer = NocheSuperficie,
    surfaceContainerHigh = NocheContenedor,
    surfaceContainerHighest = NocheContenedorAlto,

    outline = NieblaTenue,
    outlineVariant = Grafito,

    error = ErrorRojoClaro,
    onError = Carbon
)

@Composable
fun EvParcial2Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Desactivado para que se use la paleta de la tienda
    // en vez de los colores del fondo de pantalla del teléfono.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
