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
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.ui.Navigation
import shiverawe.github.com.receipt.utils.Storage

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var passwordIsVisible = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tvUserName.text = Storage.userName
        tvPhone.text = Storage.userPhone
        tvEmail.text = Storage.userEmail

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
        val password = Storage.userPassword
        val placeholder = StringBuilder()
        repeat(password.length) { placeholder.append("* ") }
        tvPassword.text = placeholder
        btnShowPassword.setImageResource(R.drawable.ic_show_password)
    }

    private fun showPassword() {
        passwordIsVisible = true
        tvPassword.text = Storage.userPassword
        btnShowPassword.setImageResource(R.drawable.ic_hide_password)
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Выйти из аккаунта?")
            .setPositiveButton("Выйти") { _, _ -> logout() }
            .setNegativeButton("Отмена") { _, _ ->  }
            .show()
    }

    private fun logout() {
        Storage.reset()
        (activity as Navigation).openLogin()
    }

    companion object {
        const val SETTINGS_TAG = "receipt_settings"
    }
}
