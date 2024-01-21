package com.dobrihlopez.simbir_soft_test_task.ioc

import com.dobrihlopez.simbir_soft_test_task.data.DiaryRepositoryImpl
import com.dobrihlopez.simbir_soft_test_task.domain.DiaryRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class DomainModule {
    @Singleton
    @Binds
    abstract fun bindRepository(impl: DiaryRepositoryImpl): DiaryRepository
}