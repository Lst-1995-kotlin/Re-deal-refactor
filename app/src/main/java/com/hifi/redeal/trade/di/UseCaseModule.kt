package com.hifi.redeal.trade.di

import com.hifi.redeal.trade.data.repository.TradeClientRepository
import com.hifi.redeal.trade.data.repository.TradeRepository
import com.hifi.redeal.trade.domain.usecase.TradeClientUseCase
import com.hifi.redeal.trade.domain.usecase.TradeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Singleton
    @Provides
    fun provideTradeUseCase(tradeRepository: TradeRepository): TradeUseCase {
        return TradeUseCase(tradeRepository)
    }

    @Singleton
    @Provides
    fun provideTradeClientUseCase(tradeClientRepository: TradeClientRepository): TradeClientUseCase {
        return TradeClientUseCase(tradeClientRepository)
    }
}