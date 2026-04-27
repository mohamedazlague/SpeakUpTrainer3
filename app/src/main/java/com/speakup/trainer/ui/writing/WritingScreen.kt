package com.speakup.trainer.ui.writing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.speakup.trainer.ui.theme.*

/**
 * Writing practice screen with a rich text editor, live word counter, and save/feedback flow.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WritingScreen(
    topicId:   Long,
    viewModel: WritingViewModel = hiltViewModel(),
    onBack:    () -> Unit,
    onSaved:   () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(topicId) { viewModel.loadTopic(topicId) }

    // After save navigate to history
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onSaved()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Writing Practice", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::saveResponse) {
                        Icon(Icons.Default.Save, contentDescription = "Save", tint = PurpleAccent)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.height(4.dp))

            // Topic title
            uiState.topic?.let { topic ->
                Text(
                    text  = topic.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
                Surface(
                    shape    = RoundedCornerShape(12.dp),
                    color    = PurpleAccent.copy(alpha = 0.08f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text     = topic.instruction,
                        style    = MaterialTheme.typography.bodyMedium,
                        color    = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            }

            // Word count bar
            WordCountBar(wordCount = uiState.wordCount, target = 250)

            // Text Editor
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 300.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(16.dp)
            ) {
                if (uiState.text.isEmpty()) {
                    Text(
                        text  = "Start writing your response here…",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                }
                BasicTextField(
                    value        = uiState.text,
                    onValueChange = viewModel::onTextChanged,
                    textStyle    = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    cursorBrush  = SolidColor(MaterialTheme.colorScheme.primary),
                    modifier     = Modifier.fillMaxWidth()
                )
            }

            // Error
            uiState.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            // Save button
            Button(
                onClick  = viewModel::saveResponse,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape    = RoundedCornerShape(16.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = PurpleAccent)
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Save & Get Feedback", fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

// ─── Word Count Progress Bar ──────────────────────────────────────────────────

@Composable
private fun WordCountBar(wordCount: Int, target: Int) {
    val progress   = (wordCount.toFloat() / target).coerceIn(0f, 1f)
    val color      = when {
        wordCount < 150 -> HardRed
        wordCount < 250 -> MediumAmber
        else            -> EasyGreen
    }
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text  = "$wordCount words",
                style = MaterialTheme.typography.labelLarge,
                color = color,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text  = "Target: $target",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        LinearProgressIndicator(
            progress = { progress },
            modifier  = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color            = color,
            trackColor       = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}
