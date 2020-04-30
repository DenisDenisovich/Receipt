package shiverawe.github.com.receipt.data.bd.room.product

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import shiverawe.github.com.receipt.data.bd.room.receipt.ReceiptEntity

@Entity(tableName = "product_table",
    foreignKeys = [ForeignKey(entity = ReceiptEntity::class, parentColumns = ["id"], childColumns = ["receiptId"], onDelete = CASCADE)])
data class ProductEntity(
    var amount: Float = 0F,
    var text: String = "",
    var price: Double = 0.0,
    var receiptId: Long = 0L,
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
)