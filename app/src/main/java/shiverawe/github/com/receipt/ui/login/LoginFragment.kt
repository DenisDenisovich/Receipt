package shiverawe.github.com.receipt.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.transition.TransitionManager
import kotlinx.android.synthetic.main.fragment_login.btnLogin
import kotlinx.android.synthetic.main.fragment_login.btnResetPassword
import kotlinx.android.synthetic.main.fragment_login.btnShowPassword
import kotlinx.android.synthetic.main.fragment_login.btnSignUp
import kotlinx.android.synthetic.main.fragment_login.errorLogin
import kotlinx.android.synthetic.main.fragment_login.etPassword
import kotlinx.android.synthetic.main.fragment_login.etPhone
import kotlinx.android.synthetic.main.fragment_login.progressLogin
import kotlinx.android.synthetic.main.fragment_login.progressResend
import kotlinx.android.synthetic.main.fragment_login.rootLogin
import org.koin.androidx.viewmodel.ext.android.viewModel
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.domain.entity.ErrorType
import shiverawe.github.com.receipt.ui.Navigation
import shiverawe.github.com.receipt.ui.login.states.AccountState
import shiverawe.github.com.receipt.ui.login.states.LoginState
import shiverawe.github.com.receipt.utils.addTextListener
import shiverawe.github.com.receipt.utils.gone
import shiverawe.github.com.receipt.utils.hideKeyboard
import shiverawe.github.com.receipt.utils.invisible
import shiverawe.github.com.receipt.utils.showKeyboard
import shiverawe.github.com.receipt.utils.toast
import shiverawe.github.com.receipt.utils.visible

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel: LoginViewModel by viewModel()

    private var incorrectDataVisibility = false
    private var passwordIsVisible = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        etPhone.addTextChangedListener(PhoneNumberListener { formattedNumber ->
            etPhone.setText(formattedNumber)
            etPhone.setSelection(formattedNumber.length)
            changeIncorrectDataVisibility(false)
        })

        etPhone.setText("+7")

        etPassword.addTextListener { changeIncorrectDataVisibility(false) }

        etPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login()
            }
            false
        }

        btnShowPassword.setOnClickListener {
            passwordIsVisible = !passwordIsVisible
            if (passwordIsVisible) showPassword()
            else hidePassword()
        }

        btnResetPassword.setOnClickListener {
            etPhone.hideKeyboard()
            etPassword.hideKeyboard()
            viewModel.onResendClicked(etPhone.text.toString())
        }

        btnLogin.setOnClickListener {
            login()
        }

        btnSignUp.setOnClickListener {
            val signUpScreen = SignUpFragment()
            signUpScreen.setTargetFragment(this, LOGIN_REQUEST_CODE)
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.container, signUpScreen)
                ?.addToBackStack(null)
                ?.commit()
        }

        viewModel.getLoginState().observe(viewLifecycleOwner, Observer { handleLoginState(it) })
        viewModel.getResendState().observe(viewLifecycleOwner, Observer { handleResendState(it) })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == LOGIN_REQUEST_CODE) {
            val phoneNumber = data?.getStringExtra(SignUpFragment.PHONE_NUMBER_EXTRA).orEmpty()
            viewModel.setSignUpResult(phoneNumber)
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun login() {
        etPhone.hideKeyboard()
        etPassword.hideKeyboard()
        viewModel.onLoginClicked(etPhone.text.toString(), etPassword.text.toString())
    }

    private fun handleLoginState(loginState: AccountState<LoginState>) {
        if (loginState.success.getFirstTime() == true) {
            (requireActivity() as Navigation).openHistory()
            return
        }

        if (loginState.state.fromSignUp.getFirstTime() == true) {
            val phoneNumber = loginState.state.phone
            etPhone.setText(phoneNumber)
            etPhone.setSelection(phoneNumber.length)
            etPassword.setText("")
            toast(getString(R.string.input_password_from_sms))
            etPassword?.showKeyboard()
        }

        if (loginState.progress) {
            progressLogin.visible()
            btnLogin.gone()
        } else {
            progressLogin.gone()
            btnLogin.visible()
        }

        if (loginState.state.error.getFirstTime() == true) {
            changeIncorrectDataVisibility(true)
        }

        val errorType = loginState.error.getFirstTime()

        if (errorType == ErrorType.ERROR) {
            toast(R.string.error)
        } else if (errorType == ErrorType.OFFLINE) {
            toast(R.string.error_network)
        }
    }

    private fun handleResendState(resendState: AccountState<Unit>) {
        if (resendState.progress) {
            btnResetPassword.invisible()
            progressResend.visible()
        } else {
            btnResetPassword.visible()
            progressResend.gone()
        }

        if (resendState.success.getFirstTime() == true) {
            toast(R.string.input_new_password_from_sms)
        }

        val errorType = resendState.error.getFirstTime()

        if (errorType == ErrorType.ERROR) {
            toast(R.string.error)
        } else if (errorType == ErrorType.OFFLINE) {
            toast(R.string.error_network)
        }
    }

    private fun changeIncorrectDataVisibility(visible: Boolean) {
        if (visible) {
            if (!incorrectDataVisibility) {
                incorrectDataVisibility = true
                TransitionManager.beginDelayedTransition(rootLogin)
                errorLogin.visible()
            }
        } else {
            if (incorrectDataVisibility) {
                incorrectDataVisibility = false
                TransitionManager.beginDelayedTransition(rootLogin)
                errorLogin.gone()
            }
        }
    }

    private fun hidePassword() {
        passwordIsVisible = false
        etPassword.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        etPassword.setSelection(etPassword.text?.length ?: 0)
        btnShowPassword.setImageResource(R.drawable.ic_show_password)
    }

    private fun showPassword() {
        passwordIsVisible = true
        etPassword.inputType = EditorInfo.TYPE_CLASS_NUMBER
        etPassword.setSelection(etPassword.text?.length ?: 0)
        btnShowPassword.setImageResource(R.drawable.ic_hide_password)
    }

    companion object {
        private const val LOGIN_REQUEST_CODE = 123
    }
}