// MainActivity.kt (corregido)
package com.example.tvapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tvapp.ui.OrdersScreen
import com.example.tvapp.ui.TableOrderDetailScreen
import com.example.tvapp.ui.theme.TvAppTheme // Usar el tema corregido
import com.example.tvapp.viewmodel.TVRestaurantViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TvAppTheme { // Usar TvAppTheme
                val navController = rememberNavController()
                TVAppNavigation(navController)
            }
        }
    }
}

@Composable
fun TVAppNavigation(navController: NavHostController) {
    val tvRestaurantViewModel: TVRestaurantViewModel = viewModel()

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = "orders"
        ) {
            composable("orders") {
                OrdersScreen(
                    onTableClick = { tableNumber ->
                        navController.navigate("table_detail/$tableNumber")
                    },
                    tvRestaurantViewModel = tvRestaurantViewModel
                )
            }

            composable("table_detail/{tableNumber}") { backStackEntry ->
                val tableNumber = backStackEntry.arguments?.getString("tableNumber") ?: ""
                TableOrderDetailScreen(
                    tableNumber = tableNumber,
                    onBack = {
                        navController.popBackStack()
                    },
                    tvRestaurantViewModel = tvRestaurantViewModel
                )
            }
        }
    }
}