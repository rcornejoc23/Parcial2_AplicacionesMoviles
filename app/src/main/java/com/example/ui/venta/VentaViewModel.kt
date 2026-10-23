package com.example.ui.venta

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class VentaViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(VentaUiState())

    val uiState: StateFlow<VentaUiState> = _uiState.asStateFlow()

    fun onTipoServicioChange(valor: String) {
        _uiState.value = _uiState.value.copy(
            tipoServicio = valor,
            tipoServicioError = null
        )
    }

    fun onFechaOperacionChange(valor: String) {
        _uiState.value = _uiState.value.copy(
            fechaOperacion = valor,
            fechaOperacionError = null
        )
    }

    fun onClienteChange(valor: String) {
        _uiState.value = _uiState.value.copy(
            cliente = valor,
            clienteError = null
        )
    }

    fun onMontoChange(valor: String) {
        _uiState.value = _uiState.value.copy(
            monto = valor,
            montoError = null
        )
    }

    fun onCodigoAutorizacionChange(valor: String) {
        _uiState.value = _uiState.value.copy(
            codigoAutorizacion = valor,
            codigoError = null
        )
    }

    fun onObservacionesChange(valor: String) {
        _uiState.value = _uiState.value.copy(
            observaciones = valor
        )
    }
    //Funciones de Validaciones
    fun validarTipoServicio(): Boolean {
        val valor = _uiState.value.tipoServicio.trim()

        return if (valor.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                tipoServicioError = "El tipo de servicio es obligatorio"
            )
            false
        } else {
            _uiState.value = _uiState.value.copy(
                tipoServicioError = null
            )
            true
        }
    }
    fun validarFechaOperacion(): Boolean {
        val valor = _uiState.value.fechaOperacion.trim()

        return if (valor.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                fechaOperacionError = "La fecha de operación es obligatoria"
            )
            false
        } else {
            _uiState.value = _uiState.value.copy(
                fechaOperacionError = null
            )
            true
        }
    }
    fun validarCliente(): Boolean {
        val valor = _uiState.value.cliente.trim()

        return if (valor.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                clienteError = "El cliente es obligatorio"
            )
            false
        } else {
            _uiState.value = _uiState.value.copy(
                clienteError = null
            )
            true
        }
    }
    fun validarMonto(): Boolean {
        val valorTexto = _uiState.value.monto.trim()
        val valorNumero = valorTexto.toDoubleOrNull()

        return when {
            valorTexto.isEmpty() -> {
                _uiState.value = _uiState.value.copy(
                    montoError = "El monto es obligatorio"
                )
                false
            }

            valorNumero == null -> {
                _uiState.value = _uiState.value.copy(
                    montoError = "El monto debe ser numérico"
                )
                false
            }

            valorNumero <= 0 -> {
                _uiState.value = _uiState.value.copy(
                    montoError = "El monto debe ser mayor que cero"
                )
                false
            }

            else -> {
                _uiState.value = _uiState.value.copy(
                    montoError = null
                )
                true
            }
        }
    }
    fun validarCodigoAutorizacion(): Boolean {
        val valor = _uiState.value.codigoAutorizacion.trim()

        return if (valor.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                codigoError = "El código de autorización es obligatorio"
            )
            false
        } else {
            _uiState.value = _uiState.value.copy(
                codigoError = null
            )
            true
        }
    }
    fun validarFormulario(): Boolean {
        val tipoServicioValido = validarTipoServicio()
        val fechaValida = validarFechaOperacion()
        val clienteValido = validarCliente()
        val montoValido = validarMonto()
        val codigoValido = validarCodigoAutorizacion()

        return tipoServicioValido &&
                fechaValida &&
                clienteValido &&
                montoValido &&
                codigoValido
    }
}