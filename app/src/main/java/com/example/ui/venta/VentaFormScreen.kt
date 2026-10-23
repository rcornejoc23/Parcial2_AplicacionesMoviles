package com.example.ui.venta

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
    viewModel: VentaViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Registrar venta")

        OutlinedTextField(
            value = uiState.tipoServicio,
            onValueChange = {
                viewModel.onTipoServicioChange(it)
            },
            label = {
                Text("Tipo de servicio")
            },
            isError = uiState.tipoServicioError != null,
            supportingText = {
                uiState.tipoServicioError?.let { mensaje ->
                    Text(mensaje)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.fechaOperacion,
            onValueChange = {
                viewModel.onFechaOperacionChange(it)
            },
            label = {
                Text("Fecha de operación")
            },
            isError = uiState.fechaOperacionError != null,
            supportingText = {
                uiState.fechaOperacionError?.let { mensaje ->
                    Text(mensaje)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.cliente,
            onValueChange = {
                viewModel.onClienteChange(it)
            },
            label = {
                Text("Cliente")
            },
            isError = uiState.clienteError != null,
            supportingText = {
                uiState.clienteError?.let { mensaje ->
                    Text(mensaje)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.monto,
            onValueChange = {
                viewModel.onMontoChange(it)
            },
            label = {
                Text("Monto")
            },
            isError = uiState.montoError != null,
            supportingText = {
                uiState.montoError?.let { mensaje ->
                    Text(mensaje)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.codigoAutorizacion,
            onValueChange = {
                viewModel.onCodigoAutorizacionChange(it)
            },
            label = {
                Text("Código de autorización")
            },
            isError = uiState.codigoError != null,
            supportingText = {
                uiState.codigoError?.let { mensaje ->
                    Text(mensaje)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.observaciones,
            onValueChange = {
                viewModel.onObservacionesChange(it)
            },
            label = {
                Text("Observaciones")
            },
            modifier = Modifier.fillMaxWidth()
        )
        //Boton de formulario de venta
        Button(
            onClick = {
                viewModel.validarFormulario()
            }
        ) {
            Text("Validar formulario")
        }
    }
}