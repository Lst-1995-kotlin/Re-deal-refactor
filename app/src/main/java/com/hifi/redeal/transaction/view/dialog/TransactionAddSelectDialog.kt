package com.hifi.redeal.transaction.view.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hifi.redeal.MainActivity
import com.hifi.redeal.R
import com.hifi.redeal.databinding.DialogTransactionAddSelectBinding

class TransactionAddSelectDialog(
    private val mainActivity: MainActivity,
    private val context: Context,
    private val layoutInflater: LayoutInflater
) {
    fun dialogShow() {
        val builder = AlertDialog.Builder(context, R.style.RoundedAlertDialog)
        val view = DialogTransactionAddSelectBinding.inflate(layoutInflater)
        builder.setView(view.root)
        val dialog = builder.show()
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

    }
}