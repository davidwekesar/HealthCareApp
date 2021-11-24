package com.android.healthcareapp.util

import android.text.Editable
import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.pow

const val DATABASE_URL =
    "https://health-care-app-4893b-default-rtdb.asia-southeast1.firebasedatabase.app/"

enum class BmiStatus(val status: String) {
    UNDERWEIGHT("Underweight"),
    NORMAL("Normal"),
    OVERWEIGHT("Overweight"),
    OBESE("Obese")
}

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

fun calculateBmi(heightInCm: Double, weight: Double): Int {
    val heightInMetres = heightInCm / 100
    return weight.div(heightInMetres.pow(2)).toInt()
}

fun getBmiStatus(bmi: Int): String {
    return when {
        bmi < 18 -> {
            BmiStatus.UNDERWEIGHT.status
        }
        bmi in 18..24 -> {
            BmiStatus.NORMAL.status
        }
        bmi in 25..29 -> {
            BmiStatus.OVERWEIGHT.status
        }
        else -> {
            BmiStatus.OBESE.status
        }
    }
}

fun calculateAge(dateOfBirth: Long): String {
    val diffInMillis = abs(System.currentTimeMillis() - dateOfBirth * 1000)
    val diff = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS)
    val weeks = diff / 7
    val months = weeks / 4
    val years = months / 12 - 2

    return years.toString()
}