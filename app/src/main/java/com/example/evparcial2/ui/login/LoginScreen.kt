package com.example.evparcial2.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LoginScreen(
    onLoginExitoso: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.autenticado) {
        if (uiState.autenticado) {
            onLoginExitoso()
            viewModel.onNavegacionRealizada()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
    ) {
        Text(
            text = "Suela Urbana",
            style = MaterialTheme.typography.titleLarge
        )

        Text(text = "Registro de ventas")

        OutlinedTextField(
            value = uiState.usuario,
            onValueChange = viewModel::onUsuarioChange,
            label = { Text("Usuario") },
            singleLine = true,
            isError = uiState.usuarioError != null,
            supportingText = {
                Text(uiState.usuarioError ?: "")
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.clave,
            onValueChange = viewModel::onClaveChange,
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            isError = uiState.claveError != null,
            supportingText = {
                Text(uiState.claveError ?: "")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = viewModel::iniciarSesion,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Ingresar")
        }

        uiState.mensaje?.let { mensaje ->
            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.error
            )
        }

        Text(
            text = "Usuario de prueba: ${LoginViewModel.USUARIO_PRUEBA} / ${LoginViewModel.CLAVE_PRUEBA}",
            style = MaterialTheme.typography.labelSmall
        )
    }
}
