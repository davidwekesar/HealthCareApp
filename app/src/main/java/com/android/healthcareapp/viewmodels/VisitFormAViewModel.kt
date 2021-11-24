package com.android.healthcareapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.healthcareapp.models.VisitForm
import com.android.healthcareapp.util.DATABASE_URL
import com.google.firebase.database.FirebaseDatabase
import timber.log.Timber

class VisitFormAViewModel: ViewModel() {

    private val database = FirebaseDatabase.getInstance(DATABASE_URL).reference

    private val _navigateToPatientsListFragment = MutableLiveData<Boolean?>()
    val navigateToPatientsListFragment: LiveData<Boolean?> get() = _navigateToPatientsListFragment

    private val _isSaveProgressVisible = MutableLiveData<Boolean?>()
    val isSaveProgressVisible: LiveData<Boolean?> get() = _isSaveProgressVisible

    private var patientId: Long? = null

    fun saveVisitFormAInfo(visitForm: VisitForm) {
        database.child("visitForm")
            .child("$patientId")
            .setValue(visitForm)
            .addOnSuccessListener {
                _navigateToPatientsListFragment.value = true
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

    fun doneNavigatingToPatientsListFragment() {
        _navigateToPatientsListFragment.value = null
    }
}