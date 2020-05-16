package shiverawe.github.com.receipt.ui.loading

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_receipt_loading.btn_back
import kotlinx.android.synthetic.main.fragment_receipt_loading.rv_receipt_loading
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.domain.entity.base.Meta
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.domain.entity.base.ReceiptStatus
import shiverawe.github.com.receipt.domain.entity.base.Shop
import shiverawe.github.com.receipt.utils.toast

class ReceiptLoadingFragment : Fragment(R.layout.fragment_receipt_loading) {

    private val receiptsAdapter = ReceiptLoadingAdapter() {
        toast("Delete: $it")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_back.setOnClickListener { requireActivity().onBackPressed() }
        rv_receipt_loading.adapter = receiptsAdapter
        receiptsAdapter.setReceipts(getReceiptData())
    }

    private fun getReceiptData() = arrayListOf(
        ReceiptHeader(0, ReceiptStatus.FAILED, Shop(23432520, "", "", "2435.54"), Meta(23432520, "", "", "")),
        ReceiptHeader(1, ReceiptStatus.FAILED, Shop(124235523, "", "", "245.54"), Meta(124235523, "", "", "")),
        ReceiptHeader(2, ReceiptStatus.FAILED, Shop(164367457, "", "", "1135.54"), Meta(4363757, "", "", "")),
        ReceiptHeader(3, ReceiptStatus.FAILED, Shop(168576823, "", "", "995.54"), Meta(3425425, "", "", "")),
        ReceiptHeader(4, ReceiptStatus.FAILED, Shop(165374575, "", "", "2345.54"), Meta(23432520, "", "", ""))
    ).apply { sortByDescending { it.shop.date } }

    companion object {

        const val LOADING_TAG = "receiptLoading"
    }
}