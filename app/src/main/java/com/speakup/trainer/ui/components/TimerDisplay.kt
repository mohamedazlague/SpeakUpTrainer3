package com.speakup.trainer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Circular timer display composable.
 *
 * @param seconds   Current timer value to display
 * @param color     Accent color for the ring and text
 * @param label     Optional sub-label (e.g., "Prep" or "Speaking")
 */
@Composable
fun TimerDisplay(
    seconds: Int,
    color: Color,
    label: String = "",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(160.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text  = formatTime(seconds),
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
            if (label.isNotBlank()) {
                Text(
                    text  = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = color.copy(alpha = 0.7f)
                )
            }
        }
    }
}

private fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return "%02d:%02d".format(m, s)
}
