package com.example.asser.di

import android.content.Context
import com.example.asser.data.AppDatabase
import com.example.asser.data.HistoryRepository
import com.example.asser.data.HistoryRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideHistoryRepository(appDatabase: AppDatabase): HistoryRepository {
        return HistoryRepositoryImpl(appDatabase.ageCalculationDao())
    }
}
