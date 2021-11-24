package com.android.healthcareapp.models

data class Patient(
    val patientId: Long? = null,
    val registrationDate: Long? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val dateOfBirth: Long? = null,
    val gender: String? = null
)

data class PatientVitals(
    val patientName: String,
    val visitDate: Long,
    val height: Double,
    val weight: Double,
    val bmi: Int
)

data class VisitFormA(
    val patientName: String,
    val visitDate: Long,
    val health: String?,
    val onDiet: String?,
    val comments: String
)

data class VisitFormB(
    val patientName: String,
    val visitDate: Long,
    val health: String?,
    val onDrugs: String?,
    val comments: String
)