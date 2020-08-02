package shiverawe.github.com.receipt.ui.receipt.create.root

import android.Manifest
import android.os.Bundle
import android.view.View
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.Disposable
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.ui.BackPressedHandle
import shiverawe.github.com.receipt.ui.base.BaseFragment
import shiverawe.github.com.receipt.ui.receipt.create.ManualFragment
import shiverawe.github.com.receipt.ui.receipt.create.QrFragment
import shiverawe.github.com.receipt.ui.receipt.info.ReceiptFragment
import shiverawe.github.com.receipt.utils.toast

class CreateReceiptRootFragment : BaseFragment(R.layout.fragment_create_receipt_root),
    BackPressedHandle, CreateReceiptNavigation {

    private var receiptIsCreated = false

    private val currentScreen: CurrentScreen
        get() = when (childFragmentManager.findFragmentById(R.id.rootCreateReceipt)) {
            is QrFragment -> CurrentScreen.QR
            is ManualFragment -> CurrentScreen.MANUAL
            is ReceiptFragment -> CurrentScreen.RECEIPT
            null -> CurrentScreen.NOTHING
            else -> CurrentScreen.OTHER
        }

    private var cameraPermissionDisposable: Disposable? = null

    override fun quitOnBackPressed(): Boolean {
        if (receiptIsCreated) return true

        if (childFragmentManager.backStackEntryCount > 1) {
            childFragmentManager.popBackStack()
            return false
        }

        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        receiptIsCreated = false
        openQr()
    }

    override fun openQr() {
        if (currentScreen == CurrentScreen.QR) return

        requestCameraPermission(
            onGranted = {
                childFragmentManager.beginTransaction().apply {
                    addToBackStack(CurrentScreen.QR.name)
                    setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    replace(R.id.rootCreateReceipt, QrFragment(), CurrentScreen.QR.name)
                    commit()
                }
            },
            onDenied = {
                openManual()
            }
        )
    }

    override fun openManual() {
        if (currentScreen == CurrentScreen.MANUAL) return

        childFragmentManager.beginTransaction().apply {
            if (currentScreen == CurrentScreen.QR) {
                // ManualFragment isn't first screen. Open with animation
                setCustomAnimations(R.anim.slide_up_alpha, R.anim.fade_out)
                //setCustomAnimations(R.anim.slide_up, R.anim.fade_out, R.anim.fade_in, R.anim.slide_down)
            }
            addToBackStack(CurrentScreen.MANUAL.name)
            replace(R.id.rootCreateReceipt, ManualFragment(), CurrentScreen.MANUAL.name)
            commit()
        }
    }

    override fun goBack() {
        activity?.onBackPressed()
    }

    override fun openReceipt(receiptHeader: ReceiptHeader) {
        if (currentScreen == CurrentScreen.RECEIPT) return

        repeat(childFragmentManager.backStackEntryCount) { childFragmentManager.popBackStack() }

        childFragmentManager.beginTransaction().apply {
            addToBackStack(CurrentScreen.QR.name)
            replace(
                R.id.rootCreateReceipt,
                ReceiptFragment.getNewInstance(receiptHeader),
                CurrentScreen.RECEIPT.name
            )
            commit()
        }
    }

    override fun receiptIsCreated() {
        receiptIsCreated = true
        toast(R.string.create_receipt_success, isLongDuration = false)
        activity?.onBackPressed()
    }

    private fun requestCameraPermission(onGranted: () -> Unit, onDenied: () -> Unit) {
        cameraPermissionDisposable = RxPermissions(this)
            .request(Manifest.permission.CAMERA)
            .subscribe({ isGranted ->
                if (isGranted) {
                    onGranted()
                } else {
                    onDenied()
                }
            }, {
                onDenied()
            })
    }

    private enum class CurrentScreen {
        MANUAL,
        QR,
        RECEIPT,
        NOTHING,
        OTHER
    }
}