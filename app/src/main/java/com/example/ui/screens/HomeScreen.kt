package com.example.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Stadium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.ClubInfoEntity
import com.example.data.local.MatchEntity
import com.example.data.local.NewsArticleEntity
import com.example.data.local.PlayerEntity
import com.example.data.local.StandingRowEntity
import com.example.ui.components.CountdownTimerDisplay
import com.example.ui.components.MatchCard
import com.example.ui.components.NewsEditorialCard
import com.example.ui.components.OpponentClubBadge
import com.example.ui.components.PlayerAvatar
import com.example.ui.components.RcrClubCrest
import com.example.ui.localization.LocalRcrStrings
import com.example.ui.theme.BarlowCondensedFamily
import com.example.ui.theme.JetBrainsMonoFamily
import com.example.ui.theme.LocalRcrColors
import com.example.ui.theme.RcrDarkGreen
import com.example.ui.theme.RcrDeepGreen
import com.example.ui.theme.RcrGoldAccent
import com.example.ui.theme.RcrPrimaryGreen
import com.example.ui.theme.RcrPureWhite
import com.example.ui.theme.RcrSoftMint
import com.example.ui.theme.RcrSpacing

@Composable
fun HomeScreen(
    clubInfo: ClubInfoEntity?,
    matches: List<MatchEntity>,
    news: List<NewsArticleEntity>,
    players: List<PlayerEntity>,
    standings: List<StandingRowEntity>,
    nowMillis: Long,
    onOpenNewsList: () -> Unit,
    onOpenArticle: (Long) -> Unit,
    onOpenMatches: () -> Unit,
    onOpenMatchDetail: (Long) -> Unit,
    onOpenTeam: () -> Unit,
    onOpenPlayerDetail: (Long) -> Unit,
    onOpenStandings: () -> Unit,
    onOpenClub: () -> Unit
) {
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current

    val nextMatch = matches.firstOrNull { it.isFeaturedNext && it.status != "Terminé" }
        ?: matches.firstOrNull { it.status == "À venir" || it.status == "En direct" }
    val recentResults = matches.filter { it.status == "Terminé" }
        .sortedByDescending { it.kickoffTimestampMillis }
        .take(3)
    val latestNews = news.take(3)
    val highlightedPlayers = players.filter { it.isHighlighted }.ifEmpty { players.take(5) }
    val topStandings = standings.sortedBy { it.position }.take(4)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.primaryBackground)
            .testTag("home_screen_list"),
        contentPadding = PaddingValues(bottom = RcrSpacing.BottomScreenBuffer),
        verticalArrangement = Arrangement.spacedBy(RcrSpacing.SectionGap)
    ) {
        // 1. Main Hero Section
        item {
            HeroBannerSection(
                clubInfo = clubInfo,
                onExploreClub = onOpenClub
            )
        }

        // 2. Featured Match Widget
        if (nextMatch != null) {
            item {
                Column(modifier = Modifier.padding(horizontal = RcrSpacing.ScreenHorizontal)) {
                    SectionHeader(
                        title = strings.sectionNextMatch,
                        actionText = strings.actionCalendar,
                        onActionClick = onOpenMatches
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    NextMatchHeroWidget(
                        match = nextMatch,
                        nowMillis = nowMillis,
                        onClick = { onOpenMatchDetail(nextMatch.id) }
                    )
                }
            }
        }

        // 3. Latest Results
        if (recentResults.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(horizontal = RcrSpacing.ScreenHorizontal)) {
                    SectionHeader(
                        title = strings.sectionLatestResults,
                        actionText = strings.actionSeeAll,
                        onActionClick = onOpenMatches
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(RcrSpacing.CardGap)) {
                        recentResults.forEach { resultMatch ->
                            MatchCard(
                                match = resultMatch,
                                onClick = { onOpenMatchDetail(resultMatch.id) }
                            )
                        }
                    }
                }
            }
        }

        // 4. Latest News
        if (latestNews.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(horizontal = RcrSpacing.ScreenHorizontal)) {
                    SectionHeader(
                        title = strings.sectionLatestNews,
                        actionText = strings.actionAllNews,
                        onActionClick = onOpenNewsList,
                        actionTestTag = "home_all_news_button"
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                        latestNews.forEach { article ->
                            NewsEditorialCard(
                                article = article,
                                onClick = { onOpenArticle(article.id) }
                            )
                        }
                    }
                }
            }
        }

        // 5. Team Highlights
        if (highlightedPlayers.isNotEmpty()) {
            item {
                Column {
                    Box(modifier = Modifier.padding(horizontal = RcrSpacing.ScreenHorizontal)) {
                        SectionHeader(
                            title = strings.sectionTeamHighlights,
                            actionText = strings.actionSquad,
                            onActionClick = onOpenTeam
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    LazyRow(
                        modifier = Modifier.testTag("home_players_row"),
                        contentPadding = PaddingValues(horizontal = RcrSpacing.ScreenHorizontal),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(highlightedPlayers, key = { it.id }) { player ->
                            PlayerHighlightCard(
                                player = player,
                                onClick = { onOpenPlayerDetail(player.id) }
                            )
                        }
                    }
                }
            }
        }

        // 6. League Standings Snapshot
        if (topStandings.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(horizontal = RcrSpacing.ScreenHorizontal)) {
                    SectionHeader(
                        title = strings.sectionStandingsPreview,
                        actionText = strings.actionFullTable,
                        onActionClick = onOpenStandings
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    StandingsTableCard(
                        standings = topStandings,
                        showLegend = false
                    )
                }
            }
        }

        // 7. Supporters & Club Identity Section
        item {
            Box(modifier = Modifier.padding(horizontal = RcrSpacing.ScreenHorizontal)) {
                SupportersSectionCard(onClick = onOpenClub)
            }
        }
    }
}

@Composable
private fun HeroBannerSection(
    clubInfo: ClubInfoEntity?,
    onExploreClub: () -> Unit
) {
    val strings = LocalRcrStrings.current
    val isArabic = strings.isArabic
    val infiniteTransition = rememberInfiniteTransition(label = "hero_pulse")
    val crestScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "crest_scale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = RcrSpacing.ScreenHorizontal, vertical = 12.dp)
            .height(340.dp)
            .clip(RoundedCornerShape(24.dp))
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_hero_stadium),
            contentDescription = clubInfo?.displayStadiumName(isArabic) ?: strings.clubOfficialName,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.28f),
                            RcrDeepGreen.copy(alpha = 0.72f),
                            RcrDeepGreen.copy(alpha = 0.94f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(22.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = RcrDeepGreen.copy(alpha = 0.92f),
                    border = BorderStroke(1.dp, RcrPureWhite.copy(alpha = 0.35f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = null,
                            tint = RcrSoftMint,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${clubInfo?.displayCity(isArabic) ?: strings.clubLocationBadge} • ${clubInfo?.foundedYear ?: "1934"}",
                            style = MaterialTheme.typography.labelSmall,
                            color = RcrPureWhite
                        )
                    }
                }

                Box(modifier = Modifier.scale(crestScale)) {
                    RcrClubCrest(size = 60.dp)
                }
            }

            Column {
                Text(
                    text = clubInfo?.displayOfficialName(isArabic)?.uppercase() ?: strings.clubOfficialName.uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    color = RcrSoftMint
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = strings.heroHeadline,
                    style = MaterialTheme.typography.displayLarge,
                    color = RcrPureWhite
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = strings.heroSubtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = RcrPureWhite.copy(alpha = 0.92f),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onExploreClub,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RcrPrimaryGreen,
                        contentColor = RcrPureWhite
                    ),
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                    modifier = Modifier
                        .heightIn(min = 48.dp)
                        .testTag("hero_club_button")
                ) {
                    Text(strings.discoverClubBtn, style = MaterialTheme.typography.labelLarge)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun NextMatchHeroWidget(
    match: MatchEntity,
    nowMillis: Long,
    onClick: () -> Unit
) {
    val strings = LocalRcrStrings.current
    val isArabic = strings.isArabic
    val opponentLabel = match.displayOpponent(isArabic)
    val homeName = if (match.isHome) strings.clubOfficialName else opponentLabel
    val awayName = if (match.isHome) opponentLabel else strings.clubOfficialName

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("home_next_match_widget"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = RcrDeepGreen),
        border = BorderStroke(1.dp, RcrPrimaryGreen),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(RcrDeepGreen, RcrDarkGreen)
                    )
                )
                .padding(22.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = RcrPureWhite.copy(alpha = 0.16f)
                    ) {
                        Text(
                            text = match.displayCompetition(isArabic),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = RcrPureWhite,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.CalendarMonth,
                            contentDescription = null,
                            tint = RcrSoftMint,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                            Text(
                                text = match.matchDateLabel,
                                style = MaterialTheme.typography.labelSmall,
                                color = RcrPureWhite
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (match.isHome) {
                            RcrClubCrest(size = 64.dp)
                        } else {
                            OpponentClubBadge(
                                shortCode = match.opponentShort,
                                clubName = match.opponentName,
                                competingClubId = match.competingClubId,
                                size = 64.dp
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = homeName,
                            style = MaterialTheme.typography.titleMedium,
                            color = RcrPureWhite,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    ) {
                        Text(
                            text = strings.vsLabel,
                            fontFamily = BarlowCondensedFamily,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 30.sp,
                            color = RcrGoldAccent
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                            Text(
                                text = match.kickoffTimeLabel,
                                fontFamily = JetBrainsMonoFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = RcrSoftMint
                            )
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
                                size = 64.dp
                            )
                        } else {
                            RcrClubCrest(size = 64.dp)
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = awayName,
                            style = MaterialTheme.typography.titleMedium,
                            color = RcrPureWhite,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                CountdownTimerDisplay(
                    targetTimestampMillis = match.kickoffTimestampMillis,
                    nowMillis = nowMillis
                )

                Spacer(modifier = Modifier.height(18.dp))
                HorizontalDivider(color = RcrPureWhite.copy(alpha = 0.16f))
                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Stadium,
                        contentDescription = null,
                        tint = RcrSoftMint,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = match.displayStadium(isArabic),
                        style = MaterialTheme.typography.bodySmall,
                        color = RcrSoftMint,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayerHighlightCard(
    player: PlayerEntity,
    onClick: () -> Unit
) {
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current
    val isArabic = strings.isArabic

    Card(
        modifier = Modifier
            .width(178.dp)
            .clickable(onClick = onClick)
            .testTag("home_player_card_${player.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
        border = BorderStroke(1.dp, colors.border),
        elevation = CardDefaults.cardElevation(defaultElevation = if (colors.isDark) 0.dp else 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PlayerAvatar(player = player, size = 90.dp)
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = player.displayFullName(isArabic),
                style = MaterialTheme.typography.titleMedium,
                color = colors.mainText,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = player.displayPosition(isArabic),
                style = MaterialTheme.typography.bodySmall,
                color = colors.accentHighlight,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
                shape = RoundedCornerShape(50),
                color = colors.chipContainer
            ) {
                Text(
                    text = "${strings.shirtNumberPrefix} ${player.shirtNumber}",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.accentHighlight
                )
            }
        }
    }
}

@Composable
private fun SupportersSectionCard(onClick: () -> Unit) {
    val strings = LocalRcrStrings.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(236.dp)
            .clip(RoundedCornerShape(22.dp))
            .clickable(onClick = onClick)
            .testTag("home_supporters_card")
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_supporters_banner),
            contentDescription = strings.supportersEyebrow,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            RcrDeepGreen.copy(alpha = 0.94f),
                            RcrDeepGreen.copy(alpha = 0.72f)
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = strings.supportersEyebrow,
                style = MaterialTheme.typography.labelSmall,
                color = RcrSoftMint
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = strings.supportersMotto,
                style = MaterialTheme.typography.displayMedium,
                color = RcrPureWhite
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = strings.supportersSubtext,
                style = MaterialTheme.typography.bodyMedium,
                color = RcrPureWhite.copy(alpha = 0.92f)
            )
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    actionTestTag: String? = null
) {
    val colors = LocalRcrColors.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            color = colors.sectionHeading,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        if (actionText != null && onActionClick != null) {
            TextButton(
                onClick = onActionClick,
                modifier = if (actionTestTag != null) Modifier.testTag(actionTestTag) else Modifier
            ) {
                Text(
                    text = actionText,
                    style = MaterialTheme.typography.labelLarge,
                    color = colors.accentHighlight
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = colors.accentHighlight
                )
            }
        }
    }
}
