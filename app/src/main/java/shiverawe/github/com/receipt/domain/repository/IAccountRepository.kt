package shiverawe.github.com.receipt.domain.repository

import retrofit2.Response
import shiverawe.github.com.receipt.domain.entity.BaseResult

interface IAccountRepository {

    suspend fun login(phone: String, password: String): Response<String>

    suspend fun resetPassword(phone: String): Response<Unit>

    suspend fun signUp(name: String, phone: String, email: String): Response<Unit>
}