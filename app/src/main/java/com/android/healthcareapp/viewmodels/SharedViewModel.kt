package com.android.healthcareapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.healthcareapp.models.Patient
import com.android.healthcareapp.models.PatientVitals

class SharedViewModel: ViewModel() {

    val registrationInfo = MutableLiveData<Patient>()

    val vitalsInfo = MutableLiveData<PatientVitals>()

    fun saveRegistrationInfo(patient: Patient) {
        registrationInfo.value = patient
    }

    fun saveVitalsInfo(patientVitals: PatientVitals) {
        vitalsInfo.value = patientVitals
    }
}