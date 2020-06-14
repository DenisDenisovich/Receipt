package shiverawe.github.com.receipt.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import shiverawe.github.com.receipt.domain.entity.ErrorType
import shiverawe.github.com.receipt.domain.entity.SingleEvent
import shiverawe.github.com.receipt.domain.interactor.login.ILoginInteractor
import shiverawe.github.com.receipt.ui.login.states.AccountState
import shiverawe.github.com.receipt.ui.login.states.LoginState

class LoginViewModel(private val interactor: ILoginInteractor) : ViewModel() {

    private val loginState: MutableLiveData<AccountState<LoginState>> = MutableLiveData()
    private val resendState: MutableLiveData<AccountState<Unit>> = MutableLiveData()
    private var currentJob: Job? = null

    fun getLoginState(): LiveData<AccountState<LoginState>> = loginState

    fun getResendState(): LiveData<AccountState<Unit>> = resendState

    fun setSignUpResult(phone: String) {
        loginState.value = AccountState(LoginState(phone, "", fromSignUp = SingleEvent(true)))
    }

    fun onLoginClicked(phone: String, password: String) {
        if (loginState.value?.progress == true || resendState.value?.progress == true) return
        currentJob?.cancel()

        loginState.value = AccountState(state = LoginState(phone, password), progress = true)

        currentJob = viewModelScope.launch { login(phone.replace("-", ""), password) }
    }

    fun onResendClicked(phone: String) {
        if (loginState.value?.progress == true || resendState.value?.progress == true) return
        currentJob?.cancel()

        resendState.value = AccountState(Unit, progress = true)

        currentJob = viewModelScope.launch { resend(phone.replace("-", "")) }
    }

    private suspend fun login(phone: String, password: String) {
        val loginResult = interactor.login(phone, password)

        loginResult.result?.let { success ->
            loginState.value = AccountState(
                state = LoginState(phone, password, error = SingleEvent(!success)),
                success = SingleEvent(success)
            )
        }

        loginResult.error?.let { error ->
            loginState.value = AccountState(
                state = LoginState(phone, password),
                error = SingleEvent<ErrorType?>(error.type)
            )
        }
    }

    private suspend fun resend(phone: String) {
        val resetResult = interactor.resetPassword(phone)

        resetResult.result?.let { success ->
            resendState.value = AccountState(Unit, success = SingleEvent(success))
        }

        resetResult.error?.let { error ->
            resendState.value = AccountState(Unit, error = SingleEvent<ErrorType?>(error.type))
        }
    }
}