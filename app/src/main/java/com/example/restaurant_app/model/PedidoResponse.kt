package com.example.restaurant_app.model

data class PedidoResponse(
    val _id: String,
    val pedidos: List<String>,
    val numeroMesa: String,
    val status: Int,
    val total: Double,
    val timestamp: Long
)