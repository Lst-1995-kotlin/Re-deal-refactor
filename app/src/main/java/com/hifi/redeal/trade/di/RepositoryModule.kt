package com.hifi.redeal.trade.di

import com.hifi.redeal.data.dao.ClientDao
import com.hifi.redeal.data.dao.TradeDao
import com.hifi.redeal.trade.data.repository.TradeClientRepository
import com.hifi.redeal.trade.data.repository.TradeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideTradeRepository(tradeDao: TradeDao): TradeRepository {
        return TradeRepository(tradeDao)
    }

    @Singleton
    @Provides
    fun provideTradeClientRepository(clientDao: ClientDao): TradeClientRepository {
        return TradeClientRepository(clientDao)
    }
}