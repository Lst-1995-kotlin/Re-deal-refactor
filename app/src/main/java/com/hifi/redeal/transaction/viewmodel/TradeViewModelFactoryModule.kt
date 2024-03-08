package com.hifi.redeal.transaction.viewmodel

import com.hifi.redeal.transaction.repository.TradeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object TradeViewModelFactoryModule {

    @Singleton
    @Provides
    fun providesTradeViewModelFactory(tradeRepository: TradeRepository): TradeViewModelFactory {
        return TradeViewModelFactory(tradeRepository)
    }
}
