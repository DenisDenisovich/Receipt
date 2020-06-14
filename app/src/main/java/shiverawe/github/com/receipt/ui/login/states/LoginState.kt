package shiverawe.github.com.receipt.ui.login.states

import shiverawe.github.com.receipt.domain.entity.SingleEvent

data class LoginState(
    val phone: String = "",
    val password: String = "",
    val fromSignUp: SingleEvent<Boolean> = SingleEvent(false),
    val error: SingleEvent<Boolean> = SingleEvent(false)
)