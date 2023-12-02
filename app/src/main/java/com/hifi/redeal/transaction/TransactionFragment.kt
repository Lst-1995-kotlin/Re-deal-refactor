package com.hifi.redeal.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hifi.redeal.MainActivity
import com.hifi.redeal.R
import com.hifi.redeal.databinding.DialogAddDepositBinding
import com.hifi.redeal.databinding.DialogAddTransactionBinding
import com.hifi.redeal.databinding.FragmentTransactionBinding
import com.hifi.redeal.databinding.TransactionSelectClientBinding
import com.hifi.redeal.databinding.TransactionSelectClientItemBinding
import com.hifi.redeal.transaction.adapter.TransactionAdapter
import com.hifi.redeal.transaction.model.ClientSimpleData
import com.hifi.redeal.transaction.model.Transaction
import com.hifi.redeal.transaction.model.TransactionData
import java.math.BigInteger

class TransactionFragment : Fragment() {

    lateinit var fragmentTransactionBinding: FragmentTransactionBinding
    lateinit var mainActivity: MainActivity
    lateinit var transactionVM: TransactionViewModel
    var clientSimpleDataList = mutableListOf<ClientSimpleData>()

    lateinit var selBulider: AlertDialog.Builder
    lateinit var selDialog: AlertDialog

    lateinit var dialogAddDepositBinding: DialogAddDepositBinding
    lateinit var addDepositBuilder: AlertDialog.Builder
    lateinit var addDepositDialog: AlertDialog

    lateinit var dialogAddTransactionBinding: DialogAddTransactionBinding
    lateinit var addTransactionBuilder: AlertDialog.Builder
    lateinit var addTransactionDialog: AlertDialog

    var inoutMode = true

    var clientIdx: Long? = null
    var selectClientIdx: Long? = null
    val uid = Firebase.auth.uid!!
    val transactions = mutableListOf<Transaction>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentTransactionBinding = FragmentTransactionBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        clientIdx = arguments?.getLong("clientIdx")

        setViewModel()
        setClickEvent()

        fragmentTransactionBinding.transactionRecyclerView.run {
            adapter = TransactionAdapter(transactions)
            layoutManager = LinearLayoutManager(context)
        }

        return fragmentTransactionBinding.root

    }

    private fun setViewModel() {
        transactionVM = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]

        transactionVM.run {

            clientSimpleDataListVM.observe(viewLifecycleOwner) { list ->
                clientSimpleDataList.clear()
                list.forEach {
                    clientSimpleDataList.add(it)
                }
            }

            transactionList.observe(viewLifecycleOwner) {
                transactions.clear()
                it.sortedByDescending { it.date }.forEach {
                    transactions.add(Transaction(it))
                }
                fragmentTransactionBinding.transactionRecyclerView.adapter?.notifyDataSetChanged()
            }

            transactionVM.getAllTransactionData(uid)
            transactionVM.getNextTransactionIdx(uid)
            transactionVM.getUserAllClient(uid)
        }

    }

    private fun setClickEvent() {

        fragmentTransactionBinding.run {

            ImgBtnAddDeposit.setOnClickListener {
                inoutMode = true
                showDepositDialog()
            }

            ImgBtnAddTransaction.setOnClickListener {
                inoutMode = false
                showTransactionDialog()
            }

            toolbarTransactionMain.run {
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.TRANSACTION_FRAGMENT)
                }
            }
        }
    }

    // 입금 추가 다이얼 로그 생성 함수
    private fun showDepositDialog() {

        dialogAddDepositBinding = DialogAddDepositBinding.inflate(layoutInflater)
        addDepositBuilder = AlertDialog.Builder(requireContext())
        addDepositDialog = addDepositBuilder.create()

        dialogAddDepositBinding.run {

            addDepositPriceEditTextNumber.setOnEditorActionListener { v, _, _ ->
                if (!v.editableText.isNullOrEmpty()) {
                    addDepositPriceinputLayout.error = null
                }
                true
            }

            if (clientIdx != null) { // 거래처 화면에서 왔을 경우
                addSelectClientDepositBtn.visibility = View.GONE

                addDepositBtn.setOnClickListener {

                    if (addDepositPriceEditTextNumber.editableText.isNullOrEmpty()) {
                        addDepositPriceinputLayout.error = "금액을 입력해 주세요."
                        addDepositPriceEditTextNumber.requestFocus()
                        return@setOnClickListener
                    }

                    val newTransactionData = TransactionData(
                        clientIdx!!,
                        Timestamp.now(),
                        true,
                        addDepositPriceEditTextNumber.editableText.toString(),
                        transactionVM.nextTransactionIdx,
                        0L,
                        "0",
                        ""
                    )
                    TransactionRepository.setTransactionData(uid, newTransactionData) {
                        TransactionRepository.setClientTransactionDataList(
                            uid,
                            newTransactionData
                        ) {
                            addDepositDialog.dismiss()
                            Snackbar.make(
                                fragmentTransactionBinding.root,
                                "입금 내용 저장 완료 되었습니다.",
                                Snackbar.LENGTH_SHORT
                            ).show()
                            transactionVM.getAllTransactionData(uid)
                            transactionVM.getNextTransactionIdx(uid)
                        }
                    }
                }
            } else { // 하단 네비게이션을 이용하여 왔을 경우
                addDepositBtn.setOnClickListener {
                    if (selectClientIdx == null) {
                        addSelectClientDepositBtn.requestFocus()
                        addSelectClientDepositBtn.callOnClick()
                    } else {

                        if (addDepositPriceEditTextNumber.editableText.isNullOrEmpty()) {
                            addDepositPriceinputLayout.error = "금액을 입력해 주세요."
                            addDepositPriceEditTextNumber.requestFocus()
                            return@setOnClickListener
                        }

                        val newTransactionData = TransactionData(
                            selectClientIdx!!,
                            Timestamp.now(),
                            true,
                            addDepositPriceEditTextNumber.editableText.toString(),
                            transactionVM.nextTransactionIdx,
                            0L,
                            "0",
                            ""
                        )
                        TransactionRepository.setTransactionData(uid, newTransactionData) {
                            TransactionRepository.setClientTransactionDataList(
                                uid,
                                newTransactionData
                            ) {
                                selectClientIdx = null
                                Snackbar.make(
                                    fragmentTransactionBinding.root,
                                    "입금 내용 저장 완료 되었습니다.",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                transactionVM.getAllTransactionData(uid)
                                transactionVM.getNextTransactionIdx(uid)
                                addDepositDialog.dismiss()
                            }
                        }
                    }
                }
                addSelectClientDepositBtn.setOnClickListener {

                    val transactionSelectClientBinding =
                        TransactionSelectClientBinding.inflate(layoutInflater)

                    clientSimpleDataList.clear()
                    transactionVM.clientSimpleDataListVM.value?.forEach {
                        clientSimpleDataList.add(it)
                    }

                    transactionSelectClientBinding.run {
                        searchClientResultRecyclerView.run {
                            adapter = SearchClientAdapter()
                            layoutManager = LinearLayoutManager(context)
                            addItemDecoration(
                                MaterialDividerItemDecoration(
                                    context,
                                    MaterialDividerItemDecoration.VERTICAL
                                )
                            )
                        }

                        searchClientEditText.setOnEditorActionListener { v, _, _ ->
                            clientSimpleDataList.clear()
                            transactionVM.clientSimpleDataListVM.value?.forEach {
                                if (it.clientName.contains(v.editableText) || it.clientManagerName.contains(
                                        v.editableText
                                    )
                                ) {
                                    clientSimpleDataList.add(it)
                                }
                                searchClientResultRecyclerView.adapter?.notifyDataSetChanged()
                            }
                            true
                        }
                    }
                    selBulider = AlertDialog.Builder(requireContext())
                    selDialog = selBulider.create()
                    selDialog.setView(transactionSelectClientBinding.root)
                    selDialog.show()

                }
            }
        }
        addDepositDialog.setView(dialogAddDepositBinding.root)
        addDepositDialog.show()
    }

    private fun showTransactionDialog() { // 거래 추가 다이얼로그


        dialogAddTransactionBinding = DialogAddTransactionBinding.inflate(layoutInflater)
        addTransactionBuilder = AlertDialog.Builder(requireContext())
        addTransactionDialog = addTransactionBuilder.create()

        dialogAddTransactionBinding.run {

            transactionNameEditText.setOnEditorActionListener { v, _, _ ->
                if (!v.editableText.isNullOrEmpty()) {
                    transactionNameLayout.error = null
                }
                true
            }
            transactionItemCountEditText.setOnEditorActionListener { v, _, _ ->
                if (!v.editableText.isNullOrEmpty()) {
                    transactionItemCountLayout.error = null
                }
                true
            }
            transactionItemPriceEditText.setOnEditorActionListener { v, _, _ ->
                if (!v.editableText.isNullOrEmpty()) {
                    transactionItemPriceLayout.error = null
                }
                true
            }
            transactionAmountReceivedEditText.setOnEditorActionListener { v, _, _ ->
                if (!v.editableText.isNullOrEmpty()) {
                    transactionAmountReceivedLayout.error = null
                }
                true
            }

            if (clientIdx != null) {
                selectTransactionClientBtn.visibility = View.GONE

                addTransactionBtn.setOnClickListener {

                    if (transactionNameEditText.editableText.isNullOrEmpty()) {
                        transactionNameLayout.error = "품명을 입력해 주세요."
                        mainActivity.showSoftInput(transactionNameEditText)
                        return@setOnClickListener
                    }
                    if (transactionItemCountEditText.editableText.isNullOrEmpty()) {
                        transactionItemCountLayout.error = "수량을 입력해 주세요."
                        mainActivity.showSoftInput(transactionItemCountEditText)
                        return@setOnClickListener
                    }
                    if (transactionItemPriceEditText.editableText.isNullOrEmpty()) {
                        transactionItemPriceLayout.error = "단가를 입력해 주세요."
                        mainActivity.showSoftInput(transactionItemPriceEditText)
                        return@setOnClickListener
                    }
                    if (transactionAmountReceivedEditText.editableText.isNullOrEmpty()) {
                        transactionAmountReceivedLayout.error = "받은 금액을 입력해 주세요."
                        mainActivity.showSoftInput(transactionAmountReceivedEditText)
                        return@setOnClickListener
                    }


                    val newTransactionData = TransactionData(
                        clientIdx!!,
                        Timestamp.now(),
                        false,
                        transactionAmountReceivedEditText.editableText.toString(),
                        transactionVM.nextTransactionIdx,
                        transactionItemCountEditText.editableText.toString().toLong(),
                        transactionItemPriceEditText.editableText.toString(),
                        transactionNameEditText.editableText.toString()
                    )
                    TransactionRepository.setTransactionData(uid, newTransactionData) {
                        TransactionRepository.setClientTransactionDataList(
                            uid,
                            newTransactionData
                        ) {
                            Snackbar.make(
                                fragmentTransactionBinding.root,
                                "거래 내용 저장 완료 되었습니다.",
                                Snackbar.LENGTH_SHORT
                            ).show()
                            transactionVM.getAllTransactionData(uid)
                            transactionVM.getNextTransactionIdx(uid)
                            addTransactionDialog.dismiss()
                        }
                    }
                }

            } else {
                addTransactionBtn.setOnClickListener {
                    if (selectClientIdx == null) {
                        selectTransactionClientBtn.requestFocus()
                        selectTransactionClientBtn.callOnClick()
                    } else {

                        if (transactionNameEditText.editableText.isNullOrEmpty()) {
                            transactionNameLayout.error = "품명을 입력해 주세요."
                            mainActivity.showSoftInput(transactionNameEditText)
                            return@setOnClickListener
                        }
                        if (transactionItemCountEditText.editableText.isNullOrEmpty()) {
                            transactionItemCountLayout.error = "수량을 입력해 주세요."
                            mainActivity.showSoftInput(transactionItemCountEditText)
                            return@setOnClickListener
                        }
                        if (transactionItemPriceEditText.editableText.isNullOrEmpty()) {
                            transactionItemPriceLayout.error = "단가를 입력해 주세요."
                            mainActivity.showSoftInput(transactionItemPriceEditText)
                            return@setOnClickListener
                        }
                        if (transactionAmountReceivedEditText.editableText.isNullOrEmpty()) {
                            transactionAmountReceivedLayout.error = "받은 금액을 입력해 주세요."
                            mainActivity.showSoftInput(transactionAmountReceivedEditText)
                            return@setOnClickListener
                        }

                        val newTransactionData = TransactionData(
                            selectClientIdx!!,
                            Timestamp.now(),
                            false,
                            transactionAmountReceivedEditText.editableText.toString(),
                            transactionVM.nextTransactionIdx,
                            transactionItemCountEditText.text.toString().toLong(),
                            transactionItemPriceEditText.text.toString(),
                            transactionNameEditText.text.toString()
                        )
                        TransactionRepository.setTransactionData(uid, newTransactionData) {
                            TransactionRepository.setClientTransactionDataList(
                                uid,
                                newTransactionData
                            ) {
                                selectClientIdx = null
                                Snackbar.make(
                                    fragmentTransactionBinding.root,
                                    "거래 내용 저장 완료 되었습니다.",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                transactionVM.getAllTransactionData(uid)
                                transactionVM.getNextTransactionIdx(uid)
                                addTransactionDialog.dismiss()
                            }
                        }
                    }
                }
                selectTransactionClientBtn.setOnClickListener {

                    val transactionSelectClientBinding =
                        TransactionSelectClientBinding.inflate(layoutInflater)

                    clientSimpleDataList.clear()
                    transactionVM.clientSimpleDataListVM.value?.forEach {
                        clientSimpleDataList.add(it)
                    }

                    transactionSelectClientBinding.run {
                        searchClientResultRecyclerView.run {
                            adapter = SearchClientAdapter()
                            layoutManager = LinearLayoutManager(context)
                            addItemDecoration(
                                MaterialDividerItemDecoration(
                                    context,
                                    MaterialDividerItemDecoration.VERTICAL
                                )
                            )
                        }

                        searchClientEditText.setOnEditorActionListener { v, _, _ ->
                            clientSimpleDataList.clear()
                            transactionVM.clientSimpleDataListVM.value?.forEach {
                                if (it.clientName.contains(v.editableText) || it.clientManagerName.contains(
                                        v.editableText
                                    )
                                ) {
                                    clientSimpleDataList.add(it)
                                }
                                searchClientResultRecyclerView.adapter?.notifyDataSetChanged()
                            }
                            true
                        }
                    }
                    selBulider = AlertDialog.Builder(requireContext())
                    selDialog = selBulider.create()
                    selDialog.setView(transactionSelectClientBinding.root)
                    selDialog.show()

                }
            }
        }
        addTransactionDialog.setView(dialogAddTransactionBinding.root)
        addTransactionDialog.show()
    }

    // 금액을 000,000 형식으로 변환하는 함수
    private fun formatAmount(amount: String): String {
        val longAmount = amount.toBigIntegerOrNull() ?: BigInteger.ZERO
        return String.format("%,d", longAmount)
    }

    inner class SearchClientAdapter() :
        RecyclerView.Adapter<SearchClientAdapter.SearchClientViewHolder>() {
        inner class SearchClientViewHolder(transactionSelectClientItemBinding: TransactionSelectClientItemBinding) :
            ViewHolder(transactionSelectClientItemBinding.root) {

            val selectTransactionClientName =
                transactionSelectClientItemBinding.selectTransactionClientName
            val selectTransactionClinetState =
                transactionSelectClientItemBinding.selectTransactionClinetState
            val selectTransactionClientManagerName =
                transactionSelectClientItemBinding.selectTransactionClientManagerName
            val selectTransactionClientBookmarkView =
                transactionSelectClientItemBinding.selectTransactionClientBookmarkView

            init {
                transactionSelectClientItemBinding.root.setOnClickListener {
                    selectClientIdx = clientSimpleDataList[bindingAdapterPosition].clientIdx

                    if (inoutMode) {
                        Snackbar.make(
                            dialogAddDepositBinding.root,
                            "${clientSimpleDataList[bindingAdapterPosition].clientName} 선택 되었습니다.",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else {
                        Snackbar.make(
                            dialogAddTransactionBinding.root,
                            "${clientSimpleDataList[bindingAdapterPosition].clientName} 선택 되었습니다.",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                    selDialog.dismiss()
                }
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchClientViewHolder {
            val transactionSelectClientItemBinding =
                TransactionSelectClientItemBinding.inflate(layoutInflater)
            val viewHolder = SearchClientViewHolder(transactionSelectClientItemBinding)

            transactionSelectClientItemBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return viewHolder
        }

        override fun getItemCount(): Int {
            return clientSimpleDataList.size
        }

        override fun onBindViewHolder(holder: SearchClientViewHolder, position: Int) {
            when (clientSimpleDataList[position].clientState) {
                1L -> {
                    holder.selectTransactionClinetState.setBackgroundResource(R.drawable.client_state_circle_trading)
                }

                2L -> {
                    holder.selectTransactionClinetState.setBackgroundResource(R.drawable.client_state_circle_trade_try)
                }

                3L -> {
                    holder.selectTransactionClinetState.setBackgroundResource(R.drawable.client_state_circle_trade_stop)
                }

                else -> return
            }
            if (clientSimpleDataList[position].isBookmark) {
                holder.selectTransactionClientBookmarkView.visibility = View.VISIBLE
                holder.selectTransactionClientBookmarkView.setBackgroundResource(R.drawable.star_fill_24px)
            } else {
                holder.selectTransactionClientBookmarkView.visibility = View.GONE
            }
            holder.selectTransactionClientName.text = clientSimpleDataList[position].clientName
            holder.selectTransactionClientManagerName.text =
                clientSimpleDataList[position].clientManagerName
        }

    }

}
