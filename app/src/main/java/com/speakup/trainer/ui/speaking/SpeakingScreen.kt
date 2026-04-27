package com.speakup.trainer.ui.speaking

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.speakup.trainer.ui.theme.*

/**
 * Speaking practice screen.
 * Shows preparation countdown → recording controls → playback → feedback.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeakingScreen(
    topicId:    Long,
    viewModel:  SpeakingViewModel = hiltViewModel(),
    onBack:     () -> Unit,
    onFinished: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(topicId) { viewModel.loadTopic(topicId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Speaking Practice", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // Topic title
            uiState.topic?.let { topic ->
                Text(
                    text  = topic.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            // Phase-based content
            AnimatedContent(targetState = uiState.phase, label = "phase_anim") { phase ->
                when (phase) {
                    SpeakingPhase.IDLE -> IdleContent(
                        onStart = viewModel::startPreparation
                    )
                    SpeakingPhase.PREPARING -> PrepContent(
                        countdown = uiState.prepCountdown,
                        onSkip    = viewModel::skipPrep
                    )
                    SpeakingPhase.RECORDING -> RecordingContent(
                        duration  = uiState.recDuration,
                        onStop    = viewModel::stopRecording
                    )
                    SpeakingPhase.PLAYBACK  -> PlaybackContent(
                        isPlaying = uiState.isPlaying,
                        onStop    = viewModel::stopPlayback
                    )
                    SpeakingPhase.FINISHED  -> FinishedContent(
                        feedback      = uiState.feedback,
                        hasRecording  = uiState.audioFilePath != null,
                        onPlayback    = viewModel::playRecording,
                        onDone        = onFinished
                    )
                }
            }

            uiState.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Phase sub-composables
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun IdleContent(onStart: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Mic,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = SecondaryTeal
        )
        Text(
            "Ready to practice your speaking?",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Text(
            "You'll have 30 seconds to prepare, then 1–2 minutes to speak.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
        Button(
            onClick = onStart,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SecondaryTeal)
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text("Start Session", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun PrepContent(countdown: Int, onSkip: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            "Preparation Time",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        // Big countdown circle
        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .background(OrangeAccent.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text  = "$countdown",
                fontSize = 52.sp,
                fontWeight = FontWeight.ExtraBold,
                color = OrangeAccent
            )
        }
        Text(
            "Think about key points you want to mention.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
        OutlinedButton(onClick = onSkip) {
            Text("Skip Prep → Record Now")
        }
    }
}

@Composable
private fun RecordingContent(duration: Int, onStop: () -> Unit) {
    val pulse by rememberInfiniteTransition(label = "pulse").animateFloat(
        initialValue = 1f, targetValue = 1.15f,
        animationSpec = infiniteRepeatable(tween(700), RepeatMode.Reverse),
        label = "pulse_scale"
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("🔴  Recording", style = MaterialTheme.typography.titleLarge, color = HardRed)
        Box(
            modifier = Modifier
                .scale(pulse)
                .size(120.dp)
                .clip(CircleShape)
                .background(HardRed.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Mic, contentDescription = null, tint = HardRed, modifier = Modifier.size(52.dp))
        }
        Text(
            text  = formatDuration(duration),
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Button(
            onClick = onStop,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = HardRed)
        ) {
            Icon(Icons.Default.Stop, contentDescription = null, tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text("Stop Recording", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun PlaybackContent(isPlaying: Boolean, onStop: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(Icons.Default.GraphicEq, contentDescription = null, modifier = Modifier.size(72.dp), tint = SecondaryTeal)
        Text("Playing your recording...", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
        Button(onClick = onStop, colors = ButtonDefaults.buttonColors(containerColor = HardRed)) {
            Text("Stop Playback")
        }
    }
}

@Composable
private fun FinishedContent(
    feedback: String,
    hasRecording: Boolean,
    onPlayback: () -> Unit,
    onDone: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(72.dp), tint = EasyGreen)
        Text("Session Complete!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)

        // Feedback card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape    = RoundedCornerShape(16.dp),
            colors   = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("📊 Feedback", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text(feedback, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
            }
        }

        if (hasRecording) {
            OutlinedButton(
                onClick = onPlayback,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Playback Recording")
            }
        }

        Button(
            onClick  = onDone,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape    = RoundedCornerShape(14.dp),
            colors   = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("View History", fontWeight = FontWeight.Bold)
        }
    }
}

// ─── Utility ─────────────────────────────────────────────────────────────────

private fun formatDuration(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return "%02d:%02d".format(m, s)
}
