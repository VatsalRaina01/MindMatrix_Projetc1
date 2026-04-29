package com.shishusneh.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shishusneh.data.repository.BabyProfileRepository
import com.shishusneh.data.repository.MilestoneRepository
import com.shishusneh.data.repository.VaccinationRepository
import com.shishusneh.domain.model.BabyProfile
import com.shishusneh.domain.model.Gender
import com.shishusneh.util.InputValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileSetupState(
    val currentStep: Int = 0,          // 0: Name, 1: DOB, 2: Weight, 3: Gender
    val babyName: String = "",
    val motherName: String = "",
    val dobMillis: Long? = null,
    val birthWeightKg: String = "",
    val gender: Gender? = null,
    val nameError: String? = null,
    val motherNameError: String? = null,
    val dobError: String? = null,
    val weightError: String? = null,
    val isLoading: Boolean = false,
    val isComplete: Boolean = false
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val profileRepository: BabyProfileRepository,
    private val vaccinationRepository: VaccinationRepository,
    private val milestoneRepository: MilestoneRepository
) : ViewModel() {

    private val _hasProfile = MutableStateFlow<Boolean?>(null)
    val hasProfile: StateFlow<Boolean?> = _hasProfile.asStateFlow()

    private val _setupState = MutableStateFlow(ProfileSetupState())
    val setupState: StateFlow<ProfileSetupState> = _setupState.asStateFlow()

    init {
        checkProfile()
    }

    private fun checkProfile() {
        viewModelScope.launch {
            _hasProfile.value = profileRepository.hasProfile()
        }
    }

    fun updateBabyName(name: String) {
        _setupState.value = _setupState.value.copy(
            babyName = name,
            nameError = null
        )
    }

    fun updateMotherName(name: String) {
        _setupState.value = _setupState.value.copy(
            motherName = name,
            motherNameError = null
        )
    }

    fun updateDOB(millis: Long) {
        _setupState.value = _setupState.value.copy(
            dobMillis = millis,
            dobError = null
        )
    }

    fun updateBirthWeight(weight: String) {
        _setupState.value = _setupState.value.copy(
            birthWeightKg = weight,
            weightError = null
        )
    }

    fun updateGender(gender: Gender) {
        _setupState.value = _setupState.value.copy(gender = gender)
    }

    fun nextStep() {
        val state = _setupState.value
        when (state.currentStep) {
            0 -> {
                val nameResult = InputValidator.validateBabyName(state.babyName)
                val motherResult = InputValidator.validateMotherName(state.motherName)
                if (!nameResult.isValid || !motherResult.isValid) {
                    _setupState.value = state.copy(
                        nameError = nameResult.errorMessageEn,
                        motherNameError = motherResult.errorMessageEn
                    )
                    return
                }
            }
            1 -> {
                if (state.dobMillis == null) {
                    _setupState.value = state.copy(dobError = "Please select date of birth")
                    return
                }
                val dobResult = InputValidator.validateDOB(state.dobMillis)
                if (!dobResult.isValid) {
                    _setupState.value = state.copy(dobError = dobResult.errorMessageEn)
                    return
                }
            }
            2 -> {
                val weight = state.birthWeightKg.toFloatOrNull()
                if (weight == null) {
                    _setupState.value = state.copy(weightError = "Please enter a valid weight")
                    return
                }
                val weightResult = InputValidator.validateBirthWeight(weight)
                if (!weightResult.isValid) {
                    _setupState.value = state.copy(weightError = weightResult.errorMessageEn)
                    return
                }
            }
            3 -> {
                if (state.gender == null) return
                createProfile()
                return
            }
        }
        _setupState.value = state.copy(currentStep = state.currentStep + 1)
    }

    fun previousStep() {
        val state = _setupState.value
        if (state.currentStep > 0) {
            _setupState.value = state.copy(currentStep = state.currentStep - 1)
        }
    }

    private fun createProfile() {
        val state = _setupState.value
        _setupState.value = state.copy(isLoading = true)

        viewModelScope.launch {
            try {
                val profile = BabyProfile(
                    name = state.babyName.trim(),
                    motherName = state.motherName.trim(),
                    dateOfBirth = state.dobMillis!!,
                    birthWeightKg = state.birthWeightKg.toFloat(),
                    gender = state.gender!!
                )
                val babyId = profileRepository.createProfile(profile)

                // Auto-generate vaccination schedule from DOB
                vaccinationRepository.generateSchedule(babyId, state.dobMillis)

                // Auto-generate milestone checklist
                milestoneRepository.generateMilestones(babyId)

                _setupState.value = state.copy(isLoading = false, isComplete = true)
            } catch (e: Exception) {
                _setupState.value = state.copy(
                    isLoading = false,
                    nameError = "Something went wrong. Please try again."
                )
            }
        }
    }
}
