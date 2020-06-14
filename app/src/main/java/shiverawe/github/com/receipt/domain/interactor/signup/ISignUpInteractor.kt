package shiverawe.github.com.receipt.domain.interactor.signup

import shiverawe.github.com.receipt.domain.entity.BaseResult

interface ISignUpInteractor {

    suspend fun signUp(name: String, phone: String, email: String): BaseResult<Boolean>
}