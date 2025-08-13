package com.example.restaurant_app.model

data class StatusUpdateRequest(
    val pedidoId: String,
    val nuevoStatus: Int
)