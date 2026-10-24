package com.example.evparcial2.ui.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Login simulado para la primera entrega.
 * La autenticación real se implementa en el examen transversal.
 */
class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onUsuarioChange(valor: String) {
        _uiState.value = _uiState.value.copy(
            usuario = valor,
            usuarioError = null,
            mensaje = null
        )
    }

    fun onClaveChange(valor: String) {
        _uiState.value = _uiState.value.copy(
            clave = valor,
            claveError = null,
            mensaje = null
        )
    }

    fun iniciarSesion() {
        val estado = _uiState.value
        val usuario = estado.usuario.trim()
        val clave = estado.clave

        val usuarioError = if (usuario.isEmpty()) "El usuario es obligatorio" else null
        val claveError = if (clave.isEmpty()) "La contraseña es obligatoria" else null

        if (usuarioError != null || claveError != null) {
            _uiState.value = estado.copy(
                usuarioError = usuarioError,
                claveError = claveError
            )
            return
        }

        if (usuario == USUARIO_PRUEBA && clave == CLAVE_PRUEBA) {
            _uiState.value = estado.copy(autenticado = true, mensaje = null)
        } else {
            _uiState.value = estado.copy(mensaje = "Usuario o contraseña incorrectos")
        }
    }

    // Se llama después de navegar para que no vuelva a disparar la navegación
    fun onNavegacionRealizada() {
        _uiState.value = LoginUiState()
    }

    companion object {
        // Credenciales ficticias de prueba
        const val USUARIO_PRUEBA = "vendedor"
        const val CLAVE_PRUEBA = "1234"
    }
}
