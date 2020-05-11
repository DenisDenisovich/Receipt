package shiverawe.github.com.receipt.domain.interactor.create_receipt.receipt_printer

import android.annotation.SuppressLint
import android.content.Context
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.domain.entity.base.Receipt
import shiverawe.github.com.receipt.utils.floorTwo
import java.lang.Math.floor
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*

class ShareReceiptPrinter(val context: Context) : IReceiptPrinter {

    private val dateFormatter = SimpleDateFormat("EEEE, dd.MM.yyyy", Locale("ru"))

    @SuppressLint("DefaultLocale")
    override fun receiptToString(receipt: Receipt): String {
        val shop = receipt.header.shop
        val shareText = StringBuilder()

        // build receipt header
        shareText.appendln(context.getString(R.string.share_title))
        shareText.appendln(context.getString(R.string.share_link, receipt.header.receiptId))
        shareText.appendln(context.getString(R.string.share_shop, shop.place))
        shareText.appendln(context.getString(R.string.share_date, dateFormatter.format(shop.date).capitalize()))
        shareText.appendln(context.getString(R.string.share_sum, shop.sum))

        // build receipt's products
        var price: String
        var amountNumber: Double
        var amountString: String
        for (productIndex in receipt.items.indices) {
            shareText.appendln("${productIndex + 1}. ${receipt.items[productIndex].text}")
            amountNumber = BigDecimal(receipt.items[productIndex].amount).setScale(3, RoundingMode.DOWN).toDouble()

            amountString = if (amountNumber == floor(amountNumber)) {
                amountNumber.toInt().toString()
            } else {
                amountNumber.toString()
            }

            shareText.appendln(context.getString(R.string.share_amount, amountString))
            price = receipt.items[productIndex].price.floorTwo()
            shareText.appendln(context.getString(R.string.share_price, price))
        }

        return shareText.toString()
    }
}