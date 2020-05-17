package shiverawe.github.com.receipt.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.data.bd.datasource.month.IMonthDatabase
import shiverawe.github.com.receipt.data.bd.datasource.month.MonthDatabase
import shiverawe.github.com.receipt.data.bd.datasource.receipt.IReceiptDatabase
import shiverawe.github.com.receipt.data.bd.datasource.receipt.ReceiptDatabase
import shiverawe.github.com.receipt.data.bd.room.ReceiptRoom
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
import shiverawe.github.com.receipt.ui.loading.LoadingReceiptsViewModel
import shiverawe.github.com.receipt.ui.receipt.create.CreateReceiptViewModel
import shiverawe.github.com.receipt.ui.receipt.info.ReceiptViewModel

val networkModule = module {
    single { createRetrofit(androidContext().resources.getString(R.string.BASE_URL)) }
    factory<IMonthNetwork> { MonthNetwork(get()) }
    factory<IReceiptNetwork> { ReceiptNetwork(get()) }
}

val dbModule = module {
    single { ReceiptRoom.getDb() }
    factory<IReceiptDatabase> { ReceiptDatabase(get()) }
    factory<IMonthDatabase> { MonthDatabase(get()) }
}

val repositoryModule = module {
    factory<IMonthRepository> { MonthRepository(get(), get()) }
    factory<IReceiptRepository> { ReceiptRepository(get(), get()) }
}

val interactorModule = module {
    factory<IMonthInteractor> { MonthInteractor(get()) }
    factory<ICreateReceiptInteractor> { CreateReceiptInteractor(get()) }
    factory<IReceiptInteractor> { ReceiptInteractor(get(), get()) }
}

val viewModelModule = module {
    viewModel { MonthViewModel(get()) }
    viewModel { LoadingReceiptsViewModel() }
    viewModel { CreateReceiptViewModel(get()) }
    viewModel { ReceiptViewModel(get()) }
}

val otherModule = module {
    single<IReceiptPrinter> { ShareReceiptPrinter(androidContext()) }
}