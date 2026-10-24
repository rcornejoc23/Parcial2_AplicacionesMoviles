package com.example.evparcial2.ui.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MenuScreen(
    onVerVentas: () -> Unit,
    onNuevaVenta: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Menú principal",
            style = MaterialTheme.typography.titleLarge
        )

        Text(text = "Módulo de registro de ventas")

        Button(
            onClick = onVerVentas,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Ventas registradas")
        }

        Button(
            onClick = onNuevaVenta,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Registrar nueva venta")
        }

        OutlinedButton(
            onClick = onCerrarSesion,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Cerrar sesión")
        }
    }
}
