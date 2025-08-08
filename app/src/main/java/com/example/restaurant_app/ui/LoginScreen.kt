package com.example.restaurant_app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restaurant_app.R
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.platform.LocalDensity

@Composable
fun LoginScreen(onLoginClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE6007E))
    ) {
        Image(
            painter = painterResource(id = R.drawable.papel_picado),
            contentDescription = "Decoración superior",
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-250).dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp, bottom = 90.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_bowl),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Mexitasty",
                fontSize = 36.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onLoginClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(50.dp)
            ) {
                Text(
                    text = "Entrar",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .align(Alignment.BottomCenter)
                .offset(y = 10  .dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.Bottom
        ) {
            Image(
                painter = painterResource(id = R.drawable.cactus1),
                contentDescription = "Cactus 1",
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.cactus2),
                contentDescription = "Cactus 2",
                modifier = Modifier
                    .height(70.dp)
                    .width(60.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.cactus3),
                contentDescription = "Cactus 3",
                modifier = Modifier
                    .height(90.dp)
                    .width(90.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.cactus4),
                contentDescription = "Cactus 4",
                modifier = Modifier
                    .height(70.dp)
                    .width(70.dp)
            )
        }
    }
}
