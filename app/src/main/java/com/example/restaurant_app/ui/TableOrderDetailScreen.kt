// ui/TableOrderDetailScreen.kt (completo mejorado)
package com.example.restaurant_app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.restaurant_app.viewmodel.RestaurantViewModel
import com.example.restaurant_app.viewmodel.calculateTimeElapsed
import com.example.restaurant_app.viewmodel.formatElapsedTime
import com.example.restaurant_app.viewmodel.calculateTotalEstimatedTime
import com.example.restaurant_app.viewmodel.formatEstimatedTime

@Composable
fun TableOrderDetailScreen(
    tableNumber: String,
    onBack: () -> Unit = {},
    restaurantViewModel: RestaurantViewModel = viewModel()
) {
    val selectedOrder by restaurantViewModel.selectedOrder.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo_menu),
            contentDescription = "Fondo mexicano",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Botón de regreso mejorado y funcional
        Surface(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 40.dp, start = 16.dp)
                .size(48.dp)
                .clickable { onBack() }, // Hacer clickable toda la superficie
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFFE6007E),
            shadowElevation = 4.dp
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Regresar a órdenes",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp, top = 60.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.papel_picado),
                contentDescription = "Papel picado",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFE6007E)
                ) {
                    Box(
                        modifier = Modifier.size(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "▶",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = tableNumber,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B0000)
                )
            }

            selectedOrder?.let { order ->
                val tiempoTranscurrido = calculateTimeElapsed(order.timestamp)
                val tiempoEstimadoTotal = calculateTotalEstimatedTime(order.pedidos)
                val tiempoRestante = maxOf(0, tiempoEstimadoTotal - tiempoTranscurrido.toInt())

                // Información de tiempos
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFFF6E8),
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Tiempo estimado",
                                fontSize = 12.sp,
                                color = Color(0xFF8B4513)
                            )
                            Text(
                                text = formatEstimatedTime(tiempoEstimadoTotal),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE6007E)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Transcurrido",
                                fontSize = 12.sp,
                                color = Color(0xFF8B4513)
                            )
                            Text(
                                text = formatElapsedTime(tiempoTranscurrido),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF8B4513)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Restante",
                                fontSize = 12.sp,
                                color = Color(0xFF8B4513)
                            )
                            Text(
                                text = if (tiempoRestante > 0) formatEstimatedTime(tiempoRestante) else "¡Listo!",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (tiempoRestante > 0) Color(0xFF8B4513) else Color(0xFF4CAF50)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botones de estado mejorados con estilo de StatusScreen
                StatusButtonsSection(
                    currentStatus = order.status,
                    onStatusChange = { newStatus ->
                        restaurantViewModel.updateOrderStatus(order._id, newStatus)
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Platillos del pedido:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8B0000)
                    )

                    order.pedidos.groupBy { it }.forEach { (platillo, lista) ->
                        OrderItemCard(
                            imageRes = getImageForDish(platillo),
                            name = platillo,
                            time = getTimeForDish(platillo),
                            quantity = lista.size
                        )
                    }
                }
            } ?: run {
                Text(
                    text = "No hay información del pedido",
                    fontSize = 18.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(32.dp)
                )
            }
        }
    }
}

@Composable
fun StatusButtonsSection(
    currentStatus: Int,
    onStatusChange: (Int) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 6.dp,
        color = Color(0xFFFFF6E8)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Actualizar Estado del Pedido:",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B0000),
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Botones estilo StatusScreen con iconos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatusIconButton(
                    iconRes = R.drawable.ic_menu,
                    label = "Recibido",
                    status = 1,
                    currentStatus = currentStatus,
                    activeColor = Color(0xFF4CAF50),
                    onClick = { onStatusChange(1) }
                )

                StatusIconButton(
                    iconRes = R.drawable.ic_pedido,
                    label = "Preparando",
                    status = 2,
                    currentStatus = currentStatus,
                    activeColor = Color(0xFFFF9800),
                    onClick = { onStatusChange(2) }
                )

                StatusIconButton(
                    iconRes = R.drawable.ic_status,
                    label = "Listo",
                    status = 3,
                    currentStatus = currentStatus,
                    activeColor = Color(0xFFF44336),
                    onClick = { onStatusChange(3) }
                )

                StatusIconButton(
                    iconRes = R.drawable.ic_restaurante,
                    label = "Entregado",
                    status = 4,
                    currentStatus = currentStatus,
                    activeColor = Color(0xFF9C27B0),
                    onClick = { onStatusChange(4) }
                )
            }
        }
    }
}

@Composable
fun StatusIconButton(
    iconRes: Int,
    label: String,
    status: Int,
    currentStatus: Int,
    activeColor: Color,
    onClick: () -> Unit
) {
    val isSelected = currentStatus == status
    val backgroundColor = if (isSelected) activeColor else Color.Gray

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        // Círculo con icono estilo StatusScreen
        Surface(
            shape = RoundedCornerShape(50),
            color = backgroundColor,
            shadowElevation = if (isSelected) 6.dp else 2.dp,
            modifier = Modifier.size(56.dp) // Más grande que en StatusScreen
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = label,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Texto del estado
        Text(
            text = label,
            fontSize = 12.sp,
            color = if (isSelected) activeColor else Color.Gray,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1
        )

        // Indicador visual adicional para el estado activo
        if (isSelected) {
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(activeColor, RoundedCornerShape(3.dp))
            )
        }
    }
}

@Composable
fun OrderItemCard(
    imageRes: Int,
    name: String,
    time: String,
    quantity: Int
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 4.dp,
        color = Color(0xFFFFF6E8)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B4513),
                    fontSize = 18.sp
                )
                Text(
                    text = "Prep: $time",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "x$quantity",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B4513)
                )
                if (quantity > 1) {
                    Text(
                        text = "Total: ${getEstimatedTimeForQuantity(name, quantity)}",
                        fontSize = 12.sp,
                        color = Color(0xFF8B4513)
                    )
                }
            }
        }
    }
}

private fun getImageForDish(dish: String): Int {
    return when {
        dish.contains("Tacos", ignoreCase = true) -> R.drawable.tacos
        dish.contains("Tamales", ignoreCase = true) -> R.drawable.tamales
        dish.contains("Pozole", ignoreCase = true) -> R.drawable.pozole
        dish.contains("Enchiladas", ignoreCase = true) -> R.drawable.enchiladas
        else -> R.drawable.tacos
    }
}

private fun getTimeForDish(dish: String): String {
    return when {
        dish.contains("Tacos", ignoreCase = true) -> "12 min"
        dish.contains("Tamales", ignoreCase = true) -> "8 min"
        dish.contains("Pozole", ignoreCase = true) -> "22 min"
        dish.contains("Enchiladas", ignoreCase = true) -> "18 min"
        else -> "15 min"
    }
}

private fun getEstimatedTimeForQuantity(dish: String, quantity: Int): String {
    val baseTime = when {
        dish.contains("Tacos", ignoreCase = true) -> 12
        dish.contains("Tamales", ignoreCase = true) -> 8
        dish.contains("Pozole", ignoreCase = true) -> 22
        dish.contains("Enchiladas", ignoreCase = true) -> 18
        else -> 15
    }
    val totalTime = baseTime + (quantity - 1) * 3
    return "${totalTime} min"
}