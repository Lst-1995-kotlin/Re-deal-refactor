package com.hifi.redeal.trade.data.repository

import com.hifi.redeal.data.dao.ClientDao
import com.hifi.redeal.trade.data.model.TradeClientData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TradeClientRepository @Inject constructor(
    private val clientDao: ClientDao
) {
    fun getClientTradeData(): Flow<List<TradeClientData>> = clientDao.getClientTradeData()


}