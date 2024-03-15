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

    private lateinit var onAddDepositClickListener:  () -> Unit
    private lateinit var onAddSalesClickListener: () -> Unit

    fun setOnAddDepositClickListener(listener: () -> Unit) {
        onAddDepositClickListener = listener
    }

    fun setOnAddSalesClickListener(listener: () -> Unit) {
        onAddSalesClickListener = listener
    }
    fun dialogShow() {
        val context = layoutInflater.context
        val builder = AlertDialog.Builder(context, R.style.RoundedAlertDialog)
        val view = DialogTransactionAddSelectBinding.inflate(layoutInflater)
        builder.setView(view.root)
        val dialog = builder.show()
        view.run {
            ImgBtnAddDeposit.setOnClickListener {
                onAddDepositClickListener()
                dialog.dismiss()
            }
            ImgBtnAddTransaction.setOnClickListener {
                onAddSalesClickListener()
                dialog.dismiss()
            }
        }

    }
}