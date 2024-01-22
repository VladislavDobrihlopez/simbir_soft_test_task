package com.dobrihlopez.simbir_soft_test_task.app

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext
import com.dobrihlopez.simbir_soft_test_task.ioc.AppComponent
import com.dobrihlopez.simbir_soft_test_task.ioc.DaggerAppComponent

class CalendarApp : Application() {
    val component: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }
}

@Composable
@ReadOnlyComposable
fun getComponent(): AppComponent {
    return (LocalContext.current.applicationContext as CalendarApp).component
}