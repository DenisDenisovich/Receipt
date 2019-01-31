package shiverawe.github.com.receipt.ui.receipt.network.datainput

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import shiverawe.github.com.receipt.R

class ManualInputFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_manual_input_receipt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }
}