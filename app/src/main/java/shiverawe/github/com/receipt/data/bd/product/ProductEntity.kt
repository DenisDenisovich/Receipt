package shiverawe.github.com.receipt.data.bd.product

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.PrimaryKey
import shiverawe.github.com.receipt.data.bd.receipt.ReceiptEntity

@Entity(tableName = "product_table",
        foreignKeys = [ForeignKey(entity = ReceiptEntity::class, parentColumns = ["id"], childColumns = ["receiptId"], onDelete = CASCADE)])
data class ProductEntity(
        var amount: Float = 0F,
        var text: String = "",
        var price: Double = 0.0,
        var receiptId: Long = 0L,
        @PrimaryKey(autoGenerate = true)
        var id: Long? = null)