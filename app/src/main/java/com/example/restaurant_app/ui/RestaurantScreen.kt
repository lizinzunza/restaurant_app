package com.example.restaurant_app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restaurant_app.R

@Composable
fun RestaurantScreen() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8F0)) // Fondo beige muy claro
    ) {
        // Fondo con patrones mexicanos sutiles
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

            Spacer(modifier = Modifier.height(20.dp))

            // Título "Restaurante"
            Text(
                text = "Restaurante",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B0000), // Rojo oscuro
                modifier = Modifier.padding(bottom = 30.dp)
            )

            // Contenedor principal con formulario de login
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 8.dp,
                color = Color(0xFFFFF6E8) // Beige claro
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Ilustración del restaurante (usando logo temporal)
                    Image(
                        painter = painterResource(id = R.drawable.logo_bowl),
                        contentDescription = "Restaurante Mexicano",
                        modifier = Modifier
                            .size(140.dp)
                            .padding(bottom = 24.dp)
                    )

                    // Nombre del restaurante
                    Text(
                        text = "Mexitasty",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE6007E), // Rosa
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    // Campo de nombre de usuario
                    Text(
                        text = "Nombre de usuario",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF008080), // Verde azulado
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(bottom = 8.dp)
                    )
                    
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        placeholder = {
                            Text(
                                text = "Ingresa Nombre de usuario",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true
                    )

                    // Campo de contraseña
                    Text(
                        text = "Contraseña",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF008080), // Verde azulado
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(bottom = 8.dp)
                    )
                    
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = {
                            Text(
                                text = "Ingresa Contraseña",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 32.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent
                        ),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true
                    )

                    // Botón de ingresar
                    Button(
                        onClick = { /* Aquí puedes agregar la lógica de login */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE6007E)) // Rosa fucsia
                    ) {
                        Text(
                            text = "Ingresar",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(
    label: String,
    value: String,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = color,
            modifier = Modifier.width(100.dp)
        )
        Text(
            text = value,
            fontSize = 16.sp,
            color = Color(0xFF8B4513), // Marrón
            modifier = Modifier.weight(1f)
        )
    }
} 