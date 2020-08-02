package shiverawe.github.com.receipt.ui.base

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

open class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {

    protected var animationIsEnd = false

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        if (nextAnim == 0) {
            animationIsEnd = true
            return null
        }

        return AnimationUtils.loadAnimation(requireContext(), nextAnim).apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    animationIsEnd = true
                    onEndAnimation()
                }

                override fun onAnimationStart(animation: Animation?) {
                    animationIsEnd = false
                }
            })
        }
    }

    open fun onEndAnimation() {}
}