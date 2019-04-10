package shiverawe.github.com.receipt.ui.settings

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.NavUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_settings.*
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.ui.Navigation
import shiverawe.github.com.receipt.utils.Settings

class SettingsFragment: Fragment() {
    companion object {
        const val SETTINGS_TAG = "receipt_settings"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_menu_settings.setOnClickListener {
            (activity as Navigation).openNavigationDrawable()
        }
        chb_settings_developer.isChecked = Settings.getDevelopMod(context!!)
        chb_settings_developer.setOnClickListener {
                Settings.setDevelopMod(context!!, chb_settings_developer.isChecked)
        }
    }
}
