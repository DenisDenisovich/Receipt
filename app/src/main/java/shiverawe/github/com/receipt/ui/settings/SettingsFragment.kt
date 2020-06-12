package shiverawe.github.com.receipt.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import shiverawe.github.com.receipt.R

class SettingsFragment : Fragment() {
    companion object {
        const val SETTINGS_TAG = "receipt_settings"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }
}
