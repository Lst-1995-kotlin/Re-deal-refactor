package com.hifi.redeal.transaction.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.hifi.redeal.data.entrie.TestEntry
import com.hifi.redeal.transaction.model.Client
import com.hifi.redeal.transaction.model.ClientData
import com.hifi.redeal.transaction.model.SelectTransactionData
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
    private var selectIndex: MutableList<Long>? = null
    private val _transactionList = MutableLiveData<List<Transaction>>()
    private val _modifyTransaction = MutableLiveData<Transaction?>()
    private val _transactionPosition = MutableLiveData<Int>()
    private val _selectTransactionIndex = MutableLiveData<List<Long>?>()

    private var newTransactionIdx = 0L
    private var selectClientIndex: Long? = null
    private var curdPosition = 0

    val transactionList: LiveData<List<Transaction>> get() = _transactionList
    val modifyTransaction: LiveData<Transaction?> get() = _modifyTransaction
    val transactionPosition: LiveData<Int> get() = _transactionPosition
    val selectTransactionIndex: LiveData<List<Long>?> get() = _selectTransactionIndex

    private val _tempList = MutableLiveData<List<TestEntry>>()
    val tempList: LiveData<List<TestEntry>> get() = _tempList

    init {
        getNextTransactionIdx()
        getAllTransactionData()
    }


    fun deleteSelectTransactions() {
        totalTransactionData.filter { it.isSelected() }.forEach {
            deleteTransactionIndex(it.getTransactionIdx())
        }
    }

    fun clearDeleteSelectTransactions() {
        totalTransactionData.replaceAll { transaction ->
            if (transaction.isSelected()) {
                transaction.replaceSelectedTransaction()
            } else transaction
        }
    }

    fun transactionSelectedChanged(index: Long) {
        totalTransactionData.replaceAll { transaction ->
            if (transaction.getTransactionIdx() == index) {
                transaction.replaceSelectedTransaction()
            } else {
                transaction
            }
        }.let {
            updateTransaction()
        }
    }

    fun postValueScrollPosition() {
        _transactionPosition.postValue(curdPosition)
    }

    fun setMoveToPosition(position: Int) {
        curdPosition = position
    }

    fun setModifyTransaction(transaction: Transaction?) {
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
            val updatedTransaction = createUpdatedTransaction(
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

    fun deleteTransactionIndex(transactionIdx: Long) {
        transactionRepository.deleteTransactionData(transactionIdx) {
            totalTransactionData.removeAll { it.getTransactionIdx() == transactionIdx }
            updateTransaction()
        }
    }

    fun addDepositTransaction(client: Client, amount: Long) {
        val newDepositTransactionData = TransactionData(
            client.getClientIdx(),
            Timestamp.now(),
            true,
            amount,
            newTransactionIdx,
            0,
            0,
            ""
        )
        transactionRepository.setTransactionData(newDepositTransactionData) {
            val newTransaction = Transaction(
                newDepositTransactionData,
                client.getClientData(),
                SelectTransactionData(false)
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
        val newSalesTransactionData = TransactionData(
            client.getClientIdx(),
            Timestamp.now(),
            false,
            amount,
            newTransactionIdx,
            itemCount,
            itemPrice,
            itemName
        )
        transactionRepository.setTransactionData(newSalesTransactionData) {
            val newTransaction = Transaction(
                newSalesTransactionData,
                client.getClientData(),
                SelectTransactionData(false)
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

    private fun getAllTransactionData() {
        transactionRepository.getAllTransactionData {
            totalTransactionData.clear()
            it.result.forEach { c1 ->
                setMoveToPosition(0)
                getClientInformation(
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

    private fun getClientInformation(transactionData: TransactionData) {
        transactionRepository.getClientInfo(transactionData.clientIdx) {
            for (c1 in it.result) {
                val newTransactionData = Transaction(
                    transactionData,
                    ClientData(
                        c1["clientIdx"] as Long,
                        c1["clientName"] as String,
                        c1["clientManagerName"] as String,
                        c1["clientState"] as Long,
                        c1["isBookmark"] as Boolean
                    ),
                    SelectTransactionData(false)
                )
                totalTransactionData.add(newTransactionData)
                updateTransaction()
            }
        }
    }

    private fun updateTransaction() {
        selectClientIndex?.let { index ->
            _transactionList.postValue(totalTransactionData.filter {
                it.equalsTransactionClientIndex(index)
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
            TransactionData(
                client.getClientIdx(),
                currentTransaction.getTransactionDate(),
                false,
                amount,
                currentTransaction.getTransactionIdx(),
                itemCount,
                itemPrice,
                itemName
            ),
            client.getClientData(),
            SelectTransactionData(false)
        )
    }

    private fun createUpdatedTransaction(
        client: Client,
        currentTransaction: Transaction,
        amount: Long
    ): Transaction {
        return Transaction(
            TransactionData(
                client.getClientIdx(),
                currentTransaction.getTransactionDate(),
                true,
                amount,
                currentTransaction.getTransactionIdx(),
                0,
                0,
                ""
            ),
            client.getClientData(),
            SelectTransactionData(false)
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
            true,
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
