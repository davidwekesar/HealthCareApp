package com.android.healthcareapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.healthcareapp.databinding.ListItemPatientBinding
import com.android.healthcareapp.models.VisitForm
import com.android.healthcareapp.util.calculateAge
import com.android.healthcareapp.util.getBmiStatus

class PatientsListAdapter(private val patientsList: List<VisitForm>): RecyclerView.Adapter<PatientViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemPatientBinding.inflate(layoutInflater, parent, false)
        return PatientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        holder.bind(patientsList[position])
    }

    override fun getItemCount(): Int = patientsList.size
}

class PatientViewHolder(binding: ListItemPatientBinding): RecyclerView.ViewHolder(binding.root) {
    private val textPatientName = binding.textPatientName
    private val textAge = binding.textAge
    private val textBmiStatus = binding.textBmiStatus

    fun bind(visitForm: VisitForm) {
        textPatientName.text = visitForm.patientName
        textAge.text = calculateAge(visitForm.dateOfBirth)
        textBmiStatus.text = getBmiStatus(visitForm.bmi)
    }
}