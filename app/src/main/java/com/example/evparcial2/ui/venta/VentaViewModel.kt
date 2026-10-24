package com.example.evparcial2.ui.venta

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.evparcial2.data.local.AppDatabase
import com.example.evparcial2.data.local.VentaEntity
import com.example.evparcial2.data.repository.VentaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VentaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: VentaRepository
    private val ventasFlow: Flow<List<VentaEntity>>

    private val _uiState = MutableStateFlow(VentaUiState())
    val uiState: StateFlow<VentaUiState> = _uiState.asStateFlow()

    // Evita recargar la venta al recomponer o rotar la pantalla
    private var ventaCargadaId: Long? = null

    init {
        val database = AppDatabase.getDatabase(application)
        repository = VentaRepository(database.ventaDao())
        ventasFlow = repository.obtenerVentas()
    }

    fun obtenerVentas(): Flow<List<VentaEntity>> {
        return ventasFlow
    }

    /**
     * Carga una venta existente en el formulario para modificarla.
     */
    fun cargarVenta(id: Long) {
        if (ventaCargadaId == id) return
        ventaCargadaId = id

        viewModelScope.launch {
            actualizarEstado { copy(cargando = true) }

            val venta = repository.obtenerVentaPorId(id)

            if (venta == null) {
                actualizarEstado {
                    copy(cargando = false, mensaje = "No se encontró la venta")
                }
                return@launch
            }

            _uiState.value = VentaUiState(
                ventaId = venta.id,
                tipoServicio = venta.tipoServicio,
                fechaOperacion = venta.fechaOperacion,
                cliente = venta.cliente,
                monto = formatearMonto(venta.monto),
                codigoAutorizacion = venta.codigoAutorizacion,
                respaldoUri = venta.respaldoUri,
                estado = venta.estado,
                observaciones = venta.observaciones,
                cargando = false
            )
        }
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

    /**
     * Guarda el contenido del QR ficticio como respaldo de la venta.
     * Si el código de autorización está vacío, se completa con el QR.
     */
    fun onRespaldoQrChange(valor: String) {
        actualizarEstado {
            val completarCodigo = codigoAutorizacion.isBlank()
            copy(
                respaldoUri = valor,
                codigoAutorizacion = if (completarCodigo) valor else codigoAutorizacion,
                codigoError = if (completarCodigo) null else codigoError,
                mensaje = "Código QR leído correctamente"
            )
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

    /**
     * Registra una venta nueva o actualiza la existente.
     */
    fun guardarVenta() {
        val estadoActual = _uiState.value

        if (!estadoActual.editable) {
            actualizarEstado {
                copy(mensaje = "Una venta validada no se puede modificar")
            }
            return
        }

        if (!validarFormulario()) {
            return
        }

        viewModelScope.launch {
            actualizarEstado {
                copy(guardando = true, mensaje = null)
            }

            val datos = _uiState.value
            val codigo = datos.codigoAutorizacion.trim()

            // En una venta nueva el id es 0, así que se revisan todas
            val codigoDuplicado = repository.codigoExisteEnOtraVenta(
                codigo,
                datos.ventaId ?: 0L
            )

            if (codigoDuplicado) {
                actualizarEstado {
                    copy(
                        codigoError = "El código de autorización ya existe",
                        guardando = false
                    )
                }
                return@launch
            }

            val venta = VentaEntity(
                id = datos.ventaId ?: 0L,
                tipoServicio = datos.tipoServicio.trim(),
                fechaOperacion = datos.fechaOperacion.trim(),
                cliente = datos.cliente.trim(),
                monto = datos.monto.trim().toDouble(),
                codigoAutorizacion = codigo,
                observaciones = datos.observaciones.trim(),
                estado = datos.estado,
                respaldoUri = datos.respaldoUri
            )

            if (datos.esEdicion) {
                repository.actualizarVenta(venta)
            } else {
                repository.guardarVenta(venta)
            }

            actualizarEstado {
                copy(
                    guardando = false,
                    guardadoExitosamente = true,
                    mensaje = if (esEdicion) {
                        "Venta actualizada correctamente"
                    } else {
                        "Venta guardada correctamente"
                    }
                )
            }
        }
    }

    private fun formatearMonto(monto: Double): String {
        return if (monto % 1.0 == 0.0) {
            monto.toLong().toString()
        } else {
            monto.toString()
        }
    }

    private fun actualizarEstado(
        cambio: VentaUiState.() -> VentaUiState
    ) {
        _uiState.value = _uiState.value.cambio()
    }
}
