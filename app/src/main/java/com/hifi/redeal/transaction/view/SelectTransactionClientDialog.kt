package com.hifi.redeal.transaction.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.hifi.redeal.databinding.DialogSelectTransactionClientBinding
import com.hifi.redeal.transaction.adapter.ClientAdapter
import com.hifi.redeal.transaction.configuration.DialogConfiguration.Companion.dialogResize
import com.hifi.redeal.transaction.viewmodel.ClientViewModel

class SelectTransactionClientDialog(
    private val clientViewModel: ClientViewModel,
) : DialogFragment() {

    lateinit var dialogSelectTransactionClientDialog: DialogSelectTransactionClientBinding
    private lateinit var clientAdapter: ClientAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialogSelectTransactionClientDialog = DialogSelectTransactionClientBinding.inflate(inflater)
        clientAdapter = ClientAdapter(clientViewModel)
        setDialog()
        return dialogSelectTransactionClientDialog.root
    }

    override fun onResume() {
        super.onResume()
        context?.dialogResize(this)
    }

    private fun setDialog() {
        dialogSelectTransactionClientDialog.run {
            searchTransactionClientRecyclerView.run {
                adapter = clientAdapter
                layoutManager = LinearLayoutManager(context)
                addItemDecoration(
                    MaterialDividerItemDecoration(
                        context,
                        MaterialDividerItemDecoration.VERTICAL,
                    ),
                )
            }

            searchTransactionClientEditText.run {
                addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        clientAdapter.clientFind("$p0")
                    }

                    override fun afterTextChanged(p0: Editable?) {
                    }
                })
            }
        }
    }
}
