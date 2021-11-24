package com.android.healthcareapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.healthcareapp.models.Patient
import com.google.firebase.database.FirebaseDatabase
import timber.log.Timber

class RegistrationViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance(
        "https://health-care-app-4893b-default-rtdb.asia-southeast1.firebasedatabase.app/"
    ).reference

    private var _navigateToVitalsFragment = MutableLiveData<Boolean>()
    val navigateToVitalsFragment: LiveData<Boolean> get() = _navigateToVitalsFragment

    fun savePatientInfo(patient: Patient) {
        database.child("patients")
            .child("${patient.patientId}")
            .setValue(patient)
            .addOnSuccessListener {
                _navigateToVitalsFragment.value = true
                Timber.i("Success! patient info saved to firebase")
            }
            .addOnFailureListener { exception ->
                _navigateToVitalsFragment.value = false
                Timber.e("Failure: ${exception.message}")
            }
    }
}