package com.example.tvapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tvapp.repository.PedidoRepository
import com.example.tvapp.model.CartItem  // Importar desde model
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {

    private val _cartItems = MutableStateFlow<Map<String, CartItem>>(emptyMap())
    val cartItems: StateFlow<Map<String, CartItem>> = _cartItems.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _orderStatus = MutableStateFlow<OrderStatus?>(null)
    val orderStatus: StateFlow<OrderStatus?> = _orderStatus.asStateFlow()

    private val pedidoRepository = PedidoRepository()

    // Precios de los platillos
    private val itemPrices = mapOf(
        "Tacos al pastor" to 7.50,
        "Enchiladas verdes" to 5.50,
        "Pozole" to 5.70,
        "Tamales" to 6.50
    )

    fun addToCart(itemName: String) {
        val currentItems = _cartItems.value.toMutableMap()
        val currentItem = currentItems[itemName]
        val price = itemPrices[itemName] ?: 0.0

        if (currentItem != null) {
            // Si ya existe, incrementar cantidad
            currentItems[itemName] = currentItem.copy(quantity = currentItem.quantity + 1)
        } else {
            // Si es nuevo, crear nuevo item
            currentItems[itemName] = CartItem(name = itemName, price = price, quantity = 1)
        }

        _cartItems.value = currentItems
    }

    fun removeFromCart(itemName: String) {
        val currentItems = _cartItems.value.toMutableMap()
        val currentItem = currentItems[itemName]

        if (currentItem != null) {
            if (currentItem.quantity > 1) {
                // Si hay más de 1, decrementar
                currentItems[itemName] = currentItem.copy(quantity = currentItem.quantity - 1)
            } else {
                // Si solo hay 1, eliminar completamente
                currentItems.remove(itemName)
            }
        }

        _cartItems.value = currentItems
    }

    fun clearCart() {
        _cartItems.value = emptyMap()
    }

    fun getTotalItems(): Int {
        return _cartItems.value.values.sumOf { it.quantity }
    }

    fun getTotalPrice(): Double {
        return _cartItems.value.values.sumOf { it.price * it.quantity }
    }

    fun getCartItemsList(): List<CartItem> {
        return _cartItems.value.values.toList()
    }

    fun enviarPedido(numeroMesa: String) {
        val cartItemsList = getCartItemsList()

        if (cartItemsList.isEmpty()) {
            _orderStatus.value = OrderStatus.Error("El carrito está vacío")
            return
        }

        _isLoading.value = true
        _orderStatus.value = null

        viewModelScope.launch {
            try {
                val result = pedidoRepository.enviarPedido(cartItemsList, numeroMesa)

                if (result.isSuccess) {
                    _orderStatus.value = OrderStatus.Success("Pedido enviado exitosamente")
                    clearCart() // Limpiar carrito después de enviar
                } else {
                    _orderStatus.value = OrderStatus.Error(result.exceptionOrNull()?.message ?: "Error desconocido")
                }
            } catch (e: Exception) {
                _orderStatus.value = OrderStatus.Error(e.message ?: "Error al enviar pedido")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearOrderStatus() {
        _orderStatus.value = null
    }
}

sealed class OrderStatus {
    data class Success(val message: String) : OrderStatus()
    data class Error(val message: String) : OrderStatus()
}