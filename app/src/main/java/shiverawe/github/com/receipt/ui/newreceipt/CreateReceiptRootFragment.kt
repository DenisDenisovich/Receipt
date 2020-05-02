package shiverawe.github.com.receipt.ui.newreceipt

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.Disposable
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.ui.Navigation

private const val QR_CODE_TAG = "QrCode"
private const val MANUAL_TAG = "Manual"

class CreateReceiptRootFragment : Fragment(R.layout.fragment_create_receipt_root) {

    private var navigation: Navigation? = null

    private val viewModel: CreateReceiptViewModel by sharedViewModel()

    private val stateObserver = Observer<CreateReceiptState> {
        when (it) {
            is QrCodeState -> {
                when {
                    // current fragment is QrFragment, do nothing
                    findFragment(QR_CODE_TAG) != null -> {
                        return@Observer
                    }
                    // current fragment is ManualFragment, return to QrFragment
                    findFragment(MANUAL_TAG) != null -> {
                        childFragmentManager.popBackStack()
                    }
                    // root doesn't have current fragment. Set QrFragment as first fragment
                    findFragment(MANUAL_TAG) == null -> {
                        requestCameraPermission(
                            onGranted = {
                                childFragmentManager.beginTransaction().apply {
                                    replace(R.id.root_create_receipt, QrFragment(), QR_CODE_TAG)
                                    commit()
                                }
                            },
                            onDenied = {
                                viewModel.goToManualScreen()
                            }
                        )
                    }
                }
            }
            is ManualState -> {
                when {
                    findFragment(MANUAL_TAG) != null -> {
                        // current fragment is ManualFragment, do nothing
                        return@Observer
                    }
                    findFragment(QR_CODE_TAG) != null -> {
                        // current fragment is QrFragment, go to ManualFragment
                        childFragmentManager.beginTransaction().apply {
                            setCustomAnimations(
                                R.anim.slide_up,
                                R.anim.fade_out
                            )
                            replace(R.id.root_create_receipt, ManualFragment(), MANUAL_TAG)
                            commit()
                        }
                    }
                    findFragment(MANUAL_TAG) == null -> {
                        // root doesn't have current fragment. Set ManualFragment as first fragment
                        childFragmentManager.beginTransaction().apply {
                            replace(R.id.root_create_receipt, ManualFragment(), MANUAL_TAG)
                            commit()
                        }
                    }
                }
            }
            is SuccessState -> {
                navigation?.updateHistory(it.date)
            }
            is ExitState -> {
                navigation?.openHistory()
            }
        }
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

    private fun findFragment(tag: String) = childFragmentManager.findFragmentByTag(tag)

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
}