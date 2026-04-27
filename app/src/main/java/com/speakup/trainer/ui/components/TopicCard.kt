package com.speakup.trainer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.speakup.trainer.domain.model.Difficulty
import com.speakup.trainer.domain.model.PracticeMode
import com.speakup.trainer.domain.model.Topic
import com.speakup.trainer.ui.theme.*

/**
 * Reusable card component that displays a topic's key information.
 * Used in topic lists and as a preview card.
 *
 * @param topic The topic to display
 * @param onFavoriteClick Callback when the favourite button is tapped
 */
@Composable
fun TopicCard(
    topic: Topic,
    modifier: Modifier = Modifier,
    onFavoriteClick: (() -> Unit)? = null
) {
    val modeColor = if (topic.mode == PracticeMode.SPEAKING) SecondaryTeal else PurpleAccent
    val diffColor = when (topic.difficulty) {
        Difficulty.EASY   -> EasyGreen
        Difficulty.MEDIUM -> MediumAmber
        Difficulty.HARD   -> HardRed
    }

    Card(
        modifier  = modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mode chip
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(modeColor.copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text  = if (topic.mode == PracticeMode.SPEAKING) "🎙️ Speaking" else "✍️ Writing",
                        style = MaterialTheme.typography.labelMedium,
                        color = modeColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                onFavoriteClick?.let {
                    IconButton(onClick = it, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = if (topic.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favourite",
                            tint = if (topic.isFavorite) HardRed else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                }
            }

            // Title
            Text(
                text  = topic.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )

            // Instruction preview (max 2 lines)
            Text(
                text     = topic.instruction,
                style    = MaterialTheme.typography.bodySmall,
                color    = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                maxLines = 2
            )

            // Bottom tags
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SmallTag(topic.difficulty.name, diffColor)
                SmallTag(topic.category.name.replace("_", " "), MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
private fun SmallTag(label: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.13f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text  = label,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Medium
        )
    }
}
