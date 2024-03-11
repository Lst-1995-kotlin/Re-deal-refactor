package com.hifi.redeal.transaction.repository

import androidx.lifecycle.LiveData
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
    fun getAllTrades(): Flow<List<TradeData>> = tradeDao.getAllTrade()

    fun getAllTradeByClient(clientId: Int): Flow<List<TradeData>> =
        tradeDao.getClientTrade(clientId)

    suspend fun deleteTrade(tradeData: TradeData) {
        tradeDao.deleteTrade(tradeData.toTradeEntry())
    }

}
