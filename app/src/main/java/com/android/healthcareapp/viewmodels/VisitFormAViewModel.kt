package com.android.healthcareapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.healthcareapp.models.VisitFormA
import com.android.healthcareapp.util.DATABASE_URL
import com.google.firebase.database.FirebaseDatabase
import timber.log.Timber

class VisitFormAViewModel: ViewModel() {

    private val database = FirebaseDatabase.getInstance(DATABASE_URL).reference

    private val _navigateToPatientsListFragment = MutableLiveData<Boolean>()
    val navigateToPatientsListFragment: LiveData<Boolean> get() = _navigateToPatientsListFragment

    fun saveVisitFormAInfo(visitFormA: VisitFormA) {
        database.child("visitFormA")
            .child(visitFormA.patientName).setValue(visitFormA)
            .addOnSuccessListener {
                _navigateToPatientsListFragment.value = true
            }
            .addOnFailureListener { e ->
                Timber.e("Failure: ${e.message}")
            }
    }
}