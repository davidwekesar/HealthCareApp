package com.android.healthcareapp.ui

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.healthcareapp.R
import com.android.healthcareapp.databinding.FragmentRegistrationBinding
import com.android.healthcareapp.models.Patient
import com.android.healthcareapp.util.*
import com.android.healthcareapp.viewmodels.RegistrationViewModel
import com.android.healthcareapp.viewmodels.SharedViewModel
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.button.MaterialButton

class RegistrationFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegistrationViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var idEditTxt: EditText
    private lateinit var regDateEditTxt: EditText
    private lateinit var firstNameEditTxt: EditText
    private lateinit var lastNameEditTxt: EditText
    private lateinit var dobEditTxt: EditText
    private lateinit var saveButton: MaterialButton
    private var gender: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        init()

        return binding.root
    }

    private fun init() {
        initViews()
        initListeners()
        initObservers()
        setupSpinner()
    }

    private fun initViews() {
        idEditTxt = binding.patientIdEditTxt
        regDateEditTxt = binding.regDateEditText
        firstNameEditTxt = binding.firstNameEditTxt
        lastNameEditTxt = binding.lastNameEditTxt
        dobEditTxt = binding.dateOfBirthEditTxt
        saveButton = binding.btnSave
        setupSaveButtonAnim()
    }

    private fun initListeners() {
        textChangeListeners()
        onClickListeners()
    }

    private fun initObservers() {
        vitalsFragmentNavigationObserver()
        saveProgressVisibilityObserver()
    }

    private fun textChangeListeners() {
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
    }

    private fun onClickListeners() {
        saveButton.setOnClickListener {
            checkInputFields()
        }
    }

    private fun setupSpinner() {
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
    }

    private fun checkInputFields() {

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
                viewModel.showSaveButtonProgress()
                sharedViewModel.saveRegistrationInfo(getPatientInfo())
                viewModel.savePatientInfo(getPatientInfo())
            }
        }
    }

    private fun getPatientInfo(): Patient {
        val id = idEditTxt.getInputValue().toLong()
        val regDate = regDateEditTxt.getInputValue().convertToUnixTime()
        val firstName = firstNameEditTxt.getInputValue()
        val lastName = lastNameEditTxt.getInputValue()
        val dob = dobEditTxt.getInputValue().convertToUnixTime()
        return Patient(id, regDate, firstName, lastName, dob, gender)
    }

    private fun vitalsFragmentNavigationObserver() {
        viewModel.navigateToVitalsFragment.observe(viewLifecycleOwner, { navigate: Boolean? ->
            navigate?.let {
                if (it) {
                    findNavController().navigate(R.id.vitalsFragment)
                    viewModel.doneShowingSaveButtonProgress()
                    viewModel.doneNavigatingToVitalsFragment()
                }
            }
        })
    }

    private fun setupSaveButtonAnim() {
        bindProgressButton(saveButton)
        saveButton.attachTextChangeAnimator()
    }

    private fun saveProgressVisibilityObserver() {
        viewModel.isSaveProgressVisible.observe(viewLifecycleOwner, { showProgress ->
            showProgress?.let {
                if (it) {
                    saveButton.showProgress {
                        buttonTextRes = R.string.saving
                    }
                } else {
                    saveButton.hideProgress(R.string.save)
                }
            }
        })
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