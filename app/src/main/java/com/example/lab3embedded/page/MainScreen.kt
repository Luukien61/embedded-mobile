package com.example.lab3embedded.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.lab3embedded.Screen

@Composable
fun MainScreen(navController: NavController){

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    // Navigate to detail page when 3.1 button is clicked
                    navController.navigate(Screen.Screen3_1.route)
                },
                modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .padding(8.dp)
            ) {
                Text("3.1")
            }

            Button(
                onClick = {
                    navController.navigate(Screen.Login.route)
                },
                modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .padding(8.dp)
            ) {
                Text("3.2")
            }
        }
    }

}
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    val mockNavController = rememberNavController()
    MainScreen(navController = mockNavController)

}