package shiverawe.github.com.receipt.ui.history

import android.content.Context
import android.graphics.*
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.view.View
import shiverawe.github.com.receipt.R

class TabLayout: View {

    val centerMonthPaint = Paint()
    val sumPaint = Paint()
    val rect = Rect()
    private var currentMonth = ""
    private var currentSum = ""
    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) {
        centerMonthPaint.color = Color.WHITE
        centerMonthPaint.isAntiAlias = true
        centerMonthPaint.textSize = context.resources.getDimensionPixelSize(R.dimen.tv_history_month_size).toFloat()
        centerMonthPaint.typeface = ResourcesCompat.getFont(context, R.font.roboto_regular)
        sumPaint.color = Color.WHITE
        sumPaint.isAntiAlias = true
        sumPaint.textSize = context.resources.getDimensionPixelSize(R.dimen.tv_history_sum_size).toFloat()
        sumPaint.typeface = ResourcesCompat.getFont(context, R.font.roboto_regular)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        centerMonthPaint.getTextBounds(currentMonth, 0, currentMonth.length, rect)
        canvas?.drawText(currentMonth, (width / 2 - rect.width() / 2).toFloat(), (height / 2 - rect.height() / 2).toFloat(), centerMonthPaint)
        sumPaint.getTextBounds(currentSum, 0, currentSum.length, rect)
        val monthBottom = (height / 2 + rect.height() / 2).toFloat()
        val sumTop = height - (height - monthBottom) / 2 - rect.height() / 2
        canvas?.drawText(currentSum, (width / 2 - rect.width() / 2).toFloat(), sumTop, sumPaint)
    }

    fun setMonth(month: String) {
        currentMonth = month
        currentSum = ""
        invalidate()
    }

    fun setSum(sum: String) {
        currentSum = sum
        invalidate()
    }
}