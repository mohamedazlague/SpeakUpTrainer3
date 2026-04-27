package com.speakup.trainer.ui.home

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.speakup.trainer.domain.model.Difficulty
import com.speakup.trainer.domain.model.PracticeMode
import com.speakup.trainer.domain.model.TopicCategory
import com.speakup.trainer.ui.theme.*

/**
 * Home screen — entry point of the app.
 * Shows mode/difficulty/category selectors and the Generate Topic button.
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onTopicGenerated: (Long, String) -> Unit,
    onHistoryClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Navigate when a topic is generated
    LaunchedEffect(uiState.generatedTopicId) {
        uiState.generatedTopicId?.let { id ->
            onTopicGenerated(id, uiState.selectedMode.name)
            viewModel.onNavigated()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 56.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // ── Header ────────────────────────────────────────────────────────
            HomeHeader(onHistoryClick = onHistoryClick)

            // ── Mode Selector ─────────────────────────────────────────────────
            SectionTitle("Practice Mode")
            ModeSelector(
                selected = uiState.selectedMode,
                onSelect = viewModel::selectMode
            )

            // ── Difficulty Selector ───────────────────────────────────────────
            SectionTitle("Difficulty")
            DifficultySelector(
                selected = uiState.selectedDifficulty,
                onSelect = viewModel::selectDifficulty
            )

            // ── Category Selector ─────────────────────────────────────────────
            SectionTitle("Category  (optional)")
            CategorySelector(
                selected = uiState.selectedCategory,
                onSelect = viewModel::selectCategory
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ── Generate Button ───────────────────────────────────────────────
            GenerateButton(
                isLoading = uiState.isLoading,
                onClick   = viewModel::generateTopic
            )

            // ── Error Message ─────────────────────────────────────────────────
            uiState.error?.let { error ->
                Text(
                    text  = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Sub-components
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun HomeHeader(onHistoryClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text  = "SpeakUp",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text  = "IELTS Practice Trainer",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        IconButton(
            onClick = onHistoryClick,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = "History",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text  = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurface,
        letterSpacing = 0.8.sp
    )
}

@Composable
private fun ModeSelector(
    selected: PracticeMode,
    onSelect: (PracticeMode) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ModeChip(
            label     = "🎙️  Speaking",
            isSelected = selected == PracticeMode.SPEAKING,
            color      = SecondaryTeal,
            modifier   = Modifier.weight(1f),
            onClick    = { onSelect(PracticeMode.SPEAKING) }
        )
        ModeChip(
            label     = "✍️  Writing",
            isSelected = selected == PracticeMode.WRITING,
            color      = PurpleAccent,
            modifier   = Modifier.weight(1f),
            onClick    = { onSelect(PracticeMode.WRITING) }
        )
    }
}

@Composable
private fun ModeChip(
    label: String,
    isSelected: Boolean,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected) color.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant
    val borderColor = if (isSelected) color else Color.Transparent

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(2.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text  = label,
            style = MaterialTheme.typography.titleMedium,
            color = if (isSelected) color else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun DifficultySelector(
    selected: Difficulty,
    onSelect: (Difficulty) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        listOf(
            Difficulty.EASY   to EasyGreen,
            Difficulty.MEDIUM to MediumAmber,
            Difficulty.HARD   to HardRed
        ).forEach { (diff, color) ->
            FilterChip(
                selected = selected == diff,
                onClick  = { onSelect(diff) },
                label    = {
                    Text(
                        text = diff.name,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (selected == diff) color else MaterialTheme.colorScheme.onSurface
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = color.copy(alpha = 0.18f),
                    selectedLabelColor     = color
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor         = if (selected == diff) color else MaterialTheme.colorScheme.outline,
                    selectedBorderColor = color,
                    enabled = true,
                    selected = selected == diff
                ),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun CategorySelector(
    selected: TopicCategory?,
    onSelect: (TopicCategory?) -> Unit
) {
    val categories = listOf<Pair<TopicCategory?, String>>(
        null to "🎲 Any",
        TopicCategory.EDUCATION    to "🎓 Education",
        TopicCategory.WORK         to "💼 Work",
        TopicCategory.SOCIAL_LIFE  to "👥 Social",
        TopicCategory.TECHNOLOGY   to "💻 Tech",
        TopicCategory.ENVIRONMENT  to "🌿 Environment",
        TopicCategory.HEALTH       to "❤️ Health",
        TopicCategory.CULTURE      to "🎭 Culture",
        TopicCategory.TRAVEL       to "✈️ Travel",
        TopicCategory.SCIENCE      to "🔬 Science",
        TopicCategory.ECONOMY      to "📊 Economy"
    )
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        categories.chunked(3).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { (cat, label) ->
                    val isActive = selected == cat
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isActive) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .border(
                                width = if (isActive) 2.dp else 0.dp,
                                color = if (isActive) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { onSelect(cat) }
                            .padding(vertical = 10.dp, horizontal = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text  = label,
                            style = MaterialTheme.typography.labelMedium,
                            color = if (isActive) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface,
                            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                            maxLines = 1
                        )
                    }
                }
                // Fill empty slots in last row
                repeat(3 - row.size) { Spacer(modifier = Modifier.weight(1f)) }
            }
        }
    }
}

@Composable
private fun GenerateButton(isLoading: Boolean, onClick: () -> Unit) {
    Button(
        onClick  = onClick,
        enabled  = !isLoading,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        shape  = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
    ) {
        AnimatedContent(targetState = isLoading, label = "btn_anim") { loading ->
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color    = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 3.dp
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null)
                    Text(
                        text  = "Generate Topic",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
