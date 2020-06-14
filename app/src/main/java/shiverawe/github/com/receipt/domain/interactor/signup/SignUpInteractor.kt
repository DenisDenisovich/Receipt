package shiverawe.github.com.receipt.domain.interactor.signup

import shiverawe.github.com.receipt.domain.entity.BaseResult
import shiverawe.github.com.receipt.domain.interactor.BaseInteractor
import shiverawe.github.com.receipt.domain.repository.IAccountRepository

class SignUpInteractor(private val repository: IAccountRepository): BaseInteractor(), ISignUpInteractor {

    override suspend fun signUp(name: String, phone: String, email: String): BaseResult<Boolean> {
        return try {
            BaseResult(repository.signUp(name, phone, email))
        } catch (e: Exception) {
            e.toBaseResult()
        }
    }
}