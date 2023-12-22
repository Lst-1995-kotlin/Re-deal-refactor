package com.hifi.redeal.transaction.view

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hifi.redeal.databinding.DialogSelectTransactionClientBinding
import com.hifi.redeal.transaction.adapter.ClientAdapter
import com.hifi.redeal.transaction.util.DialogConfiguration
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SelectTransactionClientDialog @Inject constructor(
    private val clientAdapter: ClientAdapter,
) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val dialogSelectTransactionClientDialog =
            DialogSelectTransactionClientBinding.inflate(inflater)
        clientAdapter.getClient()
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

    private fun Context.dialogResize(dialogFragment: DialogFragment) {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val rect = windowManager.currentWindowMetrics.bounds
        val window = dialogFragment.dialog?.window
        val x = (rect.width() * DialogConfiguration.DIALOG_WIDTH.size).toInt()
        val y = (rect.height() * DialogConfiguration.DIALOG_HEIGHT.size).toInt()
        window?.setLayout(x, y)
    }
}
