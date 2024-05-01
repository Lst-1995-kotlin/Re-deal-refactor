package com.hifi.redeal.trade.ui.dialog

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.hifi.redeal.databinding.DialogSelectTradeClientBinding
import com.hifi.redeal.trade.configuration.DialogConfiguration.Companion.dialogResize
import com.hifi.redeal.trade.ui.viewmodel.ClientTradeViewModel
import com.hifi.redeal.trade.ui.adapter.ClientAdapter
import com.hifi.redeal.trade.ui.adapter.ClientAdapterDiffCallback
import com.hifi.redeal.trade.ui.adapter.viewHolder.ViewHolderFactory
import com.hifi.redeal.trade.ui.adapter.viewHolder.client.TradeClientHolderFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SelectTradeClientDialog @Inject constructor(
    private val clientAdapterDiffCallback: ClientAdapterDiffCallback,
    private val tradeClientHolderFactory: TradeClientHolderFactory
) : DialogFragment() {
    private lateinit var dialogSelectTransactionClientBinding: DialogSelectTradeClientBinding
    private lateinit var clientAdapter: ClientAdapter

    private val clientTradeViewModel: ClientTradeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        dialogSelectTransactionClientBinding = DialogSelectTradeClientBinding.inflate(inflater)
        setAdapter()
        setBind()
        setViewModel()

        return dialogSelectTransactionClientBinding.root
    }

    override fun onResume() {
        super.onResume()
        context?.dialogResize(this)
    }

    override fun onPause() {
        super.onPause()
        dialogSelectTransactionClientBinding.searchTransactionClientEditText.text.clear()
    }

    private fun setAdapter() {
        val viewHolderFactories = HashMap<String, ViewHolderFactory>()

        viewHolderFactories["client"] = tradeClientHolderFactory
        clientAdapter = ClientAdapter(viewHolderFactories, clientAdapterDiffCallback)
    }

    private fun setBind() {
        dialogSelectTransactionClientBinding.run {
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
                        clientAdapter.submitList(
                            clientTradeViewModel.clients.value?.filter {
                                it.name.contains("$p0") || it.managerName.contains("$p0")
                            }
                        )
                    }

                    override fun afterTextChanged(p0: Editable?) {
                    }
                })
            }
        }
    }

    private fun setViewModel() {
        clientTradeViewModel.clients.observe(viewLifecycleOwner) {
            clientAdapter.submitList(it)
        }
    }

}
