package com.hifi.redeal.memo.model

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import java.util.Date

//@Database(entities = [RecordMemoDataTest::class], version = 1)
//@TypeConverters(Converters::class)
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun recordMemoDao(): RecordMemoDao
//}
//
//class Converters {
//    @TypeConverter
//    fun fromDate(date: Date?): Long? {
//        return date?.time
//    }
//
//    @TypeConverter
//    fun toDate(timestamp: Long?): Date? {
//        return timestamp?.let { Date(it) }
//    }
//
//    @TypeConverter
//    fun uriToString(uri: Uri?): String? {
//        return uri?.toString()
//    }
//
//    @TypeConverter
//    fun stringToUri(uriString: String?): Uri? {
//        return uriString?.let { Uri.parse(it) }
//    }
//}


data class UserRecordMemoData(
    val clientIdx: Long,
    val context: String,
    val date: Timestamp,
    val audioSrc: Uri?,
    val audioFilename: String
)

data class RecordMemoData(
    val context: String,
    val date: Date,
    val audioFileUri: Uri?,
    val audioFilename: String,
    val duration:Long
)

@Entity(tableName = "record_memos")
data class RecordMemoDataTest(
    @PrimaryKey val id:Long,
    @ColumnInfo(name = "client_owner_id") val clientOwnerId: Long,
    @ColumnInfo(name = "record_memo") val context: String,
    @ColumnInfo(name = "date") val date: Date,
    @ColumnInfo(name = "uri") val audioFileUri: Uri?,
    @ColumnInfo(name = "file_name") val audioFilename: String,
    @ColumnInfo(name = "duration") val duration:Long
)