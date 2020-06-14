package shiverawe.github.com.receipt.domain.repository

interface IAccountRepository {

    suspend fun login(phone: String, password: String): String

    suspend fun resetPassword(phone: String): Boolean

    suspend fun signUp(name: String, phone: String, email: String): Boolean
}