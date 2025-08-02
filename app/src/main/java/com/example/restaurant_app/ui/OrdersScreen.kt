package com.example.restaurant_app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restaurant_app.R

@Composable
fun OrdersScreen(onTableClick: (String) -> Unit) {
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

            // Título "Órdenes"
            Text(
                text = "Órdenes",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B0000),
                modifier = Modifier.padding(16.dp)
            )

            // Lista de mesas
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Mesa 4
                TableCard(
                    tableNumber = "Mesa 4",
                    backgroundColor = Color(0xFF90EE90), // Verde claro
                    onClick = { onTableClick("Mesa 4") }
                )
                
                // Mesa 6
                TableCard(
                    tableNumber = "Mesa 6",
                    backgroundColor = Color(0xFFFF6B6B), // Rojo
                    onClick = { onTableClick("Mesa 6") }
                )
                
                // Mesa 7
                TableCard(
                    tableNumber = "Mesa 7",
                    backgroundColor = Color(0xFFFFD93D), // Amarillo
                    onClick = { onTableClick("Mesa 7") }
                )
                
                // Mesa 9
                TableCard(
                    tableNumber = "Mesa 9",
                    backgroundColor = Color(0xFF90EE90), // Verde claro
                    onClick = { onTableClick("Mesa 9") }
                )
                
                // Mesa 2
                TableCard(
                    tableNumber = "Mesa 2",
                    backgroundColor = Color(0xFFFF6B6B), // Rojo
                    onClick = { onTableClick("Mesa 2") }
                )
            }
        }
    }
}

@Composable
fun TableCard(
    tableNumber: String,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 4.dp,
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Botón de mesa
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFFFF6E8)
            ) {
                Text(
                    text = tableNumber,
                    color = Color(0xFF008080),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            
            // Icono de flecha triangular
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFE6007E)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Triángulo apuntando a la derecha (simulado con texto)
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