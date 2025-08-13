// api/AuthService.kt (nuevo archivo)
package com.example.restaurant_app.api

import com.example.restaurant_app.model.LoginRequest
import com.example.restaurant_app.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}