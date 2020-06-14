package shiverawe.github.com.receipt.domain.interactor.signup

import shiverawe.github.com.receipt.domain.entity.BaseResult
import shiverawe.github.com.receipt.domain.repository.IAccountRepository

class SignUpInteractor(private val repository: IAccountRepository): ISignUpInteractor {

    override suspend fun signUp(name: String, phone: String, email: String): BaseResult<Boolean> {
        TODO("Not yet implemented")
    }
}