package com.speakup.trainer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.speakup.trainer.data.local.dao.ResponseDao
import com.speakup.trainer.data.local.dao.TopicDao
import com.speakup.trainer.data.local.entity.ResponseEntity
import com.speakup.trainer.data.local.entity.TopicEntity

/**
 * Room database for SpeakUp Trainer.
 * Holds two tables: topics and responses.
 *
 * Increment [version] and add a Migration whenever the schema changes.
 */
@Database(
    entities = [TopicEntity::class, ResponseEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun topicDao(): TopicDao
    abstract fun responseDao(): ResponseDao
}
