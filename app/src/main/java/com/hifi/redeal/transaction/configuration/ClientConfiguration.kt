package com.hifi.redeal.transaction.configuration

import android.view.View
import com.hifi.redeal.R

enum class ClientConfiguration(private val state: Long) {
    STATE_TRADING(1L),
    STATE_TRY(2L),
    STATE_STOP(3L),
    ;

    companion object {
        fun setClientStateResource(value: Long, view: View) {
            view.visibility = View.VISIBLE
            when (value) {
                STATE_TRADING.state -> view.setBackgroundResource(R.drawable.client_state_circle_trading)
                STATE_TRY.state -> view.setBackgroundResource(R.drawable.client_state_circle_trade_try)
                STATE_STOP.state -> view.setBackgroundResource(R.drawable.client_state_circle_trade_stop)
                else -> view.visibility = View.GONE
            }
        }

        fun setClientBookmarkResource(value: Boolean, view: View) {
            if (value) {
                view.visibility = View.VISIBLE
                view.setBackgroundResource(R.drawable.star_fill_24px)
                return
            }
            view.visibility = View.GONE
        }

        fun isClientStateNotStop(value: Long): Boolean {
            return value != STATE_STOP.state
        }
    }
}
