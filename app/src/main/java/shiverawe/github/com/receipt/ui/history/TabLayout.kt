package shiverawe.github.com.receipt.ui.history

import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import shiverawe.github.com.receipt.R

class TabLayout: View {

    val paint = Paint()
    val rect = Rect()
    private var currentMonth = ""
    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) {
        paint.color = Color.WHITE
        paint.isAntiAlias = true
        paint.textSize = context.resources.getDimensionPixelSize(R.dimen.tv_history_month_size).toFloat()
        paint.typeface = ResourcesCompat.getFont(context, R.font.roboto_regular)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        paint.getTextBounds(currentMonth, 0, currentMonth.length, rect)
        canvas?.drawText(currentMonth, (width / 2 - rect.width() / 2).toFloat(), (height / 2 - rect.height() / 2).toFloat(), paint)
    }

    fun setMonth(month: String) {
        currentMonth = month
        invalidate()
    }
}