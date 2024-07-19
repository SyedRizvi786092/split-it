package com.project.splitit.util

fun calculateTotalTip(totalBill: Int, tipInFloat: Float): Double {
    return if(totalBill>1 && totalBill.toString().isNotEmpty())
            "%.1f".format(totalBill * tipInFloat).toDouble()
    else 0.0
}

fun calculateTotalPerPerson(totalBill: Int, tipInFloat: Float, splitBy: Int): Double {
    val bill = totalBill + calculateTotalTip(totalBill = totalBill, tipInFloat = tipInFloat)
    return bill/splitBy
}

fun String.isValid(): Boolean {
    val updatedString = this.trim()
    return if (updatedString.isNotEmpty())
        !(updatedString.contains(',') || updatedString.contains('.') || updatedString.contains(' '))
    else false
}