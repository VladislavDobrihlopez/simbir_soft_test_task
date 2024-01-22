package com.dobrihlopez.simbir_soft_test_task.ioc.module

import androidx.lifecycle.ViewModel
import com.dobrihlopez.simbir_soft_test_task.ioc.annotation.ViewModelKey
import com.dobrihlopez.simbir_soft_test_task.presentation.ViewModelFactory
import com.dobrihlopez.simbir_soft_test_task.presentation.editorScreen.EditorScreenViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Provider

@Module(includes = [ViewModelModule::class])
interface EditorModule {
    @IntoMap
    @ViewModelKey(EditorScreenViewModel::class)
    @Binds
    fun bindEditorScreenViewModel(impl: EditorScreenViewModel): ViewModel
    companion object {
        @Provides
        fun provideViewModelFactory(creators: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>): ViewModelFactory {
            return ViewModelFactory(creators)
        }
    }
}