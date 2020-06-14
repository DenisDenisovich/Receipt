package shiverawe.github.com.receipt.domain.interactor.login

import retrofit2.HttpException
import shiverawe.github.com.receipt.domain.entity.BaseResult
import shiverawe.github.com.receipt.domain.interactor.BaseInteractor
import shiverawe.github.com.receipt.domain.repository.IAccountRepository
import shiverawe.github.com.receipt.utils.Storage
import java.lang.Exception

class LoginInteractor(
    private val repository: IAccountRepository,
    private val storage: Storage
) : BaseInteractor(), ILoginInteractor {

    override suspend fun login(phone: String, password: String): BaseResult<Boolean> {
        return try {
            storage.token = repository.login(phone, password)
            storage.userPhone = phone
            storage.userPassword = password
            storage.isLogin = true
            BaseResult(true)
        } catch (e: Exception) {
            if (e is HttpException && e.code() == 403) {
                BaseResult(false)
            } else {
                e.toBaseResult()
            }
        }
    }

    override suspend fun resetPassword(phone: String): BaseResult<Boolean> {
        return try {
            BaseResult(repository.resetPassword(phone))
        } catch (e: Exception) {
            e.toBaseResult()
        }
    }
}