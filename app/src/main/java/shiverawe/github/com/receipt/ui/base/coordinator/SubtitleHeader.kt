package shiverawe.github.com.receipt.ui.base.coordinator

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.StyleableRes
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.appbar_header.view.*
import shiverawe.github.com.receipt.R
import kotlin.math.abs
import kotlin.math.roundToInt


class SubtitleHeader @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var titleText: String? = null
        set(value) {
            field = value
            title?.text = value
            setTitleStartPosition()
        }
    var subtitleText: String? = null
        set(value) {
            field = value
            subtitle?.text = value
            setSubtitleStartPosition()
        }

    var typeface: Typeface? = null
        set(value) {
            field = value
            title?.typeface = value
            subtitle?.typeface = value
        }
    var titleTypeface: Typeface? = null
        set(value) {
            field = value
            title?.typeface = value
        }
    var subtitleTypeface: Typeface? = null
        set(value) {
            field = value
            subtitle?.typeface = value
        }

    @ColorInt
    var color: Int? = null
        set(value) {
            field = value
            title?.setTextColor(value ?: Color.BLACK)
            subtitle?.setTextColor(value ?: Color.BLACK)
        }
    @ColorInt
    var titleColor: Int? = null
        set(value) {
            field = value
            title?.setTextColor(value ?: Color.BLACK)
        }
    @ColorInt
    var subtitleColor: Int? = null
        set(value) {
            field = value
            subtitle?.setTextColor(value ?: Color.BLACK)
        }

    private val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SubtitleHeader)

    private val isExpanded: Boolean =
        if (typedArray.hasValue(R.styleable.SubtitleHeader_init_state)) {
            typedArray.getInt(
                R.styleable.SubtitleHeader_init_state,
                STATE_EXPANDED
            ) == STATE_EXPANDED
        } else {
            true
        }

    private val marginStartCollapsed: Int =
        toPx(
            R.styleable.SubtitleHeader_margin_start_collapsed,
            MARGIN_START_COLLAPSED
        )

    private val titleTopCollapsed: Int =
        toPx(
            R.styleable.SubtitleHeader_title_margin_top_collapsed,
            TITLE_MARGIN_TOP_COLLAPSED
        )
    private val titleTopExpanded: Int =
        toPx(
            R.styleable.SubtitleHeader_title_margin_top_expanded,
            TITLE_MARGIN_TOP_EXPANDED
        )
    private val titleTopDiff: Int = titleTopExpanded - titleTopCollapsed

    private var titleStartExpanded: Int =
        toPx(
            R.styleable.SubtitleHeader_title_margin_start_expanded,
            0F
        )
    private var titleExpandedCenterHorizontal = titleStartExpanded == 0
    private var titleStartDiff: Int = 0

    private val titleSizeCollapsed: Float =
        toPx(
            R.styleable.SubtitleHeader_title_text_size_collapsed,
            TITLE_TEXT_SIZE_COLLAPSED
        ).toFloat()
    private val titleSizeExpanded: Float =
        toPx(
            R.styleable.SubtitleHeader_title_text_size_expanded,
            TITLE_TEXT_SIZE_EXPANDED
        ).toFloat()
    private val titleSizeDiff: Float = abs(titleSizeCollapsed - titleSizeExpanded)

    private val subtitleTopCollapsed: Int = toPx(
        R.styleable.SubtitleHeader_subtitle_margin_top_collapsed,
        SUBTITLE_MARGIN_TOP_COLLAPSED
    )
    private val subtitleTopExpanded: Int = toPx(
        R.styleable.SubtitleHeader_subtitle_margin_top_expanded,
        SUBTITLE_MARGIN_TOP_EXPANDED
    )
    private val subtitleTopDiff: Int = subtitleTopExpanded - subtitleTopCollapsed

    private var subtitleStartExpanded: Int =
        toPx(
            R.styleable.SubtitleHeader_subtitle_margin_start_expanded,
            0F
        )
    private var subtitleExpandedCenterHorizontal = subtitleStartExpanded == 0
    private var subtitleStartDiff: Int = 0

    private val subtitleSizeCollapsed: Float = toPx(
        R.styleable.SubtitleHeader_subtitle_text_size_collapsed,
        SUBTITLE_TEXT_SIZE_COLLAPSED
    ).toFloat()
    private val subtitleSizeExpanded: Float =
        toPx(
            R.styleable.SubtitleHeader_subtitle_text_size_expanded,
            SUBTITLE_TEXT_SIZE_EXPANDED
        ).toFloat()
    private val subtitleSizeDiff: Float = abs(subtitleSizeExpanded - subtitleSizeCollapsed)

    private val title: TextView?
    private val subtitle: TextView?
    private val titleLayoutParams: LayoutParams
    private val subtitleLayoutParams: LayoutParams

    init {
        LayoutInflater.from(context).inflate(R.layout.appbar_header, this, true)
        title = tv_toolbar_receipt_title
        subtitle = tv_toolbar_receipt_sum
        titleLayoutParams = title.layoutParams as LayoutParams
        subtitleLayoutParams = subtitle.layoutParams as LayoutParams
        if (titleStartExpanded == 0) {
            title.layoutParams = titleLayoutParams.apply {
                gravity = Gravity.CENTER_HORIZONTAL
            }
        }
        if (subtitleStartExpanded == 0) {
            subtitle.layoutParams = subtitleLayoutParams.apply {
                gravity = Gravity.CENTER_HORIZONTAL
            }
        }
        // init text
        title.text =
            titleText ?: getString(R.styleable.SubtitleHeader_title_text) ?: ""
        subtitle.text =
            subtitleText ?: getString(R.styleable.SubtitleHeader_subtitle_text) ?: ""

        // init textSize
        title.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSizeExpanded)
        subtitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, subtitleSizeExpanded)

        // init title typeface
        when {
            titleTypeface != null -> title.typeface = titleTypeface
            typeface != null -> title.typeface = typeface
            else -> getTypeface(R.styleable.SubtitleHeader_title_typeface)?.let { typeface ->
                title.typeface = typeface
            }
        }
        // init subtitle typeface
        when {
            subtitleTypeface != null -> subtitle.typeface = subtitleTypeface
            typeface != null -> subtitle.typeface = typeface
            else -> getTypeface(R.styleable.SubtitleHeader_subtitle_typeface)?.let { typeface ->
                subtitle.typeface = typeface
            }
        }

        // init title color
        when {
            titleColor != null -> titleColor?.let { title.setTextColor(it) }
            color != null -> color?.let { title.setTextColor(it) }
            else -> getColor(R.styleable.SubtitleHeader_title_color).let {
                title.setTextColor(it)
            }
        }
        // init subtitle color
        when {
            subtitleColor != null -> subtitleColor?.let { subtitle.setTextColor(it) }
            color != null -> color?.let { subtitle.setTextColor(it) }
            else -> getColor(R.styleable.SubtitleHeader_subtitle_color).let {
                subtitle.setTextColor(it)
            }
        }

        // init start margin
        if (!title.text.isBlank()) {
            setTitleStartPosition()
        }
        if (!subtitle.text.isBlank()) {
            setSubtitleStartPosition()
        }
        typedArray.recycle()
    }

    fun setAppBarExpandedPercent(percent: Float) {
        val titleMarginStart = getMargin(titleStartExpanded, titleStartDiff, percent)
        val titleMarginTop = getMargin(titleTopExpanded, titleTopDiff, percent)
        title?.layoutParams = titleLayoutParams.apply {
            setMargins(titleMarginStart, titleMarginTop, 0, 0)
        }
        val subtitleMarginStart = getMargin(subtitleStartExpanded, subtitleStartDiff, percent)
        val subtitleMarginTop = getMargin(subtitleTopExpanded, subtitleTopDiff, percent)
        subtitle?.layoutParams = subtitleLayoutParams.apply {
            setMargins(subtitleMarginStart, subtitleMarginTop, 0, 0)
        }

        title?.scaleX = getTextScale(titleSizeExpanded, titleSizeCollapsed, titleSizeDiff, percent)
        title?.scaleY = getTextScale(titleSizeExpanded, titleSizeCollapsed, titleSizeDiff, percent)
        title?.pivotX = 0F
        title?.pivotY = 0F

        subtitle?.scaleX =
            getTextScale(subtitleSizeExpanded, subtitleSizeCollapsed, subtitleSizeDiff, percent)
        subtitle?.scaleY =
            getTextScale(subtitleSizeExpanded, subtitleSizeCollapsed, subtitleSizeDiff, percent)
        subtitle?.pivotX = 0F
        subtitle?.pivotY = 0F
    }

    private fun setTitleStartPosition() {
        title?.post {
            val titleInitTop = if (isExpanded) titleTopExpanded else titleTopCollapsed
            if (titleExpandedCenterHorizontal) {
                titleStartExpanded = width / 2 - title.width / 2
            }
            titleStartDiff = titleStartExpanded - marginStartCollapsed
            title.layoutParams = titleLayoutParams.apply {
                gravity = Gravity.START
                setMargins(titleStartExpanded, titleInitTop, 0, 0)
            }
            if (isExpanded) setAppBarExpandedPercent(0F)
            else setAppBarExpandedPercent(1F)
        }
    }

    private fun setSubtitleStartPosition() {
        subtitle?.post {
            val subtitleInitTop = if (isExpanded) subtitleTopExpanded else subtitleTopCollapsed
            if (subtitleExpandedCenterHorizontal) {
                subtitleStartExpanded = width / 2 - subtitle.width / 2
            }
            subtitleStartDiff = subtitleStartExpanded - marginStartCollapsed
            subtitle.layoutParams = subtitleLayoutParams.apply {
                gravity = Gravity.START
                setMargins(subtitleStartExpanded, subtitleInitTop, 0, 0)
            }
            if (isExpanded) setAppBarExpandedPercent(0F)
            else setAppBarExpandedPercent(1F)
        }
    }

    private fun getMargin(expanded: Int, diff: Int, percent: Float) =
        expanded - (diff * percent).toInt()

    private fun getTextScale(
        expanded: Float,
        collapsed: Float,
        diff: Float,
        percent: Float
    ): Float {
        return when {
            collapsed > expanded -> increaseTextSize(expanded, diff, percent)
            collapsed < expanded -> decreaseTextSize(expanded, diff, percent)
            else -> 1F
        }
    }

    private fun increaseTextSize(expanded: Float, diff: Float, percent: Float) =
        (expanded + diff * percent) / expanded

    private fun decreaseTextSize(expanded: Float, diff: Float, percent: Float) =
        (expanded - diff * percent) / expanded

    private fun toPx(@StyleableRes attr: Int, default: Float): Int {
        val defaultPixelSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, default, resources.displayMetrics
        ).roundToInt()
        return typedArray.getDimensionPixelSize(attr, defaultPixelSize)
    }

    private fun attrIsExist(@StyleableRes attr: Int) = typedArray.hasValue(attr)

    private fun getString(@StyleableRes attr: Int) = typedArray.getString(attr)

    private fun getTypeface(@StyleableRes attr: Int): Typeface? {
        val typefaceStr: String? =
            when {
                attrIsExist(attr) -> getString(attr)
                attrIsExist(R.styleable.SubtitleHeader_typeface) ->
                    getString(R.styleable.SubtitleHeader_typeface)
                else -> null
            }
        var typeface: Typeface? = null
        typefaceStr?.let {
            try {
                val fontRes = resources.getIdentifier(typefaceStr, "font", context.packageName)
                typeface = ResourcesCompat.getFont(context, fontRes)
            } catch (e: Resources.NotFoundException) {
                e.printStackTrace()
            }
        }
        return typeface
    }

    private fun getColor(@StyleableRes attr: Int): Int {
        return when {
            attrIsExist(attr) -> typedArray.getColor(attr, Color.BLACK)
            attrIsExist(R.styleable.SubtitleHeader_color) ->
                typedArray.getColor(R.styleable.SubtitleHeader_color, Color.BLACK)
            else -> Color.BLACK
        }
    }
}

private const val MARGIN_START_COLLAPSED = 72F
private const val TITLE_MARGIN_TOP_COLLAPSED = 4F
private const val TITLE_MARGIN_TOP_EXPANDED = 72F
private const val TITLE_TEXT_SIZE_COLLAPSED = 20F
private const val TITLE_TEXT_SIZE_EXPANDED = 16F

private const val SUBTITLE_MARGIN_TOP_COLLAPSED = 28F
private const val SUBTITLE_MARGIN_TOP_EXPANDED = 112F
private const val SUBTITLE_TEXT_SIZE_COLLAPSED = 14F
private const val SUBTITLE_TEXT_SIZE_EXPANDED = 32f

private const val STATE_EXPANDED = 0