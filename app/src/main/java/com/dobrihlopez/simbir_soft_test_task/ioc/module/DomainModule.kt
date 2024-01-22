package com.dobrihlopez.simbir_soft_test_task.ioc.module

import com.dobrihlopez.simbir_soft_test_task.data.DiaryRepositoryImpl
import com.dobrihlopez.simbir_soft_test_task.domain.DiaryRepository
import com.dobrihlopez.simbir_soft_test_task.ioc.scope.ApplicationScope
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface DomainModule {
    @ApplicationScope
    @Binds
    fun bindRepository(impl: DiaryRepositoryImpl): DiaryRepository
}