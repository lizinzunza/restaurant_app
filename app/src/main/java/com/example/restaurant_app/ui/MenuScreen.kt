package com.example.restaurant_app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ExitToApp
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
fun MenuScreen(onLogoutClick: () -> Unit = {}) {
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(100.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mexitasty",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B0000),
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    color = Color(0xFFE6007E),
                    shape = RoundedCornerShape(12.dp),
                    shadowElevation = 4.dp
                ) {
                    Text(
                        "Mesa 4",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                shape = RoundedCornerShape(32.dp),
                shadowElevation = 6.dp,
                tonalElevation = 6.dp,
                color = Color(0xFFFBE4EE)
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = {
                        Text(
                            text = "Busca tus platillos favoritos",
                            color = Color(0xFFE6007E),
                            fontSize = 14.sp
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = Color(0xFFE6007E)
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(32.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Categorías
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CategoryItem(
                    imageRes = R.drawable.elipse1,
                    label = "Antojitos",
                    selected = true
                )
                CategoryItem(
                    imageRes = R.drawable.elipse2,
                    label = "Bebidas",
                    selected = false
                )
                CategoryItem(
                    imageRes = R.drawable.elipse3,
                    label = "Postres",
                    selected = false
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            ) {
                DishCard(R.drawable.tacos, "Tacos al pastor", "10-15 min", "$7.50")
                DishCard(R.drawable.enchiladas, "Enchiladas verdes", "15-20 min", "$5.50")
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            ) {
                DishCard(R.drawable.pozole, "Pozole", "20-25 min", "$5.70")
                DishCard(R.drawable.tamales, "Tamales", "5-10 min", "$6.50")
            }
        }
    }
}

@Composable
fun DishCard(imageRes: Int, name: String, time: String, price: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .width(190.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 6.dp,
        color = Color(0xFFFFF6E8)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(12.dp)
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = name,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(50.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(name, fontWeight = FontWeight.Bold, color = Color(0xFF2D2D2D))
            Text(time, fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(price, color = Color(0xFF007F00), fontWeight = FontWeight.Bold)
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0099CC)),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text("Agregar", color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}



@Composable
fun CategoryItem(imageRes: Int, label: String, selected: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            shape = RoundedCornerShape(50),
            color = if (selected) Color(0xFFFFE066) else Color.White,
            shadowElevation = if (selected) 6.dp else 2.dp
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = label,
                modifier = Modifier
                    .size(48.dp)
                    .padding(4.dp),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            label,
            color = if (selected) Color(0xFFE6007E) else Color.Gray,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 14.sp
        )
    }
}
