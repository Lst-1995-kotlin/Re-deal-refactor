package com.hifi.redeal.transaction.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hifi.redeal.CurrentUserClass
import com.hifi.redeal.data.dao.TradeDao
import com.hifi.redeal.transaction.model.TradeData
import com.hifi.redeal.transaction.model.TransactionData
import com.hifi.redeal.transaction.model.toTradeEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
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
