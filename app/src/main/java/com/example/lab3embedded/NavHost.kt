package com.example.lab3embedded

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.lab3embedded.page.LoginScreen
import com.example.lab3embedded.page.MainScreen
import com.example.lab3embedded.page.Screen3_1
import com.example.lab3embedded.page.Screen3_2

sealed class Screen(val route: String) {
    data object MainPage : Screen("main")
    data object Screen3_1 : Screen("screen3.1")
    data object Screen3_2 : Screen("screen3.2")
    data object Login : Screen("login")
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.MainPage.route) {
        composable(Screen.MainPage.route) { MainScreen(navController) }
        composable(Screen.Screen3_1.route)  { Screen3_1()  }
        composable(Screen.Screen3_1.route)  { Screen3_1()  }
        composable(Screen.Login.route)  { LoginScreen(navController) }
        composable(Screen.Screen3_2.route){ Screen3_2(navController) }

    }
}
