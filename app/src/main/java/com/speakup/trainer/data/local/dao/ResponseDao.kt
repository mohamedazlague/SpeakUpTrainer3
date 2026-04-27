package com.speakup.trainer.data.local.dao

import androidx.room.*
import com.speakup.trainer.data.local.entity.ResponseEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for user response (recording / writing) database operations.
 */
@Dao
interface ResponseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResponse(response: ResponseEntity): Long

    @Query("SELECT * FROM responses ORDER BY createdAt DESC")
    fun getAllResponses(): Flow<List<ResponseEntity>>

    @Query("SELECT * FROM responses WHERE id = :id")
    suspend fun getResponseById(id: Long): ResponseEntity?

    @Query("SELECT * FROM responses WHERE topicId = :topicId")
    fun getResponsesForTopic(topicId: Long): Flow<List<ResponseEntity>>

    @Query("SELECT COUNT(*) FROM responses WHERE mode = 'SPEAKING'")
    suspend fun getSpeakingSessionCount(): Int

    @Query("SELECT COUNT(*) FROM responses WHERE mode = 'WRITING'")
    suspend fun getWritingSessionCount(): Int

    @Query("SELECT SUM(wordCount) FROM responses WHERE mode = 'WRITING'")
    suspend fun getTotalWordCount(): Int?

    @Query("SELECT SUM(durationSeconds) FROM responses WHERE mode = 'SPEAKING'")
    suspend fun getTotalSpeakingSeconds(): Int?

    @Delete
    suspend fun deleteResponse(response: ResponseEntity)
}
