package shiverawe.github.com.receipt.ui.loading

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_receipt_loading.btn_back
import kotlinx.android.synthetic.main.fragment_receipt_loading.progress
import kotlinx.android.synthetic.main.fragment_receipt_loading.rv_receipt_loading
import kotlinx.android.synthetic.main.fragment_receipt_loading.tv_error
import org.koin.androidx.viewmodel.ext.android.viewModel
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.domain.entity.ErrorType
import shiverawe.github.com.receipt.utils.gone
import shiverawe.github.com.receipt.utils.toast
import shiverawe.github.com.receipt.utils.visible

class ReceiptLoadingFragment : Fragment(R.layout.fragment_receipt_loading) {

    private val viewModel: LoadingReceiptsViewModel by viewModel()
    private val receiptsAdapter = ReceiptLoadingAdapter() {
        toast("Delete: $it")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_back.setOnClickListener { requireActivity().onBackPressed() }
        rv_receipt_loading.adapter = receiptsAdapter
        viewModel.getState().observe(this, Observer { handleState(it) })
        if (viewModel.getState().value == null) {
            viewModel.loadedReceipts()
        }
    }

    private fun handleState(state: LoadingReceiptUiState) {
        receiptsAdapter.setReceipts(state.receipts)
        if (state.inProgress) {
            rv_receipt_loading.gone()
            progress.visible()
        } else {
            rv_receipt_loading.visible()
            progress.gone()
        }
        state.error?.let { error ->
            tv_error.text = when(error) {
                ErrorType.ERROR -> getString(R.string.error)
                ErrorType.OFFLINE -> getString(R.string.error_network)
            }
        }
    }


    companion object {

        const val LOADING_TAG = "receiptLoading"
    }
}