package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Stadium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.MatchEntity
import com.example.ui.components.CountdownTimerDisplay
import com.example.ui.components.EmptyStateCard
import com.example.ui.components.OpponentClubBadge
import com.example.ui.components.PlaceholderInfoBanner
import com.example.ui.components.RcrClubCrest
import com.example.ui.localization.LocalRcrStrings
import com.example.ui.theme.BarlowCondensedFamily
import com.example.ui.theme.JetBrainsMonoFamily
import com.example.ui.theme.LocalRcrColors
import com.example.ui.theme.RcrDarkGreen
import com.example.ui.theme.RcrDeepGreen
import com.example.ui.theme.RcrPureWhite
import com.example.ui.theme.RcrSoftMint
import com.example.ui.theme.RcrSpacing

@Composable
fun MatchDetailScreen(
    match: MatchEntity?,
    nowMillis: Long
) {
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current

    if (match == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.primaryBackground)
                .padding(RcrSpacing.ScreenHorizontal)
        ) {
            EmptyStateCard(
                title = strings.matchNotFoundTitle,
                subtitle = strings.matchNotFoundSub
            )
        }
        return
    }

    val opponentDisplay = match.displayOpponent(strings.isArabic)
    val homeName = if (match.isHome) strings.clubOfficialName else opponentDisplay
    val awayName = if (match.isHome) opponentDisplay else strings.clubOfficialName
    val isFinished = match.status == "Terminé"
    val isLive = match.status == "En direct"
    val notesDisplay = match.displayNotes(strings.isArabic)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.primaryBackground)
            .testTag("match_detail_screen"),
        contentPadding = PaddingValues(
            start = RcrSpacing.ScreenHorizontal,
            end = RcrSpacing.ScreenHorizontal,
            top = RcrSpacing.ScreenVertical,
            bottom = RcrSpacing.BottomScreenBuffer
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                border = BorderStroke(1.dp, colors.deepGreen.copy(alpha = 0.55f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.verticalGradient(listOf(RcrDeepGreen, RcrDarkGreen)))
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = RcrPureWhite.copy(alpha = 0.16f)
                    ) {
                        Text(
                            text = strings.localizeMatchStatus(match.status).uppercase(),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = RcrSoftMint
                        )
                    }
                    Spacer(modifier = Modifier.height(22.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (match.isHome) {
                                RcrClubCrest(size = 76.dp)
                            } else {
                                OpponentClubBadge(
                                    shortCode = match.opponentShort,
                                    clubName = match.opponentName,
                                    competingClubId = match.competingClubId,
                                    size = 76.dp
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = homeName,
                                style = MaterialTheme.typography.titleMedium,
                                color = RcrPureWhite,
                                textAlign = TextAlign.Center
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        ) {
                            if ((isFinished || isLive) && match.rcrScore != null && match.opponentScore != null) {
                                val leftScore = if (match.isHome) match.rcrScore else match.opponentScore
                                val rightScore = if (match.isHome) match.opponentScore else match.rcrScore
                                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                                    Text(
                                        text = "$leftScore : $rightScore",
                                        fontFamily = BarlowCondensedFamily,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 48.sp,
                                        color = RcrPureWhite
                                    )
                                }
                            } else {
                                Text(
                                    text = strings.vsLabel,
                                    fontFamily = BarlowCondensedFamily,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 36.sp,
                                    color = RcrSoftMint
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                                    Text(
                                        text = match.kickoffTimeLabel,
                                        fontFamily = JetBrainsMonoFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = RcrPureWhite
                                    )
                                }
                            }
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (match.isHome) {
                                OpponentClubBadge(
                                    shortCode = match.opponentShort,
                                    clubName = match.opponentName,
                                    competingClubId = match.competingClubId,
                                    size = 76.dp
                                )
                            } else {
                                RcrClubCrest(size = 76.dp)
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = awayName,
                                style = MaterialTheme.typography.titleMedium,
                                color = RcrPureWhite,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    if (!isFinished && !isLive) {
                        Spacer(modifier = Modifier.height(22.dp))
                        CountdownTimerDisplay(
                            targetTimestampMillis = match.kickoffTimestampMillis,
                            nowMillis = nowMillis
                        )
                    }
                }
            }
        }

        if (match.isPlaceholder) {
            item {
                PlaceholderInfoBanner(text = strings.matchPlaceholderDetailBanner)
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
                border = BorderStroke(1.dp, colors.border)
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = strings.matchSheetHeader,
                        style = MaterialTheme.typography.headlineLarge,
                        color = colors.sectionHeading
                    )
                    HorizontalDivider(color = colors.border)
                    MatchInfoRow(Icons.Outlined.EmojiEvents, strings.matchCompetitionLabel, match.displayCompetition(strings.isArabic))
                    MatchInfoRow(Icons.Outlined.CalendarMonth, strings.matchDateLabel, match.matchDateLabel)
                    MatchInfoRow(Icons.Outlined.Schedule, strings.matchKickoffLabel, match.kickoffTimeLabel)
                    MatchInfoRow(Icons.Outlined.Stadium, strings.matchStadiumLabel, match.displayStadium(strings.isArabic))
                    if (notesDisplay.isNotBlank()) {
                        MatchInfoRow(Icons.Outlined.Info, strings.matchNotesLabel, notesDisplay)
                    }
                }
            }
        }
    }
}

@Composable
private fun MatchInfoRow(icon: ImageVector, label: String, value: String) {
    val colors = LocalRcrColors.current
    Row(verticalAlignment = Alignment.Top) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = colors.accentHighlight
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = colors.secondaryText
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = colors.mainText
            )
        }
    }
}
