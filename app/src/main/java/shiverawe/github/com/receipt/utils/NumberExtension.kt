package shiverawe.github.com.receipt.utils

import java.util.regex.Pattern

private val twoNumbers = Pattern.compile("^\\d+(\\.\\d{0,2})?")
private val threeNumbers = Pattern.compile("^\\d+(\\.\\d{0,3})?")

fun String.floorTwo(): String {
    var newStr = this
    if (newStr.indexOf(".") != -1 &&
        newStr.substring(newStr.indexOf("."), newStr.length).length == 2) {
        newStr += "0"
    }
    return newStr
}

fun Double.floorTwo(): String {
    return twoNumbers.matcher(toString()).run {
        var subPrice = if (find()) {
            group()
        } else "0.00"
        if (subPrice.indexOf(".") != -1 &&
            subPrice.substring(subPrice.indexOf("."), subPrice.length).length == 2) {
            subPrice += "0"
        }
        subPrice
    }
}

fun Double.floorThree(): String {
    return threeNumbers.matcher(toString()).run {
        if (find()) {
            group()
        } else "0.000"
    }
}

