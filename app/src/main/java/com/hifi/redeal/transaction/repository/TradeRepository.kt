package com.hifi.redeal.transaction.repository

import com.hifi.redeal.data.dao.TradeDao
import com.hifi.redeal.transaction.model.TradeData
import com.hifi.redeal.transaction.model.toTradeEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TradeRepository @Inject constructor(
    private val tradeDao: TradeDao
) {
    val trades: Flow<List<TradeData>> = tradeDao.getAllTrade()

    suspend fun deleteTrade(tradeData: TradeData) {
        tradeDao.deleteTrade(tradeData.toTradeEntry())
    }

}
