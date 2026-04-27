package com.speakup.trainer.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.speakup.trainer.data.repository.TopicRepository
import com.speakup.trainer.domain.model.Difficulty
import com.speakup.trainer.domain.model.PracticeMode
import com.speakup.trainer.domain.model.TopicCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI state for the Home screen.
 */
data class HomeUiState(
    val selectedMode: PracticeMode    = PracticeMode.SPEAKING,
    val selectedDifficulty: Difficulty = Difficulty.MEDIUM,
    val selectedCategory: TopicCategory? = null,  // null = any category
    val isLoading: Boolean = false,
    val generatedTopicId: Long? = null,
    val error: String? = null
)

/**
 * ViewModel for the Home screen.
 * Handles filter selection and topic generation.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val topicRepository: TopicRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun selectMode(mode: PracticeMode) {
        _uiState.value = _uiState.value.copy(selectedMode = mode)
    }

    fun selectDifficulty(difficulty: Difficulty) {
        _uiState.value = _uiState.value.copy(selectedDifficulty = difficulty)
    }

    fun selectCategory(category: TopicCategory?) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    /**
     * Generates a random topic matching current filters and persists it.
     * Sets [HomeUiState.generatedTopicId] so the UI can navigate to the topic screen.
     */
    fun generateTopic() {
        val state = _uiState.value
        _uiState.value = state.copy(isLoading = true, generatedTopicId = null, error = null)

        viewModelScope.launch {
            try {
                val topic = topicRepository.generateAndSave(
                    mode       = state.selectedMode,
                    difficulty = state.selectedDifficulty,
                    category   = state.selectedCategory
                )
                _uiState.value = _uiState.value.copy(
                    isLoading        = false,
                    generatedTopicId = topic.id
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error     = e.message ?: "Unknown error"
                )
            }
        }
    }

    /** Called after navigation so we don't re-navigate on recomposition. */
    fun onNavigated() {
        _uiState.value = _uiState.value.copy(generatedTopicId = null)
    }
}
