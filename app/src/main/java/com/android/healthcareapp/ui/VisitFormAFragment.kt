package com.android.healthcareapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.healthcareapp.R
import com.android.healthcareapp.databinding.FragmentVisitFormABinding
import com.android.healthcareapp.models.VisitFormA
import com.android.healthcareapp.util.convertToUnixTime
import com.android.healthcareapp.util.getInputValue
import com.android.healthcareapp.util.isTextFieldEmpty
import com.android.healthcareapp.viewmodels.VisitFormAViewModel

class VisitFormAFragment: Fragment() {

    private var _binding: FragmentVisitFormABinding? = null
    private val binding get() = _binding!!
    private lateinit var patientNameEditText: EditText
    private lateinit var visitDateEditText: EditText
    private lateinit var commentsEditText: EditText
    private var generalHealth: String? = null
    private var dietAnswer: String? = null
    private val viewModel: VisitFormAViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVisitFormABinding.inflate(inflater, container, false)

        init()

        return binding.root
    }

    private fun init() {
        initViews()
        initListeners()
        patientsListNavigationObserver()
    }

    private fun initViews() {
        patientNameEditText = binding.patientNameEditText
        visitDateEditText = binding.visitDateEditText
        commentsEditText = binding.commentsEditTxt
    }

    private fun initListeners() {
        healthRadioGroupCheckListener()
        dietRadioGroupCheckListener()
        saveButtonClickListener()
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

    private fun dietRadioGroupCheckListener() {
        binding.dietRadioGroup.setOnCheckedChangeListener { _, id ->
            dietAnswer = when (id) {
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
                viewModel.saveVisitFormAInfo(getVisitFormA())
            }
        }
    }

    private fun getVisitFormA(): VisitFormA {
        val patientName = patientNameEditText.getInputValue()
        val visitDate = visitDateEditText.getInputValue().convertToUnixTime()
        val health = generalHealth
        val onDiet = dietAnswer
        val comments = commentsEditText.getInputValue()

        return VisitFormA(patientName, visitDate, health, onDiet, comments)
    }

    private fun patientsListNavigationObserver() {
        viewModel.navigateToPatientsListFragment.observe(viewLifecycleOwner, {
            if (it) {
                findNavController().navigate(R.id.patientsListFragment)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}