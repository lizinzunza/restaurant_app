package com.example.tvapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tvapp.api.PedidoResponse
import com.example.tvapp.repository.PedidoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TVViewModel : ViewModel() {

    private val _pedidos = MutableStateFlow<List<PedidoResponse>>(emptyList())
    val pedidos: StateFlow<List<PedidoResponse>> = _pedidos.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _connectionStatus = MutableStateFlow("Conectando...")
    val connectionStatus: StateFlow<String> = _connectionStatus.asStateFlow()

    private val _selectedMesa = MutableStateFlow<String?>(null)
    val selectedMesa: StateFlow<String?> = _selectedMesa.asStateFlow()

    private val repository = PedidoRepository()

    init {
        testConnection()
    }

    private fun testConnection() {
        viewModelScope.launch {
            try {
                val response = repository.testConnection()
                if (response.isSuccessful) {
                    _connectionStatus.value = "✅ Conectado"
                    cargarPedidos()
                } else {
                    _connectionStatus.value = "❌ Error de conexión"
                }
            } catch (e: Exception) {
                _connectionStatus.value = "❌ Sin conexión"
            }
        }
    }

    fun cargarPedidos() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.obtenerTodosPedidos()
                if (response.isSuccessful) {
                    _pedidos.value = response.body() ?: emptyList()
                } else {
                    // Datos de prueba si no hay conexión
                    _pedidos.value = getDatosPrueba()
                }
            } catch (e: Exception) {
                _pedidos.value = getDatosPrueba()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun seleccionarMesa(numeroMesa: String) {
        _selectedMesa.value = numeroMesa
    }

    fun actualizarStatus(pedidoId: String, nuevoStatus: Int) {
        viewModelScope.launch {
            try {
                val response = repository.actualizarStatusPedido(pedidoId, nuevoStatus)
                if (response.isSuccessful) {
                    cargarPedidos() // Recargar para ver cambios
                }
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }

    fun getPedidosMesa(numeroMesa: String): List<PedidoResponse> {
        return _pedidos.value.filter { it.numeroMesa == numeroMesa }
    }

    private fun getDatosPrueba(): List<PedidoResponse> {
        return listOf(
            PedidoResponse(
                id = "1",
                pedidos = listOf("Tacos al Pastor", "Tamales"),
                numeroMesa = "4",
                status = 1,
                total = 25.50
            ),
            PedidoResponse(
                id = "2",
                pedidos = listOf("Pozole"),
                numeroMesa = "6",
                status = 0,
                total = 18.00
            ),
            PedidoResponse(
                id = "3",
                pedidos = listOf("Enchiladas verdes", "Tacos al Pastor"),
                numeroMesa = "7",
                status = 2,
                total = 22.75
            )
        )
    }
}