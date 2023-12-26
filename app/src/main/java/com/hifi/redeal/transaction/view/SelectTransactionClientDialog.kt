package com.hifi.redeal.transaction.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.hifi.redeal.databinding.DialogSelectTransactionClientBinding
import com.hifi.redeal.transaction.adapter.ClientAdapter
import com.hifi.redeal.transaction.repository.ClientRepository
import com.hifi.redeal.transaction.repository.TransactionRepository
import com.hifi.redeal.transaction.util.DialogConfiguration.Companion.dialogResize
import com.hifi.redeal.transaction.viewmodel.ClientViewModel

class SelectTransactionClientDialog(
    private val clientViewModel: ClientViewModel,
    private val clientRepository: ClientRepository
) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val dialogSelectTransactionClientDialog =
            DialogSelectTransactionClientBinding.inflate(inflater)

        val clientAdapter = ClientAdapter(clientViewModel)
        clientAdapter.getClient(clientRepository)

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

        return dialogSelectTransactionClientDialog.root
    }

    override fun onResume() {
        super.onResume()
        context?.dialogResize(this)
    }
}
