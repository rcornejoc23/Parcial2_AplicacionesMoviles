package com.example.ui.venta

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ListaVentasScreen(
    viewModel: VentaViewModel = viewModel()
) {
    val ventas by viewModel
        .obtenerVentas()
        .collectAsState(initial = emptyList())

    if (ventas.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "No hay ventas registradas")
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(text = "Ventas registradas")
            }

            items(
                items = ventas,
                key = { venta -> venta.id }
            ) { venta ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "Servicio: ${venta.tipoServicio}")
                        Text(text = "Fecha: ${venta.fechaOperacion}")
                        Text(text = "Cliente: ${venta.cliente}")
                        Text(text = "Monto: ${venta.monto}")
                        Text(
                            text = "Código: ${venta.codigoAutorizacion}"
                        )
                        Text(text = "Estado: ${venta.estado}")
                    }
                }
            }
        }
    }
}
