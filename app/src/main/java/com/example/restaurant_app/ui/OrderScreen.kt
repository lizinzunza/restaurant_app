package com.example.restaurant_app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.restaurant_app.R

@Composable
fun OrderScreen() {
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
                text = "Mi Pedido",
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B0000),
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Start)
            )
            
            OrderItem(
                imageRes = R.drawable.tacos,
                name = "Tacos al Pastor",
                time = "10-15 min",
                price = "$ 20",
                quantity = 1
            )
            
            OrderItem(
                imageRes = R.drawable.tamales,
                name = "Tamales",
                time = "5-10 min",
                price = "$ 15",
                quantity = 1
            )
            
            OrderItem(
                imageRes = R.drawable.pozole,
                name = "Pozole",
                time = "20-25 min",
                price = "$ 15",
                quantity = 1
            )

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
                            text = "100 $",
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
                            text = "10 $",
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
                            text = "10 $",
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
                            text = "110$",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Botón de realizar pedido
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFF6E8))
                    ) {
                        Text(
                            text = "Realizar mi pedido",
                            color = Color(0xFF8B4513),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun OrderItem(
    imageRes: Int,
    name: String,
    time: String,
    price: String,
    quantity: Int
) {
    var currentQuantity by remember { mutableStateOf(quantity) }
    
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
            // Imagen del platillo
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = name,
                modifier = Modifier
                    .size(80.dp)
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
                    color = Color(0xFF2D2D2D),
                    fontSize = 16.sp
                )
                Text(
                    text = time,
                    fontSize = 12.sp,
                    color = Color.Gray
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
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { if (currentQuantity > 0) currentQuantity-- },
                    modifier = Modifier.size(32.dp),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFBE4EE)),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "-",
                        color = Color(0xFFE6007E),
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Text(
                    text = currentQuantity.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D2D2D),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                
                Button(
                    onClick = { currentQuantity++ },
                    modifier = Modifier.size(32.dp),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE6007E)),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "+",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
} 