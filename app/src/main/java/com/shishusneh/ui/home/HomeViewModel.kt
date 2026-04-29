package com.shishusneh.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shishusneh.data.constants.FeedingTips
import com.shishusneh.data.repository.*
import com.shishusneh.domain.model.*
import com.shishusneh.util.AgeCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeState(
    val profile: BabyProfile? = null,
    val babyAge: AgeCalculator.BabyAge? = null,
    val latestGrowth: GrowthEntry? = null,
    val nextVaccine: Vaccination? = null,
    val overdueVaccineCount: Int = 0,
    val completedVaccines: Int = 0,
    val totalVaccines: Int = 0,
    val achievedMilestones: Int = 0,
    val totalMilestones: Int = 0,
    val todayTipEn: String = "",
    val todayTipHi: String = "",
    val todayTipCategory: String = "",
    val feedingsToday: Int = 0,
    val growthStreak: Int = 0,
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val profileRepo: BabyProfileRepository,
    private val growthRepo: GrowthRepository,
    private val vaccinationRepo: VaccinationRepository,
    private val milestoneRepo: MilestoneRepository,
    private val feedingRepo: FeedingRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init { loadData() }

    private fun loadData() {
        viewModelScope.launch {
            profileRepo.getActiveProfile().collect { profile ->
                if (profile != null) {
                    val age = AgeCalculator.calculateAge(profile.dateOfBirth)
                    val tip = FeedingTips.getTipForWeek(age.totalWeeks.toInt())

                    // Update overdue status
                    vaccinationRepo.updateOverdueStatus(profile.id, System.currentTimeMillis())

                    val completedV = vaccinationRepo.getCompletedCount(profile.id)
                    val totalV = vaccinationRepo.getTotalCount(profile.id)
                    val achievedM = milestoneRepo.getAchievedCount(profile.id)
                    val totalM = milestoneRepo.getTotalCount(profile.id)
                    val feedCount = feedingRepo.getFeedingCountToday(profile.id)

                    _state.value = _state.value.copy(
                        profile = profile, babyAge = age,
                        completedVaccines = completedV, totalVaccines = totalV,
                        achievedMilestones = achievedM, totalMilestones = totalM,
                        todayTipEn = tip?.contentEn ?: "", todayTipHi = tip?.contentHi ?: "",
                        todayTipCategory = tip?.category ?: "",
                        feedingsToday = feedCount, isLoading = false
                    )

                    // Collect reactive flows
                    launch {
                        growthRepo.getLatestEntry(profile.id).collect { entry ->
                            _state.value = _state.value.copy(latestGrowth = entry)
                        }
                    }
                    launch {
                        vaccinationRepo.getNextUpcoming(profile.id).collect { vax ->
                            _state.value = _state.value.copy(nextVaccine = vax)
                        }
                    }
                    launch {
                        vaccinationRepo.getOverdueVaccinations(profile.id).collect { list ->
                            _state.value = _state.value.copy(overdueVaccineCount = list.size)
                        }
                    }
                }
            }
        }
    }
}
