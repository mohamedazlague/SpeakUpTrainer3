package com.speakup.trainer.domain.model

/**
 * Represents a practice mode: Speaking or Writing.
 */
enum class PracticeMode {
    SPEAKING, WRITING
}

/**
 * Difficulty levels for topic filtering.
 */
enum class Difficulty {
    EASY, MEDIUM, HARD
}

/**
 * Topic categories aligned with IELTS speaking/writing subjects.
 */
enum class TopicCategory {
    EDUCATION, WORK, SOCIAL_LIFE, TECHNOLOGY, ENVIRONMENT, HEALTH, CULTURE, TRAVEL, SCIENCE, ECONOMY
}

/**
 * Domain model for an IELTS practice topic.
 *
 * @param id Unique identifier
 * @param title Short title of the topic
 * @param instruction Detailed task instruction shown to the user
 * @param category Topic category
 * @param difficulty Difficulty level
 * @param mode Whether this topic is for speaking or writing practice
 * @param isFavorite Whether the user has starred this topic
 */
data class Topic(
    val id: Long = 0,
    val title: String,
    val instruction: String,
    val category: TopicCategory,
    val difficulty: Difficulty,
    val mode: PracticeMode,
    val isFavorite: Boolean = false
)
