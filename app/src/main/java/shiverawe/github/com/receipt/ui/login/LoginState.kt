package shiverawe.github.com.receipt.ui.login

data class LoginState(
    val phone: String = "",
    val password: String = "",
    val error: Boolean = false,
    val credError: Boolean = false,
    val isSuccess: Boolean = false,
    val inProgress: Boolean = false,
    val restoreInProgress: Boolean = false,
    val restoreIsSuccess: Boolean = false
)