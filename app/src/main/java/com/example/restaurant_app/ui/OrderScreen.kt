package com.example.restaurant_app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.restaurant_app.R
import com.example.restaurant_app.viewmodel.CartViewModel
import com.example.restaurant_app.viewmodel.OrderStatus
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable

@Composable
fun OrderScreen(
    onLogoutClick: () -> Unit = {},
    cartViewModel: CartViewModel = viewModel()
) {
    // Observar el estado del carrito
    val cartItems by cartViewModel.cartItems.collectAsState()
    val cartItemsList = cartViewModel.getCartItemsList()
    val totalPrice = cartViewModel.getTotalPrice()
    val isLoading by cartViewModel.isLoading.collectAsState()
    val orderStatus by cartViewModel.orderStatus.collectAsState()
    
    var showNotification by remember { mutableStateOf(false) }
    var notificationMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    // Observar cambios en el estado del pedido
    LaunchedEffect(orderStatus) {
        orderStatus?.let { status ->
            when (status) {
                is OrderStatus.Success -> {
                    notificationMessage = status.message
                    showNotification = true
                    scope.launch {
                        delay(3000)
                        showNotification = false
                        cartViewModel.clearOrderStatus()
                    }
                }
                is OrderStatus.Error -> {
                    notificationMessage = status.message
                    showNotification = true
                    scope.launch {
                        delay(3000)
                        showNotification = false
                        cartViewModel.clearOrderStatus()
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo_menu),
            contentDescription = "Fondo decorativo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Image(
            painter = painterResource(id = R.drawable.papel_picado),
            contentDescription = "Papel picado",
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-250).dp)
        )

        // Botón de cerrar sesión
        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 40.dp, end = 16.dp)
                .size(48.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFFE6007E)
        ) {
            IconButton(
                onClick = onLogoutClick,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Cerrar sesión",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Notificación de estado del pedido
        AnimatedVisibility(
            visible = showNotification,
            enter = slideInVertically(
                initialOffsetY = { -it },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300)),
            exit = slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300)),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 60.dp)
                .zIndex(1000f)
        ) {
            Surface(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                color = if (orderStatus is OrderStatus.Success) Color(0xFF4CAF50) else Color(0xFFF44336),
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = notificationMessage,
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(100.dp))

            Text(
                text = "Mi Pedido",
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B0000),
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Start)
            )
            
            // Mostrar items del carrito
            if (cartItemsList.isEmpty()) {
                Text(
                    text = "No hay items en tu pedido",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(32.dp)
                )
            } else {
                cartItemsList.forEach { item ->
                    OrderItem(
                        name = item.name,
                        price = "$${item.price}",
                        quantity = item.quantity,
                        onRemove = { 
                            cartViewModel.removeFromCart(item.name)
                            notificationMessage = "${item.name} removido del pedido"
                            showNotification = true
                            scope.launch {
                                delay(2000)
                                showNotification = false
                            }
                        },
                        onAdd = { 
                            cartViewModel.addToCart(item.name)
                            notificationMessage = "${item.name} agregado al pedido"
                            showNotification = true
                            scope.launch {
                                delay(2000)
                                showNotification = false
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Resumen del pedido
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFE6007E)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Sub-Total",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "$${String.format("%.2f", totalPrice)}",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Delivery Charge",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "$2.00",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Discount",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "$0.00",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$${String.format("%.2f", totalPrice + 2.00)}",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Botón de realizar pedido
                    Button(
                        onClick = { cartViewModel.enviarPedido("4") }, // Mesa 4
                        enabled = !isLoading && cartItemsList.isNotEmpty(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isLoading) Color.Gray else Color(0xFFFFF6E8)
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color(0xFF8B4513)
                            )
                        } else {
                            Text(
                                text = "Realizar mi pedido",
                                color = Color(0xFF8B4513),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun OrderItem(
    name: String,
    price: String,
    quantity: Int,
    onRemove: () -> Unit,
    onAdd: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 4.dp,
        color = Color(0xFFFFF6E8)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Información del platillo
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D2D2D),
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = price,
                    color = Color(0xFF007F00),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            
            // Selector de cantidad
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Botón menos
                Surface(
                    modifier = Modifier
                        .size(36.dp)
                        .clickable { onRemove() },
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFFE6007E),
                    shadowElevation = 4.dp
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "-",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
                
                // Cantidad
                Surface(
                    modifier = Modifier
                        .width(40.dp)
                        .height(36.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFFBE4EE)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = quantity.toString(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D2D2D)
                        )
                    }
                }
                
                // Botón más
                Surface(
                    modifier = Modifier
                        .size(36.dp)
                        .clickable { onAdd() },
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFF0099CC),
                    shadowElevation = 4.dp
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
} 