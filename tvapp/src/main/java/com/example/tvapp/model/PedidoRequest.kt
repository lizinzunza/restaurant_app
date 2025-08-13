package com.example.tvapp.model

data class PedidoRequest(
    val pedidos: List<String>,
    val numeroMesa: String,
    val status: Int,
    val total: Double
)