package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.data.local.MatchEntity
import com.example.data.local.StandingRowEntity
import com.example.ui.components.EmptyStateCard
import com.example.ui.components.MatchCard
import com.example.ui.components.OpponentClubBadge
import com.example.ui.components.PlaceholderInfoBanner
import com.example.ui.components.RcrClubCrest
import com.example.ui.localization.LocalRcrStrings
import com.example.ui.theme.JetBrainsMonoFamily
import com.example.ui.theme.LocalRcrColors
import com.example.ui.theme.RcrDeepGreen
import com.example.ui.theme.RcrPureWhite
import com.example.ui.theme.RcrSpacing

@Composable
fun MatchesScreen(
    matches: List<MatchEntity>,
    standings: List<StandingRowEntity>,
    onOpenMatchDetail: (Long) -> Unit
) {
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current

    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf(strings.matchTabUpcoming, strings.matchTabResults, strings.matchTabStandings)

    val upcomingMatches = matches.filter { it.status == "À venir" || it.status == "En direct" }
    val completedMatches = matches.filter { it.status == "Terminé" }.sortedByDescending { it.kickoffTimestampMillis }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.primaryBackground)
            .testTag("matches_screen")
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = colors.secondaryBackground,
            contentColor = colors.sectionHeading,
            indicator = { tabPositions ->
                if (selectedTab < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = colors.primaryGreen
                    )
                }
            },
            divider = { HorizontalDivider(color = colors.border) }
        ) {
            tabs.forEachIndexed { index, label ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    selectedContentColor = colors.sectionHeading,
                    unselectedContentColor = colors.secondaryText,
                    text = {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    },
                    modifier = Modifier.testTag("matches_tab_$index")
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = RcrSpacing.ScreenHorizontal,
                end = RcrSpacing.ScreenHorizontal,
                top = RcrSpacing.ScreenVertical,
                bottom = RcrSpacing.BottomScreenBuffer
            ),
            verticalArrangement = Arrangement.spacedBy(RcrSpacing.CardGap)
        ) {
            when (selectedTab) {
                0 -> {
                    if (upcomingMatches.any { it.isPlaceholder }) {
                        item {
                            PlaceholderInfoBanner(text = strings.matchPlaceholderUpcoming)
                        }
                    }
                    if (upcomingMatches.isEmpty()) {
                        item {
                            EmptyStateCard(
                                title = strings.matchEmptyUpcomingTitle,
                                subtitle = strings.matchEmptyUpcomingSub
                            )
                        }
                    } else {
                        items(upcomingMatches, key = { it.id }) { match ->
                            MatchCard(
                                match = match,
                                onClick = { onOpenMatchDetail(match.id) }
                            )
                        }
                    }
                }
                1 -> {
                    if (completedMatches.any { it.isPlaceholder }) {
                        item {
                            PlaceholderInfoBanner(text = strings.matchPlaceholderResults)
                        }
                    }
                    if (completedMatches.isEmpty()) {
                        item {
                            EmptyStateCard(
                                title = strings.matchEmptyResultsTitle,
                                subtitle = strings.matchEmptyResultsSub
                            )
                        }
                    } else {
                        items(completedMatches, key = { it.id }) { match ->
                            MatchCard(
                                match = match,
                                onClick = { onOpenMatchDetail(match.id) }
                            )
                        }
                    }
                }
                2 -> {
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
        }
    }
}

@Composable
fun StandingsTableCard(
    standings: List<StandingRowEntity>,
    modifier: Modifier = Modifier,
    showLegend: Boolean = true
) {
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current

    if (standings.isEmpty()) {
        EmptyStateCard(
            title = strings.standingsEmptyTitle,
            subtitle = strings.standingsEmptySub
        )
        return
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("standings_table_card"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
        border = BorderStroke(1.dp, colors.border)
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val isWideScreen = maxWidth >= 520.dp

            Column(modifier = Modifier.padding(vertical = 16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = strings.colPos,
                        modifier = Modifier.width(28.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.secondaryText
                    )
                    Text(
                        text = strings.colClub,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.secondaryText
                    )
                    StatColHeader(strings.colPlayed, width = 34.dp, color = colors.secondaryText)
                    if (isWideScreen) {
                        StatColHeader(strings.colWins, width = 30.dp, color = colors.secondaryText)
                        StatColHeader(strings.colDraws, width = 30.dp, color = colors.secondaryText)
                        StatColHeader(strings.colLosses, width = 30.dp, color = colors.secondaryText)
                        StatColHeader(strings.colGoalsFor, width = 32.dp, color = colors.secondaryText)
                        StatColHeader(strings.colGoalsAgainst, width = 32.dp, color = colors.secondaryText)
                    }
                    StatColHeader(strings.colDiff, width = 44.dp, color = colors.secondaryText)
                    Text(
                        text = strings.colPoints,
                        modifier = Modifier.width(44.dp),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center,
                        color = colors.accentHighlight
                    )
                }

                HorizontalDivider(color = colors.border)

                standings.sortedBy { it.position }.forEach { row ->
                    val bgColor = if (row.isRcr) colors.rcrRowHighlight else Color.Transparent
                    val diffStr = if (row.goalDifference > 0) "+${row.goalDifference}" else row.goalDifference.toString()

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(bgColor)
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = row.position.toString(),
                                modifier = Modifier.width(28.dp),
                                fontFamily = JetBrainsMonoFamily,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (row.isRcr) colors.accentHighlight else colors.secondaryText
                            )
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (row.isRcr) {
                                    RcrClubCrest(size = 30.dp)
                                } else {
                                    OpponentClubBadge(
                                        shortCode = row.clubShort,
                                        clubName = row.clubName,
                                        competingClubId = row.competingClubId,
                                        size = 30.dp
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = row.displayClubName(strings.isArabic),
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = if (row.isRcr) FontWeight.Bold else FontWeight.SemiBold
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        color = colors.mainText
                                    )
                                    if (!isWideScreen) {
                                        Spacer(modifier = Modifier.height(2.dp))
                                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                                            Text(
                                                text = "${row.wins}${strings.colWins}  ${row.draws}${strings.colDraws}  ${row.losses}${strings.colLosses}  •  ${row.goalsFor}:${row.goalsAgainst}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = colors.secondaryText,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                            }
                            StatColValue(row.played.toString(), width = 34.dp, color = colors.mainText)
                            if (isWideScreen) {
                                StatColValue(row.wins.toString(), width = 30.dp, color = colors.mainText)
                                StatColValue(row.draws.toString(), width = 30.dp, color = colors.mainText)
                                StatColValue(row.losses.toString(), width = 30.dp, color = colors.mainText)
                                StatColValue(row.goalsFor.toString(), width = 32.dp, color = colors.mainText)
                                StatColValue(row.goalsAgainst.toString(), width = 32.dp, color = colors.mainText)
                            }
                            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                                StatColValue(diffStr, width = 44.dp, color = colors.mainText)
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (row.isRcr) RcrDeepGreen else colors.secondaryBackground,
                                modifier = Modifier.width(44.dp)
                            ) {
                                Text(
                                    text = row.points.toString(),
                                    modifier = Modifier.padding(vertical = 5.dp),
                                    fontFamily = JetBrainsMonoFamily,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                    color = if (row.isRcr) RcrPureWhite else colors.mainText
                                )
                            }
                        }
                    }
                    HorizontalDivider(color = colors.border.copy(alpha = 0.7f))
                }

                if (showLegend) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = strings.standingsLegend,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.secondaryText
                    )
                }
            }
        }
    }
}

@Composable
private fun StatColHeader(label: String, width: Dp, color: Color) {
    Text(
        text = label,
        modifier = Modifier.width(width),
        style = MaterialTheme.typography.labelSmall,
        textAlign = TextAlign.Center,
        maxLines = 1,
        color = color
    )
}

@Composable
private fun StatColValue(value: String, width: Dp, color: Color) {
    Text(
        text = value,
        modifier = Modifier.width(width),
        fontFamily = JetBrainsMonoFamily,
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center,
        color = color
    )
}
