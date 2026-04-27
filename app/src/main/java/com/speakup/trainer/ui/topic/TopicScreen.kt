package com.speakup.trainer.ui.topic

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.speakup.trainer.domain.model.Difficulty
import com.speakup.trainer.domain.model.PracticeMode
import com.speakup.trainer.domain.model.Topic
import com.speakup.trainer.domain.model.TopicCategory
import com.speakup.trainer.ui.theme.*

/**
 * Topic detail screen — shows the full topic card and a "Start" button.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicScreen(
    topicId:  Long,
    mode:     String,
    viewModel: TopicViewModel = hiltViewModel(),
    onStart:  (Long, String) -> Unit,
    onBack:   () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Load topic once
    LaunchedEffect(topicId) { viewModel.loadTopic(topicId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Topic", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    uiState.topic?.let { topic ->
                        IconButton(onClick = viewModel::toggleFavorite) {
                            Icon(
                                imageVector = if (topic.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favourite",
                                tint = if (topic.isFavorite) HardRed else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isLoading -> CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                uiState.error != null -> Text(uiState.error!!, color = MaterialTheme.colorScheme.error)
                uiState.topic != null -> {
                    TopicContent(
                        topic  = uiState.topic!!,
                        mode   = mode,
                        onStart = onStart
                    )
                }
            }
        }
    }
}

@Composable
private fun TopicContent(topic: Topic, mode: String, onStart: (Long, String) -> Unit) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(
        visible = visible,
        enter   = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Mode + Difficulty badges
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ModeBadge(topic.mode)
                DifficultyBadge(topic.difficulty)
                CategoryBadge(topic.category)
            }

            // Topic title card
            Card(
                modifier  = Modifier.fillMaxWidth(),
                shape     = RoundedCornerShape(20.dp),
                colors    = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text  = topic.title,
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                    Text(
                        text  = "📋 Task Instructions",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text  = topic.instruction,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Tip card
            TipCard(mode = topic.mode)

            // Start button
            Button(
                onClick   = { onStart(topic.id, mode) },
                modifier  = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape     = RoundedCornerShape(16.dp),
                colors    = ButtonDefaults.buttonColors(
                    containerColor = if (topic.mode == PracticeMode.SPEAKING) SecondaryTeal else PurpleAccent
                )
            ) {
                Icon(
                    imageVector = if (topic.mode == PracticeMode.SPEAKING) Icons.Default.Mic else Icons.Default.Edit,
                    contentDescription = null
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text  = if (topic.mode == PracticeMode.SPEAKING) "Start Speaking" else "Start Writing",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun TipCard(mode: PracticeMode) {
    val (icon, tip) = if (mode == PracticeMode.SPEAKING) {
        Icons.Default.Lightbulb to "Tip: You'll have 30 seconds to prepare, then 1–2 minutes to speak. Structure your answer with an intro, main points, and conclusion."
    } else {
        Icons.Default.Lightbulb to "Tip: Aim for 250–300 words. Use essay structure: Introduction → Body (2–3 paragraphs) → Conclusion."
    }
    Surface(
        shape  = RoundedCornerShape(14.dp),
        color  = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(
                text  = tip,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// ── Badge helpers ─────────────────────────────────────────────────────────────

@Composable
private fun ModeBadge(mode: PracticeMode) {
    val (label, color) = when (mode) {
        PracticeMode.SPEAKING -> "🎙️ Speaking" to SecondaryTeal
        PracticeMode.WRITING  -> "✍️ Writing"  to PurpleAccent
    }
    Badge(label, color)
}

@Composable
private fun DifficultyBadge(difficulty: Difficulty) {
    val (label, color) = when (difficulty) {
        Difficulty.EASY   -> "Easy"   to EasyGreen
        Difficulty.MEDIUM -> "Medium" to MediumAmber
        Difficulty.HARD   -> "Hard"   to HardRed
    }
    Badge(label, color)
}

@Composable
private fun CategoryBadge(category: TopicCategory) {
    Badge(category.name.replace("_", " "), MaterialTheme.colorScheme.primary)
}

@Composable
private fun Badge(label: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text  = label,
            style = MaterialTheme.typography.labelMedium,
            color = color,
            fontWeight = FontWeight.SemiBold
        )
    }
}
