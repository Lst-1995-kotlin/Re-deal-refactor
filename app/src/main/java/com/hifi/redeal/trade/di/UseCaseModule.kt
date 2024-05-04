package com.hifi.redeal.trade.di

import com.hifi.redeal.data.dao.TradeDao
import com.hifi.redeal.trade.data.repository.TradeClientRepository
import com.hifi.redeal.trade.data.repository.TradeRepository
import com.hifi.redeal.trade.domain.usecase.TradeClientUseCase
import com.hifi.redeal.trade.domain.usecase.TradeUseCase
import dagger.Provides
import javax.inject.Singleton

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