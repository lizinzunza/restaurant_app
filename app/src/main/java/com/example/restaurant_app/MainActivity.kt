package com.example.restaurant_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.restaurant_app.ui.LoginScreen
import com.example.restaurant_app.ui.MenuScreen
import com.example.restaurant_app.ui.theme.Restaurant_appTheme
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Restaurant_appTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    AppNavHost(navController)
                }
            }
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(onLoginClick = { navController.navigate("menu") })
        }
        composable("menu") {
            MenuScreen()
        }
    }
}

