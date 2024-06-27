package com.hifi.redeal.account.repository.model

import com.google.firebase.Timestamp
import java.util.Date

data class ClientInputData(
    val clientAddress: String? = null,
    val clientCeoPhone: String? = null,
    val clientDetailAdd: String? = null,
    val clientExplain: String? = null,
    val clientFaxNumber: String? = null,
    val clientIdx: Long? = null,
    val clientManagerName: String? = null,
    val clientManagerPhone: String? = null,
    val clientMemo: String? = null,
    val clientName: String? = null,
    val clientState: Long? = null,
    @field:JvmField
    val isBookmark: Boolean? = null,
    val photoMemoIdxList: List<Long>? = null,
    val recordMemoIdxList: List<Long>? = null,
    val transactionIdxList: List<Long>? = null,
    val viewCount: Long? = null,
)

data class ClientData(
    val clientAddress: String? = null,
    val clientCeoPhone: String? = null,
    val clientDetailAdd: String? = null,
    val clientExplain: String? = null,
    val clientFaxNumber: String? = null,
    val clientIdx: Long? = null,
    val clientManagerName: String? = null,
    val clientManagerPhone: String? = null,
    val clientMemo: String? = null,
    var clientName: String? = null,
    var clientState: Long? = null,
    @field:JvmField
    var isBookmark: Boolean? = null,
    val photoMemoIdxList: List<Long>? = null,
    val recordMemoIdxList: List<Long>? = null,
    val transactionIdxList: List<Long>? = null,
    val viewCount: Long? = null,
    var recentContactDate: Timestamp? = null,
    var recentVisitDate: Timestamp? = null
)

data class ClientListData(
    val id: Int,
    val name: String,
    val address: String,
    val addressDetail: String,
    val representativeNumber: String,
    val managerName: String,
    val manageNumber: String,
    val state: Int,
    val bookmark: Boolean,
    val lastVisitDate: Date,
    val viewCount: Long
)