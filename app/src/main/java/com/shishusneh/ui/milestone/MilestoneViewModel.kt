package com.shishusneh.ui.milestone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shishusneh.data.constants.MilestoneData
import com.shishusneh.data.repository.BabyProfileRepository
import com.shishusneh.data.repository.MilestoneRepository
import com.shishusneh.domain.model.Milestone
import com.shishusneh.domain.model.MilestoneStatus
import com.shishusneh.util.AgeCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MilestoneState(
    val currentWeek: Int = 1,
    val selectedWeek: Int = 1,
    val availableWeeks: List<Int> = emptyList(),
    val milestones: List<Milestone> = emptyList(),
    val showAdvisory: Boolean = false,
    val achievedCount: Int = 0,
    val totalCount: Int = 0,
    val showConfetti: Boolean = false,
    val isLoading: Boolean = true
)

@HiltViewModel
class MilestoneViewModel @Inject constructor(
    private val profileRepo: BabyProfileRepository,
    private val milestoneRepo: MilestoneRepository
) : ViewModel() {
    private val _state = MutableStateFlow(MilestoneState())
    val state: StateFlow<MilestoneState> = _state.asStateFlow()

    private var babyId: Long = 0

    init { loadData() }

    private fun loadData() {
        viewModelScope.launch {
            profileRepo.getActiveProfile().collect { profile ->
                if (profile != null) {
                    babyId = profile.id
                    val age = AgeCalculator.calculateAge(profile.dateOfBirth)
                    val currentWeek = age.totalWeeks.toInt().coerceIn(1, 52)
                    val nearestWeek = MilestoneData.getNearestMilestoneWeek(currentWeek)
                    val achieved = milestoneRepo.getAchievedCount(profile.id)
                    val total = milestoneRepo.getTotalCount(profile.id)

                    _state.value = _state.value.copy(currentWeek = currentWeek, selectedWeek = nearestWeek,
                        achievedCount = achieved, totalCount = total, isLoading = false)

                    launch {
                        milestoneRepo.getAvailableWeeks(profile.id).collect { weeks ->
                            _state.value = _state.value.copy(availableWeeks = weeks)
                        }
                    }
                    selectWeek(nearestWeek)
                }
            }
        }
    }

    fun selectWeek(week: Int) {
        _state.value = _state.value.copy(selectedWeek = week)
        viewModelScope.launch {
            milestoneRepo.getMilestonesByWeek(babyId, week).collect { milestones ->
                _state.value = _state.value.copy(milestones = milestones)
                checkAdvisory()
            }
        }
    }

    fun updateStatus(milestone: Milestone, status: MilestoneStatus) {
        viewModelScope.launch {
            milestoneRepo.updateStatus(milestone.id, status)
            if (status == MilestoneStatus.ACHIEVED) {
                _state.value = _state.value.copy(showConfetti = true, achievedCount = _state.value.achievedCount + 1)
            }
            checkAdvisory()
        }
    }

    fun dismissConfetti() { _state.value = _state.value.copy(showConfetti = false) }

    private suspend fun checkAdvisory() {
        val notYetCount = milestoneRepo.getNotYetCount(babyId, (_state.value.selectedWeek - 4).coerceAtLeast(1))
        _state.value = _state.value.copy(showAdvisory = notYetCount >= 3)
    }
}
