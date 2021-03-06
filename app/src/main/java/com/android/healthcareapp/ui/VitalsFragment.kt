package com.android.healthcareapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.healthcareapp.R
import com.android.healthcareapp.databinding.FragmentVitalsBinding
import com.android.healthcareapp.models.PatientVitals
import com.android.healthcareapp.util.*
import com.android.healthcareapp.viewmodels.SharedViewModel
import com.android.healthcareapp.viewmodels.VitalsViewModel
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.button.MaterialButton
import kotlin.math.pow

class VitalsFragment : Fragment() {

    private var _binding: FragmentVitalsBinding? = null
    private val binding get() = _binding!!
    private lateinit var patientNameEditTxt: EditText
    private lateinit var visitDateEditTxt: EditText
    private lateinit var heightEditTxt: EditText
    private lateinit var weightEditTxt: EditText
    private lateinit var bmiEditTxt: EditText
    private lateinit var saveButton: MaterialButton
    private val viewModel: VitalsViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVitalsBinding.inflate(inflater, container, false)

        init()

        return binding.root
    }

    private fun init() {
        initViews()
        initListeners()
        initObservers()
        setupSaveButtonAnim()
    }

    private fun initViews() {
        patientNameEditTxt = binding.patientNameEditText
        visitDateEditTxt = binding.visitDateEditText
        heightEditTxt = binding.heightEditText
        weightEditTxt = binding.weightEditText
        bmiEditTxt = binding.bmiEditText
        saveButton = binding.saveBtn
    }

    private fun initListeners() {
        onClickListeners()
        textChangeListeners()
    }

    private fun initObservers() {
        visitFormANavigationObserver()
        visitFormBNavigationObserver()
        saveProgressVisibilityObserver()
        patientRegistrationInfoObserver()
    }

    private fun onClickListeners() {
        binding.saveBtn.setOnClickListener {
            checkTextFields()
        }
    }

    private fun textChangeListeners() {
        visitDateEditTxt.addTextChangedListener { text ->
            if (!inputMatchesDateFormat(text)) {
                binding.visitDateInputLayout.error = getString(R.string.error_date_format)
            } else {
                binding.visitDateInputLayout.error = null
            }
        }
        heightEditTxt.addTextChangedListener { heightEditable ->
            weightEditTxt.addTextChangedListener { weightEditable ->
                heightEditable?.let {
                    weightEditable?.let {
                        if (heightEditable.isNotBlank() && weightEditable.isNotBlank()) {
                            val height = heightEditTxt.getInputValue().toDouble()
                            val weight = weightEditTxt.getInputValue().toDouble()
                            val bmi = calculateBmi(height, weight)
                            bmiEditTxt.setText(bmi.toString())
                        }
                    }
                }
            }
        }
    }

    private fun setupSaveButtonAnim() {
        bindProgressButton(saveButton)
        saveButton.attachTextChangeAnimator()
    }

    private fun checkTextFields() {

        when {
            patientNameEditTxt.isTextFieldEmpty() -> {
                binding.patientNameInputLayout.error = getString(R.string.input_required)
            }
            visitDateEditTxt.isTextFieldEmpty() -> {
                binding.visitDateInputLayout.error = getString(R.string.input_required)
            }
            heightEditTxt.isTextFieldEmpty() -> {
                binding.heightInputLayout.error = getString(R.string.input_required)
            }
            weightEditTxt.isTextFieldEmpty() -> {
                binding.weightInputLayout.error = getString(R.string.input_required)
            }
            bmiEditTxt.isTextFieldEmpty() -> {
                binding.bmiInputLayout.error = getString(R.string.input_required)
            }
            else -> {
                viewModel.showSaveButtonProgress()
                sharedViewModel.saveVitalsInfo(getPatientVitalsInput())
                viewModel.savePatientVitals(getPatientVitalsInput())
            }
        }
    }

    private fun getPatientVitalsInput(): PatientVitals {
        val patientName = patientNameEditTxt.getInputValue()
        val visitDate = visitDateEditTxt.getInputValue().convertToUnixTime()
        val height = heightEditTxt.getInputValue().toDouble()
        val weight = weightEditTxt.getInputValue().toDouble()
        val bmi = bmiEditTxt.getInputValue().toInt()

        return PatientVitals(patientName, visitDate, height, weight, bmi)
    }

    private fun visitFormANavigationObserver() {
        viewModel.navigateToVisitFormA.observe(viewLifecycleOwner, { navigate: Boolean? ->
            navigate?.let {
                if (it) {
                    findNavController().navigate(R.id.visitFormAFragment)
                    viewModel.doneShowingSaveButtonProgress()
                    viewModel.doneNavigatingToVisitFormA()
                }
            }
        })
    }

    private fun visitFormBNavigationObserver() {
        viewModel.navigateToVisitFormB.observe(viewLifecycleOwner, { navigate: Boolean? ->
            navigate?.let {
                if (it) {
                    findNavController().navigate(R.id.visitFormBFragment)
                    viewModel.doneShowingSaveButtonProgress()
                    viewModel.doneNavigatingToVisitFormB()
                }
            }
        })
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

    private fun patientRegistrationInfoObserver() {
        sharedViewModel.registrationInfo.observe(viewLifecycleOwner, { patient ->
            val patientName = "${patient.firstName} ${patient.lastName}"
            patientNameEditTxt.setText(patientName)
            viewModel.setPatientId(patient.patientId!!)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}