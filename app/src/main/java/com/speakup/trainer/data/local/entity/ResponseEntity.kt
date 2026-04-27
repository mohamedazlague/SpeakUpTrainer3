package com.speakup.trainer.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room entity representing a saved user response (recording or writing).
 * Has a foreign key to [TopicEntity] and maps to the "responses" table.
 */
@Entity(
    tableName = "responses",
    foreignKeys = [
        ForeignKey(
            entity = TopicEntity::class,
            parentColumns = ["id"],
            childColumns = ["topicId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("topicId")]
)
data class ResponseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val topicId: Long,
    val topicTitle: String,
    val mode: String,               // "SPEAKING" or "WRITING"
    val audioFilePath: String? = null,
    val writingText: String? = null,
    val wordCount: Int = 0,
    val durationSeconds: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val feedback: String = ""
)
