package com.example.tvapp.model

data class Mesa(
    val numero: String,
    val pedido: PedidoResponse?,
    val tiempoEspera: Long, // en minutos
    val tiempoEstimadoTotal: Int, // en minutos
    val color: Long
)