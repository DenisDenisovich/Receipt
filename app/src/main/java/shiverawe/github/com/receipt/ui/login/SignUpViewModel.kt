package shiverawe.github.com.receipt.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import shiverawe.github.com.receipt.domain.entity.ErrorType
import shiverawe.github.com.receipt.domain.entity.SingleEvent
import shiverawe.github.com.receipt.domain.interactor.signup.ISignUpInteractor
import shiverawe.github.com.receipt.ui.login.states.AccountState
import shiverawe.github.com.receipt.ui.login.states.SignUpState

class SignUpViewModel(private val interactor: ISignUpInteractor) : ViewModel() {

    private var currentJob: Job? = null

    private val signUpState: MutableLiveData<AccountState<SignUpState>> = MutableLiveData()

    fun getSignUpState(): LiveData<AccountState<SignUpState>> = signUpState

    fun onSignUpClicked(name: String, phone: String, email: String) {
        if (signUpState.value?.progress == true) return
        currentJob?.cancel()

        // set progress to state
        signUpState.value = AccountState(state = SignUpState(name, phone, email), progress = true)

        currentJob = viewModelScope.launch {
            signUp(name, phone.replace("-", ""), email)
        }
    }

    private suspend fun signUp(name: String, phone: String, email: String) {
        // sign up
        val signUpResult = interactor.signUp(name, phone, email)
        // handle success result
        signUpResult.result?.let { success ->
            signUpState.value = AccountState(
                state = SignUpState(name, phone, email),
                success = SingleEvent(success)
            )
        }
        // handle error result
        signUpResult.error?.let { error ->
            signUpState.value = AccountState(
                state = SignUpState(name, phone, email),
                error = SingleEvent<ErrorType?>(error.type)
            )
        }
    }
}