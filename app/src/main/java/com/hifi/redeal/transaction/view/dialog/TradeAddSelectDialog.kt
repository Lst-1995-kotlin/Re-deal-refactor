package com.hifi.redeal.transaction.view.dialog

import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import com.hifi.redeal.R
import com.hifi.redeal.databinding.DialogTransactionAddSelectBinding

class TradeAddSelectDialog(
    private val naviController: NavController,
    private val layoutInflater: LayoutInflater
) {
    fun dialogShow() {
        val context = layoutInflater.context
        val builder = AlertDialog.Builder(context, R.style.RoundedAlertDialog)
        val view = DialogTransactionAddSelectBinding.inflate(layoutInflater)
        builder.setView(view.root)
        val dialog = builder.show()
        view.run {
            ImgBtnAddDeposit.setOnClickListener {
                dialog.dismiss()
                naviController.navigate(R.id.action_tradeFragment_to_transactionDepositFragment)
            }
            ImgBtnAddTransaction.setOnClickListener {
                dialog.dismiss()
                naviController.navigate(R.id.action_tradeFragment_to_transactionSalesFragment)
            }
        }

    }
}