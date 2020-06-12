package shiverawe.github.com.receipt.ui.login.states

import shiverawe.github.com.receipt.domain.entity.SingleEvent

data class AccountState<T>(
    val state: T,
    val error: SingleEvent<Boolean> = SingleEvent(false),
    val success: SingleEvent<Boolean> = SingleEvent(false),
    val progress: Boolean = false
)