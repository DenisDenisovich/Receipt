package shiverawe.github.com.receipt.ui.history

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import shiverawe.github.com.receipt.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TabLayout: View {

    val monthPaint = Paint()
    val sumPaint = Paint()
    val rect = Rect()
    private val calendar = GregorianCalendar()
    private val currentDate = GregorianCalendar()
    private val monthArray: ArrayList<String> = arrayListOf("", "", "", "", "")
    private var currentSum = ""
    private val dateFormatterMonth = SimpleDateFormat("LLLL", Locale("ru"))
    private var offset: Float = 0F
    private var monthStep = 0
    private var maxTextSize = context.resources.getDimensionPixelSize(R.dimen.tv_history_month_size_max).toFloat()
    private var minTextSize = context.resources.getDimensionPixelSize(R.dimen.tv_history_month_size_min).toFloat()
    private var deltaTextSize = maxTextSize - minTextSize
    private var centerMonthY = 0
    private var edgeMonthY = 0
    private var sumDisplayY = 0F
    private var deltaMonthY = 0
    private var animation: ValueAnimator? = null

    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) {
        monthPaint.color = Color.WHITE
        monthPaint.isAntiAlias = true
        monthPaint.typeface = ResourcesCompat.getFont(context, R.font.roboto_regular)
        sumPaint.color = Color.WHITE
        sumPaint.isAntiAlias = true
        sumPaint.textSize = context.resources.getDimensionPixelSize(R.dimen.tv_history_sum_size).toFloat()
        sumPaint.typeface = ResourcesCompat.getFont(context, R.font.roboto_regular)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        monthStep = width / 3
        monthPaint.getTextBounds("Тест", 0, 4, rect)
        monthPaint.textSize = maxTextSize
        centerMonthY = height / 2 - rect.height() / 2
        monthPaint.textSize = minTextSize
        monthPaint.getTextBounds("Тест", 0, 4, rect)
        edgeMonthY = height / 2 + rect.height() / 2
        deltaMonthY = edgeMonthY - centerMonthY
        sumDisplayY = height - context.resources.getDimensionPixelSize(R.dimen.tv_history_sum_margin_bottom).toFloat()

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        var displayX: Float
        var displayY: Float
        var textSize: Float
        for (i in 0 until 5) {
            displayX = - monthStep / 2 + monthStep * i + offset * monthStep
            if (displayX > monthStep * 0.5 && displayX < monthStep * 1.5) {
                if (offset > 0) {
                    textSize = (minTextSize + deltaTextSize * offset)
                    displayY = (edgeMonthY - deltaMonthY * offset)
                } else {
                    textSize = (maxTextSize + deltaTextSize * offset)
                    displayY = (centerMonthY - deltaMonthY * offset)
                }
            } else if (displayX > monthStep * 1.5 && displayX < monthStep * 2.5){
                if (offset > 0) {
                    textSize = (maxTextSize - deltaTextSize * offset)
                    displayY = (centerMonthY + deltaMonthY * offset)
                } else {
                    textSize = (minTextSize - deltaTextSize * offset)
                    displayY = (edgeMonthY + deltaMonthY * offset)
                }
            } else if (displayX == monthStep * 1.5.toFloat()){
                textSize = maxTextSize
                displayY = centerMonthY.toFloat()
            } else {
                textSize = minTextSize
                displayY = edgeMonthY.toFloat()
            }
            monthPaint.textSize = textSize
            monthPaint.getTextBounds(monthArray[i], 0, monthArray[i].length, rect)
            displayX -= rect.width() / 2
            canvas?.drawText(monthArray[i], displayX, displayY, monthPaint)
        }
        if (offset == 0F) {
            sumPaint.getTextBounds(currentSum, 0, currentSum.length, rect)
            val sumDisplayX = width / 2 - rect.width() / 2
            canvas?.drawText(currentSum, sumDisplayX.toFloat(), sumDisplayY, sumPaint)
        }
    }

    fun setMonth(month: Date) {
        if (animation?.isRunning == true) return
        currentDate.time = month
        calendar.time = month
        calendar.add(Calendar.MONTH, -2)
        for (i in 0 until 5) {
            monthArray[i] = dateFormatterMonth.format(calendar.time).capitalize()
            calendar.add(Calendar.MONTH, 1)
        }
        currentSum = ""
        offset = 0F
        invalidate()
    }

    fun setSum(sum: String) {
        currentSum = sum
        invalidate()
    }

    fun moveMonth(offset: Float) {
        this.offset = offset
        invalidate()
    }

    fun startEndAnimation(offset: Float, endOffset: Float, moveToRight: Boolean) {
        if (animation?.isRunning == true) return
        animation = ValueAnimator.ofFloat(offset, endOffset).apply {
            addUpdateListener {
                this@TabLayout.offset = it.animatedValue as Float
                invalidate()
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    if (moveToRight) currentDate.add(Calendar.MONTH, -1)
                    else currentDate.add(Calendar.MONTH, 1)
                    setMonth(currentDate.time)
                    super.onAnimationEnd(animation)
                }
            })
            duration = 200
            interpolator = DecelerateInterpolator()
            start()
        }
    }

}