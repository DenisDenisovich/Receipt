package shiverawe.github.com.receipt.entity

import shiverawe.github.com.receipt.data.network.entity.receipt.Item

data class Receipt( val shop: Shop, val items: ArrayList<Item>)