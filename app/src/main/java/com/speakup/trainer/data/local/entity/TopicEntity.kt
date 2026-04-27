package com.speakup.trainer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a generated/saved topic in the database.
 * Maps to the "topics" table.
 */
@Entity(tableName = "topics")
data class TopicEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val instruction: String,
    val category: String,        // Stored as String name of TopicCategory enum
    val difficulty: String,      // Stored as String name of Difficulty enum
    val mode: String,            // Stored as String name of PracticeMode enum
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
