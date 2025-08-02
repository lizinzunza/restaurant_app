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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restaurant_app.R

@Composable
fun TableOrderDetailScreen(tableNumber: String) {
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Papel picado decorativo
            Image(
                painter = painterResource(id = R.drawable.papel_picado),
                contentDescription = "Papel picado",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )

            // Título de la mesa con icono
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                // Icono triangular rosa
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
                
                // Título de la mesa
                Text(
                    text = tableNumber,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B0000)
                )
            }

            // Tiempo estimado
            Text(
                text = "Estimado 20- 25 min",
                fontSize = 16.sp,
                color = Color(0xFF8B4513),
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Lista de platillos del pedido
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Tacos al Pastor
                OrderItemCard(
                    imageRes = R.drawable.tacos,
                    name = "Tacos al Pastor",
                    time = "10-15 min",
                    quantity = 1
                )
                
                // Tamales
                OrderItemCard(
                    imageRes = R.drawable.tamales,
                    name = "Tamales",
                    time = "5-10 mins",
                    quantity = 1
                )
                
                // Pozole
                OrderItemCard(
                    imageRes = R.drawable.pozole,
                    name = "Pozole",
                    time = "20-25 mins",
                    quantity = 1
                )
            }
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
            // Imagen del platillo
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Información del platillo
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
                    text = time,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            
            // Cantidad
            Text(
                text = quantity.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B4513)
            )
        }
    }
} 