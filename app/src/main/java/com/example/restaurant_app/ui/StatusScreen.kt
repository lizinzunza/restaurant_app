// ui/StatusScreen.kt (completo corregido)
package com.example.restaurant_app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.restaurant_app.R
import com.example.restaurant_app.viewmodel.StatusViewModel
import com.example.restaurant_app.viewmodel.calculateTimeElapsed
import com.example.restaurant_app.viewmodel.formatElapsedTime
import com.example.restaurant_app.viewmodel.calculateTotalEstimatedTime
import com.example.restaurant_app.viewmodel.formatEstimatedTime

@Composable
fun StatusScreen(
    onLogoutClick: () -> Unit = {},
    statusViewModel: StatusViewModel = viewModel()
) {
    val currentOrder by statusViewModel.currentOrder.collectAsState()
    val isLoading by statusViewModel.isLoading.collectAsState()

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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(100.dp))

            Text(
                text = "Status",
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B0000),
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Start)
            )

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(32.dp),
                    color = Color(0xFFE6007E)
                )
            } else if (currentOrder != null) {
                OrderStatusCard(currentOrder!!)
            } else {
                Text(
                    text = "No hay pedidos activos",
                    fontSize = 18.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(32.dp)
                )
            }
        }
    }
}

@Composable
fun OrderStatusCard(order: com.example.restaurant_app.model.PedidoResponse) {
    val tiempoTranscurrido = calculateTimeElapsed(order.timestamp)
    val tiempoEstimadoTotal = calculateTotalEstimatedTime(order.pedidos)
    val tiempoRestante = maxOf(0, tiempoEstimadoTotal - tiempoTranscurrido.toInt())

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 8.dp,
        color = Color(0xFFFFF6E8)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Información de tiempo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TimeInfoCard(
                    title = "Tiempo estimado",
                    time = formatEstimatedTime(tiempoEstimadoTotal),
                    color = Color(0xFFE6007E)
                )

                TimeInfoCard(
                    title = "Tiempo restante",
                    time = if (tiempoRestante > 0) formatEstimatedTime(tiempoRestante) else "¡Listo!",
                    color = if (tiempoRestante > 0) Color(0xFF8B4513) else Color(0xFF4CAF50)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            OrderProgressBar(order.status)

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Orden",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0099CC),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            order.pedidos.groupBy { it }.forEach { (platillo, lista) ->
                StatusOrderItem(
                    imageRes = getImageForDish(platillo),
                    name = platillo,
                    time = getTimeForDish(platillo),
                    price = "$${getPriceForDish(platillo)}",
                    quantity = lista.size
                )
            }
        }
    }
}

@Composable
fun TimeInfoCard(
    title: String,
    time: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = color
        )
        Text(
            text = time,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
fun OrderProgressBar(currentStatus: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProgressIcon(
            iconRes = R.drawable.ic_menu,
            isActive = currentStatus >= 1,
            label = "Recibido"
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(2.dp)
                .background(if (currentStatus >= 2) Color(0xFF0099CC) else Color.Gray)
        )

        ProgressIcon(
            iconRes = R.drawable.ic_pedido,
            isActive = currentStatus >= 2,
            label = "Preparando"
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(2.dp)
                .background(if (currentStatus >= 3) Color(0xFF0099CC) else Color.Gray)
        )

        ProgressIcon(
            iconRes = R.drawable.ic_status,
            isActive = currentStatus >= 3,
            label = "Listo"
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(2.dp)
                .background(if (currentStatus >= 4) Color(0xFF0099CC) else Color.Gray)
        )

        ProgressIcon(
            iconRes = R.drawable.ic_restaurante,
            isActive = currentStatus >= 4,
            label = "Entregado"
        )
    }
}

@Composable
fun ProgressIcon(
    iconRes: Int,
    isActive: Boolean,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = RoundedCornerShape(50),
            color = if (isActive) Color(0xFF0099CC) else Color.Gray,
            modifier = Modifier.size(40.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = label,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            color = if (isActive) Color(0xFF0099CC) else Color.Gray,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun StatusOrderItem(
    imageRes: Int,
    name: String,
    time: String,
    price: String,
    quantity: Int
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFFFF6E8),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = name,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B4513),
                    fontSize = 14.sp
                )
                Text(
                    text = time,
                    fontSize = 12.sp,
                    color = Color(0xFF8B4513)
                )
                Text(
                    text = price,
                    color = Color(0xFF007F00),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Text(
                text = quantity.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B4513)
            )
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

private fun getPriceForDish(dish: String): Double {
    return when {
        dish.contains("Tacos", ignoreCase = true) -> 7.50
        dish.contains("Tamales", ignoreCase = true) -> 6.50
        dish.contains("Pozole", ignoreCase = true) -> 5.70
        dish.contains("Enchiladas", ignoreCase = true) -> 5.50
        else -> 7.50
    }
}