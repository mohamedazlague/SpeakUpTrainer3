package com.speakup.trainer.data.repository

import com.speakup.trainer.data.local.dao.ResponseDao
import com.speakup.trainer.data.local.entity.ResponseEntity
import com.speakup.trainer.domain.model.PracticeMode
import com.speakup.trainer.domain.model.UserResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for user responses (recordings and writings).
 * Converts between domain [UserResponse] and Room [ResponseEntity].
 */
@Singleton
class ResponseRepository @Inject constructor(
    private val responseDao: ResponseDao
) {

    /** Live stream of all saved responses, newest first. */
    val allResponses: Flow<List<UserResponse>> = responseDao.getAllResponses().map { list ->
        list.map { it.toDomain() }
    }

    /** Insert a new response and return its generated ID. */
    suspend fun saveResponse(response: UserResponse): Long {
        return responseDao.insertResponse(response.toEntity())
    }

    /** Delete a response record. */
    suspend fun deleteResponse(response: UserResponse) {
        responseDao.deleteResponse(response.toEntity())
    }

    /** Aggregate stats for the progress screen. */
    suspend fun getTotalSpeakingSessions() = responseDao.getSpeakingSessionCount()
    suspend fun getTotalWritingSessions() = responseDao.getWritingSessionCount()
    suspend fun getTotalWordCount() = responseDao.getTotalWordCount() ?: 0
    suspend fun getTotalSpeakingSeconds() = responseDao.getTotalSpeakingSeconds() ?: 0

    // ── Mapping helpers ────────────────────────────────────────────────────────

    private fun ResponseEntity.toDomain() = UserResponse(
        id = id,
        topicId = topicId,
        topicTitle = topicTitle,
        mode = PracticeMode.valueOf(mode),
        audioFilePath = audioFilePath,
        writingText = writingText,
        wordCount = wordCount,
        durationSeconds = durationSeconds,
        createdAt = createdAt,
        feedback = feedback
    )

    private fun UserResponse.toEntity() = ResponseEntity(
        id = id,
        topicId = topicId,
        topicTitle = topicTitle,
        mode = mode.name,
        audioFilePath = audioFilePath,
        writingText = writingText,
        wordCount = wordCount,
        durationSeconds = durationSeconds,
        createdAt = createdAt,
        feedback = feedback
    )
}
