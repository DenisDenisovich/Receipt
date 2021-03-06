package shiverawe.github.com.receipt.domain.interactor.signup

import shiverawe.github.com.receipt.domain.entity.BaseResult
import shiverawe.github.com.receipt.domain.interactor.BaseInteractor
import shiverawe.github.com.receipt.domain.repository.IAccountRepository
import shiverawe.github.com.receipt.utils.Storage

class SignUpInteractor(
    private val repository: IAccountRepository,
    private val storage: Storage
): BaseInteractor(), ISignUpInteractor {

    override suspend fun signUp(name: String, phone: String, email: String): BaseResult<Boolean> {
        return try {
            val signUpResult = repository.signUp(name, phone, email)
            if (signUpResult) {
                storage.userName = name
                storage.userPhone = phone
                storage.userEmail = email
            }
            BaseResult(signUpResult)
        } catch (e: Exception) {
            e.toBaseResult()
        }
    }
}