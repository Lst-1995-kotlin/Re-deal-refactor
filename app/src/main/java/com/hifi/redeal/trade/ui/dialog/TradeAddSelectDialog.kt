package com.hifi.redeal.trade.ui.dialog

import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.hifi.redeal.R
import com.hifi.redeal.databinding.DialogTransactionAddSelectBinding

class TradeAddSelectDialog(
    private val layoutInflater: LayoutInflater,
    private val addDepositClickEvent: () -> Unit,
    private val addSalesClickEvent: () -> Unit
) {

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