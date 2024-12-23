package com.example.lab3embedded.page

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab3embedded.retrofit.DataResponse
import com.example.lab3embedded.retrofit.RetrofitInstance
import com.example.lab3embedded.ui.theme.Green80
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@Preview
@Composable
fun Screen3_1() {
    var led1Value by remember { mutableStateOf(false) }
    var led2Value by remember { mutableStateOf(false) }
    var temperatureValue by remember { mutableFloatStateOf(0f) }
    var humidityValue by remember { mutableFloatStateOf(0f) }
    var dataResponse by remember { mutableStateOf<DataResponse?>(null) }


    fun fetchData() = flow {
        while (true) {
            try {
                val response = RetrofitInstance.api.getData()
                Log.d("Data response: {}", response.toString())
                emit(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            delay(1000)
        }
    }
    val coroutineScope = rememberCoroutineScope()

    suspend fun toggleLed(ledId: Int) {
        try{
            RetrofitInstance.api.toggleLed(ledId)
        }catch (e: Exception){
            e.printStackTrace()

        }
    }

    // Fetch data when page is first loaded
    LaunchedEffect(Unit) {
        fetchData().collect { data ->
            dataResponse = data
            led1Value = data.button1
            led2Value = data.button2
            temperatureValue = data.temperature
            humidityValue = data.humidity
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Text views to display fetched values
            Text(
                text = "Temperature: $temperatureValue *C",
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "Humidity: $humidityValue%",
                modifier = Modifier.padding(8.dp)
            )

            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Led 1"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = led1Value,
                    onCheckedChange = {

                        coroutineScope.launch {
                            toggleLed(1)
                        }
                        led1Value = it
                    }
                )
            }
            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Led 2"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = led2Value,
                    onCheckedChange = {

                        coroutineScope.launch {
                            toggleLed(2)
                        }
                        led2Value = it
                    }
                )
            }
        }
    }
}