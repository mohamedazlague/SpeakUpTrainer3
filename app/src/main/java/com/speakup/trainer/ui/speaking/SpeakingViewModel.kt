package com.speakup.trainer.ui.speaking

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.speakup.trainer.data.repository.ResponseRepository
import com.speakup.trainer.data.repository.TopicRepository
import com.speakup.trainer.domain.model.PracticeMode
import com.speakup.trainer.domain.model.Topic
import com.speakup.trainer.domain.model.UserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

/** Phases of the speaking practice session. */
enum class SpeakingPhase { IDLE, PREPARING, RECORDING, PLAYBACK, FINISHED }

data class SpeakingUiState(
    val topic: Topic? = null,
    val phase: SpeakingPhase = SpeakingPhase.IDLE,
    val prepCountdown: Int = 30,   // Preparation timer (seconds)
    val recDuration: Int = 0,      // Elapsed recording time (seconds)
    val isPlaying: Boolean = false,
    val audioFilePath: String? = null,
    val feedback: String = "",
    val error: String? = null
)

/**
 * ViewModel for the Speaking practice screen.
 * Manages:
 * - Preparation + recording timers
 * - MediaRecorder lifecycle
 * - MediaPlayer playback
 * - Session persistence via [ResponseRepository]
 */
@HiltViewModel
class SpeakingViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val topicRepository: TopicRepository,
    private val responseRepository: ResponseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SpeakingUiState())
    val uiState: StateFlow<SpeakingUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private var audioFile: File? = null

    // ──────────────────────────────────────────────────────────────────────────
    // Public API
    // ──────────────────────────────────────────────────────────────────────────

    fun loadTopic(topicId: Long) {
        viewModelScope.launch {
            topicRepository.savedTopics.collect { topics ->
                val found = topics.firstOrNull { it.id == topicId }
                if (found != null) {
                    _uiState.value = _uiState.value.copy(topic = found)
                }
            }
        }
    }

    /** Start the 30-second preparation countdown. */
    fun startPreparation() {
        _uiState.value = _uiState.value.copy(phase = SpeakingPhase.PREPARING, prepCountdown = 30)
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            for (i in 30 downTo 1) {
                _uiState.value = _uiState.value.copy(prepCountdown = i)
                delay(1000L)
            }
            // Auto-start recording after prep
            startRecording()
        }
    }

    /** Skip prep timer and start recording immediately. */
    fun skipPrep() {
        timerJob?.cancel()
        startRecording()
    }

    /** Stop recording, save response, generate feedback. */
    fun stopRecording() {
        timerJob?.cancel()
        val duration = _uiState.value.recDuration
        stopRecorder()
        val feedback = generateFeedback(duration)
        _uiState.value = _uiState.value.copy(
            phase    = SpeakingPhase.FINISHED,
            feedback = feedback
        )
        saveSession(duration, feedback)
    }

    fun playRecording() {
        val path = _uiState.value.audioFilePath ?: return
        player?.release()
        player = MediaPlayer().apply {
            setDataSource(path)
            prepare()
            start()
            setOnCompletionListener {
                _uiState.value = _uiState.value.copy(isPlaying = false)
            }
        }
        _uiState.value = _uiState.value.copy(isPlaying = true, phase = SpeakingPhase.PLAYBACK)
    }

    fun stopPlayback() {
        player?.stop()
        player?.release()
        player = null
        _uiState.value = _uiState.value.copy(isPlaying = false, phase = SpeakingPhase.FINISHED)
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Private helpers
    // ──────────────────────────────────────────────────────────────────────────

    private fun startRecording() {
        try {
            audioFile = File(context.cacheDir, "rec_${System.currentTimeMillis()}.mp4")
            recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder()
            }.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(audioFile!!.absolutePath)
                prepare()
                start()
            }
            _uiState.value = _uiState.value.copy(
                phase         = SpeakingPhase.RECORDING,
                recDuration   = 0,
                audioFilePath = audioFile!!.absolutePath
            )
            // Elapsed recording timer
            timerJob = viewModelScope.launch {
                var elapsed = 0
                while (true) {
                    delay(1000L)
                    elapsed++
                    _uiState.value = _uiState.value.copy(recDuration = elapsed)
                    if (elapsed >= 120) { stopRecording(); break } // Auto-stop at 2 min
                }
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                error = "Failed to start recording: ${e.message}",
                phase = SpeakingPhase.IDLE
            )
        }
    }

    private fun stopRecorder() {
        try { recorder?.stop() } catch (_: Exception) {}
        recorder?.release()
        recorder = null
    }

    /** Basic rule-based feedback generation. */
    private fun generateFeedback(durationSec: Int): String = buildString {
        when {
            durationSec < 30  -> append("⚠️ You spoke for only $durationSec seconds. Try to aim for at least 1 minute.\n\n")
            durationSec < 60  -> append("✅ Good start! You spoke for ${durationSec}s. Try to reach 1–2 minutes for full marks.\n\n")
            durationSec < 120 -> append("🌟 Excellent! You spoke for ${durationSec} seconds — within the target range.\n\n")
            else              -> append("🏆 Great endurance! You spoke for over ${durationSec}s.\n\n")
        }
        append("💡 Suggestions:\n")
        append("• Use transition words (Firstly, Moreover, In conclusion)\n")
        append("• Avoid filler sounds (um, uh) — replace with pauses\n")
        append("• Vary your speaking speed for emphasis\n")
        append("• Include personal examples to make answers vivid")
    }

    private fun saveSession(durationSec: Int, feedback: String) {
        val topic = _uiState.value.topic ?: return
        viewModelScope.launch {
            responseRepository.saveResponse(
                UserResponse(
                    topicId         = topic.id,
                    topicTitle      = topic.title,
                    mode            = PracticeMode.SPEAKING,
                    audioFilePath   = _uiState.value.audioFilePath,
                    durationSeconds = durationSec,
                    feedback        = feedback
                )
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        stopRecorder()
        player?.release()
    }
}
