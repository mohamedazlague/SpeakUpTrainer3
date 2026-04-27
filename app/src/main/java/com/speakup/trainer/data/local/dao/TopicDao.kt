package com.speakup.trainer.data.local.dao

import androidx.room.*
import com.speakup.trainer.data.local.entity.TopicEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for topic-related database operations.
 * All queries that return live data use [Flow] so the UI auto-updates.
 */
@Dao
interface TopicDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopic(topic: TopicEntity): Long

    @Query("SELECT * FROM topics ORDER BY createdAt DESC")
    fun getAllTopics(): Flow<List<TopicEntity>>

    @Query("SELECT * FROM topics WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteTopics(): Flow<List<TopicEntity>>

    @Query("SELECT * FROM topics WHERE id = :id")
    suspend fun getTopicById(id: Long): TopicEntity?

    @Query("UPDATE topics SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Long, isFavorite: Boolean)

    @Delete
    suspend fun deleteTopic(topic: TopicEntity)

    @Query("SELECT COUNT(*) FROM topics")
    suspend fun getTopicCount(): Int
}
