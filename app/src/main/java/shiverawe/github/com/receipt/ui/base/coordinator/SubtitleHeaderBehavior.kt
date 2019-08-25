package shiverawe.github.com.receipt.ui.base.coordinator

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout

class SubtitleHeaderBehavior @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : CoordinatorLayout.Behavior<SubtitleHeader>(context, attrs) {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: SubtitleHeader,
        dependency: View
    ): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: SubtitleHeader,
        dependency: View
    ): Boolean {
        val behavior = (dependency.layoutParams as CoordinatorLayout.LayoutParams).behavior
        if (behavior is AppBarLayout.Behavior) {
            val maxOffset = dependency.height - dependency.minimumHeight
            child.setAppBarExpandedPercent(-dependency.y / maxOffset)
        }
        return true
    }
}