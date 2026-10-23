package com.example.ui.venta

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.evparcial2.data.local.AppDatabase
import com.example.evparcial2.data.local.VentaEntity
import com.example.evparcial2.data.repository.VentaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VentaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: VentaRepository

    private val _uiState = MutableStateFlow(VentaUiState())
    val uiState: StateFlow<VentaUiState> = _uiState.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = VentaRepository(database.ventaDao())
    }

    fun onTipoServicioChange(valor: String) {
        actualizarEstado {
            copy(tipoServicio = valor, tipoServicioError = null, mensaje = null)
        }
    }

    fun onFechaOperacionChange(valor: String) {
        actualizarEstado {
            copy(fechaOperacion = valor, fechaOperacionError = null, mensaje = null)
        }
    }

    fun onClienteChange(valor: String) {
        actualizarEstado {
            copy(cliente = valor, clienteError = null, mensaje = null)
        }
    }

    fun onMontoChange(valor: String) {
        actualizarEstado {
            copy(monto = valor, montoError = null, mensaje = null)
        }
    }

    fun onCodigoAutorizacionChange(valor: String) {
        actualizarEstado {
            copy(codigoAutorizacion = valor, codigoError = null, mensaje = null)
        }
    }

    fun onObservacionesChange(valor: String) {
        actualizarEstado {
            copy(observaciones = valor, mensaje = null)
        }
    }

    fun validarTipoServicio(): Boolean {
        val valor = _uiState.value.tipoServicio.trim()

        return if (valor.isEmpty()) {
            actualizarEstado {
                copy(tipoServicioError = "El tipo de servicio es obligatorio")
            }
            false
        } else {
            actualizarEstado { copy(tipoServicioError = null) }
            true
        }
    }

    fun validarFechaOperacion(): Boolean {
        val valor = _uiState.value.fechaOperacion.trim()

        return if (valor.isEmpty()) {
            actualizarEstado {
                copy(fechaOperacionError = "La fecha de operación es obligatoria")
            }
            false
        } else {
            actualizarEstado { copy(fechaOperacionError = null) }
            true
        }
    }

    fun validarCliente(): Boolean {
        val valor = _uiState.value.cliente.trim()

        return if (valor.isEmpty()) {
            actualizarEstado {
                copy(clienteError = "El cliente es obligatorio")
            }
            false
        } else {
            actualizarEstado { copy(clienteError = null) }
            true
        }
    }

    fun validarMonto(): Boolean {
        val valorTexto = _uiState.value.monto.trim()
        val valorNumero = valorTexto.toDoubleOrNull()

        return when {
            valorTexto.isEmpty() -> {
                actualizarEstado { copy(montoError = "El monto es obligatorio") }
                false
            }

            valorNumero == null -> {
                actualizarEstado { copy(montoError = "El monto debe ser numérico") }
                false
            }

            valorNumero <= 0 -> {
                actualizarEstado {
                    copy(montoError = "El monto debe ser mayor que cero")
                }
                false
            }

            else -> {
                actualizarEstado { copy(montoError = null) }
                true
            }
        }
    }

    fun validarCodigoAutorizacion(): Boolean {
        val valor = _uiState.value.codigoAutorizacion.trim()

        return if (valor.isEmpty()) {
            actualizarEstado {
                copy(codigoError = "El código de autorización es obligatorio")
            }
            false
        } else {
            actualizarEstado { copy(codigoError = null) }
            true
        }
    }

    fun validarFormulario(): Boolean {
        val tipoValido = validarTipoServicio()
        val fechaValida = validarFechaOperacion()
        val clienteValido = validarCliente()
        val montoValido = validarMonto()
        val codigoValido = validarCodigoAutorizacion()

        return tipoValido &&
                fechaValida &&
                clienteValido &&
                montoValido &&
                codigoValido
    }

    fun guardarVenta() {
        if (!validarFormulario()) {
            return
        }

        viewModelScope.launch {
            actualizarEstado {
                copy(guardando = true, mensaje = null)
            }

            val datos = _uiState.value
            val codigoExiste = repository.codigoExiste(
                datos.codigoAutorizacion.trim()
            )

            if (codigoExiste) {
                actualizarEstado {
                    copy(
                        codigoError = "El código de autorización ya existe",
                        guardando = false
                    )
                }
                return@launch
            }

            val venta = VentaEntity(
                tipoServicio = datos.tipoServicio.trim(),
                fechaOperacion = datos.fechaOperacion.trim(),
                cliente = datos.cliente.trim(),
                monto = datos.monto.trim().toDouble(),
                codigoAutorizacion = datos.codigoAutorizacion.trim(),
                observaciones = datos.observaciones.trim(),
                estado = "Registrada"
            )

            repository.guardarVenta(venta)

            actualizarEstado {
                copy(
                    estado = "Registrada",
                    guardando = false,
                    mensaje = "Venta guardada correctamente"
                )
            }
        }
    }

    private fun actualizarEstado(
        cambio: VentaUiState.() -> VentaUiState
    ) {
        _uiState.value = _uiState.value.cambio()
    }
}
