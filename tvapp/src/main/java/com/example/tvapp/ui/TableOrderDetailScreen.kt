// ui/TableOrderDetailScreen.kt (nueva para TV)
package com.example.tvapp.ui

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
import com.example.tvapp.R
import com.example.tvapp.viewmodel.TVRestaurantViewModel
import com.example.tvapp.viewmodel.calculateTimeElapsed
import com.example.tvapp.viewmodel.formatElapsedTime
import com.example.tvapp.viewmodel.calculateTotalEstimatedTime
import com.example.tvapp.viewmodel.formatEstimatedTime

@Composable
fun TableOrderDetailScreen(
    tableNumber: String,
    onBack: () -> Unit = {},
    tvRestaurantViewModel: TVRestaurantViewModel = viewModel()
) {
    val selectedOrder by tvRestaurantViewModel.selectedOrder.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.fondo_menu),
            contentDescription = "Fondo mexicano",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Papel picado decorativo
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .offset(y = (-10).dp)
        ) {
            repeat(6) { index ->
                Image(
                    painter = painterResource(id = R.drawable.papel_picado),
                    contentDescription = "Papel picado ${index + 1}",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .offset(y = if (index % 2 == 0) (-150).dp else (-120).dp),
                    contentScale = ContentScale.FillBounds
                )
            }
        }

        // Botón de regreso
        Surface(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 40.dp, start = 48.dp)
                .size(56.dp)
                .clickable { onBack() },
            shape = RoundedCornerShape(28.dp),
            color = Color(0xFFE6007E),
            shadowElevation = 6.dp
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Regresar a órdenes",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 120.dp, start = 48.dp, end = 48.dp, bottom = 48.dp)
        ) {
            // Columna izquierda - Información del pedido
            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
            ) {
                // Título de la mesa con icono
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFE6007E),
                        shadowElevation = 4.dp
                    ) {
                        Box(
                            modifier = Modifier.size(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "▶",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = tableNumber,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8B0000)
                    )
                }

                selectedOrder?.let { order ->
                    val tiempoTranscurrido = calculateTimeElapsed(order.timestamp)
                    val tiempoEstimadoTotal = calculateTotalEstimatedTime(order.pedidos)
                    val tiempoRestante = maxOf(0, tiempoEstimadoTotal - tiempoTranscurrido.toInt())

                    // Información de tiempos - Diseño para TV
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFFFFF6E8),
                        shadowElevation = 8.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Text(
                                text = "Información del Pedido",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF8B0000),
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                TimeInfoCardTV(
                                    title = "Tiempo Estimado",
                                    time = formatEstimatedTime(tiempoEstimadoTotal),
                                    color = Color(0xFFE6007E),
                                    large = true
                                )

                                TimeInfoCardTV(
                                    title = "Transcurrido",
                                    time = formatElapsedTime(tiempoTranscurrido),
                                    color = Color(0xFF8B4513),
                                    large = true
                                )

                                TimeInfoCardTV(
                                    title = "Restante",
                                    time = if (tiempoRestante > 0) formatEstimatedTime(tiempoRestante) else "¡Listo!",
                                    color = if (tiempoRestante > 0) Color(0xFF8B4513) else Color(0xFF4CAF50),
                                    large = true
                                )
                            }
                        }
                    }

                    // Estado actual del pedido
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFFFFF6E8),
                        shadowElevation = 8.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Text(
                                text = "Estado Actual",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF8B0000),
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            StatusProgressBarTV(order.status)
                        }
                    }

                } ?: run {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFFFFEBEE),
                        shadowElevation = 4.dp
                    ) {
                        Box(
                            modifier = Modifier.padding(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No hay información del pedido",
                                fontSize = 24.sp,
                                color = Color(0xFF8B0000),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(32.dp))

            // Columna derecha - Lista de platillos
            Column(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight()
            ) {
                Text(
                    text = "Platillos del Pedido",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B0000),
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                selectedOrder?.let { order ->
                    LazyColumnTV(
                        items = order.pedidos.groupBy { it }.toList()
                    ) { (platillo, lista) ->
                        OrderItemCardTV(
                            imageRes = getImageForDish(platillo),
                            name = platillo,
                            time = getTimeForDish(platillo),
                            quantity = lista.size
                        )
                    }
                } ?: run {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFE0E0E0),
                        shadowElevation = 4.dp
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Sin platillos",
                                fontSize = 20.sp,
                                color = Color(0xFF666666)
                            )
                        }
                    }
                }
            }
        }

        // Cactus en la esquina
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 0.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Image(
                painter = painterResource(id = R.drawable.cactus1),
                contentDescription = "Cactus 1",
                modifier = Modifier.size(64.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.cactus2),
                contentDescription = "Cactus 2",
                modifier = Modifier.size(70.dp)
            )
        }
    }
}

@Composable
fun LazyColumnTV(
    items: List<Pair<String, List<String>>>,
    content: @Composable (Pair<String, List<String>>) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items.forEach { item ->
            content(item)
        }
    }
}

@Composable
fun TimeInfoCardTV(
    title: String,
    time: String,
    color: Color,
    large: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = if (large) 16.sp else 14.sp,
            fontWeight = FontWeight.Medium,
            color = color
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = time,
            fontSize = if (large) 24.sp else 20.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
fun StatusProgressBarTV(currentStatus: Int) {
    Column {
        // Barra de progreso horizontal para TV
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatusIconTV(
                iconRes = R.drawable.ic_menu,
                isActive = currentStatus >= 1,
                label = "Recibido"
            )

            StatusLineTV(isActive = currentStatus >= 2)

            StatusIconTV(
                iconRes = R.drawable.ic_pedido,
                isActive = currentStatus >= 2,
                label = "Preparando"
            )

            StatusLineTV(isActive = currentStatus >= 3)

            StatusIconTV(
                iconRes = R.drawable.ic_status,
                isActive = currentStatus >= 3,
                label = "Listo"
            )

            StatusLineTV(isActive = currentStatus >= 4)

            StatusIconTV(
                iconRes = R.drawable.ic_restaurante,
                isActive = currentStatus >= 4,
                label = "Entregado"
            )
        }
    }
}

@Composable
fun StatusIconTV(
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
            shadowElevation = if (isActive) 6.dp else 2.dp,
            modifier = Modifier.size(48.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = label,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontSize = 14.sp,
            color = if (isActive) Color(0xFF0099CC) else Color.Gray,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun RowScope.StatusLineTV(isActive: Boolean) {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(4.dp)
            .background(
                if (isActive) Color(0xFF0099CC) else Color.Gray,
                RoundedCornerShape(2.dp)
            )
    )
}

@Composable
fun OrderItemCardTV(
    imageRes: Int,
    name: String,
    time: String,
    quantity: Int
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 6.dp,
        color = Color(0xFFFFF6E8)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del platillo
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(20.dp))

            // Información del platillo
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B4513),
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Prep: $time",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }

            // Cantidad
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFE6007E),
                shadowElevation = 4.dp
            ) {
                Text(
                    text = "x$quantity",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

// Funciones auxiliares
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