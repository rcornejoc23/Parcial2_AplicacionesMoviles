package com.example.evparcial2.data.repository

import com.example.evparcial2.data.local.VentaDao
import com.example.evparcial2.data.local.VentaEntity
import kotlinx.coroutines.flow.Flow

class VentaRepository(
    private val ventaDao: VentaDao
) {

    fun obtenerVentas(): Flow<List<VentaEntity>> {
        return ventaDao.obtenerTodas()
    }

    suspend fun guardarVenta(venta: VentaEntity): Long {
        return ventaDao.insertar(venta)
    }

    suspend fun actualizarVenta(venta: VentaEntity) {
        ventaDao.actualizar(venta)
    }

    suspend fun eliminarVenta(venta: VentaEntity) {
        ventaDao.eliminar(venta)
    }

    suspend fun buscarPorCodigo(codigo: String): VentaEntity? {
        return ventaDao.buscarPorCodigo(codigo)
    }

    suspend fun codigoExiste(codigo: String): Boolean {
        return ventaDao.buscarPorCodigo(codigo) != null
    }
}
