package com.example.evparcial2.ui.venta

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private fun fechaActual(): String {
    return SimpleDateFormat(
        "dd/MM/yyyy",
        Locale.getDefault()
    ).format(Date())
}

data class VentaUiState(
    // Venta que se está editando (null = venta nueva)
    val ventaId: Long? = null,

    val tipoServicio: String = "",
    val fechaOperacion: String = fechaActual(),
    val cliente: String = "",
    val monto: String = "",
    val codigoAutorizacion: String = "",
    val respaldoUri: String? = null,
    val estado: String = ESTADO_BORRADOR,
    val observaciones: String = "",

    val tipoServicioError: String? = null,
    val fechaOperacionError: String? = null,
    val clienteError: String? = null,
    val montoError: String? = null,
    val codigoError: String? = null,

    val cargando: Boolean = false,
    val guardando: Boolean = false,
    val guardadoExitosamente: Boolean = false,
    val mensaje: String? = null
) {
    val esEdicion: Boolean
        get() = ventaId != null

    // Una venta solo se puede modificar mientras no esté validada
    val editable: Boolean
        get() = estado != ESTADO_VALIDADA

    companion object {
        const val ESTADO_BORRADOR = "Borrador"
        const val ESTADO_VALIDADA = "Validada"
    }
}
