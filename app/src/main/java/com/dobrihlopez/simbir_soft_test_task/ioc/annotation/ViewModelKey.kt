package com.dobrihlopez.simbir_soft_test_task.ioc.annotation

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewModelKey(val name: KClass<out ViewModel>)
