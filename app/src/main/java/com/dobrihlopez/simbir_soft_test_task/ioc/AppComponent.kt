package com.dobrihlopez.simbir_soft_test_task.ioc

import android.content.Context
import com.dobrihlopez.simbir_soft_test_task.ioc.module.CommonModule
import com.dobrihlopez.simbir_soft_test_task.ioc.module.DataModule
import com.dobrihlopez.simbir_soft_test_task.ioc.module.DomainModule
import com.dobrihlopez.simbir_soft_test_task.ioc.module.ViewModelModule
import com.dobrihlopez.simbir_soft_test_task.ioc.scope.ApplicationScope
import com.dobrihlopez.simbir_soft_test_task.presentation.ViewModelFactory
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        ViewModelModule::class,
        DataModule::class,
        DomainModule::class,
        CommonModule::class
    ]
)
interface AppComponent {
    fun viewModelsFactory(): ViewModelFactory
    fun editorComponentBuilder(): EditorScreenSubcomponent.Factory

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
        ): AppComponent
    }
}