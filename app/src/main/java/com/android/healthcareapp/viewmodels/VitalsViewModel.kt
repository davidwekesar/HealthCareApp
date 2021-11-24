package com.android.healthcareapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.healthcareapp.models.PatientVitals
import com.android.healthcareapp.util.DATABASE_URL
import com.google.firebase.database.FirebaseDatabase
import timber.log.Timber

class VitalsViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance(DATABASE_URL).reference

    private val _navigateToVisitFormA = MutableLiveData<Boolean?>()
    val navigateToVisitFormA: LiveData<Boolean?> get() = _navigateToVisitFormA

    private val _navigateToVisitFormB = MutableLiveData<Boolean?>()
    val navigateToVisitFormB: LiveData<Boolean?> get() = _navigateToVisitFormB

    private val _isSaveProgressVisible = MutableLiveData<Boolean?>()
    val isSaveProgressVisible: LiveData<Boolean?> get() = _isSaveProgressVisible

    private var patientId: Long? = null

    fun savePatientVitals(patientVitals: PatientVitals) {
        database.child("patientVitals")
            .child("$patientId")
            .setValue(patientVitals)
            .addOnSuccessListener {
                Timber.i("Success! saved patient vitals to firebase.")
                _isSaveProgressVisible.value = false
                if (patientVitals.bmi >= 25) {
                    _navigateToVisitFormB.value = true
                } else {
                    _navigateToVisitFormA.value = true
                }
            }
            .addOnFailureListener { e ->
                Timber.e("Failure: ${e.message}")
            }
    }

    fun showSaveButtonProgress() {
        _isSaveProgressVisible.value = true
    }

    fun setPatientId(id: Long) {
        patientId = id
    }

    fun doneShowingSaveButtonProgress() {
        _isSaveProgressVisible.value = null
    }

    fun doneNavigatingToVisitFormA() {
        _navigateToVisitFormA.value = null
    }

    fun doneNavigatingToVisitFormB() {
        _navigateToVisitFormB.value = null
    }
}