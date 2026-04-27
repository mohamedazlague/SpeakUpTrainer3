package com.speakup.trainer.ui.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.speakup.trainer.domain.model.PracticeMode
import com.speakup.trainer.domain.model.UserResponse
import com.speakup.trainer.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * History screen showing all past practice sessions and progress stats.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("History & Progress", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {

            // ── Stats row ───────────────────────────────────────────────────
            item {
                StatsRow(uiState)
            }

            if (uiState.responses.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(top = 64.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.HistoryEdu, null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
                            Spacer(Modifier.height(12.dp))
                            Text("No sessions yet", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
                            Text("Generate a topic and start practising!", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
                        }
                    }
                }
            } else {
                item {
                    Text("Past Sessions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                }
                items(uiState.responses, key = { it.id }) { response ->
                    ResponseCard(response = response, onDelete = { viewModel.deleteResponse(response) })
                }
            }

            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

// ─── Stats summary ────────────────────────────────────────────────────────────

@Composable
private fun StatsRow(uiState: HistoryUiState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard("🎙️", "${uiState.totalSpeakingSessions}", "Speaking\nSessions", SecondaryTeal, Modifier.weight(1f))
        StatCard("✍️", "${uiState.totalWritingSessions}", "Writing\nSessions", PurpleAccent, Modifier.weight(1f))
        StatCard("📝", "${uiState.totalWords}", "Total\nWords", PrimaryBlue, Modifier.weight(1f))
    }
}

@Composable
private fun StatCard(emoji: String, value: String, label: String, color: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(emoji, style = MaterialTheme.typography.titleLarge)
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = color)
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), maxLines = 2)
        }
    }
}

// ─── Response Card ────────────────────────────────────────────────────────────

@Composable
private fun ResponseCard(
    response: UserResponse,
    onDelete: () -> Unit
) {
    val isSpeaking = response.mode == PracticeMode.SPEAKING
    val accentColor = if (isSpeaking) SecondaryTeal else PurpleAccent
    var showFeedback by remember { mutableStateOf(false) }

    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mode badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(accentColor.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text  = if (isSpeaking) "🎙️ Speaking" else "✍️ Writing",
                        style = MaterialTheme.typography.labelMedium,
                        color = accentColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(Modifier.weight(1f))
                // Delete button
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
                }
            }

            // Title
            Text(
                text  = response.topicTitle,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )

            // Stats
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                if (isSpeaking) {
                    MetaChip(Icons.Default.Timer, formatDuration(response.durationSeconds))
                } else {
                    MetaChip(Icons.Default.TextFields, "${response.wordCount} words")
                }
                MetaChip(Icons.Default.CalendarToday, formatDate(response.createdAt))
            }

            // Feedback toggle
            if (response.feedback.isNotBlank()) {
                TextButton(
                    onClick  = { showFeedback = !showFeedback },
                    modifier = Modifier.padding(0.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = if (showFeedback) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text  = if (showFeedback) "Hide Feedback" else "Show Feedback",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                AnimatedVisibility(visible = showFeedback, enter = fadeIn() + slideInVertically()) {
                    Text(
                        text  = response.feedback,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.background)
                            .padding(10.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MetaChip(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
    }
}

// ─── Utility ─────────────────────────────────────────────────────────────────

private fun formatDuration(seconds: Int): String {
    val m = seconds / 60; val s = seconds % 60
    return "%02d:%02d".format(m, s)
}

private fun formatDate(epochMillis: Long): String {
    val sdf = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
    return sdf.format(Date(epochMillis))
}
