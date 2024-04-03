package com.hifi.redeal.trade.data.repository

import com.hifi.redeal.data.dao.TradeDao
import com.hifi.redeal.data.entrie.TradeEntry
import com.hifi.redeal.trade.data.model.TradeData
import com.hifi.redeal.trade.data.model.toTradeEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

class TradeRepository @Inject constructor(
    private val tradeDao: TradeDao
) {
    fun getAllTrades(): Flow<List<TradeData>> = tradeDao.getAllTrade()

    fun getAllTradeByClient(clientId: Int): Flow<List<TradeData>> =
        tradeDao.getClientTrade(clientId)

    suspend fun deleteTrade(tradeData: TradeData) {
        tradeDao.deleteTrade(tradeData.toTradeEntry())
    }

    suspend fun insertTrade(tradeEntry: TradeEntry) {
        tradeDao.insertTrade(tradeEntry)
    }
}
