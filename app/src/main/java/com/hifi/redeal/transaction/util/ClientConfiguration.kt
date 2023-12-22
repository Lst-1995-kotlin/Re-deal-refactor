package com.hifi.redeal.transaction.util

import com.hifi.redeal.R

object ClientConfiguration {
    fun getClientStateResource(value: Long): Int? {
        return when (value) {
            1L -> R.drawable.client_state_circle_trading
            2L -> R.drawable.client_state_circle_trade_try
            3L -> R.drawable.client_state_circle_trade_stop
            else -> null
        }
    }

    fun getClientBookmarkResource(value: Boolean): Int? {
        if (value) return R.drawable.star_fill_24px
        return null
    }
}
