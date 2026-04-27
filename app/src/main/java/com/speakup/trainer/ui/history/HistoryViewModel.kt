package com.speakup.trainer.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.speakup.trainer.data.repository.ResponseRepository
import com.speakup.trainer.domain.model.UserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HistoryUiState(
    val responses: List<UserResponse> = emptyList(),
    val totalSpeakingSessions: Int = 0,
    val totalWritingSessions: Int = 0,
    val totalWords: Int = 0,
    val totalSpeakingSeconds: Int = 0,
    val isLoading: Boolean = true
)

/**
 * ViewModel for the History screen.
 * Aggregates all past sessions and stats from [ResponseRepository].
 */
@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val responseRepository: ResponseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            responseRepository.allResponses.collect { responses ->
                _uiState.value = _uiState.value.copy(
                    responses = responses,
                    isLoading = false
                )
            }
        }
        loadStats()
    }

    private fun loadStats() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                totalSpeakingSessions = responseRepository.getTotalSpeakingSessions(),
                totalWritingSessions  = responseRepository.getTotalWritingSessions(),
                totalWords            = responseRepository.getTotalWordCount(),
                totalSpeakingSeconds  = responseRepository.getTotalSpeakingSeconds()
            )
        }
    }

    fun deleteResponse(response: UserResponse) {
        viewModelScope.launch {
            responseRepository.deleteResponse(response)
            loadStats()
        }
    }
}
