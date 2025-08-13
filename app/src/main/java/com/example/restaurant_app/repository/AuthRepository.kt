// repository/AuthRepository.kt (nuevo archivo)
package com.example.restaurant_app.repository

import com.example.restaurant_app.api.RetrofitClient
import com.example.restaurant_app.model.LoginRequest
import com.example.restaurant_app.model.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository {

    suspend fun login(loginRequest: LoginRequest): Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.authService.login(loginRequest)

                if (response.isSuccessful) {
                    Result.success(response.body() ?: LoginResponse(false, null, "Error desconocido"))
                } else {
                    Result.failure(Exception("Error de autenticación: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}