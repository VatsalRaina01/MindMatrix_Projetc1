package com.shishusneh.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shishusneh.data.repository.BabyProfileRepository
import com.shishusneh.data.repository.UserPreferencesRepository
import com.shishusneh.domain.model.BabyProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefsRepo: UserPreferencesRepository,
    private val profileRepo: BabyProfileRepository
) : ViewModel() {

    val language: StateFlow<String> = prefsRepo.language
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "en")

    val notificationsEnabled: StateFlow<Boolean> = prefsRepo.notificationsEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val isDarkMode: StateFlow<Boolean> = prefsRepo.isDarkMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _profile = MutableStateFlow<BabyProfile?>(null)
    val profile: StateFlow<BabyProfile?> = _profile

    init {
        viewModelScope.launch {
            profileRepo.getActiveProfile().collect { _profile.value = it }
        }
    }

    fun toggleLanguage() {
        viewModelScope.launch {
            val current = language.value
            prefsRepo.setLanguage(if (current == "en") "hi" else "en")
        }
    }

    fun toggleNotifications() {
        viewModelScope.launch {
            prefsRepo.setNotificationsEnabled(!notificationsEnabled.value)
        }
    }

    fun toggleDarkMode() {
        viewModelScope.launch {
            prefsRepo.setDarkMode(!isDarkMode.value)
        }
    }
}
