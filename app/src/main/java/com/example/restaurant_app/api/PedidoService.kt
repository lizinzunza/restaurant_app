// api/PedidoService.kt (actualizado)
package com.example.restaurant_app.api

import com.example.restaurant_app.model.*
import retrofit2.Response
import retrofit2.http.*

interface PedidoService {
    // Tu endpoint actual
    @POST("api/pedido/pedidos")
    suspend fun crearPedido(@Body pedido: PedidoRequest): Response<Any>

    // Nuevos endpoints que acabamos de crear en el backend
    @GET("api/pedido/mesa/{numeroMesa}")
    suspend fun obtenerPedidoPorMesa(@Path("numeroMesa") numeroMesa: String): Response<PedidoResponse>

    @GET("api/pedido/todos")
    suspend fun obtenerTodosPedidos(): Response<Any>

    @PUT("api/pedido/status")
    suspend fun actualizarStatus(@Body statusUpdate: StatusUpdateRequest): Response<Unit>
}