package com.example.restaurant_app.repository

import com.example.restaurant_app.api.RetrofitClient
import com.example.restaurant_app.model.PedidoRequest
import com.example.restaurant_app.viewmodel.CartItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PedidoRepository {
    
    suspend fun enviarPedido(cartItems: List<CartItem>, numeroMesa: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                // Convertir items del carrito al formato requerido por el backend
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
} 