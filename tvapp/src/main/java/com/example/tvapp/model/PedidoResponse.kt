// model/PedidoResponse.kt (copia exacta de tu app móvil)
package com.example.tvapp.model

data class PedidoResponse(
    val _id: String,
    val pedidos: List<String>,
    val numeroMesa: String,
    val status: Int,
    val total: Double,
    val timestamp: Long
)