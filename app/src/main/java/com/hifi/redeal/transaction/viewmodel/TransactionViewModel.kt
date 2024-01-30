package com.hifi.redeal.transaction.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.hifi.redeal.transaction.model.Client
import com.hifi.redeal.transaction.model.LoadTransactionData
import com.hifi.redeal.transaction.model.Transaction
import com.hifi.redeal.transaction.model.TransactionData
import com.hifi.redeal.transaction.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
) : ViewModel() {

    private val totalTransactionData = mutableListOf<Transaction>()
    private val _transactionList = MutableLiveData<List<Transaction>>()
    private val _modifyTransaction = MutableLiveData<Transaction>()
    private val _transactionPosition = MutableLiveData<Int>()

    private var newTransactionIdx = 0L
    private var selectClientIndex: Long? = null
    private var curdPosition = 0
    val transactionList: LiveData<List<Transaction>> get() = _transactionList
    val modifyTransaction: LiveData<Transaction> get() = _modifyTransaction
    val transactionPosition: LiveData<Int> get() = _transactionPosition

    init {
        getNextTransactionIdx()
        getAllTransactionData()
    }

    fun setMoveToPosition(position: Int) {
        curdPosition = position
    }

    fun postValueScrollPosition() {
        _transactionPosition.postValue(curdPosition)
    }

    fun setModifyTransaction(transaction: Transaction) {
        _modifyTransaction.postValue(transaction)
    }

    fun updateModifySalesTransaction(
        client: Client,
        itemName: String,
        itemCount: Long,
        itemPrice: Long,
        amount: Long
    ) {
        val currentTransaction = modifyTransaction.value

        currentTransaction?.let {
            val updatedTransaction =
                createUpdatedTransaction(
                    client,
                    it,
                    itemName,
                    itemCount,
                    itemPrice,
                    amount
                )

            replaceTransactionInList(it, updatedTransaction)

            reflectUpdatedTransactionToRepository(
                client,
                it,
                itemName,
                itemCount,
                itemPrice,
                amount
            )
        }
    }

    fun updateModifyDepositTransaction(client: Client, amount: Long) {
        modifyTransaction.value?.let { currentTransaction ->
            val updatedTransaction = createUpdatedTransaction(
                client,
                currentTransaction,
                amount
            )

            replaceTransactionInList(currentTransaction, updatedTransaction)
            reflectUpdatedTransactionToRepository(client, currentTransaction, amount)
        }
    }

    fun deleteTransactionData(transactionIdx: Long) {
        transactionRepository.deleteTransactionData(transactionIdx) {
            totalTransactionData.removeAll { it.getTransactionIdx() == transactionIdx }
            updateTransaction()
        }
    }

    fun addDepositTransaction(client: Client, amount: Long) {
        val createTime = Timestamp.now()
        val newDepositTransactionData = TransactionData(
            client.getClientIdx(),
            createTime,
            true,
            amount,
            newTransactionIdx,
            0,
            0,
            ""
        )
        transactionRepository.setTransactionData(newDepositTransactionData) {
            val newTransaction = Transaction(
                LoadTransactionData(
                    client.getClientIdx(),
                    client.getClientName(),
                    createTime,
                    true,
                    amount,
                    newTransactionIdx,
                    0,
                    0,
                    ""
                )
            )
            totalTransactionData.add(newTransaction)
            updateTransaction()
            getNextTransactionIdx()
        }
    }

    fun addSalesTransaction(
        client: Client,
        itemName: String,
        itemCount: Long,
        itemPrice: Long,
        amount: Long,
    ) {
        val createTime = Timestamp.now()
        val newSalesTransactionData = TransactionData(
            client.getClientIdx(),
            createTime,
            false,
            amount,
            newTransactionIdx,
            itemCount,
            itemPrice,
            itemName
        )
        transactionRepository.setTransactionData(newSalesTransactionData) {
            val newTransaction = Transaction(
                LoadTransactionData(
                    client.getClientIdx(),
                    client.getClientName(),
                    createTime,
                    false,
                    amount,
                    newTransactionIdx,
                    itemCount,
                    itemPrice,
                    itemName
                )
            )
            totalTransactionData.add(newTransaction)
            updateTransaction()
            getNextTransactionIdx()
        }
    }

    fun setSelectClientIndex(clientIndex: Long?) {
        selectClientIndex = clientIndex
        updateTransaction()
    }

    fun getAllTransactionData() {
        setMoveToPosition(0)
        Log.d("ttt", "갱신됨")
        transactionRepository.getAllTransactionData {
            totalTransactionData.clear()
            it.result.forEach { c1 ->
                getClientName(
                    TransactionData(
                        c1["clientIdx"] as Long,
                        c1["date"] as Timestamp,
                        c1["isDeposit"] as Boolean,
                        c1["transactionAmountReceived"] as Long,
                        c1["transactionIdx"] as Long,
                        c1["transactionItemCount"] as Long,
                        c1["transactionItemPrice"] as Long,
                        c1["transactionItemName"] as String
                    )
                )
            }
        }
    }

    private fun getClientName(
        transactionData: TransactionData
    ) {
        transactionRepository.getClientInfo(transactionData.clientIdx) {
            for (c1 in it.result) {
                val newTransactionData = Transaction(
                    LoadTransactionData(
                        transactionData.clientIdx,
                        c1["clientName"] as String,
                        transactionData.date,
                        transactionData.isDeposit,
                        transactionData.transactionAmountReceived,
                        transactionData.transactionIdx,
                        transactionData.transactionItemCount,
                        transactionData.transactionItemPrice,
                        transactionData.transactionItemName
                    )
                )
                totalTransactionData.add(newTransactionData)
                updateTransaction()
            }
        }
    }

    private fun updateTransaction() {
        selectClientIndex?.let { index ->
            _transactionList.postValue(totalTransactionData.filter {
                it.getTransactionClientIdx() == index
            })
        } ?: _transactionList.postValue(totalTransactionData)
    }

    private fun getNextTransactionIdx() {
        transactionRepository.getNextTransactionIdx {
            for (c1 in it.result) {
                newTransactionIdx = c1["transactionIdx"] as Long + 1L
            }
        }
    }

    private fun createUpdatedTransaction(
        client: Client,
        currentTransaction: Transaction,
        itemName: String,
        itemCount: Long,
        itemPrice: Long,
        amount: Long
    ): Transaction {
        return Transaction(
            LoadTransactionData(
                client.getClientIdx(),
                client.getClientName(),
                currentTransaction.getTransactionDate(),
                false,
                amount,
                currentTransaction.getTransactionIdx(),
                itemCount,
                itemPrice,
                itemName
            )
        )
    }

    private fun createUpdatedTransaction(
        client: Client,
        currentTransaction: Transaction,
        amount: Long
    ): Transaction {
        return Transaction(
            LoadTransactionData(
                client.getClientIdx(),
                client.getClientName(),
                currentTransaction.getTransactionDate(),
                true,
                amount,
                currentTransaction.getTransactionIdx(),
                0,
                0,
                ""
            )
        )
    }

    private fun replaceTransactionInList(
        currentTransaction: Transaction,
        updatedTransaction: Transaction
    ) {
        totalTransactionData.replaceAll { transactionData ->
            if (transactionData == currentTransaction) updatedTransaction
            else transactionData
        }
    }

    private fun reflectUpdatedTransactionToRepository(
        client: Client,
        currentTransaction: Transaction,
        itemName: String,
        itemCount: Long,
        itemPrice: Long,
        amount: Long
    ) {
        val updatedTransactionData = TransactionData(
            client.getClientIdx(),
            currentTransaction.getTransactionDate(),
            false,
            amount,
            currentTransaction.getTransactionIdx(),
            itemCount,
            itemPrice,
            itemName
        )

        transactionRepository.setTransactionData(updatedTransactionData) {
            updateTransaction()
        }
    }

    private fun reflectUpdatedTransactionToRepository(
        client: Client,
        currentTransaction: Transaction,
        amount: Long
    ) {
        val updatedTransactionData = TransactionData(
            client.getClientIdx(),
            currentTransaction.getTransactionDate(),
            false,
            amount,
            currentTransaction.getTransactionIdx(),
            0,
            0,
            ""
        )

        transactionRepository.setTransactionData(updatedTransactionData) {
            updateTransaction()
        }
    }
}
