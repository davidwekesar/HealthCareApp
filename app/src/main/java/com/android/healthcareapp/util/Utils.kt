package com.android.healthcareapp.util

import android.text.Editable
import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.*

fun EditText.isTextFieldEmpty(): Boolean {
    val editTextValue = text.toString().trim()
    return editTextValue.isEmpty()
}

fun EditText.getInputValue(): String {
    return text.toString().trim()
}

fun getFormattedDate(unixTime: Long?): String {
    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

    return if (unixTime != null) {
        val date = Date(unixTime)
        sdf.format(date)
    } else {
        sdf.format(System.currentTimeMillis())
    }
}

fun inputMatchesDateFormat(text: Editable?): Boolean {
    val inputValue = text.toString().trim()
    val regex = Regex("^(0?[1-9]|[12][0-9]|3[01])[\\/\\-](0?[1-9]|1[012])[\\/\\-]\\d{4}\$")
    return inputValue.matches(regex)
}

fun String.convertToUnixTime(): Long {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val parsedDate = sdf.parse(this)!!
    return parsedDate.time / 1000
}