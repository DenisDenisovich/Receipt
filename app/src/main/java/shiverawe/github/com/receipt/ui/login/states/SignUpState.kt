package shiverawe.github.com.receipt.ui.login.states

import shiverawe.github.com.receipt.domain.entity.SingleEvent

data class SignUpState(
    val name: String,
    val phone: String,
    val email: String,
    val error: SingleEvent<Boolean> = SingleEvent(false)
)