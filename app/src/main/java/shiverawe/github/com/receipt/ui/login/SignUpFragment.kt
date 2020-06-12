package shiverawe.github.com.receipt.ui.login

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.transition.TransitionManager
import kotlinx.android.synthetic.main.fragment_signup.btnSingUp
import kotlinx.android.synthetic.main.fragment_signup.errorSignUp
import kotlinx.android.synthetic.main.fragment_signup.etEmail
import kotlinx.android.synthetic.main.fragment_signup.etName
import kotlinx.android.synthetic.main.fragment_signup.etPhone
import kotlinx.android.synthetic.main.fragment_signup.progressSignUp
import kotlinx.android.synthetic.main.fragment_signup.rootSignUp
import org.koin.androidx.viewmodel.ext.android.viewModel
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.ui.login.states.AccountState
import shiverawe.github.com.receipt.ui.login.states.SignUpState
import shiverawe.github.com.receipt.utils.addTextListener
import shiverawe.github.com.receipt.utils.gone
import shiverawe.github.com.receipt.utils.toast
import shiverawe.github.com.receipt.utils.visible

class SignUpFragment : Fragment(R.layout.fragment_signup) {

    private var incorrectDataVisibility = false

    private val viewModel: SignUpViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        etName.addTextListener { changeIncorrectDataVisibility(false) }
        etPhone.addTextListener { changeIncorrectDataVisibility(false) }
        etEmail.addTextListener { changeIncorrectDataVisibility(false) }

        etEmail.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                singUp()
            }
            false
        }

        btnSingUp.setOnClickListener {
            singUp()
        }

        viewModel.getSignUpState().observe(viewLifecycleOwner, Observer { handleSignUpState(it) })
    }

    private fun handleSignUpState(signUpState: AccountState<SignUpState>) {
        if (signUpState.progress) {
            progressSignUp.visible()
            btnSingUp.gone()
        } else {
            progressSignUp.gone()
            btnSingUp.visible()
        }

        if (signUpState.state.error.getFirstTime() == true) {
            changeIncorrectDataVisibility(true)
        }

        if (signUpState.success.getFirstTime() == true) {
            toast("Success")
        }
        if (signUpState.error.getFirstTime() == true) {
            toast("base error")
        }
    }

    private fun singUp() {
        viewModel.signUp(etName.text.toString(), etPhone.text.toString(), etEmail.text.toString())
    }

    private fun changeIncorrectDataVisibility(visible: Boolean) {
        if (visible) {
            if (!incorrectDataVisibility) {
                incorrectDataVisibility = true
                TransitionManager.beginDelayedTransition(rootSignUp)
                errorSignUp.visible()
            }
        } else {
            if (incorrectDataVisibility) {
                incorrectDataVisibility = false
                TransitionManager.beginDelayedTransition(rootSignUp)
                errorSignUp.gone()
            }
        }
    }
}