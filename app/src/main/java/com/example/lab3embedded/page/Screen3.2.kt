package com.example.lab3embedded.page

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lab3embedded.Screen
import com.example.lab3embedded.retrofit.RetrofitInstance
import com.example.lab3embedded.retrofit.UserEntity
import kotlinx.coroutines.launch


@Composable
fun Screen3_2(navController: NavController) {
    var isEditing by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var userMessage by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var userId by remember { mutableLongStateOf(-1) }
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        val localUserId = sharedPreferences.getLong("userId", -1)
        if (localUserId > 0) {
            val user = RetrofitInstance.api.getUser(localUserId)
            user.let {
                email = it.username
                message = it.message
                userMessage=it.message
                userId= localUserId
            }
        }
    }

    fun logout() {
        val editor = sharedPreferences.edit()
        editor.remove("userId")
        editor.remove("username")
        editor.apply()
        navController.navigate(Screen.Login.route)
    }

    suspend fun updateMessage() {
        if(message!=userMessage){
            val user = UserEntity(userId, email, "", message)
            val response = RetrofitInstance.api.updateUser(userId,user)
            val message1 = response.message
            userMessage= message1
            message= message1
        }
    }
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier =
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth(), elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(
                        16.dp
                    )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Account",
                                modifier = Modifier.size(30.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = email,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Black,
                            )
                        }
                        Text(
                            text = "Logout",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Red,
                            modifier = Modifier.clickable { logout() })
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isEditing) {
                            TextField(
                                value = message,
                                onValueChange = { message = it },
                                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                                singleLine = true
                            )
                            Row(
                                modifier = Modifier
                                    .height(IntrinsicSize.Min)
                            ) {
                                IconButton(
                                    modifier = Modifier
                                        .height(20.dp)
                                        .width(20.dp),
                                    onClick = {
                                        isEditing = false
                                        coroutineScope.launch {
                                            updateMessage()
                                        }
                                    }) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Save"
                                    )
                                }
                                Spacer(Modifier.width(10.dp))
                                IconButton(
                                    modifier = Modifier
                                        .height(20.dp)
                                        .width(20.dp),
                                    onClick = {
                                        isEditing = false
                                        message=userMessage
                                    }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "close"
                                    )
                                }
                            }
                        } else {
                            Text(
                                text = message,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Black,
                            )
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable { isEditing = true }

                            )
                        }
                    }
                }
            }
        }
    }
}