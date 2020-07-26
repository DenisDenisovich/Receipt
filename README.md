# Receipt

[![TeamcityBuild](https://teamcity.shefer.space/app/rest/builds/strob:(buildType:(project:(id:ReceiptAndroid)))/statusIcon.svg)](https://teamcity.shefer.space/viewType.html?buildTypeId=ReceiptAndroid_Build)
[![Gitter](https://badges.gitter.im/receipt-project/receipt-android.svg)](https://gitter.im/receipt-project/receipt-android?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

[Download APK](https://receipt.shefer.space/android/distributions/)

## Application architecture
- Clean Architecture
- MVVM
## Used technologies
- Kotlin
  - Coroutines
- [Android Jetpack](https://developer.android.com/jetpack)
  - ViewModel
  - LiveData
  - Room
  - CameraX
- [Koin](https://insert-koin.io/) - Service Locator

## Application UI. Экраны приложения.

### Purchases info. Информация о покупках
#### Purchases list for a month. Список покупок за месяц.
<img src="https://psv4.userapi.com/c856320/u58821353/docs/d6/d0f97cefe870/photo_2020-07-26_15-28-30.jpg?extra=opiL64FpocC_qndXrgNNPAC_MPyz2CBFdlKQZNqhe0pk1H3OtBEVQder-QRMH5gB71N0WJ9buzfXVz81e9DcsBLBWeMaQdmHyt1A2aOJwlvCE2-NbgAtdsdGF0wLT1xHlH2iL4PzigqSoN-qPONL" alt="drawing" width="400"/>

#### Receipt. Чек.
<img src="https://psv4.userapi.com/c856320/u58821353/docs/d14/dee332688e7f/photo_2020-07-26_15-28-29_2.jpg?extra=lkTsbit8Ii0oks-rmwijtn7R95Vo4D28TJoLN1Ggnwki5NMShBQi37q4FWtcYu0_kvWm_Yp8lBMTgSdCSQgYqGnL51iAeVvxqhFVrm4J5qwlPs9XeEBWtijLkuQGGa1IK9YLjS_WMdVXfzdJ2dVF" alt="drawing" width="400"/>

### Add new receipt to history. Добавить новый чек в историю
#### Receipt scanning. Сканирование чека.
<img src="https://psv4.userapi.com/c856320/u58821353/docs/d16/28938548aba2/photo_2020-07-26_17-20-25.jpg?extra=jyQMplcoIY4G7uG-76WrMeu5jDPLMgrVSn2cJtBxVjDqDYt1SEfZJa1U9Rs__wsJ5T5r2yPJMoSwBstKYlBrT35XUXVPFyJpjchTdrbrn7z-aUKJqbbRar2FJoJUlAcjFQvvp0xOqaZVeDk68w6V" alt="drawing" width="400"/>

#### Manual entry of a receipt. Ручной ввод чека
<img src="https://psv4.userapi.com/c856320/u58821353/docs/d10/5b1058d84ffc/photo_2020-07-26_17-24-21.jpg?extra=dwjaQZa4M6FxvmRri-lvAKK6b95BeweI2aMWh41DbePZPrwN6RcRPITQCWKkNEgL3xmCAuIaKHogPi0aB1c12zus0h39-7yDwFhyv3qVKZ9Y4l6SIT1q_IeVGHYkvgmIC49qLnRm8BqEtShzeeLA" alt="drawing" width="400"/>

### Login/SignUp. Логин/Регистрация
#### Login. Логин
<img src="https://psv4.userapi.com/c856320/u58821353/docs/d18/142af6821ea6/photo_2020-07-26_15-28-38.jpg?extra=91gWPYnUfV1fFPSonN9k2RjGt1fFQtToUwN-K9wRoECSlzC7ryt8Q9XuywEY3GfLH_rkz-pZrs8XgMkcnXLiszrzmFkSjlra9kA5PSuRMt-1A_zy3k99Xoyd-hD71bUuh2Hrcrkg1E6ZTrUSyoeW" alt="drawing" width="400"/>

#### SignUp. Регистрация
<img src="https://psv4.userapi.com/c856320/u58821353/docs/d16/6ecbe7de0db7/photo_2020-07-26_15-28-29.jpg?extra=egg7jUD-1i3Tu7qGw_ZImgVNkCY3nIOJlb7kyAUyQ8-CPM99TcP_H5LcR4yrVJDCsMfzywBHO2mYN3tqW4EAGbd6pkWBTQRM7wqByUZ2wuMUPidixp4IFDwhVgFVXO7CWGX6w-nDnRL6rYI0XULa" alt="drawing" width="400"/>
