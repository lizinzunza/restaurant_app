package com.example.restaurant_app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.restaurant_app.R

@Composable
fun StatusScreen() {
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

            // Tarjeta principal con el contenido
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 8.dp,
                color = Color(0xFFFFF6E8)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    // Sección de tiempo de espera
                    Text(
                        text = "Tu tiempo de espera",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE6007E)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Estimado 20- 25 min",
                        fontSize = 16.sp,
                        color = Color(0xFF8B4513),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Barra de progreso
                    OrderProgressBar()
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Sección de orden
                    Text(
                        text = "Orden",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0099CC),
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    // Items del pedido
                    StatusOrderItem(
                        imageRes = R.drawable.tacos,
                        name = "Tacos al Pastor",
                        time = "10- 15 min",
                        price = "$ 20",
                        quantity = 1
                    )
                    
                    StatusOrderItem(
                        imageRes = R.drawable.tamales,
                        name = "Tamales",
                        time = "5- 10 min",
                        price = "$ 15",
                        quantity = 1
                    )
                    
                    StatusOrderItem(
                        imageRes = R.drawable.pozole,
                        name = "Pozole",
                        time = "20- 25 min",
                        price = "$ 15",
                        quantity = 1
                    )
                }
            }
        }
    }
}

@Composable
fun OrderProgressBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icono 1: Clipboard (Orden colocada)
        ProgressIcon(
            iconRes = R.drawable.ic_menu, // Usando icono temporal
            isActive = true,
            label = "Orden"
        )
        
        // Línea conectora
        Box(
            modifier = Modifier
                .weight(1f)
                .height(2.dp)
                .background(Color(0xFF0099CC))
        )
        
        // Icono 2: Cooking pot (En preparación)
        ProgressIcon(
            iconRes = R.drawable.ic_menu, // Usando icono temporal
            isActive = true,
            label = "Preparando"
        )
        
        // Línea conectora
        Box(
            modifier = Modifier
                .weight(1f)
                .height(2.dp)
                .background(Color(0xFF0099CC))
        )
        
        // Icono 3: Delivery scooter (En entrega)
        ProgressIcon(
            iconRes = R.drawable.ic_menu, // Usando icono temporal
            isActive = true,
            label = "Enviando"
        )
        
        // Línea conectora
        Box(
            modifier = Modifier
                .weight(1f)
                .height(2.dp)
                .background(Color.Gray)
        )
        
        // Icono 4: Checkmark (Completado)
        ProgressIcon(
            iconRes = R.drawable.ic_menu, // Usando icono temporal
            isActive = false,
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
            // Imagen del platillo
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = name,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Información del platillo
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
            
            // Cantidad
            Text(
                text = quantity.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B4513)
            )
        }
    }
} 