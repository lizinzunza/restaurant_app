// viewmodel/RestaurantViewModel.kt (versión corregida completa)
package com.example.restaurant_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurant_app.model.LoginRequest
import com.example.restaurant_app.model.PedidoResponse
import com.example.restaurant_app.repository.AuthRepository
import com.example.restaurant_app.repository.PedidoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

data class Mesa(
    val numero: String,
    val pedido: PedidoResponse?,
    val tiempoEspera: Long, // en minutos (tiempo transcurrido)
    val tiempoEstimadoTotal: Int, // en minutos (tiempo total estimado de preparación)
    val color: Long
)

class RestaurantViewModel : ViewModel() {
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    private val _mesas = MutableStateFlow<List<Mesa>>(emptyList())
    val mesas: StateFlow<List<Mesa>> = _mesas.asStateFlow()

    private val _selectedOrder = MutableStateFlow<PedidoResponse?>(null)
    val selectedOrder: StateFlow<PedidoResponse?> = _selectedOrder.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    private val authRepository = AuthRepository()
    private val pedidoRepository = PedidoRepository()

    // Tiempos de preparación estimados por platillo (en minutos)
    private val tiemposPreparacion = mapOf(
        "Tacos al pastor" to 12,
        "Enchiladas verdes" to 18,
        "Pozole" to 22,
        "Tamales" to 8
    )

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _loginError.value = null

            try {
                val loginRequest = LoginRequest(username, password)
                val result = authRepository.login(loginRequest)

                if (result.isSuccess) {
                    val response = result.getOrNull()
                    if (response?.success == true) {
                        _isAuthenticated.value = true
                        startPollingOrders()
                    } else {
                        _loginError.value = response?.message ?: "Credenciales incorrectas"
                    }
                } else {
                    _loginError.value = "Error de conexión"
                }
            } catch (e: Exception) {
                _loginError.value = e.message ?: "Error desconocido"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun startPollingOrders() {
        viewModelScope.launch {
            while (_isAuthenticated.value) {
                loadAllOrders()
                delay(3000) // Actualizar cada 3 segundos
            }
        }
    }

    private suspend fun loadAllOrders() {
        try {
            val result = pedidoRepository.obtenerTodosPedidos()
            if (result.isSuccess) {
                val pedidos = result.getOrNull() ?: emptyList()
                val mesasActualizadas = createMesasFromPedidos(pedidos)
                _mesas.value = mesasActualizadas

                // Actualizar pedido seleccionado si existe
                _selectedOrder.value?.let { currentSelected ->
                    val updatedOrder = pedidos.find { it._id == currentSelected._id }
                    _selectedOrder.value = updatedOrder
                }
            }
        } catch (e: Exception) {
            println("Error loading orders: ${e.message}")
        }
    }

    private fun createMesasFromPedidos(pedidos: List<PedidoResponse>): List<Mesa> {
        val numerosMesas = listOf("2", "4", "6", "7", "9")
        val currentTime = System.currentTimeMillis()

        return numerosMesas.map { numeroMesa ->
            // Buscar pedido activo para esta mesa (status < 4 = no entregado)
            val pedido = pedidos.find { it.numeroMesa == numeroMesa && it.status < 4 }

            val tiempoEspera = pedido?.let {
                val tiempoTranscurrido = (currentTime - it.timestamp) / (1000 * 60) // en minutos
                maxOf(0L, tiempoTranscurrido)
            } ?: 0L

            val tiempoEstimadoTotal = pedido?.let { calcularTiempoEstimadoTotal(it.pedidos) } ?: 0

            val color = if (pedido != null) getColorForEstimatedTime(tiempoEstimadoTotal) else 0xFFE0E0E0L

            println("Mesa $numeroMesa: Tiempo estimado total: ${tiempoEstimadoTotal}min, Color: 0x${color.toString(16).uppercase()}")

            Mesa(
                numero = "Mesa $numeroMesa",
                pedido = pedido,
                tiempoEspera = tiempoEspera,
                tiempoEstimadoTotal = tiempoEstimadoTotal,
                color = color
            )
        }
    }

    private fun calcularTiempoEstimadoTotal(pedidos: List<String>): Int {
        // Agrupar pedidos y calcular tiempo total
        val platillosAgrupados = pedidos.groupBy { it }
        var tiempoTotal = 0

        platillosAgrupados.forEach { (platillo, lista) ->
            val tiempoUnitario = tiemposPreparacion[platillo] ?: 15 // 15 min por defecto
            val cantidad = lista.size
            // El tiempo aumenta proporcionalmente con la cantidad
            tiempoTotal += tiempoUnitario + (cantidad - 1) * 3 // +3 min por cada unidad adicional
        }

        return tiempoTotal
    }

    private fun getColorForEstimatedTime(tiempoEstimadoMinutos: Int): Long {
        return when {
            tiempoEstimadoMinutos <= 15 -> {
                println("Color VERDE para $tiempoEstimadoMinutos minutos estimados")
                0xFF90EE90L   // Verde (0-15 min) - Rápido
            }
            tiempoEstimadoMinutos <= 25 -> {
                println("Color AMARILLO para $tiempoEstimadoMinutos minutos estimados")
                0xFFFFD93DL  // Amarillo (15-25 min) - Medio
            }
            tiempoEstimadoMinutos <= 35 -> {
                println("Color NARANJA para $tiempoEstimadoMinutos minutos estimados")
                0xFFFF9800L  // Naranja (25-35 min) - Tardado
            }
            else -> {
                println("Color ROJO para $tiempoEstimadoMinutos minutos estimados")
                0xFFFF6B6BL  // Rojo (35+ min) - Muy tardado
            }
        }
    }

    fun selectOrder(numeroMesa: String) {
        val mesa = _mesas.value.find { it.numero == numeroMesa }
        _selectedOrder.value = mesa?.pedido
    }

    fun updateOrderStatus(pedidoId: String, nuevoStatus: Int) {
        viewModelScope.launch {
            try {
                val result = pedidoRepository.actualizarStatus(pedidoId, nuevoStatus)
                if (result.isSuccess) {
                    loadAllOrders()
                }
            } catch (e: Exception) {
                println("Error updating status: ${e.message}")
            }
        }
    }

    fun logout() {
        _isAuthenticated.value = false
        _mesas.value = emptyList()
        _selectedOrder.value = null
    }

    fun clearLoginError() {
        _loginError.value = null
    }
}

// Función auxiliar para calcular tiempo transcurrido
fun calculateTimeElapsed(timestamp: Long): Long {
    return (System.currentTimeMillis() - timestamp) / (1000 * 60)
}

// Función auxiliar para formatear tiempo
fun formatElapsedTime(tiempoMinutos: Long): String {
    return when {
        tiempoMinutos < 1 -> "Menos de 1 min"
        tiempoMinutos < 60 -> "${tiempoMinutos} min"
        else -> {
            val horas = tiempoMinutos / 60
            val minutos = tiempoMinutos % 60
            "${horas}h ${minutos}min"
        }
    }
}

// Función auxiliar para calcular tiempo estimado total de pedido
fun calculateTotalEstimatedTime(pedidos: List<String>): Int {
    val tiemposPreparacion = mapOf(
        "Tacos al pastor" to 12,
        "Enchiladas verdes" to 18,
        "Pozole" to 22,
        "Tamales" to 8
    )

    val platillosAgrupados = pedidos.groupBy { it }
    var tiempoTotal = 0

    platillosAgrupados.forEach { (platillo, lista) ->
        val tiempoUnitario = tiemposPreparacion[platillo] ?: 15
        val cantidad = lista.size
        tiempoTotal += tiempoUnitario + (cantidad - 1) * 3
    }

    return tiempoTotal
}

// Función auxiliar para formatear tiempo estimado
fun formatEstimatedTime(tiempoMinutos: Int): String {
    return when {
        tiempoMinutos < 60 -> "$tiempoMinutos min"
        else -> {
            val horas = tiempoMinutos / 60
            val minutos = tiempoMinutos % 60
            if (minutos == 0) "${horas}h" else "${horas}h ${minutos}min"
        }
    }
}