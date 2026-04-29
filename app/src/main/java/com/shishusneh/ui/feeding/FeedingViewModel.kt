package com.shishusneh.ui.feeding

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

data class FeedingState(
    val dailyTipEn: String = "",
    val dailyTipHi: String = "",
    val tipTitleEn: String = "",
    val tipTitleHi: String = "",
    val mythBusters: List<FeedingTips.MythBuster> = FeedingTips.mythBusters,
    val recentLogs: List<FeedingLog> = emptyList(),
    val todayCount: Int = 0,
    val babyAgeWeeks: Int = 0,
    val isLoading: Boolean = true,
    // Add feeding dialog state
    val showAddDialog: Boolean = false,
    val selectedFeedType: FeedType = FeedType.BREAST,
    val durationMins: String = "",
    val quantityMl: String = "",
    val foodDescription: String = "",
    val selectedSide: BreastSide = BreastSide.LEFT,
    val notes: String = ""
)

data class ChatState(
    val conversations: List<GenAIConversation> = emptyList(),
    val currentQuery: String = "",
    val isTyping: Boolean = false,
    val babyAgeWeeks: Int = 0
)

@HiltViewModel
class FeedingViewModel @Inject constructor(
    private val profileRepo: BabyProfileRepository,
    private val feedingRepo: FeedingRepository,
    private val genAIRepo: GenAIRepository
) : ViewModel() {
    private val _feedingState = MutableStateFlow(FeedingState())
    val feedingState: StateFlow<FeedingState> = _feedingState.asStateFlow()

    private val _chatState = MutableStateFlow(ChatState())
    val chatState: StateFlow<ChatState> = _chatState.asStateFlow()

    private var babyId: Long = 0

    init { loadData() }

    private fun loadData() {
        viewModelScope.launch {
            profileRepo.getActiveProfile().collect { profile ->
                if (profile != null) {
                    babyId = profile.id
                    val age = AgeCalculator.calculateAge(profile.dateOfBirth)
                    val weeks = age.totalWeeks.toInt()
                    val tip = FeedingTips.getTipForWeek(weeks)
                    val count = feedingRepo.getFeedingCountToday(profile.id)

                    _feedingState.value = _feedingState.value.copy(
                        dailyTipEn = tip?.contentEn ?: "", dailyTipHi = tip?.contentHi ?: "",
                        tipTitleEn = tip?.titleEn ?: "", tipTitleHi = tip?.titleHi ?: "",
                        todayCount = count, babyAgeWeeks = weeks, isLoading = false
                    )
                    _chatState.value = _chatState.value.copy(babyAgeWeeks = weeks)

                    launch {
                        feedingRepo.getRecentLogs(profile.id, 10).collect { logs ->
                            _feedingState.value = _feedingState.value.copy(recentLogs = logs)
                        }
                    }
                    launch {
                        genAIRepo.getRecentConversations(profile.id, 50).collect { convos ->
                            _chatState.value = _chatState.value.copy(conversations = convos)
                        }
                    }
                }
            }
        }
    }

    // === Feeding log functions ===

    fun showAddFeedingDialog() {
        _feedingState.value = _feedingState.value.copy(
            showAddDialog = true,
            selectedFeedType = FeedType.BREAST,
            durationMins = "",
            quantityMl = "",
            foodDescription = "",
            selectedSide = BreastSide.LEFT,
            notes = ""
        )
    }

    fun hideAddFeedingDialog() {
        _feedingState.value = _feedingState.value.copy(showAddDialog = false)
    }

    fun updateFeedType(type: FeedType) {
        _feedingState.value = _feedingState.value.copy(selectedFeedType = type)
    }

    fun updateDuration(mins: String) {
        _feedingState.value = _feedingState.value.copy(durationMins = mins)
    }

    fun updateQuantity(ml: String) {
        _feedingState.value = _feedingState.value.copy(quantityMl = ml)
    }

    fun updateFoodDescription(desc: String) {
        _feedingState.value = _feedingState.value.copy(foodDescription = desc)
    }

    fun updateSide(side: BreastSide) {
        _feedingState.value = _feedingState.value.copy(selectedSide = side)
    }

    fun updateFeedingNotes(notes: String) {
        _feedingState.value = _feedingState.value.copy(notes = notes)
    }

    fun addFeedingLog() {
        val s = _feedingState.value
        if (babyId == 0L) return

        viewModelScope.launch {
            val log = FeedingLog(
                babyId = babyId,
                timestamp = System.currentTimeMillis(),
                feedType = s.selectedFeedType,
                durationMins = s.durationMins.toIntOrNull(),
                quantityMl = s.quantityMl.toIntOrNull(),
                foodDescription = s.foodDescription.ifBlank { null },
                side = if (s.selectedFeedType == FeedType.BREAST) s.selectedSide else null,
                notes = s.notes.ifBlank { null }
            )
            feedingRepo.addLog(log)

            // Refresh today count
            val count = feedingRepo.getFeedingCountToday(babyId)
            _feedingState.value = _feedingState.value.copy(
                showAddDialog = false,
                todayCount = count
            )
        }
    }

    fun deleteFeedingLog(log: FeedingLog) {
        viewModelScope.launch {
            feedingRepo.deleteLog(log)
            val count = feedingRepo.getFeedingCountToday(babyId)
            _feedingState.value = _feedingState.value.copy(todayCount = count)
        }
    }

    // === Chat functions ===

    fun updateQuery(query: String) { _chatState.value = _chatState.value.copy(currentQuery = query) }

    fun sendMessage() {
        val query = _chatState.value.currentQuery.trim()
        if (query.isEmpty()) return
        _chatState.value = _chatState.value.copy(currentQuery = "", isTyping = true)

        viewModelScope.launch {
            genAIRepo.askQuestion(babyId, query, "en", _chatState.value.babyAgeWeeks)
            _chatState.value = _chatState.value.copy(isTyping = false)
        }
    }

    fun updateFeedback(id: Long, rating: Int) {
        viewModelScope.launch { genAIRepo.updateFeedback(id, rating) }
    }
}
