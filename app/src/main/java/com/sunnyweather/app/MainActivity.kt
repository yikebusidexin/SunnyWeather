package com.sunnyweather.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.sunnyweather.app.ui.SunnyWeatherHomeScreen
import com.sunnyweather.app.ui.theme.SunnyWeatherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SunnyWeatherTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SunnyWeatherHomeScreen()
                }
            }
        }
    }
}
