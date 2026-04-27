package com.speakup.trainer.domain.model

/**
 * Represents a user's response (recording or text) to a practice topic.
 *
 * @param id Unique database ID
 * @param topicId FK to the topic answered
 * @param topicTitle Cached title for display in history
 * @param mode Speaking or Writing
 * @param audioFilePath Absolute path to .mp4 recording file (Speaking mode)
 * @param writingText Saved essay/writing response text (Writing mode)
 * @param wordCount Number of words written (Writing mode)
 * @param durationSeconds Duration of the recording in seconds (Speaking mode)
 * @param createdAt Epoch millis timestamp
 * @param feedback AI evaluation feedback string
 */
data class UserResponse(
    val id: Long = 0,
    val topicId: Long,
    val topicTitle: String,
    val mode: PracticeMode,
    val audioFilePath: String? = null,
    val writingText: String? = null,
    val wordCount: Int = 0,
    val durationSeconds: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val feedback: String = ""
)
