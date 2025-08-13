package com.example.tvapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tvapp.model.PedidoResponse
import com.example.tvapp.repository.PedidoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

data class MesaTV(
    val numero: String,
    val pedido: PedidoResponse?,
    val tiempoEspera: Long,
    val tiempoEstimadoTotal: Int,
    val color: Long
)

class TVRestaurantViewModel : ViewModel() {
    private val _mesas = MutableStateFlow<List<MesaTV>>(emptyList())
    val mesas: StateFlow<List<MesaTV>> = _mesas.asStateFlow()

    private val _selectedOrder = MutableStateFlow<PedidoResponse?>(null)
    val selectedOrder: StateFlow<PedidoResponse?> = _selectedOrder.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val pedidoRepository = PedidoRepository()

    // Tiempos de preparación (mismos que la app móvil)
    private val tiemposPreparacion = mapOf(
        "Tacos al pastor" to 12,
        "Enchiladas verdes" to 18,
        "Pozole" to 22,
        "Tamales" to 8
    )

    init {
        startPolling()
    }

    private fun startPolling() {
        viewModelScope.launch {
            while (true) {
                loadAllOrders()
                delay(5000) // Actualizar cada 5 segundos
            }
        }
    }

    private suspend fun loadAllOrders() {
        try {
            _isLoading.value = true
            val result = pedidoRepository.obtenerTodosPedidos()
            if (result.isSuccess) {
                val pedidos = result.getOrNull() ?: emptyList()
                val mesasActualizadas = createMesasFromPedidos(pedidos)
                _mesas.value = mesasActualizadas
            }
        } catch (e: Exception) {
            println("Error loading orders: ${e.message}")
        } finally {
            _isLoading.value = false
        }
    }

    private fun createMesasFromPedidos(pedidos: List<PedidoResponse>): List<MesaTV> {
        val numerosMesas = listOf("4", "6", "7") // Solo las mesas que maneja la TV
        val currentTime = System.currentTimeMillis()

        return numerosMesas.map { numeroMesa ->
            val pedido = pedidos.find { it.numeroMesa == numeroMesa && it.status < 4 }

            val tiempoEspera = pedido?.let {
                val tiempoTranscurrido = (currentTime - it.timestamp) / (1000 * 60)
                maxOf(0L, tiempoTranscurrido)
            } ?: 0L

            val tiempoEstimadoTotal = pedido?.let { calcularTiempoEstimadoTotal(it.pedidos) } ?: 0
            val color = if (pedido != null) getColorForEstimatedTime(tiempoEstimadoTotal) else 0xFFCCCCCCL

            MesaTV(
                numero = numeroMesa,
                pedido = pedido,
                tiempoEspera = tiempoEspera,
                tiempoEstimadoTotal = tiempoEstimadoTotal,
                color = color
            )
        }
    }

    private fun calcularTiempoEstimadoTotal(pedidos: List<String>): Int {
        val platillosAgrupados = pedidos.groupBy { it }
        var tiempoTotal = 0

        platillosAgrupados.forEach { (platillo, lista) ->
            val tiempoUnitario = tiemposPreparacion[platillo] ?: 15
            val cantidad = lista.size
            tiempoTotal += tiempoUnitario + (cantidad - 1) * 3
        }

        return tiempoTotal
    }

    private fun getColorForEstimatedTime(tiempoEstimadoMinutos: Int): Long {
        return when {
            tiempoEstimadoMinutos <= 15 -> 0xFF7CB342L  // Verde lima
            tiempoEstimadoMinutos <= 25 -> 0xFFFFA726L  // Amarillo anaranjado
            tiempoEstimadoMinutos <= 35 -> 0xFFFF9800L  // Naranja
            else -> 0xFFEF5350L                         // Rojo coral
        }
    }

    fun selectOrder(numeroMesa: String) {
        val mesa = _mesas.value.find { it.numero == numeroMesa }
        _selectedOrder.value = mesa?.pedido
    }
}

// Funciones auxiliares (mismas que la app móvil)
fun calculateTimeElapsed(timestamp: Long): Long {
    return (System.currentTimeMillis() - timestamp) / (1000 * 60)
}

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