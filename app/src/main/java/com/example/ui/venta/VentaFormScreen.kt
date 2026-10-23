package com.example.ui.venta

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun VentaFormScreen(
    onVolver: () -> Unit = {},
    viewModel: VentaViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var mostrarScanner by remember {
        mutableStateOf(false)
    }

    var tienePermisoCamara by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permisoLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { permisoConcedido ->
        tienePermisoCamara = permisoConcedido

        if (permisoConcedido) {
            mostrarScanner = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (mostrarScanner && tienePermisoCamara) {
            Text(text = "Enfoca un código QR")

            QrScanner(
                onQrCodeScanned = { codigo ->
                    viewModel.onRespaldoQrChange(codigo)
                    mostrarScanner = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 12.dp)
            )

            Button(
                onClick = {
                    mostrarScanner = false
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Cancelar")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
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
                    onClick = {
                        if (tienePermisoCamara) {
                            mostrarScanner = true
                        } else {
                            permisoLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Escanear código QR")
                }

                Text(
                    text = if (uiState.respaldoQr == null) {
                        "Sin respaldo QR"
                    } else {
                        "Respaldo QR agregado"
                    }
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
    }
}
