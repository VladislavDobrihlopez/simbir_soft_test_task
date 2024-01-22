package com.dobrihlopez.simbir_soft_test_task.ioc.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dobrihlopez.simbir_soft_test_task.ioc.annotation.ViewModelKey
import com.dobrihlopez.simbir_soft_test_task.presentation.ViewModelFactory
import com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen.CalendarViewModel
import com.dobrihlopez.simbir_soft_test_task.presentation.editorScreen.EditorScreenViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import javax.inject.Provider
import javax.inject.Singleton

@Module
interface ViewModelModule {
    @IntoMap
    @ViewModelKey(CalendarViewModel::class)
    @Binds
    fun bindCalendarScreenViewModel(impl: CalendarViewModel): ViewModel
//    companion object {
//        @Provides
//        fun provideViewModelFactory(creators: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>): ViewModelFactory {
//            return ViewModelFactory(creators)
//        }
//    }
}