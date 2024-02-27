package com.hifi.redeal.transaction.view.dialog

import android.content.Context
import android.view.LayoutInflater
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hifi.redeal.MainActivity
import com.hifi.redeal.databinding.DialogTransactionAddSelectBinding

class TransactionAddSelectDialog(
    private val mainActivity: MainActivity,
    private val context: Context,
    private val layoutInflater: LayoutInflater
) {
    fun dialogShow() {
        val builder = MaterialAlertDialogBuilder(context)
        val view = DialogTransactionAddSelectBinding.inflate(layoutInflater)
        builder.setView(view.root)
        val dialog = builder.create()
        view.run {
            ImgBtnAddDeposit.setOnClickListener {
                dialog.dismiss()
                mainActivity.replaceFragment(MainActivity.TRANSACTION_DEPOSIT_FRAGMENT, true, null)
            }
            ImgBtnAddTransaction.setOnClickListener {
                dialog.dismiss()
                mainActivity.replaceFragment(MainActivity.TRANSACTION_SALES_FRAGMENT, true, null)
            }
        }
        dialog.show()
    }
}