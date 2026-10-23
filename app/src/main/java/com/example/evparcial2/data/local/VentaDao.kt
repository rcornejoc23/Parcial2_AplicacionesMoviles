package com.example.evparcial2.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface VentaDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertar(venta: VentaEntity): Long

    @Update
    suspend fun actualizar(venta: VentaEntity)

    @Delete
    suspend fun eliminar(venta: VentaEntity)

    @Query("SELECT * FROM ventas ORDER BY id DESC")
    fun obtenerTodas(): Flow<List<VentaEntity>>

    @Query(
        "SELECT * FROM ventas " +
            "WHERE codigoAutorizacion = :codigo " +
            "LIMIT 1"
    )
    suspend fun buscarPorCodigo(codigo: String): VentaEntity?
}
