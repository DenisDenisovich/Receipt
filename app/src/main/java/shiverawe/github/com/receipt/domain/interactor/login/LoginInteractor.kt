package shiverawe.github.com.receipt.domain.interactor.login

import retrofit2.HttpException
import shiverawe.github.com.receipt.domain.entity.BaseResult
import shiverawe.github.com.receipt.domain.interactor.BaseInteractor
import shiverawe.github.com.receipt.domain.repository.IAccountRepository
import shiverawe.github.com.receipt.utils.Storage
import java.lang.Exception

class LoginInteractor(private val repository: IAccountRepository) : BaseInteractor(), ILoginInteractor {

    override suspend fun login(phone: String, password: String): BaseResult<Boolean> {
        return try {
            val response = repository.login(phone, password)
            return when(response.code()) {
                200 -> {
                    Storage.token = response.body().orEmpty()
                    BaseResult(true)
                }
                403 -> {
                    BaseResult(false)
                }
                else -> {
                    HttpException(response).toBaseResult()
                }
            }
        } catch (e: Exception) {
            e.toBaseResult()
        }
    }

    override suspend fun resetPassword(phone: String): BaseResult<Boolean> {
        TODO("Not yet implemented")
    }
}