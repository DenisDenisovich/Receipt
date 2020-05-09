package shiverawe.github.com.receipt.ui.newreceipt

import android.Manifest
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.Disposable
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.ui.Navigation
import shiverawe.github.com.receipt.ui.BackPressedHandle
import shiverawe.github.com.receipt.ui.receipt.ReceiptFragment
import shiverawe.github.com.receipt.utils.toast

class CreateReceiptRootFragment : Fragment(R.layout.fragment_create_receipt_root), BackPressedHandle {

    private var navigation: Navigation? = null

    private val viewModel: CreateReceiptViewModel by sharedViewModel(from = { this })

    // Handle current screen's states. All navigation commands handled there.
    private val stateObserver = Observer<CreateReceiptUiState> { state ->
        when (state) {
            is QrCodeState -> {
                handleQrCodeState()
            }

            is ManualState -> {
                if (currentScreen != CurrentScreen.MANUAL) {
                    openManualScreen()
                }
            }

            is SuccessState -> {
                toast(R.string.create_receipt_success, isLongDuration = false)
                navigation?.updateHistory(state.date)
            }

            is ShowReceiptState -> {
                    openReceiptScreen(state.receiptHeader)
            }

            is ExitState -> {
                requireActivity().onBackPressed()
            }
        }
    }

    private val currentScreen: CurrentScreen
        get() = when (childFragmentManager.findFragmentById(R.id.root_create_receipt)) {
            is QrFragment -> CurrentScreen.QR
            is ManualFragment -> CurrentScreen.MANUAL
            is ReceiptFragment -> CurrentScreen.RECEIPT
            null -> CurrentScreen.NOTHING
            else -> CurrentScreen.OTHER
        }

    private var cameraPermissionDisposable: Disposable? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigation = context as? Navigation
    }

    override fun onResume() {
        super.onResume()
        viewModel.state.observe(this, stateObserver)
    }

    override fun onPause() {
        super.onPause()
        viewModel.state.removeObserver(stateObserver)
    }

    override fun quitOnBackPressed(): Boolean {
        return when {
            viewModel.state.value is ExitState -> {
                true
            }

            currentScreen == CurrentScreen.RECEIPT -> {
                true
            }

            else -> {
                viewModel.goBack()
                false
            }
        }
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

    private fun handleQrCodeState() {
        when (currentScreen) {

            CurrentScreen.MANUAL -> {
                childFragmentManager.popBackStack()
            }

            CurrentScreen.NOTHING -> {
                requestCameraPermission(
                    onGranted = { openQrScreen() },
                    onDenied = { viewModel.goToManualScreen(isFirstScreen = true) }
                )
            }

            else -> {
            }
        }
    }

    private fun openQrScreen() {
        childFragmentManager.beginTransaction().apply {
            addToBackStack(CurrentScreen.QR.name)
            replace(R.id.root_create_receipt, QrFragment(), CurrentScreen.QR.name)
            commit()
        }
    }

    private fun openManualScreen() {
        childFragmentManager.beginTransaction().apply {
            if (currentScreen == CurrentScreen.QR) {
                // ManualFragment isn't first screen. Open with animation
                setCustomAnimations(
                    R.anim.slide_up,
                    R.anim.fade_out
                )
            }
            addToBackStack(CurrentScreen.MANUAL.name)
            replace(R.id.root_create_receipt, ManualFragment(), CurrentScreen.MANUAL.name)
            commit()
        }
    }

    private fun openReceiptScreen(receiptHeader: ReceiptHeader) {
        childFragmentManager.beginTransaction().apply {
            addToBackStack(CurrentScreen.QR.name)
            replace(
                R.id.root_create_receipt,
                ReceiptFragment.getNewInstance(receiptHeader),
                CurrentScreen.RECEIPT.name
            )
            commit()
        }
    }

    private enum class CurrentScreen {
        MANUAL,
        QR,
        RECEIPT,
        NOTHING,
        OTHER
    }
}