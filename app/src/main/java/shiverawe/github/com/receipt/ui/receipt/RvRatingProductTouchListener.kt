package shiverawe.github.com.receipt.ui.receipt

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import shiverawe.github.com.receipt.R

class RvRatingProductTouchListener(private val recyclerView: RecyclerView, val startAnimation: () -> Unit) : View.OnTouchListener {
    var toolbarAnimationInProgress = false
    var toolbarIsExpanded = true
    private var startY = 0F
    private val moveDistance = recyclerView.context.resources.getDimensionPixelSize(R.dimen.open_toolbar_product_min_swipe)
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when {
            event!!.action == MotionEvent.ACTION_DOWN -> {
                startY = event.y
            }
            event.action == MotionEvent.ACTION_MOVE -> {
                if (toolbarIsExpanded) {
                    return true
                } else if (startY - event.y < 0){
                    val firstPosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                    if (firstPosition == 0 || firstPosition == -1) {
                        return true
                    }
                }
            }
            event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL -> {
                if (startY - event.y > moveDistance) {
                    if (toolbarIsExpanded && !toolbarAnimationInProgress) {
                        toolbarIsExpanded = false
                        startAnimation()
                    }
                } else if (event.y - startY > moveDistance){
                    if (!toolbarIsExpanded && !toolbarAnimationInProgress) {
                        val firstPosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                        if (firstPosition == 0 || firstPosition == -1) {
                            toolbarIsExpanded = true
                            startAnimation()
                        }
                    }
                }
            }
        }
        return toolbarAnimationInProgress
    }
}