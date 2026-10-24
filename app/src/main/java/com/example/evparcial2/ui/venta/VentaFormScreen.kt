package com.example.evparcial2.ui.venta

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun VentaFormScreen(
    ventaId: Long? = null,
    onVolver: () -> Unit = {},
    viewModel: VentaViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Modo edición: se cargan los datos de la venta seleccionada
    LaunchedEffect(ventaId) {
        if (ventaId != null) {
            viewModel.cargarVenta(ventaId)
        }
    }

    // Al guardar correctamente se vuelve a la lista (que se actualiza sola)
    LaunchedEffect(uiState.guardadoExitosamente) {
        if (uiState.guardadoExitosamente) {
            onVolver()
        }
    }

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

    val camposHabilitados = uiState.editable && !uiState.guardando

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
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
                Text(
                    text = if (uiState.esEdicion) "Modificar venta" else "Registrar venta",
                    style = MaterialTheme.typography.titleLarge
                )

                Text(text = "Estado: ${uiState.estado}")

                if (!uiState.editable) {
                    Text(
                        text = "Esta venta está validada y no se puede modificar",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                OutlinedTextField(
                    value = uiState.tipoServicio,
                    onValueChange = viewModel::onTipoServicioChange,
                    label = { Text("Tipo de servicio") },
                    enabled = camposHabilitados,
                    isError = uiState.tipoServicioError != null,
                    supportingText = {
                        Text(uiState.tipoServicioError ?: "")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = uiState.fechaOperacion,
                    onValueChange = viewModel::onFechaOperacionChange,
                    label = { Text("Fecha de operación") },
                    placeholder = { Text("dd/mm/aaaa") },
                    readOnly = true,
                    enabled = camposHabilitados,
                    isError = uiState.fechaOperacionError != null,
                    supportingText = {
                        Text(uiState.fechaOperacionError ?: "")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = uiState.cliente,
                    onValueChange = viewModel::onClienteChange,
                    label = { Text("Cliente") },
                    enabled = camposHabilitados,
                    isError = uiState.clienteError != null,
                    supportingText = {
                        Text(uiState.clienteError ?: "")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = uiState.monto,
                    onValueChange = viewModel::onMontoChange,
                    label = { Text("Monto") },
                    enabled = camposHabilitados,
                    isError = uiState.montoError != null,
                    supportingText = {
                        Text(uiState.montoError ?: "")
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
                    enabled = camposHabilitados,
                    isError = uiState.codigoError != null,
                    supportingText = {
                        Text(uiState.codigoError ?: "")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = uiState.observaciones,
                    onValueChange = viewModel::onObservacionesChange,
                    label = { Text("Observaciones") },
                    enabled = camposHabilitados,
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
                    enabled = camposHabilitados,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Escanear código QR")
                }

                Text(
                    text = if (uiState.respaldoUri == null) {
                        "Sin respaldo QR"
                    } else {
                        "Respaldo QR: ${uiState.respaldoUri}"
                    }
                )

                Button(
                    onClick = viewModel::guardarVenta,
                    enabled = camposHabilitados,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = when {
                            uiState.guardando -> "Guardando..."
                            uiState.esEdicion -> "Guardar cambios"
                            else -> "Guardar venta"
                        }
                    )
                }

                OutlinedButton(
                    onClick = onVolver,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Volver")
                }

                uiState.mensaje?.let { mensaje ->
                    Text(text = mensaje)
                }
            }
        }
    }
}
