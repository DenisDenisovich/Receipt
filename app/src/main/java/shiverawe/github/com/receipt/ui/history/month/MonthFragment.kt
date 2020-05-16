package shiverawe.github.com.receipt.ui.history.month

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_month.pb_month
import kotlinx.android.synthetic.main.fragment_month.rv_month
import kotlinx.android.synthetic.main.fragment_month.swipe_refresh_layout
import kotlinx.android.synthetic.main.fragment_month.tv_month_error_message
import org.koin.androidx.viewmodel.ext.android.viewModel
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.ui.MainActivity
import shiverawe.github.com.receipt.ui.Navigation
import shiverawe.github.com.receipt.ui.history.HistoryFragment
import shiverawe.github.com.receipt.ui.history.month.adapter.MonthAdapter
import shiverawe.github.com.receipt.utils.gone
import shiverawe.github.com.receipt.utils.toast
import shiverawe.github.com.receipt.utils.visible

class MonthFragment : Fragment(R.layout.fragment_month) {

    private val viewModel: MonthViewModel by viewModel()

    private lateinit var navigation: Navigation
    private lateinit var adapter: MonthAdapter
    private val monthDate: Long by lazy { arguments?.getLong(DATE_KEY) ?: 0L }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigation = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = MonthAdapter { receipt -> navigation.openReceipt(receipt.receiptId) }
        rv_month.adapter = adapter

        swipe_refresh_layout.setOnRefreshListener {
            viewModel.loadReceipts(monthDate, isRefresh = true)
        }
        viewModel.loadReceipts(monthDate)
        viewModel.state.observe(this, Observer { handleMonthState(it)})
    }

    private fun handleMonthState(state: MonthUiState) {
        setReceipts(ArrayList(state.receipts))
        setSum(state.sum)

        if (state.inProgress) {
            pb_month.visible()
        } else {
            pb_month.gone()
        }

        setMessage(state.message)
    }

    private fun setReceipts(items: ArrayList<ReceiptHeader>) {
        adapter.setItems(items)
        rv_month.visible()

        if (items.isEmpty()) {
            tv_month_error_message.visible()
            tv_month_error_message.text = getString(R.string.no_data)
        } else {
            tv_month_error_message.gone()
        }

        swipe_refresh_layout.isRefreshing = false
    }

    private fun setMessage(messageType: MessageType?) {
        if (messageType == null) {
            tv_month_error_message.gone()
            return
        }

        val message = when (messageType) {
            MessageType.OFFLINE -> R.string.error_network
            MessageType.ERROR -> R.string.error
            MessageType.EMPTY_LIST -> R.string.no_data
        }

        pb_month.gone()

        if (adapter.itemCount == 0) {
            tv_month_error_message.text = getString(message)
            tv_month_error_message.visible()
        } else {
            toast(message, isLongDuration = false)
        }

        swipe_refresh_layout.isRefreshing = false
    }

    private fun setSum(sum: String) {
        (parentFragment as? HistoryFragment)?.setSum(sum)
    }

    companion object {
        private const val DATE_KEY = "dateKey"
        private const val POSITION_KEY = "positionKey"

        fun getNewInstance(date: Long, position: Int): MonthFragment =
            MonthFragment().apply {
                arguments = Bundle().apply {
                    putLong(DATE_KEY, date)
                    putInt(POSITION_KEY, position)
                }
            }
    }
}