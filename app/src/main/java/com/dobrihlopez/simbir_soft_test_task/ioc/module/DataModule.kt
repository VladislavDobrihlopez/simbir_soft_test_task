package com.dobrihlopez.simbir_soft_test_task.ioc.module

import android.content.Context
import androidx.room.Room
import com.dobrihlopez.simbir_soft_test_task.data.database.AppDatabase
import com.dobrihlopez.simbir_soft_test_task.data.database.EventConverterHolder
import com.dobrihlopez.simbir_soft_test_task.data.database.EventsDao
import com.dobrihlopez.simbir_soft_test_task.ioc.scope.ApplicationScope
import dagger.Module
import dagger.Provides
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import javax.inject.Singleton

@Module
interface DataModule {
    companion object {
        @Provides
        fun provideZoneId(): ZoneId {
            return ZoneId.systemDefault()
        }
        @ApplicationScope
        @Provides
        fun provideDatabase(context: Context, zoneId: ZoneId): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DB_NAME)
                .addTypeConverter(EventConverterHolder(OffsetDateTime.now(zoneId).offset))
                .build()
        }

        @ApplicationScope
        @Provides
        fun provideEventsDao(db: AppDatabase): EventsDao {
            return db.eventsDao()
        }
    }
}