package com.dobrihlopez.simbir_soft_test_task.ioc.module

import com.dobrihlopez.simbir_soft_test_task.ioc.DispatcherIO
import com.dobrihlopez.simbir_soft_test_task.ioc.DispatcherMain
import com.dobrihlopez.simbir_soft_test_task.ioc.scope.ApplicationScope
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
interface CommonModule {
    companion object {
        @DispatcherIO
        @Provides
        @ApplicationScope
        fun provideDispatcherIO(): CoroutineDispatcher {
            return Dispatchers.IO
        }

        @DispatcherMain
        @Provides
        @ApplicationScope
        fun provideDispatcherMain(): CoroutineDispatcher {
            return Dispatchers.Main
        }
    }
}