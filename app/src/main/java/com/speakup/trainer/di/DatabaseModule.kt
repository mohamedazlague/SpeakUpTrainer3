package com.speakup.trainer.di

import android.content.Context
import androidx.room.Room
import com.speakup.trainer.data.local.AppDatabase
import com.speakup.trainer.data.local.dao.ResponseDao
import com.speakup.trainer.data.local.dao.TopicDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that provides Room database and DAO dependencies.
 * Installed in [SingletonComponent] so the database lives for the app's lifetime.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "speakup_db"
        ).build()
    }

    @Provides
    fun provideTopicDao(db: AppDatabase): TopicDao = db.topicDao()

    @Provides
    fun provideResponseDao(db: AppDatabase): ResponseDao = db.responseDao()
}
