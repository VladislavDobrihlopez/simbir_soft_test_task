package com.dobrihlopez.simbir_soft_test_task.ioc

import android.content.Context
import androidx.room.Room
import com.dobrihlopez.simbir_soft_test_task.data.database.AppDatabase
import com.dobrihlopez.simbir_soft_test_task.data.database.EventsDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface DataModule {
    companion object {
        @Singleton
        @Provides
        fun provideDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DB_NAME)
                .build()
        }

        @Provides
        fun provideEventsDao(db: AppDatabase): EventsDao {
            return db.eventsDao()
        }
    }
}