package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.PlayerEntity
import com.example.ui.components.EmptyStateCard
import com.example.ui.components.PlaceholderInfoBanner
import com.example.ui.components.PlayerAvatar
import com.example.ui.localization.LocalRcrStrings
import com.example.ui.theme.BarlowCondensedFamily
import com.example.ui.theme.LocalRcrColors
import com.example.ui.theme.RcrDeepGreen
import com.example.ui.theme.RcrPureWhite
import com.example.ui.theme.RcrSpacing

private val squadGroups = listOf("Tous", "Gardiens", "Défenseurs", "Milieux", "Attaquants")

@Composable
fun TeamScreen(
    players: List<PlayerEntity>,
    onOpenPlayerDetail: (Long) -> Unit
) {
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current

    var selectedGroup by rememberSaveable { mutableStateOf("Tous") }

    val orderedGroups = listOf("Gardiens", "Défenseurs", "Milieux", "Attaquants")
    val visibleGroups = if (selectedGroup == "Tous") orderedGroups else listOf(selectedGroup)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.primaryBackground)
            .testTag("team_screen_list"),
        contentPadding = PaddingValues(
            start = RcrSpacing.ScreenHorizontal,
            end = RcrSpacing.ScreenHorizontal,
            top = RcrSpacing.ScreenVertical,
            bottom = RcrSpacing.BottomScreenBuffer
        ),
        verticalArrangement = Arrangement.spacedBy(RcrSpacing.CardGap)
    ) {
        if (players.any { it.isPlaceholder }) {
            item {
                PlaceholderInfoBanner(text = strings.squadPlaceholderBanner)
            }
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(RcrSpacing.ChipGap)) {
                items(squadGroups) { group ->
                    val isSelected = group == selectedGroup
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedGroup = group },
                        label = {
                            Text(
                                text = strings.localizeSquadGroup(group),
                                modifier = Modifier.padding(vertical = 4.dp),
                                style = MaterialTheme.typography.labelLarge
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = colors.secondaryBackground,
                            labelColor = colors.mainText,
                            selectedContainerColor = RcrDeepGreen,
                            selectedLabelColor = RcrPureWhite
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = colors.border,
                            selectedBorderColor = RcrDeepGreen
                        ),
                        modifier = Modifier.testTag("team_group_chip_$group")
                    )
                }
            }
        }

        if (players.isEmpty()) {
            item {
                EmptyStateCard(
                    title = strings.squadEmptyTitle,
                    subtitle = strings.squadEmptySub
                )
            }
        } else {
            visibleGroups.forEach { groupName ->
                val groupPlayers = players.filter { it.positionGroup == groupName }
                if (groupPlayers.isNotEmpty()) {
                    item(key = "header_$groupName") {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = strings.localizeSquadGroup(groupName).uppercase(),
                            style = MaterialTheme.typography.headlineLarge,
                            color = colors.sectionHeading
                        )
                    }
                    items(groupPlayers, key = { it.id }) { player ->
                        PlayerSquadRowCard(
                            player = player,
                            onClick = { onOpenPlayerDetail(player.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PlayerSquadRowCard(
    player: PlayerEntity,
    onClick: () -> Unit
) {
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current
    val nationality = player.displayNationality(strings.isArabic)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("player_card_${player.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
        border = BorderStroke(1.dp, colors.border)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlayerAvatar(player = player, size = 78.dp)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = player.displayFullName(strings.isArabic),
                    style = MaterialTheme.typography.titleLarge,
                    color = colors.mainText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = player.displayPosition(strings.isArabic),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.accentHighlight
                )
                if (nationality.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = nationality,
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.secondaryText
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = colors.chipContainer,
                border = BorderStroke(1.dp, colors.accentHighlight.copy(alpha = 0.35f))
            ) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Text(
                        text = "#${player.shirtNumber}",
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        fontFamily = BarlowCondensedFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp,
                        color = colors.accentHighlight
                    )
                }
            }
        }
    }
}
