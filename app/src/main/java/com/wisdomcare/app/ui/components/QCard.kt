package com.wisdomcare.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wisdomcare.app.ui.theme.QMDJTheme

@Composable
fun QCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = modifier,
        shape = QMDJTheme.shapes.card,
        colors = CardDefaults.cardColors(containerColor = QMDJTheme.colors.card),
        elevation = CardDefaults.cardElevation(defaultElevation = QMDJTheme.spacing.cardElevation),
    ) {
        Column(
            Modifier.padding(QMDJTheme.spacing.cardPadding),
            verticalArrangement = Arrangement.spacedBy(QMDJTheme.spacing.controlGap),
            content = content,
        )
    }
}
