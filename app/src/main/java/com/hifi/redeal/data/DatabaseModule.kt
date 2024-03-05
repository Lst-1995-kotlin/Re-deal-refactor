package com.hifi.redeal.data

import android.content.Context
import androidx.room.Room
import com.hifi.redeal.data.dao.ClientDao
import com.hifi.redeal.data.dao.TestDao
import com.hifi.redeal.data.dao.TestDao2
import com.hifi.redeal.data.dao.TradeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideLocalDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): TestDao = appDatabase.testDao()

    @Provides
    @Singleton
    fun provideUserDao2(appDatabase: AppDatabase): TestDao2 = appDatabase.testDao2()

    @Provides
    @Singleton
    fun provideClientDao(appDatabase: AppDatabase): ClientDao = appDatabase.clientDao()

    @Provides
    @Singleton
    fun provideTransactionsDao(appDatabase: AppDatabase): TradeDao =
        appDatabase.tradeDao()
}