package com.example.restaurant_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.restaurant_app.ui.LoginScreen
import com.example.restaurant_app.ui.MenuScreen
import com.example.restaurant_app.ui.OrderScreen
import com.example.restaurant_app.ui.StatusScreen
import com.example.restaurant_app.ui.RestaurantScreen
import com.example.restaurant_app.ui.theme.Restaurant_appTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Configurar pantalla completa edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            Restaurant_appTheme {
                val navController = rememberNavController()
                AppWithBottomNavigation(navController)
            }
        }
    }
}

@Composable
fun AppWithBottomNavigation(navController: NavHostController) {
    var selectedTab by remember { mutableStateOf(0) }

    // Ruta actual en tiempo real
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    LaunchedEffect(currentRoute) {
        when (currentRoute) {
            "menu" -> selectedTab = 0
            "order" -> selectedTab = 1
            "status" -> selectedTab = 2
            "restaurant" -> selectedTab = 3
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (currentRoute != "login") {
                BottomNavigationBar(
                    selectedTab = selectedTab,
                    onTabSelected = { tabIndex ->
                        selectedTab = tabIndex
                        when (tabIndex) {
                            0 -> navController.navigate("menu") {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                            1 -> navController.navigate("order") {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                            2 -> navController.navigate("status") {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                            3 -> navController.navigate("restaurant") {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AppNavHost(navController)
        }
    }
}

@Composable
fun BottomNavigationBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color(0xFFE6007E)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(
            iconRes = R.drawable.ic_menu,
            label = "Menú",
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) }
        )
        BottomNavItem(
            iconRes = R.drawable.ic_pedido,
            label = "Pedido",
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) }
        )
        BottomNavItem(
            iconRes = R.drawable.ic_status,
            label = "Status",
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) }
        )
        BottomNavItem(
            iconRes = R.drawable.ic_restaurante,
            label = "Restaurante",
            selected = selectedTab == 3,
            onClick = { onTabSelected(3) }
        )
    }
}

@Composable
fun BottomNavItem(
    iconRes: Int,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(vertical = 4.dp)
            .clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier.size(28.dp)
        )
        Text(
            label,
            color = if (selected) Color(0xFFFFE066) else Color.White,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
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
        composable("order") {
            OrderScreen()
        }
        composable("status") {
            StatusScreen()
        }
        composable("restaurant") {
            RestaurantScreen()
        }
    }
}
