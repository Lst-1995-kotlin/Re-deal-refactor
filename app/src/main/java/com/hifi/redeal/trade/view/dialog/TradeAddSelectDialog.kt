package com.hifi.redeal.trade.view.dialog

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import com.hifi.redeal.R
import com.hifi.redeal.databinding.DialogTransactionAddSelectBinding
import com.hifi.redeal.trade.data.model.TradeData

class TradeAddSelectDialog(
    private val layoutInflater: LayoutInflater
) {

    private lateinit var addDepositClickEvent:  () -> Unit
    private lateinit var addSalesClickEvent: () -> Unit

    fun setOnAddDepositClickEvent(event: () -> Unit) {
        addDepositClickEvent = event
    }

    fun setOnAddSalesClickEvent(event: () -> Unit) {
        addSalesClickEvent = event
    }
    fun dialogShow() {
        val context = layoutInflater.context
        val builder = AlertDialog.Builder(context, R.style.RoundedAlertDialog)
        val view = DialogTransactionAddSelectBinding.inflate(layoutInflater)
        builder.setView(view.root)
        val dialog = builder.show()
        view.run {
            ImgBtnAddDeposit.setOnClickListener {
                addDepositClickEvent()
                dialog.dismiss()
            }
            ImgBtnAddTransaction.setOnClickListener {
                addSalesClickEvent()
                dialog.dismiss()
            }
        }

    }
}