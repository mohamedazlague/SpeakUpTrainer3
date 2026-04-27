package com.speakup.trainer.data.repository

import com.speakup.trainer.data.local.dao.TopicDao
import com.speakup.trainer.data.local.entity.TopicEntity
import com.speakup.trainer.domain.TopicProvider
import com.speakup.trainer.domain.model.Difficulty
import com.speakup.trainer.domain.model.PracticeMode
import com.speakup.trainer.domain.model.Topic
import com.speakup.trainer.domain.model.TopicCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for topic data. Bridges the domain layer and Room DAO.
 * Uses [TopicProvider] for in-memory topic generation and [TopicDao] for persistence.
 */
@Singleton
class TopicRepository @Inject constructor(
    private val topicDao: TopicDao
) {

    /** Observe all saved (generated) topics as a stream. */
    val savedTopics: Flow<List<Topic>> = topicDao.getAllTopics().map { list ->
        list.map { it.toDomain() }
    }

    /** Observe only favourite topics. */
    val favoriteTopics: Flow<List<Topic>> = topicDao.getFavoriteTopics().map { list ->
        list.map { it.toDomain() }
    }

    /**
     * Generate a random topic from the predefined list, filtered by the given params,
     * then persist it to the database and return it.
     */
    suspend fun generateAndSave(
        mode: PracticeMode?,
        difficulty: Difficulty?,
        category: TopicCategory?
    ): Topic {
        val topic = TopicProvider.getRandom(mode, difficulty, category)
        val entity = topic.toEntity()
        val newId = topicDao.insertTopic(entity)
        return topic.copy(id = newId)
    }

    /** Toggle favourite status of a saved topic. */
    suspend fun toggleFavorite(topicId: Long, currentStatus: Boolean) {
        topicDao.updateFavorite(topicId, !currentStatus)
    }

    // ── Mapping helpers ────────────────────────────────────────────────────────

    private fun TopicEntity.toDomain() = Topic(
        id = id,
        title = title,
        instruction = instruction,
        category = TopicCategory.valueOf(category),
        difficulty = Difficulty.valueOf(difficulty),
        mode = PracticeMode.valueOf(mode),
        isFavorite = isFavorite
    )

    private fun Topic.toEntity() = TopicEntity(
        id = id,
        title = title,
        instruction = instruction,
        category = category.name,
        difficulty = difficulty.name,
        mode = mode.name,
        isFavorite = isFavorite
    )
}
