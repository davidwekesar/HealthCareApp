package com.android.healthcareapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.healthcareapp.models.Patient
import com.android.healthcareapp.util.DATABASE_URL
import com.google.firebase.database.FirebaseDatabase
import timber.log.Timber

class RegistrationViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance(DATABASE_URL).reference

    private var _navigateToVitalsFragment = MutableLiveData<Boolean>()
    val navigateToVitalsFragment: LiveData<Boolean> get() = _navigateToVitalsFragment

    fun savePatientInfo(patient: Patient) {
        database.child("patients")
            .child("${patient.patientId}")
            .setValue(patient)
            .addOnSuccessListener {
                _navigateToVitalsFragment.value = true
                Timber.i("Success! patient info saved to Firebase")
            }
            .addOnFailureListener { exception ->
                _navigateToVitalsFragment.value = false
                Timber.e("Failure: ${exception.message}")
            }
    }
}