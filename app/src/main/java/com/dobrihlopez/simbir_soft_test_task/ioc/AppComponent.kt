package com.dobrihlopez.simbir_soft_test_task.ioc

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.dobrihlopez.simbir_soft_test_task.presentation.ViewModelFactory
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class, DomainModule::class, ViewModelModule::class])
interface AppComponent {
    fun getViewModelFactory(): ViewModelFactory
    fun editorComponentBuilder(): EditorScreenSubcomponent.Builder

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}