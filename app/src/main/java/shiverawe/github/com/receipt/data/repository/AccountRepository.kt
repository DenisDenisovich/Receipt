package shiverawe.github.com.receipt.data.repository

import retrofit2.Response
import shiverawe.github.com.receipt.data.network.api.Api
import shiverawe.github.com.receipt.data.network.entity.account.LoginRequest
import shiverawe.github.com.receipt.domain.repository.IAccountRepository

class AccountRepository(private val api: Api) : IAccountRepository {

    override suspend fun login(phone: String, password: String): Response<String> =
        api.login(LoginRequest(phone, password))

    override suspend fun resetPassword(phone: String): Response<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun signUp(name: String, phone: String, email: String): Response<Unit> {
        TODO("Not yet implemented")
    }
}