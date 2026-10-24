package com.example.evparcial2.ui.venta

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ListaVentasScreen(
    onNuevaVenta: () -> Unit,
    onSeleccionarVenta: (Long) -> Unit,
    onVolverMenu: () -> Unit,
    viewModel: VentaViewModel = viewModel()
) {
    // Flow de Room: la lista se actualiza sola al registrar o modificar
    val ventas by viewModel
        .obtenerVentas()
        .collectAsState(initial = emptyList())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Ventas registradas",
                style = MaterialTheme.typography.titleLarge
            )
        }

        item {
            Button(
                onClick = onNuevaVenta,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Nueva venta")
            }
        }

        item {
            OutlinedButton(
                onClick = onVolverMenu,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Volver al menú")
            }
        }

        if (ventas.isEmpty()) {
            item {
                Text(text = "No hay ventas registradas")
            }
        } else {
            items(
                items = ventas,
                key = { venta -> venta.id }
            ) { venta ->
                val editable = venta.estado != VentaUiState.ESTADO_VALIDADA

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSeleccionarVenta(venta.id) }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "Código: ${venta.codigoAutorizacion}")
                        Text(text = "Servicio: ${venta.tipoServicio}")
                        Text(text = "Fecha: ${venta.fechaOperacion}")
                        Text(text = "Cliente: ${venta.cliente}")
                        Text(text = "Monto: ${venta.monto}")
                        Text(text = "Estado: ${venta.estado}")
                        Text(
                            text = if (venta.respaldoUri == null) {
                                "Respaldo QR: No"
                            } else {
                                "Respaldo QR: Sí"
                            }
                        )
                        Text(
                            text = if (editable) {
                                "Toca para consultar o modificar"
                            } else {
                                "Validada: solo consulta"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    }
}
