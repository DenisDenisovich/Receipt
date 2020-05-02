package shiverawe.github.com.receipt.ui.newreceipt

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.ui.Navigation

private const val QR_CODE_TAG = "QrCode"
private const val MANUAL_TAG = "Manual"

class CreateReceiptRootFragment : Fragment(R.layout.fragment_create_receipt_root) {

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
                        childFragmentManager.beginTransaction().apply {
                            replace(R.id.root_create_receipt, QrFragment(), QR_CODE_TAG)
                            commit()
                        }
                    }
                }
            }
            is ManualState -> {
                if (findFragment(MANUAL_TAG) != null) {
                    // current fragment is ManualFragment, do nothing
                    return@Observer
                } else if (findFragment(QR_CODE_TAG) != null) {
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
            }
            is SuccessState -> {
                (activity as? Navigation)?.updateHistory(it.date)
            }
        }
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
}