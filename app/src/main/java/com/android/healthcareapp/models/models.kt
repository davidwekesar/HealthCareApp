package com.android.healthcareapp.models

data class Patient(
    val patientId: Long? = null,
    val registrationDate: Long? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val dateOfBirth: Long? = null,
    val gender: String? = null
)