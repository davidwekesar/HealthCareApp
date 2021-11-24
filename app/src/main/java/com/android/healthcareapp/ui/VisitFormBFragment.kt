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
import com.android.healthcareapp.databinding.FragmentVisitFormBBinding
import com.android.healthcareapp.models.VisitForm
import com.android.healthcareapp.util.convertToUnixTime
import com.android.healthcareapp.util.getInputValue
import com.android.healthcareapp.util.inputMatchesDateFormat
import com.android.healthcareapp.util.isTextFieldEmpty
import com.android.healthcareapp.viewmodels.SharedViewModel
import com.android.healthcareapp.viewmodels.VisitFormBViewModel
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.button.MaterialButton

class VisitFormBFragment : Fragment() {

    private var _binding: FragmentVisitFormBBinding? = null
    private val binding get() = _binding!!
    private lateinit var patientNameEditText: EditText
    private lateinit var visitDateEditText: EditText
    private lateinit var commentsEditText: EditText
    private lateinit var saveButton: MaterialButton
    private var generalHealth: String? = null
    private var onDrugsAnswer: String? = null
    private var bmi: Int? = null
    private var dateOfBirth: Long? = null
    private val viewModel: VisitFormBViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVisitFormBBinding.inflate(inflater, container, false)

        init()

        return binding.root
    }

    private fun init() {
        initViews()
        initListeners()
        initObservers()
    }

    private fun initViews() {
        patientNameEditText = binding.patientNameEditText
        visitDateEditText = binding.visitDateEditText
        commentsEditText = binding.commentsEditTxt
        saveButton = binding.saveBtn
        setupSaveButtonAnim()
    }

    private fun initListeners() {
        healthRadioGroupCheckListener()
        drugsRadioGroupCheckListener()
        saveButtonClickListener()
        textChangeListeners()
    }

    private fun initObservers() {
        patientsListNavigationObserver()
        patientVitalsInfoObserver()
        saveProgressVisibilityObserver()
        patientRegistrationInfoObserver()
    }

    private fun textChangeListeners() {
        visitDateEditText.addTextChangedListener { text ->
            if (!inputMatchesDateFormat(text)) {
                binding.visitDateInputLayout.error = getString(R.string.error_date_format)
            } else {
                binding.visitDateInputLayout.error = null
            }
        }
    }

    private fun healthRadioGroupCheckListener() {
        binding.healthRadioGroup.setOnCheckedChangeListener { _, id ->
            generalHealth = when (id) {
                R.id.radio_btn_good -> {
                    getString(R.string.good)
                }
                R.id.radio_btn_poor -> {
                    getString(R.string.poor)
                }
                else -> null
            }
        }
    }

    private fun drugsRadioGroupCheckListener() {
        binding.drugsRadioGroup.setOnCheckedChangeListener { _, id ->
            onDrugsAnswer = when (id) {
                R.id.radio_btn_yes -> {
                    getString(R.string.yes)
                }
                R.id.radio_btn_no -> {
                    getString(R.string.no)
                }
                else -> {
                    null
                }
            }
        }
    }

    private fun saveButtonClickListener() {
        binding.saveBtn.setOnClickListener {
            checkInputFields()
        }
    }

    private fun checkInputFields() {

        when {
            patientNameEditText.isTextFieldEmpty() -> {
                binding.nameInputLayout.error = getString(R.string.input_required)
            }
            visitDateEditText.isTextFieldEmpty() -> {
                binding.visitDateInputLayout.error = getString(R.string.input_required)
            }
            else -> {
                viewModel.showSaveButtonProgress()
                viewModel.saveVisitFormBInfo(getVisitForm())
            }
        }
    }

    private fun getVisitForm(): VisitForm {
        val patientName = patientNameEditText.getInputValue()
        val visitDate = visitDateEditText.getInputValue().convertToUnixTime()
        val health = generalHealth
        val onDrugs = onDrugsAnswer
        val comments = commentsEditText.getInputValue()

        return VisitForm(
            patientName, dateOfBirth!!, visitDate, health, null, onDrugs, comments, bmi!!
        )
    }

    private fun patientsListNavigationObserver() {
        viewModel.navigateToPatientsListFragment.observe(viewLifecycleOwner, { navigate: Boolean? ->
            navigate?.let {
                if (it) {
                    findNavController().navigate(R.id.patientsListFragment)
                    viewModel.doneShowingSaveButtonProgress()
                    viewModel.doneNavigatingToPatientsListFragment()
                }
            }
        })
    }

    private fun patientVitalsInfoObserver() {
        sharedViewModel.vitalsInfo.observe(viewLifecycleOwner, { patientVitals ->
            patientNameEditText.setText(patientVitals.patientName)
            bmi = patientVitals.bmi
        })
    }

    private fun patientRegistrationInfoObserver() {
        sharedViewModel.registrationInfo.observe(viewLifecycleOwner, { patient ->
            viewModel.setPatientId(patient.patientId!!)
            dateOfBirth = patient.dateOfBirth
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
}