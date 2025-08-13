// repository/PedidoRepository.kt (versión simplificada para TV - solo lectura)
package com.example.tvapp.repository

import com.example.tvapp.api.RetrofitClient
import com.example.tvapp.model.PedidoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PedidoRepository {

    suspend fun obtenerPedidoActual(numeroMesa: String): Result<PedidoResponse?> {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.pedidoService.obtenerPedidoPorMesa(numeroMesa)

                if (response.isSuccessful) {
                    Result.success(response.body())
                } else if (response.code() == 404) {
                    Result.success(null)
                } else {
                    Result.failure(Exception("Error al obtener pedido: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun obtenerTodosPedidos(): Result<List<PedidoResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.pedidoService.obtenerTodosPedidos()

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body is Map<*, *> && body["pedidos"] is List<*>) {
                        @Suppress("UNCHECKED_CAST")
                        val pedidos = body["pedidos"] as List<Map<String, Any>>
                        val pedidosList = pedidos.map { pedidoMap ->
                            PedidoResponse(
                                _id = pedidoMap["_id"] as String,
                                pedidos = pedidoMap["pedidos"] as List<String>,
                                numeroMesa = pedidoMap["numeroMesa"] as String,
                                status = (pedidoMap["status"] as Double).toInt(),
                                total = pedidoMap["total"] as Double,
                                timestamp = (pedidoMap["timestamp"] as Double).toLong()
                            )
                        }
                        Result.success(pedidosList)
                    } else {
                        Result.success(emptyList())
                    }
                } else {
                    Result.failure(Exception("Error al obtener pedidos: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}