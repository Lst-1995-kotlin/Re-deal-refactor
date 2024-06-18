package com.hifi.redeal.data.entrie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity("client")
data class ClientEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val address: String,
    @ColumnInfo(name = "address_detail")
    val addressDetail: String,
    @ColumnInfo(name = "representative_number")
    val representativeNumber: String,
    @ColumnInfo(name = "fax_number")
    val faxNumber: String,
    @ColumnInfo(name = "manager_name")
    val managerName: String,
    @ColumnInfo(name = "manager_number")
    val managerNumber: String,
    @ColumnInfo val description: String,
    @ColumnInfo val state: Int,
    @ColumnInfo val bookmark: Boolean,
    @ColumnInfo(name = "last_visit_date")
    val lastVisitDate: Date?,
    @ColumnInfo(name = "last_contact_date")
    val lastContactDate: Date?,
    @ColumnInfo(name = "create_date")
    val createDate: Date?,
    @ColumnInfo(name = "view_count")
    val viewCount: Long
)