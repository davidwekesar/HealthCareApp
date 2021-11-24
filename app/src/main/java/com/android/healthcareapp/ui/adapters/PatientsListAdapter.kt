package com.android.healthcareapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.healthcareapp.databinding.ListItemPatientBinding

class PatientsListAdapter: RecyclerView.Adapter<PatientViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemPatientBinding.inflate(layoutInflater, parent, false)
        return PatientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}

class PatientViewHolder(binding: ListItemPatientBinding): RecyclerView.ViewHolder(binding.root) {
    private val textPatientName = binding.textPatientName
    private val textAge = binding.textAge
    private val textBmiStatus = binding.textBmiStatus

    fun bind() {

    }
}