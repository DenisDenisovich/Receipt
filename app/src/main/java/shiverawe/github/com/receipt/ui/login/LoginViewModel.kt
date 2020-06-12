package shiverawe.github.com.receipt.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import shiverawe.github.com.receipt.domain.entity.SingleEvent
import shiverawe.github.com.receipt.ui.login.states.AccountState
import shiverawe.github.com.receipt.ui.login.states.LoginState

class LoginViewModel : ViewModel() {

    private val loginState: MutableLiveData<AccountState<LoginState>> = MutableLiveData()
    private val resendState: MutableLiveData<AccountState<Unit>> = MutableLiveData()
    private var currentJob: Job? = null

    fun getLoginState(): LiveData<AccountState<LoginState>> = loginState

    fun getResendState(): LiveData<AccountState<Unit>> = resendState

    fun login(phone: String, password: String) {
        if (loginState.value?.progress == true || resendState.value?.progress == true) return
        currentJob?.cancel()

        loginState.value = AccountState(
            state = LoginState(phone, password),
            progress = true
        )

        currentJob = viewModelScope.launch {
            delay(1000)

            loginState.value = AccountState(
                state = LoginState(phone, password, error = SingleEvent(true)),
                success = SingleEvent(true)
            )
        }
    }

    fun resend() {
        if (loginState.value?.progress == true || resendState.value?.progress == true) return
        currentJob?.cancel()
        resendState.value = AccountState(Unit, progress = true)

        currentJob = viewModelScope.launch {
            delay(1000)
            resendState.value = AccountState(Unit)
        }
    }

}