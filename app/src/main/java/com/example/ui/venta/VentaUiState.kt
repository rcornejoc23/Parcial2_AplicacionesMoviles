package com.example.ui.venta

data class VentaUiState(
    val tipoServicio: String = "",
    val fechaOperacion: String = "",
    val cliente: String = "",
    val monto: String = "",
    val codigoAutorizacion: String = "",
    val observaciones: String = "",
    val estado: String = "Borrador",

    val tipoServicioError: String? = null,
    val fechaOperacionError: String? = null,
    val clienteError: String? = null,
    val montoError: String? = null,
    val codigoError: String? = null,

    val guardando: Boolean = false,
    val mensaje: String? = null
)
