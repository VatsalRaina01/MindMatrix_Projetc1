package com.shishusneh.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shishusneh.data.repository.BabyProfileRepository
import com.shishusneh.domain.model.BabyProfile
import com.shishusneh.domain.model.Gender
import com.shishusneh.util.InputValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditProfileState(
    val profile: BabyProfile? = null,
    val name: String = "",
    val motherName: String = "",
    val dobMillis: Long? = null,
    val birthWeightKg: String = "",
    val gender: Gender? = null,
    val nameError: String? = null,
    val motherNameError: String? = null,
    val weightError: String? = null,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val isLoading: Boolean = true
)

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val profileRepo: BabyProfileRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EditProfileState())
    val state: StateFlow<EditProfileState> = _state.asStateFlow()

    init { loadProfile() }

    private fun loadProfile() {
        viewModelScope.launch {
            val profile = profileRepo.getActiveProfileOnce()
            if (profile != null) {
                _state.value = _state.value.copy(
                    profile = profile,
                    name = profile.name,
                    motherName = profile.motherName,
                    dobMillis = profile.dateOfBirth,
                    birthWeightKg = profile.birthWeightKg.toString(),
                    gender = profile.gender,
                    isLoading = false
                )
            } else {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

    fun updateName(name: String) {
        _state.value = _state.value.copy(name = name, nameError = null)
    }

    fun updateMotherName(name: String) {
        _state.value = _state.value.copy(motherName = name, motherNameError = null)
    }

    fun updateWeight(weight: String) {
        _state.value = _state.value.copy(birthWeightKg = weight, weightError = null)
    }

    fun updateGender(gender: Gender) {
        _state.value = _state.value.copy(gender = gender)
    }

    fun saveProfile() {
        val s = _state.value
        val original = s.profile ?: return

        // Validate
        val nameResult = InputValidator.validateBabyName(s.name)
        val motherResult = InputValidator.validateMotherName(s.motherName)
        val weight = s.birthWeightKg.toFloatOrNull()

        if (!nameResult.isValid || !motherResult.isValid) {
            _state.value = s.copy(
                nameError = nameResult.errorMessageEn,
                motherNameError = motherResult.errorMessageEn
            )
            return
        }
        if (weight == null) {
            _state.value = s.copy(weightError = "Please enter a valid weight")
            return
        }
        val weightResult = InputValidator.validateBirthWeight(weight)
        if (!weightResult.isValid) {
            _state.value = s.copy(weightError = weightResult.errorMessageEn)
            return
        }

        _state.value = s.copy(isSaving = true)

        viewModelScope.launch {
            try {
                val updated = original.copy(
                    name = s.name.trim(),
                    motherName = s.motherName.trim(),
                    birthWeightKg = weight,
                    gender = s.gender ?: original.gender
                )
                profileRepo.updateProfile(updated)
                _state.value = _state.value.copy(isSaving = false, isSaved = true)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isSaving = false,
                    nameError = "Failed to save. Please try again."
                )
            }
        }
    }
}
