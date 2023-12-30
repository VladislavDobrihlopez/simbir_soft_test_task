package com.dobrihlopez.simbir_soft_test_task.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.dobrihlopez.simbir_soft_test_task.core_ui.theme.Simbir_soft_test_taskTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Simbir_soft_test_taskTheme {
                AppScreenManager()
            }
        }
    }
}
