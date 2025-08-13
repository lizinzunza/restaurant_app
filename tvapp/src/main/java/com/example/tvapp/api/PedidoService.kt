// api/PedidoService.kt (solo los endpoints de lectura que necesitas)
package com.example.tvapp.api

import com.example.tvapp.model.PedidoResponse
import retrofit2.Response
import retrofit2.http.*

interface PedidoService {
    @GET("api/pedido/mesa/{numeroMesa}")
    suspend fun obtenerPedidoPorMesa(@Path("numeroMesa") numeroMesa: String): Response<PedidoResponse>

    @GET("api/pedido/todos")
    suspend fun obtenerTodosPedidos(): Response<Any>
}