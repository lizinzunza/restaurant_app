package com.example.restaurant_app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MenuScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE6007E))
    ) {
        Text(
            text = "Menú Principal",
            fontSize = 32.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
} 