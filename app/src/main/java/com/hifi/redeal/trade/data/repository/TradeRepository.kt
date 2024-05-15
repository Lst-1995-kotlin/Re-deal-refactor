package com.hifi.redeal.trade.data.repository

import com.hifi.redeal.data.dao.TradeDao
import com.hifi.redeal.data.entrie.TradeEntity
import com.hifi.redeal.trade.data.model.TradeData
import com.hifi.redeal.trade.data.model.TradeSelectData
import com.hifi.redeal.trade.data.model.toTradeEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TradeRepository @Inject constructor(
    private val tradeDao: TradeDao
) {
    fun getTrades(): Flow<List<TradeData>> = tradeDao.getAllTrade()

    fun getAllSelectTrade(): Flow<List<TradeSelectData>> = tradeDao.getAllSelectTrade()

    fun getTradeById(id: Int): Flow<TradeData> {
        return tradeDao.getTradeById(id)
    }


    fun getTradeByClient(clientId: Int): Flow<List<TradeData>> =
        tradeDao.getTradeByClient(clientId)

    fun getSelectTradeByClient(clientId: Int): Flow<List<TradeSelectData>> =
        tradeDao.getSelectTradeByClient(clientId)

    suspend fun selectHistoryClear() {
        tradeDao.selectHistoryClear()
    }
    suspend fun deleteTrade(tradeData: TradeData) {
        tradeDao.deleteTrade(tradeData.toTradeEntry())
    }

    suspend fun insertTrade(tradeEntity: TradeEntity) {
        tradeDao.insertTrade(tradeEntity)
    }

    suspend fun updateTrade(tradeEntity: TradeEntity) {
        tradeDao.updateTrade(tradeEntity)
    }
}
