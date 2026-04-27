package com.speakup.trainer.ui.writing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.speakup.trainer.data.repository.ResponseRepository
import com.speakup.trainer.data.repository.TopicRepository
import com.speakup.trainer.domain.model.PracticeMode
import com.speakup.trainer.domain.model.Topic
import com.speakup.trainer.domain.model.UserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WritingUiState(
    val topic: Topic? = null,
    val text: String = "",
    val wordCount: Int = 0,
    val isSaved: Boolean = false,
    val feedback: String = "",
    val error: String? = null
)

/**
 * ViewModel for the Writing practice screen.
 * Tracks text input, word count, and persists the essay via [ResponseRepository].
 */
@HiltViewModel
class WritingViewModel @Inject constructor(
    private val topicRepository: TopicRepository,
    private val responseRepository: ResponseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WritingUiState())
    val uiState: StateFlow<WritingUiState> = _uiState.asStateFlow()

    fun loadTopic(topicId: Long) {
        viewModelScope.launch {
            topicRepository.savedTopics.collect { topics ->
                val found = topics.firstOrNull { it.id == topicId }
                if (found != null) _uiState.value = _uiState.value.copy(topic = found)
            }
        }
    }

    fun onTextChanged(newText: String) {
        val wc = countWords(newText)
        _uiState.value = _uiState.value.copy(text = newText, wordCount = wc)
    }

    /**
     * Save the writing to the database and generate basic feedback.
     */
    fun saveResponse() {
        val topic = _uiState.value.topic ?: return
        val text  = _uiState.value.text
        val wc    = _uiState.value.wordCount
        if (text.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Please write something before saving.")
            return
        }
        val feedback = generateFeedback(wc, text)
        viewModelScope.launch {
            try {
                responseRepository.saveResponse(
                    UserResponse(
                        topicId    = topic.id,
                        topicTitle = topic.title,
                        mode       = PracticeMode.WRITING,
                        writingText = text,
                        wordCount  = wc,
                        feedback   = feedback
                    )
                )
                _uiState.value = _uiState.value.copy(isSaved = true, feedback = feedback, error = null)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun countWords(text: String): Int =
        text.trim().split("\\s+".toRegex()).count { it.isNotEmpty() }

    private fun generateFeedback(wordCount: Int, text: String): String = buildString {
        // Word count check
        when {
            wordCount < 150 -> append("⚠️ Your essay is too short ($wordCount words). Aim for at least 250 words.\n\n")
            wordCount < 250 -> append("📝 You wrote $wordCount words. Try to reach 250 for full marks.\n\n")
            wordCount < 350 -> append("✅ Great job! $wordCount words — within the IELTS target range.\n\n")
            else            -> append("🌟 Excellent! $wordCount words — a comprehensive essay.\n\n")
        }
        // Paragraph check (rough heuristic)
        val paraCount = text.split("\n\n").count { it.isNotBlank() }
        if (paraCount < 3) {
            append("💡 Structure: Try to use at least 3 paragraphs (Intro, Body, Conclusion).\n\n")
        } else {
            append("✅ Good paragraph structure detected.\n\n")
        }
        // Vocabulary hints
        append("📌 Tips:\n")
        append("• Use academic vocabulary and avoid repetition\n")
        append("• Link ideas with cohesive devices (However, Furthermore, As a result)\n")
        append("• Support each argument with an example or statistic\n")
        append("• Re-read and correct grammar / spelling errors")
    }
}
