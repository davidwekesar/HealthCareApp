package com.android.healthcareapp.ui

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.healthcareapp.R
import com.android.healthcareapp.databinding.FragmentRegistrationBinding
import com.android.healthcareapp.models.Patient
import com.android.healthcareapp.util.*
import com.android.healthcareapp.viewmodels.RegistrationViewModel

class RegistrationFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegistrationViewModel by viewModels()
    private var gender: String? = null

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

        binding.regDateEditText.addTextChangedListener { text: Editable? ->
            val regDateInputLayout = binding.regDateInputLayout
            if (!inputMatchesDateFormat(text)) {
                regDateInputLayout.error = getString(R.string.error_date_format)
            } else {
                regDateInputLayout.error = null
            }
        }

        binding.dateOfBirthEditTxt.addTextChangedListener { text: Editable? ->
            val dobInputLayout = binding.dobInputLayout
            if (!inputMatchesDateFormat(text)) {
                dobInputLayout.error = getString(R.string.error_date_format)
            } else {
                dobInputLayout.error = null
            }
        }

        viewModel.navigateToVitalsFragment.observe(viewLifecycleOwner, {
            if (it) {
                findNavController().navigate(R.id.vitalsFragment)
            }
        })

        return binding.root
    }

    private fun getInputFromTextFields() {
        val idEditTxt = binding.patientIdEditTxt
        val regDateEditTxt = binding.regDateEditText
        val firstNameEditTxt = binding.firstNameEditTxt
        val lastNameEditTxt = binding.lastNameEditTxt
        val dobEditTxt = binding.dateOfBirthEditTxt

        when {
            idEditTxt.isTextFieldEmpty() -> {
                idEditTxt.error = getString(R.string.input_required)
            }
            regDateEditTxt.isTextFieldEmpty() -> {
                binding.regDateInputLayout.error = getString(R.string.input_required)
            }
            firstNameEditTxt.isTextFieldEmpty() -> {
                firstNameEditTxt.error = getString(R.string.input_required)
            }
            lastNameEditTxt.isTextFieldEmpty() -> {
                lastNameEditTxt.error = getString(R.string.input_required)
            }
            dobEditTxt.isTextFieldEmpty() -> {
                binding.dobInputLayout.error = getString(R.string.input_required)
            }
            else -> {
                val id = idEditTxt.getInputValue().toLong()
                val regDate = regDateEditTxt.getInputValue().convertToUnixTime()
                val firstName = firstNameEditTxt.getInputValue()
                val lastName = lastNameEditTxt.getInputValue()
                val dob = dobEditTxt.getInputValue().convertToUnixTime()
                val patient = Patient(id, regDate, firstName, lastName, dob, gender)
                viewModel.savePatientInfo(patient)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        gender = parent?.getItemAtPosition(pos).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}