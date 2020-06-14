package shiverawe.github.com.receipt.di

import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.data.bd.datasource.month.IMonthDatabase
import shiverawe.github.com.receipt.data.bd.datasource.month.MonthDatabase
import shiverawe.github.com.receipt.data.bd.datasource.receipt.IReceiptDatabase
import shiverawe.github.com.receipt.data.bd.datasource.receipt.ReceiptDatabase
import shiverawe.github.com.receipt.data.network.api.RetrofitBuilder
import shiverawe.github.com.receipt.data.network.datasource.month.IMonthNetwork
import shiverawe.github.com.receipt.data.network.datasource.receipt.IReceiptNetwork
import shiverawe.github.com.receipt.data.network.datasource.month.MonthNetwork
import shiverawe.github.com.receipt.data.network.datasource.receipt.ReceiptNetwork
import shiverawe.github.com.receipt.data.repository.AccountRepository
import shiverawe.github.com.receipt.data.repository.MonthRepository
import shiverawe.github.com.receipt.data.repository.ReceiptRepository
import shiverawe.github.com.receipt.domain.interactor.create_receipt.CreateReceiptInteractor
import shiverawe.github.com.receipt.domain.interactor.create_receipt.ICreateReceiptInteractor
import shiverawe.github.com.receipt.domain.interactor.create_receipt.receipt_printer.IReceiptPrinter
import shiverawe.github.com.receipt.domain.interactor.create_receipt.receipt_printer.ShareReceiptPrinter
import shiverawe.github.com.receipt.domain.interactor.login.ILoginInteractor
import shiverawe.github.com.receipt.domain.interactor.login.LoginInteractor
import shiverawe.github.com.receipt.domain.interactor.month.IMonthInteractor
import shiverawe.github.com.receipt.domain.interactor.month.MonthInteractor
import shiverawe.github.com.receipt.domain.interactor.receipt.IReceiptInteractor
import shiverawe.github.com.receipt.domain.interactor.receipt.ReceiptInteractor
import shiverawe.github.com.receipt.domain.interactor.signup.ISignUpInteractor
import shiverawe.github.com.receipt.domain.interactor.signup.SignUpInteractor
import shiverawe.github.com.receipt.domain.repository.IAccountRepository
import shiverawe.github.com.receipt.domain.repository.IMonthRepository
import shiverawe.github.com.receipt.domain.repository.IReceiptRepository
import shiverawe.github.com.receipt.ui.history.month.MonthViewModel
import shiverawe.github.com.receipt.ui.login.LoginViewModel
import shiverawe.github.com.receipt.ui.login.SignUpViewModel
import shiverawe.github.com.receipt.ui.receipt.create.CreateReceiptViewModel
import shiverawe.github.com.receipt.ui.receipt.info.ReceiptViewModel
import shiverawe.github.com.receipt.utils.Storage

val monthModule = module {
    factory<IMonthNetwork> { MonthNetwork(get()) }
    factory<IMonthInteractor> { MonthInteractor(get()) }
    factory<IMonthRepository> { MonthRepository(get(), get()) }
    viewModel { MonthViewModel(get()) }
}

val receiptModule = module {
    factory<IReceiptNetwork> { ReceiptNetwork(get()) }
    factory<IReceiptRepository> { ReceiptRepository(get(), get()) }
    factory<ICreateReceiptInteractor> { CreateReceiptInteractor(get()) }
    single<IReceiptPrinter> { ShareReceiptPrinter(androidContext()) }
    factory<IReceiptInteractor> { ReceiptInteractor(get(), get()) }
    viewModel { CreateReceiptViewModel(get()) }
    viewModel { ReceiptViewModel(get()) }

}

val accountModule = module {
    factory<ILoginInteractor> { LoginInteractor(get(), get()) }
    factory<ISignUpInteractor> { SignUpInteractor(get(), get()) }
    factory<IAccountRepository> { AccountRepository(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
}

val dbModule = module {
    single { Storage(androidApplication()) }
    factory<IReceiptDatabase> { ReceiptDatabase() }
    factory<IMonthDatabase> { MonthDatabase() }
}

val networkModule = module {
    single { RetrofitBuilder(androidContext().resources.getString(R.string.BASE_URL), get()).api }
}
