package com.shishusneh.ui.growth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shishusneh.data.constants.WHOGrowthData
import com.shishusneh.data.repository.BabyProfileRepository
import com.shishusneh.data.repository.GrowthRepository
import com.shishusneh.domain.model.BabyProfile
import com.shishusneh.domain.model.Gender
import com.shishusneh.domain.model.GrowthEntry
import com.shishusneh.util.AgeCalculator
import com.shishusneh.util.InputValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GrowthState(
    val profile: BabyProfile? = null,
    val entries: List<GrowthEntry> = emptyList(),
    val showAddDialog: Boolean = false,
    val newWeight: String = "",
    val newHeight: String = "",
    val newDate: Long = System.currentTimeMillis(),
    val weightError: String? = null,
    val heightError: String? = null,
    val dateError: String? = null,
    val weightWarning: String? = null,
    val percentileLines: Map<String, List<Pair<Int, Double>>> = emptyMap(),
    val isLoading: Boolean = true
)

@HiltViewModel
class GrowthViewModel @Inject constructor(
    private val profileRepo: BabyProfileRepository,
    private val growthRepo: GrowthRepository
) : ViewModel() {
    private val _state = MutableStateFlow(GrowthState())
    val state: StateFlow<GrowthState> = _state.asStateFlow()

    init { loadData() }

    private fun loadData() {
        viewModelScope.launch {
            profileRepo.getActiveProfile().collect { profile ->
                if (profile != null) {
                    val isMale = profile.gender == Gender.MALE
                    val lines = WHOGrowthData.getWeightPercentileLines(isMale)
                    _state.value = _state.value.copy(profile = profile, percentileLines = lines, isLoading = false)
                    growthRepo.getAllEntries(profile.id).collect { entries ->
                        val enriched = entries.map { entry ->
                            val ageMonths = AgeCalculator.getAgeInMonthsAtDate(profile.dateOfBirth, entry.date).toInt().coerceIn(0, 12)
                            val percentile = WHOGrowthData.getWeightPercentile(entry.weightKg.toDouble(), ageMonths, isMale)
                            val zone = WHOGrowthData.getGrowthZone(percentile)
                            entry.copy(percentile = percentile, growthZone = zone.name)
                        }
                        _state.value = _state.value.copy(entries = enriched)
                    }
                }
            }
        }
    }

    fun showAddDialog() { _state.value = _state.value.copy(showAddDialog = true, newWeight = "", newHeight = "", weightError = null) }
    fun hideAddDialog() { _state.value = _state.value.copy(showAddDialog = false) }
    fun updateNewWeight(w: String) { _state.value = _state.value.copy(newWeight = w, weightError = null) }
    fun updateNewHeight(h: String) { _state.value = _state.value.copy(newHeight = h, heightError = null) }
    fun updateNewDate(d: Long) { _state.value = _state.value.copy(newDate = d, dateError = null) }

    fun addEntry() {
        val state = _state.value
        val profile = state.profile ?: return
        val weight = state.newWeight.toFloatOrNull()
        if (weight == null) { _state.value = state.copy(weightError = "Enter valid weight"); return }
        val wResult = InputValidator.validateWeight(weight)
        if (!wResult.isValid) { _state.value = state.copy(weightError = wResult.errorMessageEn); return }
        val dResult = InputValidator.validateMeasurementDate(state.newDate, profile.dateOfBirth)
        if (!dResult.isValid) { _state.value = state.copy(dateError = dResult.errorMessageEn); return }

        // Check for suspicious weight changes
        val lastEntry = state.entries.lastOrNull()
        if (lastEntry != null) {
            val daysBetween = ((state.newDate - lastEntry.date) / (24 * 60 * 60 * 1000)).toInt()
            val changeResult = InputValidator.checkWeightChange(lastEntry.weightKg, weight, daysBetween)
            if (!changeResult.isValid) {
                _state.value = state.copy(weightWarning = changeResult.errorMessageEn)
            }
        }

        viewModelScope.launch {
            val height = state.newHeight.toFloatOrNull()
            growthRepo.addEntry(GrowthEntry(babyId = profile.id, date = state.newDate, weightKg = weight, heightCm = height))
            _state.value = _state.value.copy(showAddDialog = false, weightWarning = null)
        }
    }

    fun deleteEntry(entry: GrowthEntry) {
        viewModelScope.launch { growthRepo.deleteEntry(entry) }
    }
}
