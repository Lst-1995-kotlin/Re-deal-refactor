package com.hifi.redeal.trade.domain.usecase

import com.hifi.redeal.trade.data.model.TradeClientData
import com.hifi.redeal.trade.data.repository.TradeClientRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TradeClientUseCase @Inject constructor(
    private val tradeClientRepository: TradeClientRepository
) {

    fun getClients(): Flow<List<TradeClientData>> = tradeClientRepository.getClientTradeData()

}