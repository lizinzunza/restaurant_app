// repository/PedidoRepository.kt (actualizado)
package com.example.restaurant_app.repository

import com.example.restaurant_app.api.RetrofitClient
import com.example.restaurant_app.model.PedidoRequest
import com.example.restaurant_app.model.PedidoResponse
import com.example.restaurant_app.model.StatusUpdateRequest
import com.example.restaurant_app.viewmodel.CartItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PedidoRepository {

    suspend fun enviarPedido(cartItems: List<CartItem>, numeroMesa: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val pedidos = mutableListOf<String>()
                cartItems.forEach { item ->
                    repeat(item.quantity) {
                        pedidos.add(item.name)
                    }
                }

                val total = cartItems.sumOf { it.price * it.quantity }

                val pedidoRequest = PedidoRequest(
                    pedidos = pedidos,
                    numeroMesa = numeroMesa,
                    status = 1,
                    total = total
                )

                val response = RetrofitClient.pedidoService.crearPedido(pedidoRequest)

                if (response.isSuccessful) {
                    Result.success(true)
                } else {
                    Result.failure(Exception("Error al enviar pedido: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    // Nuevo método para obtener pedido actual por mesa
    suspend fun obtenerPedidoActual(numeroMesa: String): Result<PedidoResponse?> {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.pedidoService.obtenerPedidoPorMesa(numeroMesa)

                if (response.isSuccessful) {
                    Result.success(response.body())
                } else if (response.code() == 404) {
                    // No hay pedidos activos, esto es normal
                    Result.success(null)
                } else {
                    Result.failure(Exception("Error al obtener pedido: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    // Nuevo método para obtener todos los pedidos
    suspend fun obtenerTodosPedidos(): Result<List<PedidoResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.pedidoService.obtenerTodosPedidos()

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body is Map<*, *> && body["pedidos"] is List<*>) {
                        // El backend devuelve { pedidos: [...] }
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

    // Nuevo método para actualizar status
    suspend fun actualizarStatus(pedidoId: String, nuevoStatus: Int): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val statusUpdate = StatusUpdateRequest(pedidoId, nuevoStatus)
                val response = RetrofitClient.pedidoService.actualizarStatus(statusUpdate)

                if (response.isSuccessful) {
                    Result.success(true)
                } else {
                    Result.failure(Exception("Error al actualizar status: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}