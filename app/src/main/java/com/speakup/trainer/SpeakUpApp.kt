package com.speakup.trainer

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class annotated with @HiltAndroidApp to enable Hilt dependency injection.
 * This triggers Hilt's code generation and creates the application-level DI component.
 */
@HiltAndroidApp
class SpeakUpApp : Application()
