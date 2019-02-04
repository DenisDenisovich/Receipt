package shiverawe.github.com.receipt.ui.history

import android.support.v4.view.ViewPager

class PageChangeListener {

    interface PageListener {
        fun monthIsSelected()
        fun monthIsMoved(offset: Float)
    }

    private var changeDateByCalendar = false
    private var moveToRight = false
    private var previewPosition = 0
    private var offset = 0F
    private var pageIsSelected = false
    private var previewState = 0

    fun addPageListener(vp: ViewPager, listener: PageListener) {
        previewPosition = vp.currentItem
        vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                if (changeDateByCalendar) {
                    if (state == ViewPager.SCROLL_STATE_SETTLING) {
                        listener.monthIsSelected()
                        offset = 0F
                    } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                        changeDateByCalendar = false
                    }
                } else if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    pageIsSelected = false
                    if (previewState != ViewPager.SCROLL_STATE_IDLE) {
                        // when scroll is very fast
                        // end of animation. Update month array in TabLayout
                        listener.monthIsSelected()
                        offset = 0F
                    }
                }
                previewState = state
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, p2: Int) {
                if (changeDateByCalendar) return
                if (!pageIsSelected) {
                    moveToRight = position < vp.currentItem
                    previewPosition = position
                }
                if (positionOffset != 0F) {
                    // animation in progress. Move month text
                    offset = if (moveToRight)
                        1 - positionOffset
                    else
                        -positionOffset
                    listener.monthIsMoved(offset)
                } else {
                    // end of animation. Update month array in TabLayout
                    offset = 0F
                    listener.monthIsSelected()
                }
            }

            override fun onPageSelected(position: Int) {
                if (changeDateByCalendar) return
                pageIsSelected = true
                if (offset == 0F) {
                    // if page selected by currentItem without swipe
                    moveToRight = previewPosition > position
                    previewPosition = position
                }
            }
        })
    }

    fun dateChangedByCalendar() {
        changeDateByCalendar = true
    }

}