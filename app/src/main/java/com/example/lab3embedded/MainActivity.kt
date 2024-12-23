package com.example.lab3embedded

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.lab3embedded.ui.theme.Lab3EmbeddedTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab3EmbeddedTheme{
                val navController = rememberNavController()
                AppNavHost(navController)
            }
        }
    }
}
