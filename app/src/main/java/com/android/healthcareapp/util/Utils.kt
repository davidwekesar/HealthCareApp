package com.android.healthcareapp.util

import android.widget.EditText

fun EditText.isTextFieldEmpty(): Boolean {
    val editTextValue = text.toString().trim()
    return editTextValue.isEmpty()
}