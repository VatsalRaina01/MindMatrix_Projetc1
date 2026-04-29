package com.shishusneh.ui.vaccination

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shishusneh.data.repository.BabyProfileRepository
import com.shishusneh.data.repository.VaccinationRepository
import com.shishusneh.domain.model.BabyProfile
import com.shishusneh.domain.model.Vaccination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class VaccinationState(
    val profile: BabyProfile? = null,
    val vaccinations: List<Vaccination> = emptyList(),
    val selectedTab: Int = 0, // 0=All, 1=Upcoming, 2=Done
    val completedCount: Int = 0,
    val totalCount: Int = 0,
    val isLoading: Boolean = true,
    // Date picker state
    val showDatePicker: Boolean = false,
    val pendingMarkDoneId: Long? = null
)

@HiltViewModel
class VaccinationViewModel @Inject constructor(
    private val profileRepo: BabyProfileRepository,
    private val vaccinationRepo: VaccinationRepository
) : ViewModel() {
    private val _state = MutableStateFlow(VaccinationState())
    val state: StateFlow<VaccinationState> = _state.asStateFlow()

    init { loadData() }

    private fun loadData() {
        viewModelScope.launch {
            profileRepo.getActiveProfile().collect { profile ->
                if (profile != null) {
                    vaccinationRepo.updateOverdueStatus(profile.id, System.currentTimeMillis())
                    val completed = vaccinationRepo.getCompletedCount(profile.id)
                    val total = vaccinationRepo.getTotalCount(profile.id)
                    _state.value = _state.value.copy(profile = profile, completedCount = completed, totalCount = total, isLoading = false)
                    vaccinationRepo.getAllVaccinations(profile.id).collect { list ->
                        _state.value = _state.value.copy(vaccinations = list, completedCount = list.count { it.status.name == "DONE" })
                    }
                }
            }
        }
    }

    fun selectTab(tab: Int) { _state.value = _state.value.copy(selectedTab = tab) }

    /** Show date picker before marking as done */
    fun requestMarkDone(id: Long) {
        _state.value = _state.value.copy(showDatePicker = true, pendingMarkDoneId = id)
    }

    /** User selected a date — finalize marking as done */
    fun confirmMarkDone(dateAdministered: Long) {
        val id = _state.value.pendingMarkDoneId ?: return
        viewModelScope.launch {
            vaccinationRepo.markAsDone(id, dateAdministered)
            _state.value = _state.value.copy(showDatePicker = false, pendingMarkDoneId = null)
        }
    }

    fun dismissDatePicker() {
        _state.value = _state.value.copy(showDatePicker = false, pendingMarkDoneId = null)
    }

    fun markAsPending(id: Long) {
        viewModelScope.launch { vaccinationRepo.markAsPending(id) }
    }
}
