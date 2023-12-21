package com.hifi.redeal.transaction.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.hifi.redeal.databinding.DialogSelectTransactionClientBinding
import com.hifi.redeal.transaction.adapter.ClientAdapter
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class SelectTransactionClientDialog @Inject constructor(
    @ActivityContext private val context: Context,
    private val clientAdapter: ClientAdapter,
) {
    private val inflater = LayoutInflater.from(context)
    private val dialogSelectTransactionClientDialog =
        DialogSelectTransactionClientBinding.inflate(inflater)
    private var builder = AlertDialog.Builder(context)
    private var dialog = builder.create()

    init {
        bind()
        clientAdapter.getClient()
        dialog.setView(dialogSelectTransactionClientDialog.root)
    }

    fun show() {
        dialog.show()
    }

    private fun bind() {
        dialogSelectTransactionClientDialog.run {
            searchTransactionClientRecyclerView.adapter = clientAdapter
            searchTransactionClientRecyclerView.layoutManager = LinearLayoutManager(context)
            searchTransactionClientEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    clientAdapter.clientFilterResult("$p0")
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
        }
    }
}
