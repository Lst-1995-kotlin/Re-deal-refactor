package com.hifi.redeal.transaction.view.dialog

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.hifi.redeal.databinding.DialogSelectTransactionClientBinding
import com.hifi.redeal.transaction.adapter.ClientAdapter
import com.hifi.redeal.transaction.adapter.ClientAdapterDiffCallback
import com.hifi.redeal.transaction.configuration.DialogConfiguration.Companion.dialogResize
import com.hifi.redeal.transaction.viewHolder.ViewHolderFactory
import com.hifi.redeal.transaction.viewHolder.client.TransactionClientHolderFactory
import com.hifi.redeal.transaction.viewmodel.TransactionClientViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SelectTransactionClientDialog(
    private val transactionClientViewModel: TransactionClientViewModel,
) : DialogFragment() {
    private lateinit var dialogSelectTransactionClientDialog: DialogSelectTransactionClientBinding
    private lateinit var clientAdapter: ClientAdapter

    @Inject
    lateinit var clientAdapterDiffCallback: ClientAdapterDiffCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        dialogSelectTransactionClientDialog = DialogSelectTransactionClientBinding.inflate(inflater)
        setAdapter()
        setBind()
        setViewModel()

        return dialogSelectTransactionClientDialog.root
    }

    override fun onResume() {
        super.onResume()
        context?.dialogResize(this)
    }

    override fun onPause() {
        super.onPause()
        dialogSelectTransactionClientDialog.searchTransactionClientEditText.text.clear()
    }

    private fun setAdapter() {
        val viewHolderFactories = HashMap<String, ViewHolderFactory>()
        viewHolderFactories["client"] = TransactionClientHolderFactory(transactionClientViewModel, this)
        clientAdapter = ClientAdapter(viewHolderFactories, clientAdapterDiffCallback)
    }

    private fun setBind() {
        dialogSelectTransactionClientDialog.run {
            searchTransactionClientRecyclerView.run {
                adapter = clientAdapter
                layoutManager = LinearLayoutManager(context)
                addItemDecoration(
                    MaterialDividerItemDecoration(
                        context,
                        MaterialDividerItemDecoration.VERTICAL,
                    )
                )
            }

            searchTransactionClientEditText.run {
                addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        transactionClientViewModel.clients.value?.let { clients ->
                            clientAdapter.setClients(clients.filter { it.filter("$p0") })
                        }
                    }

                    override fun afterTextChanged(p0: Editable?) {
                    }
                })
            }
        }
    }

    private fun setViewModel() {
        transactionClientViewModel.clients.observe(viewLifecycleOwner) {
            clientAdapter.setClients(it)
        }
    }
}
