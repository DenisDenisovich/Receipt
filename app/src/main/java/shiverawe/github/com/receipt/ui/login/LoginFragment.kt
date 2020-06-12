package shiverawe.github.com.receipt.ui.login

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.transition.TransitionManager
import kotlinx.android.synthetic.main.fragment_login.btnLogin
import kotlinx.android.synthetic.main.fragment_login.btnResetPassword
import kotlinx.android.synthetic.main.fragment_login.btnSignUp
import kotlinx.android.synthetic.main.fragment_login.errorLogin
import kotlinx.android.synthetic.main.fragment_login.etPassword
import kotlinx.android.synthetic.main.fragment_login.etPhone
import kotlinx.android.synthetic.main.fragment_login.progressLogin
import kotlinx.android.synthetic.main.fragment_login.progressResend
import kotlinx.android.synthetic.main.fragment_login.rootLogin
import org.koin.androidx.viewmodel.ext.android.viewModel
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.ui.login.states.AccountState
import shiverawe.github.com.receipt.ui.login.states.LoginState
import shiverawe.github.com.receipt.utils.addTextListener
import shiverawe.github.com.receipt.utils.gone
import shiverawe.github.com.receipt.utils.invisible
import shiverawe.github.com.receipt.utils.toast
import shiverawe.github.com.receipt.utils.visible

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel: LoginViewModel by viewModel()

    private var incorrectDataVisibility = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        etPhone.addTextListener {
            changeIncorrectDataVisibility(false)
        }

        etPassword.addTextListener {
            changeIncorrectDataVisibility(false)
        }

        etPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login()
            }
            false
        }

        btnResetPassword.setOnClickListener {
            viewModel.resend()
        }

        btnLogin.setOnClickListener {
            login()
        }

        btnSignUp.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, SignUpFragment())?.commit()
        }

        viewModel.getLoginState().observe(viewLifecycleOwner, Observer { handleLoginState(it) })
        viewModel.getResendState().observe(viewLifecycleOwner, Observer { handleResendState(it) })
    }

    private fun login() {
        viewModel.login(etPhone.text.toString(), etPassword.text.toString())
    }

    private fun handleLoginState(loginState: AccountState<LoginState>) {
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

        if (loginState.success.getFirstTime() == true) {
            toast("Success")
        }
        if (loginState.error.getFirstTime() == true) {
            toast("base error")
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
        if (resendState.error.getFirstTime() == true) {
            toast("resend error")
        }
        if (resendState.success.getFirstTime() == true) {
            toast("resend success")
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
}