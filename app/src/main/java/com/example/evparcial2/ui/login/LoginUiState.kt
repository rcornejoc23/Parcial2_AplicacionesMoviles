package com.example.evparcial2.ui.login

data class LoginUiState(
    val usuario: String = "",
    val clave: String = "",
    val usuarioError: String? = null,
    val claveError: String? = null,
    val mensaje: String? = null,
    val autenticado: Boolean = false
)
