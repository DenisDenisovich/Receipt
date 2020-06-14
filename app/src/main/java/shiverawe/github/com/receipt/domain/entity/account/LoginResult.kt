package shiverawe.github.com.receipt.domain.entity.account

data class LoginResult(val token: String = "", val dataIsInvalid: Boolean = false, val error: Boolean)