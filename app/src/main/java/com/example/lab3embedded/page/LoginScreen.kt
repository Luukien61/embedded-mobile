package com.example.lab3embedded.page

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lab3embedded.Screen
import com.example.lab3embedded.retrofit.RetrofitInstance
import com.example.lab3embedded.retrofit.UserEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun LoginScreen(navController: NavController) {

    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    LaunchedEffect(Unit) {
        val userId = sharedPreferences.getLong("userId", -1)

        if (userId >0 ) {
            navController.navigate(Screen.Screen3_2.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login", style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isLoading = true
                errorMessage = ""
                login(email, password, context,navController)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text(text = if (isLoading) "Loading..." else "Login")
        }

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}


fun login(email: String, password: String, context: Context, navController: NavController) {
    val request = UserEntity(null, email, password, "")
    RetrofitInstance.api.login(request).enqueue(object : Callback<UserEntity> {
        override fun onResponse(call: Call<UserEntity>, response: Response<UserEntity>) {
            if (response.isSuccessful) {
                val loginResponse = response.body()
                loginResponse?.let {
                    val sharedPreferences =
                        context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    it.id?.let { it1 -> editor.putLong("userId", it1) }
                    editor.putString("username", it.username)
                    editor.apply()
                    Log.d("Login", it.toString())
                    navController.navigate(Screen.Screen3_2.route){
                        popUpTo(Screen.MainPage.route){inclusive=true}
                    }
                }
            } else {
                Log.e("Login", "Invalid credentials")
            }
        }

        override fun onFailure(call: Call<UserEntity>, t: Throwable) {
            Log.e("Login", "Error: ${t.message}")
        }
    })
}


