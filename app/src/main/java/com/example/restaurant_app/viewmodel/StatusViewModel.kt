// viewmodel/StatusViewModel.kt (nuevo archivo)
package com.example.restaurant_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurant_app.model.PedidoResponse
import com.example.restaurant_app.repository.PedidoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class StatusViewModel : ViewModel() {
    private val _currentOrder = MutableStateFlow<PedidoResponse?>(null)
    val currentOrder: StateFlow<PedidoResponse?> = _currentOrder.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val pedidoRepository = PedidoRepository()

    init {
        startPolling()
    }

    // En StatusViewModel.kt, cambia la línea 25
    private fun startPolling() {
        viewModelScope.launch {
            while (true) {
                loadCurrentOrder("4") // Cambiar de "Mesa 4" a solo "4"
                delay(5000) // Actualizar cada 5 segundos
            }
        }
    }

    private suspend fun loadCurrentOrder(numeroMesa: String) {
        try {
            val result = pedidoRepository.obtenerPedidoActual(numeroMesa)
            if (result.isSuccess) {
                _currentOrder.value = result.getOrNull()
            }
        } catch (e: Exception) {
            // Manejar error silenciosamente para no interrumpir el polling
        }
    }
}