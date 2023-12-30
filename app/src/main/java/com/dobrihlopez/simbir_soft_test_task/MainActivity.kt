package com.dobrihlopez.simbir_soft_test_task

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.dobrihlopez.simbir_soft_test_task.app.theme.Simbir_soft_test_taskTheme
import com.dobrihlopez.simbir_soft_test_task.presentation.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Simbir_soft_test_taskTheme {
                MainScreen()
            }
        }
    }
}
