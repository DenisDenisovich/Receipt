package shiverawe.github.com.receipt.ui.login.states

data class SignUpState(
    val name: String,
    val phone: String,
    val email: String,
    val error: Boolean = false
)