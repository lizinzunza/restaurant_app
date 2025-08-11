package com.example.restaurant_app.api

import com.example.restaurant_app.model.PedidoRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PedidoService {
    @POST("api/pedido/pedidos")
    suspend fun crearPedido(@Body pedido: PedidoRequest): Response<Any>
} 