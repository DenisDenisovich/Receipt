package shiverawe.github.com.receipt.data

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class Receipt(val shop: Shop, val items: ArrayList<Product>?)