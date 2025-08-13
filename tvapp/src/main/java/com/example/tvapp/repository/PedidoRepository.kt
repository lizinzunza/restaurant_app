// repository/PedidoRepository.kt (CORREGIDO)
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
                println("🔍 Llamando a API: obtenerTodosPedidos")
                val response = RetrofitClient.pedidoService.obtenerTodosPedidos()

                if (response.isSuccessful) {
                    val body = response.body()
                    println("📦 Respuesta del servidor: $body")

                    if (body is Map<*, *> && body["pedidos"] is List<*>) {
                        @Suppress("UNCHECKED_CAST")
                        val pedidos = body["pedidos"] as List<Map<String, Any>>
                        val pedidosList = pedidos.map { pedidoMap ->
                            PedidoResponse(
                                _id = pedidoMap["_id"] as String, // ✅ CORREGIDO: era *id
                                pedidos = pedidoMap["pedidos"] as List<String>,
                                numeroMesa = pedidoMap["numeroMesa"] as String,
                                status = (pedidoMap["status"] as Double).toInt(),
                                total = pedidoMap["total"] as Double,
                                timestamp = (pedidoMap["timestamp"] as Double).toLong()
                            )
                        }
                        println("✅ Pedidos procesados: ${pedidosList.size}")
                        pedidosList.forEach { pedido ->
                            println("📝 Mesa ${pedido.numeroMesa}: ${pedido.pedidos.size} platillos, status: ${pedido.status}")
                        }
                        Result.success(pedidosList)
                    } else {
                        println("⚠️ Formato de respuesta inesperado o sin pedidos")
                        Result.success(emptyList())
                    }
                } else {
                    println("❌ Error HTTP: ${response.code()}")
                    Result.failure(Exception("Error al obtener pedidos: ${response.code()}"))
                }
            } catch (e: Exception) {
                println("🚨 Exception en obtenerTodosPedidos: ${e.message}")
                e.printStackTrace()
                Result.failure(e)
            }
        }
    }
}