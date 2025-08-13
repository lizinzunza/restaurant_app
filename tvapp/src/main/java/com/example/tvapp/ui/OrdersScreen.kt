// ui/OrdersScreen.kt (CON LOGS DE DEBUG)
package com.example.tvapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.tvapp.R
import com.example.tvapp.viewmodel.TVRestaurantViewModel
import com.example.tvapp.viewmodel.formatElapsedTime
import com.example.tvapp.viewmodel.formatEstimatedTime

@Composable
fun OrdersScreen(
    onTableClick: (String) -> Unit,
    tvRestaurantViewModel: TVRestaurantViewModel = viewModel()
) {
    val mesas by tvRestaurantViewModel.mesas.collectAsState()
    val isLoading by tvRestaurantViewModel.isLoading.collectAsState()

    // LOGS DE DEBUG EN UI
    LaunchedEffect(mesas) {
        println("🖥️ UI OrdersScreen - Mesas actualizadas: ${mesas.size}")
        mesas.forEachIndexed { index, mesa ->
            println("🖥️ UI Mesa [$index]: Número ${mesa.numero}, Tiene pedido: ${mesa.pedido != null}")
            if (mesa.pedido != null) {
                println("🖥️ UI Mesa ${mesa.numero} - Pedido: ${mesa.pedido!!.pedidos}, Status: ${mesa.pedido!!.status}")
            }
        }
    }

    LaunchedEffect(isLoading) {
        println("🖥️ UI OrdersScreen - Estado de carga: $isLoading")
    }

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

        // Papel picado mejorado y más visible
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .offset(y = (-10).dp)
        ) {
            repeat(4) { index ->
                Image(
                    painter = painterResource(id = R.drawable.papel_picado),
                    contentDescription = "Papel picado ${index + 1}",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .offset(y = if (index % 2 == 0) (-195).dp else (-165).dp), // Intercalado
                    contentScale = ContentScale.FillBounds
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Espaciado para el papel picado
            Spacer(modifier = Modifier.height(90.dp))

            // Título "Órdenes" con indicador de conexión y DEBUG INFO
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 48.dp, top = 20.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Órdenes",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B0000)
                )

                Spacer(modifier = Modifier.width(24.dp))

                // DEBUG INFO - Contador de mesas
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF2196F3),
                    shadowElevation = 4.dp
                ) {
                    Text(
                        text = "Mesas: ${mesas.size}",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Indicador de conexión/carga
                if (isLoading) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFFFF9800),
                        shadowElevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Actualizando...",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFF4CAF50),
                        shadowElevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(Color.White, RoundedCornerShape(4.dp))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "En vivo",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Leyenda de colores para TV
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp, vertical = 12.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFFFF6E8),
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ColorLegendTV("≤15min", Color(0xFF7CB342), "Rápido")
                    ColorLegendTV("15-25min", Color(0xFFFFA726), "Normal")
                    ColorLegendTV("25-35min", Color(0xFFFF9800), "Atención")
                    ColorLegendTV("35+min", Color(0xFFEF5350), "Urgente")
                }
            }

            // INFORMACIÓN DE DEBUG
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp, vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFE3F2FD),
                shadowElevation = 2.dp
            ) {
//                Column(
//                    modifier = Modifier.padding(16.dp)
//                ) {
//                    Text(
//                        text = "🔍 DEBUG INFO",
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color(0xFF1976D2)
//                    )
//                    Text(
//                        text = "Total mesas: ${mesas.size}",
//                        fontSize = 14.sp,
//                        color = Color(0xFF1976D2)
//                    )
//                    Text(
//                        text = "Cargando: $isLoading",
//                        fontSize = 14.sp,
//                        color = Color(0xFF1976D2)
//                    )
//                    Text(
//                        text = "Mesas con pedidos: ${mesas.count { it.pedido != null }}",
//                        fontSize = 14.sp,
//                        color = Color(0xFF1976D2)
//                    )
//                }
            }

            // Lista de mesas con datos reales
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                println("🖥️ UI Renderizando ${mesas.size} mesas")

                if (mesas.isNotEmpty()) {
                    mesas.forEach { mesa ->
                        println("🖥️ UI Renderizando mesa ${mesa.numero}")
                        TableCardTV(
                            mesa = mesa,
                            onClick = {
                                println("🖥️ UI Click en mesa ${mesa.numero}")
                                if (mesa.pedido != null) {
                                    tvRestaurantViewModel.selectOrder(mesa.numero)
                                    onTableClick("Mesa ${mesa.numero}")
                                }
                            }
                        )
                    }
                } else {
                    println("🖥️ UI No hay mesas para mostrar")
                    // Estado de carga o sin pedidos
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(85.dp),
                        shape = RoundedCornerShape(16.dp),
                        shadowElevation = 4.dp,
                        color = Color(0xFFE0E0E0)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isLoading) "Cargando órdenes..." else "No hay pedidos activos",
                                fontSize = 20.sp,
                                color = Color(0xFF666666),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Espaciado para empujar contenido hacia arriba
            Spacer(modifier = Modifier.height(140.dp))
        }

        // Cactus (sin cambios - perfectos como están)
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 8.dp, bottom = 0.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Image(
                painter = painterResource(id = R.drawable.cactus1),
                contentDescription = "Cactus 1",
                modifier = Modifier.size(80.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.cactus2),
                contentDescription = "Cactus 2",
                modifier = Modifier.size(88.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.cactus3),
                contentDescription = "Cactus 3",
                modifier = Modifier.size(84.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.cactus4),
                contentDescription = "Cactus 4",
                modifier = Modifier.size(76.dp)
            )
        }
    }
}

@Composable
fun ColorLegendTV(
    timeRange: String,
    color: Color,
    description: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(color, RoundedCornerShape(10.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = timeRange,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B4513)
            )
        }
        Text(
            text = description,
            fontSize = 12.sp,
            color = Color(0xFF8B4513)
        )
    }
}

@Composable
fun TableCardTV(
    mesa: com.example.tvapp.viewmodel.MesaTV,
    onClick: () -> Unit
) {
    println("🖥️ UI TableCardTV - Mesa ${mesa.numero}, Tiene pedido: ${mesa.pedido != null}")

    val backgroundColor = Color(mesa.color)
    val hasPedido = mesa.pedido != null

    val statusText = if (hasPedido) {
        when (mesa.pedido?.status) {
            1 -> "Recibido"
            2 -> "Preparando"
            3 -> "Listo"
            4 -> "Entregado"
            else -> "Desconocido"
        }
    } else "Sin pedidos"

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable(enabled = hasPedido) { onClick() },
        shape = RoundedCornerShape(16.dp),
        shadowElevation = if (hasPedido) 8.dp else 4.dp,
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Información de la mesa
            Column {
                // Botón de mesa mejorado
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    shadowElevation = 4.dp
                ) {
                    Text(
                        text = "Mesa ${mesa.numero}",
                        color = Color(0xFF008080),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
                    )
                }

                if (hasPedido) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Estado: $statusText",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                } else {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Sin pedidos",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Información de tiempo (solo si hay pedido)
            if (hasPedido) {
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Estimado: ${formatEstimatedTime(mesa.tiempoEstimadoTotal)}",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Transcurrido: ${formatElapsedTime(mesa.tiempoEspera)}",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }

            // Icono de flecha (solo si hay pedido)
            if (hasPedido) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF008080),
                    shadowElevation = 4.dp
                ) {
                    Box(
                        modifier = Modifier.size(44.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "▶",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}