package shiverawe.github.com.receipt.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_login.btnSignUp
import shiverawe.github.com.receipt.R

class LoginFragment: Fragment(R.layout.fragment_login) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btnSignUp.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, SignUpFragment())?.commit()
        }
    }
}