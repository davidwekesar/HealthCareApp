package com.android.healthcareapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.healthcareapp.models.VisitFormB
import com.android.healthcareapp.util.DATABASE_URL
import com.google.firebase.database.FirebaseDatabase
import timber.log.Timber

class VisitFormBViewModel: ViewModel() {

    private val database = FirebaseDatabase.getInstance(DATABASE_URL).reference

    private val _navigateToPatientsListFragment = MutableLiveData<Boolean>()
    val navigateToPatientsListFragment: LiveData<Boolean> get() = _navigateToPatientsListFragment

    fun saveVisitFormBInfo(visitFormB: VisitFormB) {
        database.child("visitFormA")
            .child(visitFormB.patientName).setValue(visitFormB)
            .addOnSuccessListener {
                _navigateToPatientsListFragment.value = true
            }
            .addOnFailureListener { e ->
                Timber.e("Failure: ${e.message}")
            }
    }
}