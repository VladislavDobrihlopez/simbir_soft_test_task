package com.dobrihlopez.simbir_soft_test_task.ioc

import com.dobrihlopez.simbir_soft_test_task.ioc.module.EditorModule
import com.dobrihlopez.simbir_soft_test_task.presentation.ViewModelFactory
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [EditorModule::class])
interface EditorScreenSubcomponent {
//    fun viewModelsFactory(): ViewModelFactory

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance id: Long? = null,
            @BindsInstance cardColor: String? = null,
        ): EditorScreenSubcomponent
    }
}