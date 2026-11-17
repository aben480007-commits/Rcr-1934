package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.data.local.StandingRowEntity
import com.example.ui.components.PlaceholderInfoBanner
import com.example.ui.localization.LocalRcrStrings
import com.example.ui.theme.LocalRcrColors
import com.example.ui.theme.RcrSpacing

@Composable
fun StandingsScreen(standings: List<StandingRowEntity>) {
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.primaryBackground)
            .testTag("standings_screen"),
        contentPadding = PaddingValues(
            start = RcrSpacing.ScreenHorizontal,
            end = RcrSpacing.ScreenHorizontal,
            top = RcrSpacing.ScreenVertical,
            bottom = RcrSpacing.BottomScreenBuffer
        ),
        verticalArrangement = Arrangement.spacedBy(RcrSpacing.CardGap)
    ) {
        if (standings.any { it.isPlaceholder }) {
            item {
                PlaceholderInfoBanner(text = strings.standingsPlaceholderBanner)
            }
        }
        item {
            StandingsTableCard(standings = standings)
        }
    }
}
