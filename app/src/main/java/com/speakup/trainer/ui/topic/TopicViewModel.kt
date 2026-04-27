package com.speakup.trainer.ui.topic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.speakup.trainer.data.repository.TopicRepository
import com.speakup.trainer.domain.model.Topic
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TopicUiState(
    val topic: Topic? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

/**
 * ViewModel for the Topic detail screen.
 * Loads the topic from the database by ID.
 */
@HiltViewModel
class TopicViewModel @Inject constructor(
    private val topicRepository: TopicRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TopicUiState())
    val uiState: StateFlow<TopicUiState> = _uiState.asStateFlow()

    /**
     * Load topic from in-memory provider by matching on topic in saved topics.
     * Since topics are generated and immediately inserted into Room, we read from the repo.
     */
    fun loadTopic(topicId: Long) {
        viewModelScope.launch {
            try {
                // Topics are saved in Room at generation time; collect first emission matching id.
                topicRepository.savedTopics.collect { topics ->
                    val found = topics.firstOrNull { it.id == topicId }
                    if (found != null) {
                        _uiState.value = TopicUiState(topic = found, isLoading = false)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = TopicUiState(isLoading = false, error = e.message)
            }
        }
    }

    fun toggleFavorite() {
        val topic = _uiState.value.topic ?: return
        viewModelScope.launch {
            topicRepository.toggleFavorite(topic.id, topic.isFavorite)
        }
    }
}
