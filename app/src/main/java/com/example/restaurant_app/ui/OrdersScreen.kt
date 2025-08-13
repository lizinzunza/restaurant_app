// ui/OrdersScreen.kt (completo corregido)
package com.example.restaurant_app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.restaurant_app.R
import com.example.restaurant_app.viewmodel.RestaurantViewModel
import com.example.restaurant_app.viewmodel.formatElapsedTime
import com.example.restaurant_app.viewmodel.formatEstimatedTime

@Composable
fun OrdersScreen(
    onTableClick: (String) -> Unit,
    onLogout: () -> Unit = {},
    restaurantViewModel: RestaurantViewModel = viewModel()
) {
    val mesas by restaurantViewModel.mesas.collectAsState()

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

        // Botón de logout
        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 40.dp, end = 16.dp)
                .size(48.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFFE6007E)
        ) {
            IconButton(
                onClick = onLogout,
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
            Image(
                painter = painterResource(id = R.drawable.papel_picado),
                contentDescription = "Papel picado",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )

            Text(
                text = "Órdenes",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B0000),
                modifier = Modifier.padding(16.dp)
            )

            // Leyenda de colores basada en tiempo estimado
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFFFF6E8),
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = "Prioridad por tiempo estimado:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8B4513)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ColorLegend("≤15min", Color(0xFF90EE90))
                        ColorLegend("15-25min", Color(0xFFFFD93D))
                        ColorLegend("25-35min", Color(0xFFFF9800))
                        ColorLegend("35+min", Color(0xFFFF6B6B))
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                mesas.forEach { mesa ->
                    TableCard(
                        tableNumber = mesa.numero,
                        backgroundColor = Color(mesa.color),
                        hasPedido = mesa.pedido != null,
                        tiempoEspera = mesa.tiempoEspera,
                        tiempoEstimadoTotal = mesa.tiempoEstimadoTotal,
                        status = mesa.pedido?.status ?: 0,
                        onClick = {
                            if (mesa.pedido != null) {
                                restaurantViewModel.selectOrder(mesa.numero)
                                onTableClick(mesa.numero)
                            }
                        }
                    )
                }

                if (mesas.isEmpty()) {
                    Text(
                        text = "Cargando pedidos...",
                        fontSize = 18.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(32.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ColorLegend(text: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, RoundedCornerShape(6.dp))
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            fontSize = 10.sp,
            color = Color(0xFF8B4513)
        )
    }
}

@Composable
fun TableCard(
    tableNumber: String,
    backgroundColor: Color,
    hasPedido: Boolean,
    tiempoEspera: Long,
    tiempoEstimadoTotal: Int,
    status: Int,
    onClick: () -> Unit
) {
    val statusText = when (status) {
        1 -> "Recibido"
        2 -> "Preparando"
        3 -> "Listo"
        4 -> "Entregado"
        else -> "Sin pedidos"
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable(enabled = hasPedido) { onClick() },
        shape = RoundedCornerShape(16.dp),
        shadowElevation = if (hasPedido) 6.dp else 2.dp,
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = tableNumber,
                    color = if (hasPedido) Color.White else Color.Gray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                if (hasPedido) {
                    Text(
                        text = "Estimado: ${formatEstimatedTime(tiempoEstimadoTotal)}",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Transcurrido: ${formatElapsedTime(tiempoEspera)}",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Estado: $statusText",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                } else {
                    Text(
                        text = "Sin pedidos activos",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }

            if (hasPedido) {
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
            }
        }
    }
}