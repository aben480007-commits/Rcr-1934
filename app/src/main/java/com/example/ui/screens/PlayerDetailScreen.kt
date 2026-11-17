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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.PlayerEntity
import com.example.ui.components.EmptyStateCard
import com.example.ui.components.PlaceholderInfoBanner
import com.example.ui.components.PlayerAvatar
import com.example.ui.localization.LocalRcrStrings
import com.example.ui.theme.BarlowCondensedFamily
import com.example.ui.theme.LocalRcrColors
import com.example.ui.theme.RcrDarkGreen
import com.example.ui.theme.RcrDeepGreen
import com.example.ui.theme.RcrPureWhite
import com.example.ui.theme.RcrSoftMint
import com.example.ui.theme.RcrSpacing

@Composable
fun PlayerDetailScreen(player: PlayerEntity?) {
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current

    if (player == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.primaryBackground)
                .padding(RcrSpacing.ScreenHorizontal)
        ) {
            EmptyStateCard(
                title = strings.playerNotFoundTitle,
                subtitle = strings.playerNotFoundSub
            )
        }
        return
    }

    val hasAnyStat = player.appearances != null || player.goals != null || player.assists != null
    val nationality = player.displayNationality(strings.isArabic)
    val biography = player.displayBiography(strings.isArabic)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.primaryBackground)
            .testTag("player_detail_screen"),
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
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PlayerAvatar(player = player, size = 152.dp)
                    Spacer(modifier = Modifier.height(18.dp))
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = RcrPureWhite.copy(alpha = 0.16f)
                    ) {
                        Text(
                            text = "${strings.shirtNumberPrefix} ${player.shirtNumber} • ${strings.localizeSquadGroup(player.positionGroup).uppercase()}",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = RcrSoftMint
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = player.displayFullName(strings.isArabic),
                        style = MaterialTheme.typography.displaySmall,
                        color = RcrPureWhite,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = player.displayPosition(strings.isArabic),
                        style = MaterialTheme.typography.titleMedium,
                        color = RcrSoftMint
                    )
                    if (nationality.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${strings.playerNationalityPrefix} : $nationality",
                            style = MaterialTheme.typography.bodyMedium,
                            color = RcrPureWhite.copy(alpha = 0.9f)
                        )
                    }
                }
            }
        }

        if (player.isPlaceholder) {
            item {
                PlaceholderInfoBanner(text = strings.playerPlaceholderBanner)
            }
        }

        if (hasAnyStat) {
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
                    border = BorderStroke(1.dp, colors.border)
                ) {
                    Column(modifier = Modifier.padding(22.dp)) {
                        Text(
                            text = strings.playerStatsAvailableHeader,
                            style = MaterialTheme.typography.headlineLarge,
                            color = colors.sectionHeading
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            player.appearances?.let {
                                PlayerStatBox(label = strings.statMatches, value = it.toString(), modifier = Modifier.weight(1f))
                            }
                            player.goals?.let {
                                PlayerStatBox(label = strings.statGoals, value = it.toString(), modifier = Modifier.weight(1f))
                            }
                            player.assists?.let {
                                PlayerStatBox(label = strings.statAssists, value = it.toString(), modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        } else {
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
                    border = BorderStroke(1.dp, colors.border)
                ) {
                    Column(modifier = Modifier.padding(22.dp)) {
                        Text(
                            text = strings.playerStatsHeader,
                            style = MaterialTheme.typography.headlineLarge,
                            color = colors.sectionHeading
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = strings.playerNoStatsMsg,
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.secondaryText
                        )
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
                border = BorderStroke(1.dp, colors.border)
            ) {
                Column(modifier = Modifier.padding(22.dp)) {
                    Text(
                        text = strings.playerBioHeader,
                        style = MaterialTheme.typography.headlineLarge,
                        color = colors.sectionHeading
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = biography.ifBlank { strings.playerNoBioFallback },
                        style = MaterialTheme.typography.bodyLarge,
                        color = colors.mainText
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayerStatBox(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val colors = LocalRcrColors.current
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = colors.chipContainer,
        border = BorderStroke(1.dp, colors.accentHighlight.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontFamily = BarlowCondensedFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 34.sp,
                color = colors.accentHighlight
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = colors.secondaryText
            )
        }
    }
}
