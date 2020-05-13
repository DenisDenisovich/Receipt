package shiverawe.github.com.receipt.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.data.bd.datasource.month.IMonthDatabase
import shiverawe.github.com.receipt.data.bd.datasource.month.MonthDatabase
import shiverawe.github.com.receipt.data.bd.datasource.receipt.IReceiptDatabase
import shiverawe.github.com.receipt.data.bd.datasource.receipt.ReceiptDatabase
import shiverawe.github.com.receipt.data.network.datasource.month.IMonthNetwork
import shiverawe.github.com.receipt.data.network.datasource.receipt.IReceiptNetwork
import shiverawe.github.com.receipt.data.network.datasource.month.MonthNetwork
import shiverawe.github.com.receipt.data.network.datasource.receipt.ReceiptNetwork
import shiverawe.github.com.receipt.data.network.api.createRetrofit
import shiverawe.github.com.receipt.data.repository.MonthRepository
import shiverawe.github.com.receipt.data.repository.ReceiptRepository
import shiverawe.github.com.receipt.domain.interactor.create_receipt.CreateReceiptInteractor
import shiverawe.github.com.receipt.domain.interactor.create_receipt.ICreateReceiptInteractor
import shiverawe.github.com.receipt.domain.interactor.create_receipt.receipt_printer.IReceiptPrinter
import shiverawe.github.com.receipt.domain.interactor.create_receipt.receipt_printer.ShareReceiptPrinter
import shiverawe.github.com.receipt.domain.interactor.month.IMonthInteractor
import shiverawe.github.com.receipt.domain.interactor.month.MonthInteractor
import shiverawe.github.com.receipt.domain.interactor.receipt.IReceiptInteractor
import shiverawe.github.com.receipt.domain.interactor.receipt.ReceiptInteractor
import shiverawe.github.com.receipt.domain.repository.IMonthRepository
import shiverawe.github.com.receipt.domain.repository.IReceiptRepository
import shiverawe.github.com.receipt.ui.history.month.MonthViewModel
import shiverawe.github.com.receipt.ui.receipt.create.CreateReceiptViewModel
import shiverawe.github.com.receipt.ui.receipt.info.ReceiptViewModel

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

val dbModule = module {
    factory<IReceiptDatabase> { ReceiptDatabase() }
    factory<IMonthDatabase> { MonthDatabase() }
}

val networkModule = module {
    single { createRetrofit(androidContext().resources.getString(R.string.BASE_URL)) }
}
