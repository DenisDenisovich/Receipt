package shiverawe.github.com.receipt.ui.settings

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import kotlinx.android.synthetic.main.fragment_settings.btnLogout
import kotlinx.android.synthetic.main.fragment_settings.btnShowPassword
import kotlinx.android.synthetic.main.fragment_settings.tvEmail
import kotlinx.android.synthetic.main.fragment_settings.tvPassword
import kotlinx.android.synthetic.main.fragment_settings.tvPhone
import kotlinx.android.synthetic.main.fragment_settings.tvUserName
import org.koin.android.ext.android.inject
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.ui.Navigation
import shiverawe.github.com.receipt.utils.Storage

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var passwordIsVisible = false
    private val storage: Storage by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tvUserName.text = storage.userName
        tvPhone.text = storage.userPhone
        tvEmail.text = storage.userEmail

        btnShowPassword.setOnClickListener {
            passwordIsVisible = !passwordIsVisible

            if (passwordIsVisible) {
                showPassword()
            } else {
                hidePassword()
            }
        }

        btnLogout.setOnClickListener {
            showLogoutDialog()
        }

        hidePassword()
    }

    private fun hidePassword() {
        passwordIsVisible = false
        val password = storage.userPassword
        val placeholder = StringBuilder()
        repeat(password.length) { placeholder.append("* ") }
        tvPassword.text = placeholder
        btnShowPassword.setImageResource(R.drawable.ic_show_password)
    }

    private fun showPassword() {
        passwordIsVisible = true
        tvPassword.text = storage.userPassword
        btnShowPassword.setImageResource(R.drawable.ic_hide_password)
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.logout_dialog_title)
            .setPositiveButton(R.string.logout) { _, _ -> logout() }
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .show()
    }

    private fun logout() {
        storage.reset()
        (activity as Navigation).openLogin()
    }

    companion object {
        const val SETTINGS_TAG = "receipt_settings"
    }
}
