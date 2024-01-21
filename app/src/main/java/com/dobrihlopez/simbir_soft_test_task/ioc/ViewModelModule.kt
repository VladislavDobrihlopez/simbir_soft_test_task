package com.dobrihlopez.simbir_soft_test_task.ioc

import androidx.lifecycle.ViewModel
import com.dobrihlopez.simbir_soft_test_task.ioc.annotation.ViewModelKey
import com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen.CalendarViewModel
import com.dobrihlopez.simbir_soft_test_task.presentation.editorScreen.EditorScreenViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(EditorScreenViewModel::class)
    fun bindEditorScreenViewModel(impl: EditorScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CalendarViewModel::class)
    fun bindCalendarScreenViewModel(impl: CalendarViewModel): ViewModel
}