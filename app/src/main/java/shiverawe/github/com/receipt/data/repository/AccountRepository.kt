package shiverawe.github.com.receipt.data.repository

import retrofit2.HttpException
import shiverawe.github.com.receipt.data.network.api.Api
import shiverawe.github.com.receipt.data.network.entity.account.LoginRequest
import shiverawe.github.com.receipt.data.network.entity.account.PasswordResetRequest
import shiverawe.github.com.receipt.domain.repository.IAccountRepository

class AccountRepository(private val api: Api) : IAccountRepository {

    override suspend fun login(phone: String, password: String): String =
        api.login(LoginRequest(phone, password))

    override suspend fun resetPassword(phone: String): Boolean {
        val response = api.reset(PasswordResetRequest(phone))
        if (response.code() == 200) {
            return true
        } else {
            throw HttpException(response)
        }
    }

    override suspend fun signUp(name: String, phone: String, email: String): Boolean {
        TODO("Not yet implemented")
    }
}