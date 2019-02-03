package shiverawe.github.com.receipt.data.bd.receipt

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "receipt_table", indices = [Index(value = ["date"])])
data class ReceiptEntity(
        var date: Long = 0L,
        var place: String = "",
        var sum: Double = 0.0,
        var fn: String = "",
        var fd: String = "",
        var fp: String = "",
        @PrimaryKey(autoGenerate = true)
        var id: Long? = null)