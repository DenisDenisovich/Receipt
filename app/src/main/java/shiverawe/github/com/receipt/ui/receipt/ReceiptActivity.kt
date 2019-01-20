package shiverawe.github.com.receipt.ui.receipt

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_receipt.*
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.data.Receipt
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import android.view.View
import android.view.ViewOutlineProvider
import android.content.Intent
import java.lang.StringBuilder
import java.math.BigDecimal
import java.math.RoundingMode


const val RECEIPT_TAG = "receipt"
class ReceiptActivity : AppCompatActivity(), View.OnClickListener {
    var receipt: Receipt? = null
    var dateStr = ""
    private val dateFormatterDigits = SimpleDateFormat("dd.MM_HH:mm", Locale("ru"))
    private val dateFormatterDay = DateFormat.getDateInstance(SimpleDateFormat.FULL, Locale("ru"))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt)
        receipt = Gson().fromJson(intent.getStringExtra(RECEIPT_TAG), Receipt::class.java)

        val fullDate = dateFormatterDigits.format(Date(receipt!!.shop.date)).split("_")
        val day = dateFormatterDay.format(Date(receipt!!.shop.date)).split(",")[0].capitalize()
        val date = fullDate[0]
        val time = fullDate[1]
        dateStr = "$date $day $time"
        tv_receipt_date.text = dateStr

        tv_receipt_shop_name.text = receipt!!.shop.place
        tv_receipt_shop_price.text = receipt!!.shop.sum
        rv_receipt.adapter = RvAdapterReceipt(receipt!!.items!!)
        rv_receipt.layoutManager = LinearLayoutManager(this)
        btn_receipt_back.setOnClickListener(this)
        btn_receipt_share.setOnClickListener(this)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            changeDownShadow()
        }
        fl_receipt_top_ticket.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_receipt_back -> {
                finish()
            }
            R.id.btn_receipt_share -> {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, getReceiptString())
                sendIntent.type = "text/plain"
                startActivity(Intent.createChooser(sendIntent, "Отправить чек"))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun changeDownShadow() {
        val receiptBottomOutlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                val rect = Rect()
                view.background?.copyBounds(rect)
                rect.top += resources.getDimensionPixelSize(R.dimen.receipt_arc_radius)
                rect.bottom -= resources.getDimensionPixelSize(R.dimen.receipt_zigzag_height)
                outline.setRect(rect)
            }
        }
        rv_receipt.outlineProvider = receiptBottomOutlineProvider

        rv_receipt.post {
            val bounds = Rect(0, 0, rv_receipt.width, rv_receipt.height)
            drawReceiptBottom(bounds)
        }

        val receiptTopOutlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                val rect = Rect()
                view.background?.copyBounds(rect)
                rect.top += resources.getDimensionPixelSize(R.dimen.receipt_top_corners)
                rect.bottom -= resources.getDimensionPixelSize(R.dimen.receipt_arc_radius) * 2
                outline.setRect(rect)
            }
        }
        fl_receipt_top_ticket.outlineProvider = receiptTopOutlineProvider
        fl_receipt_top_ticket.post {
            val bounds = RectF(0F, 0F, fl_receipt_top_ticket.width.toFloat(), fl_receipt_top_ticket.height.toFloat())
            drawReceiptTop(bounds)
        }
    }

    private fun drawReceiptBottom(background: Rect) {
        val arcRadius = resources.getDimensionPixelSize(R.dimen.receipt_arc_radius).toFloat()
        val path = Path()
        // top left arc
        val oval = RectF(-arcRadius, -arcRadius,arcRadius, arcRadius)
        path.addArc(oval, 90F, -90F)
        // top line
        path.lineTo(background.width() - arcRadius, 0F)
        // top right arc
        oval.set(background.width() - arcRadius, -arcRadius,background.width() + arcRadius, arcRadius)
        path.addArc(oval, 180F, -90F)
        // right line
        path.lineTo(background.width().toFloat(), background.height().toFloat())
        // bottom zigzag
        val zigzagCount = 10
        val zigzagWidth = background.width().toFloat() / zigzagCount / 2
        val zigzagHeight = resources.getDimensionPixelSize(R.dimen.receipt_zigzag_height).toFloat()
        for (i in 1 .. 20) {
            val lineY: Float = if (i % 2 == 0) background.height().toFloat()
            else background.height() - zigzagHeight
            path.lineTo(background.width() - zigzagWidth * i, lineY)
        }
        // left line
        path.lineTo(0F, arcRadius)

        val b = Bitmap.createBitmap(background.width(), background.height(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(b)
        val paint = Paint()
        paint.strokeWidth = 1F
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        canvas.clipPath(path)
        canvas.drawColor(ContextCompat.getColor(this, R.color.receiptColor))
        rv_receipt.background = BitmapDrawable(resources, b)
    }

    private fun drawReceiptTop(background: RectF) {
        val topRadius = resources.getDimensionPixelSize(R.dimen.receipt_top_corners).toFloat()
        val bottomRadius = resources.getDimensionPixelSize(R.dimen.receipt_arc_radius).toFloat()
        val b = Bitmap.createBitmap(background.width().toInt(), background.height().toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(b)
        // draw round rect
        val paint = Paint()
        paint.strokeWidth = 1F
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = ContextCompat.getColor(this, R.color.receiptColor)
        canvas.drawRoundRect(background, topRadius, topRadius, paint)
        // draw inner corners
        val circlePaint = Paint()
        circlePaint.strokeWidth = 1F
        circlePaint.isAntiAlias = true
        circlePaint.style = Paint.Style.FILL_AND_STROKE
        circlePaint.color = ContextCompat.getColor(this, R.color.colorPrimary)
        val circle = RectF(-bottomRadius, background.height() - bottomRadius, bottomRadius, background.height() + bottomRadius)
        canvas.drawArc(circle, 270F, 90F, true, circlePaint)
        circle.set(background.width() - bottomRadius, background.height() - bottomRadius, background.width() + bottomRadius, background.height() + bottomRadius)
        canvas.drawArc(circle, 180F, 90F, true, circlePaint)
        // draw dash line
        val dashPaint = Paint()
        dashPaint.color = Color.BLACK
        dashPaint.strokeWidth = resources.getDimensionPixelSize(R.dimen.receipt_top_line_width).toFloat()
        val dashSize = resources.getDimensionPixelSize(R.dimen.receipt_top_line_dash_size).toFloat()
        val gapSize = resources.getDimensionPixelSize(R.dimen.receipt_top_line_gap_size).toFloat()
        dashPaint.pathEffect = DashPathEffect(floatArrayOf(dashSize, gapSize), 0F)
        canvas.drawLine(bottomRadius, background.height(), background.width() - bottomRadius, background.height(), dashPaint)
        fl_receipt_top_ticket.background = BitmapDrawable(resources, b)
    }

    private fun getReceiptString(): String {
        val link = "https://github.com/DenisDenisovich/Receipt"
        val sb = StringBuilder()
        sb.appendln("Скачать приложение по ссылке:")
        sb.appendln(link)
        sb.appendln("Магазин: ${receipt!!.shop.place}")
        sb.appendln("Дата:    $dateStr")
        sb.appendln("Сумма:   ${receipt!!.shop.sum}")
        var price: String
        var amountNumber: Double
        var amountString: String
        for (productIndex in 0 until receipt!!.items!!.size) {
            sb.appendln("${productIndex + 1}. ${receipt!!.items!![productIndex].text}")
            amountNumber = BigDecimal(receipt!!.items!![productIndex].amount).setScale(3, RoundingMode.DOWN).toDouble()
            amountString = if (amountNumber == Math.floor(amountNumber)) amountNumber.toInt().toString()
            else amountNumber.toString()
            sb.appendln("Кол-во: $amountString")
            price = BigDecimal(receipt!!.items!![productIndex].price).setScale(2, RoundingMode.DOWN).toString() + " p"
            sb.appendln("Цена:   $price")
        }
        return sb.toString()
    }
}