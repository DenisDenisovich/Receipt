package shiverawe.github.com.receipt.di

import org.koin.dsl.module
import shiverawe.github.com.receipt.data.bd.IReceiptDatabase
import shiverawe.github.com.receipt.data.bd.ReceiptDatabase
import shiverawe.github.com.receipt.data.bd.utils.CacheDiffUtility
import shiverawe.github.com.receipt.data.bd.utils.ICacheDiffUtility
import shiverawe.github.com.receipt.data.network.IMonthNetwork
import shiverawe.github.com.receipt.data.network.IReceiptNetwork
import shiverawe.github.com.receipt.data.network.MonthNetwork
import shiverawe.github.com.receipt.data.network.ReceiptNetwork
import shiverawe.github.com.receipt.data.network.mapper.IMapperNetwork
import shiverawe.github.com.receipt.data.network.mapper.MapperNetwork
import shiverawe.github.com.receipt.data.network.utils.IUtilsNetwork
import shiverawe.github.com.receipt.data.network.utils.UtilsNetwork
import shiverawe.github.com.receipt.data.repository.MonthRepository
import shiverawe.github.com.receipt.data.repository.ReceiptRepository
import shiverawe.github.com.receipt.domain.repository.IMonthRepository
import shiverawe.github.com.receipt.domain.repository.IReceiptRepository
import shiverawe.github.com.receipt.ui.history.month.MonthContract
import shiverawe.github.com.receipt.ui.history.month.MonthPresenter
import shiverawe.github.com.receipt.ui.receipt.ReceiptContact
import shiverawe.github.com.receipt.ui.receipt.ReceiptPresenter

val monthModule = module {
    factory<IMonthNetwork> { MonthNetwork(get()) }
    factory<IMonthRepository> { MonthRepository(get(), get(), get()) }
    factory<MonthContract.Presenter> { (dateFrom: Int) -> MonthPresenter(get(), dateFrom) }
}
val receiptModule = module {
    factory<IReceiptNetwork> { ReceiptNetwork(get()) }
    factory<IReceiptRepository> { ReceiptRepository(get(), get()) }
    factory<ReceiptContact.Presenter> { ReceiptPresenter(get()) }
}
val dbModule = module {
    factory<IReceiptDatabase> { ReceiptDatabase(get()) }
}

val mappersModule = module {
    single<IMapperNetwork> { MapperNetwork() }
}

val utilsModule = module {
    single<ICacheDiffUtility> { CacheDiffUtility() }
    single<IUtilsNetwork> { UtilsNetwork() }
}