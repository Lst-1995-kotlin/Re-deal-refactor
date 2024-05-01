package com.hifi.redeal.trade.domain.usecase

import com.hifi.redeal.data.entrie.TradeEntity
import com.hifi.redeal.trade.data.model.TradeData
import com.hifi.redeal.trade.data.repository.TradeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TradeUseCase @Inject constructor(
    private val tradeRepository: TradeRepository
) {
    fun getTrades(): Flow<List<TradeData>> {
        return tradeRepository.getTrades()
    }

    fun getTradeByClient(clientId: Int): Flow<List<TradeData>> {
        return tradeRepository.getTradeByClient(clientId)
    }

    suspend fun deleteTrade(tradeData: TradeData) {
        tradeRepository.deleteTrade(tradeData)
    }

    suspend fun insertTrade(tradeEntity: TradeEntity) {
        tradeRepository.insertTrade(tradeEntity)
    }
}