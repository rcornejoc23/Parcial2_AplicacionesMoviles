package com.example.ui.venta

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun VentaFormScreen(
    onVolver: () -> Unit = {},
    viewModel: VentaViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Registrar venta")

        OutlinedTextField(
            value = uiState.tipoServicio,
            onValueChange = viewModel::onTipoServicioChange,
            label = { Text("Tipo de servicio") },
            isError = uiState.tipoServicioError != null,
            supportingText = {
                uiState.tipoServicioError?.let { Text(it) }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.fechaOperacion,
            onValueChange = viewModel::onFechaOperacionChange,
            label = { Text("Fecha de operación") },
            isError = uiState.fechaOperacionError != null,
            supportingText = {
                uiState.fechaOperacionError?.let { Text(it) }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.cliente,
            onValueChange = viewModel::onClienteChange,
            label = { Text("Cliente") },
            isError = uiState.clienteError != null,
            supportingText = {
                uiState.clienteError?.let { Text(it) }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.monto,
            onValueChange = viewModel::onMontoChange,
            label = { Text("Monto") },
            isError = uiState.montoError != null,
            supportingText = {
                uiState.montoError?.let { Text(it) }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.codigoAutorizacion,
            onValueChange = viewModel::onCodigoAutorizacionChange,
            label = { Text("Código de autorización") },
            isError = uiState.codigoError != null,
            supportingText = {
                uiState.codigoError?.let { Text(it) }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.observaciones,
            onValueChange = viewModel::onObservacionesChange,
            label = { Text("Observaciones") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = viewModel::guardarVenta,
            enabled = !uiState.guardando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (uiState.guardando) {
                    "Guardando..."
                } else {
                    "Guardar venta"
                }
            )
        }

        Button(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Volver a la lista")
        }

        uiState.mensaje?.let { mensaje ->
            Text(text = mensaje)
        }
    }
}
