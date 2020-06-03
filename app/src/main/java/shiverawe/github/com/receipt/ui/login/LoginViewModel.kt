package shiverawe.github.com.receipt.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class LoginViewModel {

    private val state: MutableLiveData<LoginState> = MutableLiveData()

    fun login(phone: String, password: String) {

    }

    fun restorePassword() {
        if (state.value?.inProgress == true ||
            state.value?.restoreInProgress == true) {
            return
        }

        state.value = LoginState()
    }

    fun getState(): LiveData<LoginState> = state
}