package com.Meztido0.Dewgov.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.Meztido0.Dewgov.core.theme.ChromeCyan
import com.Meztido0.Dewgov.core.theme.TextSecondary

@Composable
fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier,
    accent: Color = ChromeCyan
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(
            modifier = Modifier
                .width(16.dp)
                .height(1.dp)
                .background(accent)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
    }
}
