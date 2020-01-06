package shiverawe.github.com.receipt.data.bd.room.receipt

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "receipt_table", indices = [Index(value = ["date"])])
data class ReceiptEntity(
        var date: Long = 0L,
        var place: String = "",
        var sum: Double = 0.0,
        var fn: String = "",
        var fd: String = "",
        var fp: String = "",
        @PrimaryKey(autoGenerate = true)
        var id: Long? = null
)
