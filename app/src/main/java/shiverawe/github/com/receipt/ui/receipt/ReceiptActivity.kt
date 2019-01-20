package shiverawe.github.com.receipt.ui.receipt

import android.app.Activity
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
import android.widget.FrameLayout
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import shiverawe.github.com.receipt.data.network.entity.create.CreateRequest
import shiverawe.github.com.receipt.data.network.entity.create.CreateResponce
import shiverawe.github.com.receipt.ui.App
import shiverawe.github.com.receipt.ui.EXTRA_DATE_RECEIPT
import java.lang.Exception
import java.lang.StringBuilder
import java.math.BigDecimal
import java.math.RoundingMode


const val RECEIPT_TAG = "receipt"
const val RECEIPT_QR_CODE = "receipt_qr_code"
class ReceiptActivity : AppCompatActivity(), View.OnClickListener {
    var receipt: Receipt? = null
    var createCall: Call<CreateResponce>? = null
    var dateStr = ""
    var qrCode = ""
    var qrDate: Long = 0
    private val dateFormatterDigits = SimpleDateFormat("dd.MM_HH:mm", Locale("ru"))
    private val dateFormatterDay = DateFormat.getDateInstance(SimpleDateFormat.FULL, Locale("ru"))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt)
        receipt = Gson().fromJson(intent.getStringExtra(RECEIPT_TAG), Receipt::class.java)
        qrCode = intent.getStringExtra(RECEIPT_QR_CODE)?: ""
        val lp = rv_receipt.layoutParams as FrameLayout.LayoutParams
        if (qrCode.isNotBlank()) {
            lp.bottomMargin = resources.getDimensionPixelSize(R.dimen.receipt_network_bottom_margin)
        } else {
            btn_receipt_save.visibility = View.GONE
            lp.bottomMargin = resources.getDimensionPixelSize(R.dimen.receipt_offline_bottom_margin)
        }
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
        btn_receipt_save.setOnClickListener(this)
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
                sendIntent.putExtra(Intent.EXTRA_TEXT, getShareString())
                sendIntent.type = "text/plain"
                startActivity(Intent.createChooser(sendIntent, "Отправить чек"))
            }
            R.id.btn_receipt_save -> {
                saveReceipt()
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

    private fun getShareString(): String {
        val url = StringBuilder()
        val date = getDateForShare(receipt!!.shop.date)
        url.appendln("Магазин: ${receipt!!.shop.place}")
        url.appendln("Дата:    $dateStr")
        url.appendln("Сумма:   ${receipt!!.shop.sum}")
        url.appendln("Посмотреть чек по ссылке:")
        url.append("http://receipt.shefer.space/?")
        url.appendln("fn=${receipt!!.shop.fnShare}&i=${receipt!!.shop.fdShare}&fp=${receipt!!.shop.fpShare}&s=${receipt!!.shop.sShare}&t=$date")
        return url.toString()
    }

    private fun saveReceipt() {
        container_receipt.visibility = View.INVISIBLE
        container_save_request.visibility = View.VISIBLE
        try {
            val parameters = qrCode.split("&")
            val fp = parameters.filter { it.contains("fp") }[0].split("=")[1]
            val fn = parameters.filter { it.contains("fp") }[0].split("=")[1]
            val i = parameters.filter { it.contains("fp") }[0].split("=")[1]
            val s = parameters.filter { it.contains("s") }[0].split("=")[1]
            val t = parameters.filter { it.contains("t") }[0].split("=")[1]
            qrDate = mapDate(t)
            val createRequest = CreateRequest(fn, fp, i, s, t)
            createCall = App.api.createReceipt(createRequest)
            createCall?.enqueue(object : Callback<CreateResponce> {
                override fun onFailure(call: Call<CreateResponce>, t: Throwable) {
                    showError()
                }

                override fun onResponse(call: Call<CreateResponce>, response: Response<CreateResponce>) {
                    try {
                        val body = response.body()
                        if (body!!.status != "OK")
                            showError()
                        else {
                            val intent = Intent()
                            intent.putExtra(EXTRA_DATE_RECEIPT, qrDate)
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        }
                    } catch (e: Exception) {
                        showError()
                    }
                }

            })
        } catch (e: Exception) {
            showError()
        }
    }

    fun showError() {
        container_receipt.visibility = View.VISIBLE
        container_save_request.visibility = View.GONE
        Toast.makeText(this, "Ошибка", Toast.LENGTH_LONG).show()
    }

    private fun mapDate(dateStr: String): Long {
        val qrCalendar = GregorianCalendar(TimeZone.getDefault())
        val year = dateStr.substring(0, 4).toInt()
        val month = dateStr.substring(4, 6).toInt() - 1
        qrCalendar.set(Calendar.YEAR, year)
        qrCalendar.set(Calendar.MONTH, month)
        qrCalendar.set(Calendar.DAY_OF_MONTH, 1)
        qrCalendar.set(Calendar.HOUR_OF_DAY, 0)
        qrCalendar.set(Calendar.MINUTE, 0)
        qrCalendar.set(Calendar.SECOND, 0)
        qrCalendar.set(Calendar.MILLISECOND, 0)
        return qrCalendar.timeInMillis
    }

    private fun getDateForShare(date: Long): String {
        val strDate = StringBuilder()
        val shareCalendar = GregorianCalendar(TimeZone.getDefault())
        shareCalendar.time = Date(date)
        val year = shareCalendar.get(Calendar.YEAR)
        var month = (shareCalendar.get(Calendar.MONTH) + 1).toString()
        if (month.length == 1) month = "0$month"
        var day = shareCalendar.get(Calendar.DAY_OF_MONTH).toString()
        if (day.length == 1) day = "0$day"
        var hour = shareCalendar.get(Calendar.HOUR_OF_DAY).toString()
        if (hour.length == 1) hour = "0$hour"
        var minutes = shareCalendar.get(Calendar.MINUTE).toString()
        if (minutes.length == 1) minutes = "0$minutes"
        strDate.append(year)
        strDate.append(month)
        strDate.append(day)
        strDate.append("T")
        strDate.append(hour)
        strDate.append(minutes)
        return strDate.toString()
    }

    override fun onDestroy() {
        createCall?.cancel()
        super.onDestroy()
    }
}