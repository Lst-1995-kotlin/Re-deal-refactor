package com.hifi.redeal.transaction.configuration

import com.hifi.redeal.R

enum class ClientConfiguration(private val state: Long) {
    STATE_TRADING(1L),
    STATE_TRY(2L),
    STATE_STOP(3L),
    ;

    companion object {
        fun getClientStateResource(value: Long): Int? {
            return when (value) {
                STATE_TRADING.state -> R.drawable.client_state_circle_trading
                STATE_TRY.state -> R.drawable.client_state_circle_trade_try
                STATE_STOP.state -> R.drawable.client_state_circle_trade_stop
                else -> null
            }
        }

        fun getClientBookmarkResource(value: Boolean): Int? {
            if (value) return R.drawable.star_fill_24px
            return null
        }

        fun isClientStateNotStop(value: Long): Boolean {
            return value != STATE_STOP.state
        }
    }
}
