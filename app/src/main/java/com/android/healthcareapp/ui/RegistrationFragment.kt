package com.android.healthcareapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.healthcareapp.R
import com.android.healthcareapp.databinding.FragmentRegistrationBinding
import com.android.healthcareapp.util.isTextFieldEmpty

class RegistrationFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        val saveButton = binding.btnSave
        saveButton.setOnClickListener {
            getInputFromTextFields()
        }

        val spinnerGender = binding.spinnerGender
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.genders_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerGender.adapter = adapter
        }
        spinnerGender.onItemSelectedListener = this

        return binding.root
    }

    private fun getInputFromTextFields() {
        val patientNumber = binding.editTxtPatientNum
        val registrationDate = binding.editTxtRegDate
        val firstName = binding.editTxtFirstName
        val lastName = binding.editTxtLastName
        val dateOfBirth = binding.editTxtDoB

        when {
            patientNumber.isTextFieldEmpty() -> {
                patientNumber.error = getString(R.string.input_required)
            }
            registrationDate.isTextFieldEmpty() -> {
                registrationDate.error = getString(R.string.input_required)
            }
            firstName.isTextFieldEmpty() -> {
                firstName.error = getString(R.string.input_required)
            }
            lastName.isTextFieldEmpty() -> {
                lastName.error = getString(R.string.input_required)
            }
            dateOfBirth.isTextFieldEmpty() -> {
                dateOfBirth.error = getString(R.string.input_required)
            }
            else -> {
                Toast.makeText(requireContext(), "Saving patient info...", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}