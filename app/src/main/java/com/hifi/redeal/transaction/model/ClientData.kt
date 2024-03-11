package com.hifi.redeal.transaction.model

data class ClientData(
    val id: Int,
    val name: String,
    val managerName: String,
    var state: Long,
    val bookmark: Boolean
)
