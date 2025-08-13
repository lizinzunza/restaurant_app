package com.example.restaurant_app.model

data class LoginResponse(
    val success: Boolean,
    val token: String? = null,
    val message: String
)