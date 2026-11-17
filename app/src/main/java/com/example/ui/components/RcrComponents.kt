package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Stadium
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.local.CompetingClubEntity
import com.example.data.local.CompetingClubIdentity
import com.example.data.local.MatchEntity
import com.example.data.local.MediaStorageHelper
import com.example.data.local.NewsArticleEntity
import com.example.data.local.PlayerEntity
import com.example.ui.localization.LocalRcrStrings
import com.example.ui.navigation.BottomNavDestination
import com.example.ui.navigation.mainBottomDestinations
import com.example.ui.theme.BarlowCondensedFamily
import com.example.ui.theme.JetBrainsMonoFamily
import com.example.ui.theme.LocalRcrColors
import com.example.ui.theme.RcrDeepGreen
import com.example.ui.theme.RcrGoldAccent
import com.example.ui.theme.RcrLiveRed
import com.example.ui.theme.RcrPrimaryGreen
import com.example.ui.theme.RcrPureWhite
import com.example.ui.theme.RcrSoftMint
import com.example.ui.theme.RcrSpacing
import java.io.File
import java.util.concurrent.TimeUnit

val LocalRcrClubLogoPath = compositionLocalOf { "" }
val LocalCompetingClubs = compositionLocalOf<List<CompetingClubEntity>> { emptyList() }

@Composable
fun RcrClubCrest(
    modifier: Modifier = Modifier,
    size: Dp = 44.dp
) {
    val context = LocalContext.current
    val strings = LocalRcrStrings.current
    val logoPath = LocalRcrClubLogoPath.current
    val customFile = remember(logoPath) {
        if (MediaStorageHelper.isReadableImageFile(context, logoPath)) {
            MediaStorageHelper.resolveStoredMediaFile(context, logoPath)
        } else {
            null
        }
    }
    var loadFailed by remember(logoPath) { mutableStateOf(false) }

    Box(
        modifier = modifier
            .size(size)
            .testTag("rcr_club_crest"),
        contentAlignment = Alignment.Center
    ) {
        if (customFile != null && !loadFailed) {
            AsyncImage(
                model = customFile,
                contentDescription = strings.clubOfficialName,
                contentScale = ContentScale.Fit,
                onError = { loadFailed = true },
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("rcr_club_crest_custom")
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.rcr_crest_transparent),
                contentDescription = strings.clubOfficialName,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("rcr_club_crest_default")
            )
        }
    }
}

@Composable
fun OpponentClubBadge(
    shortCode: String,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    clubName: String = "",
    competingClubId: Long? = null,
    logoPathOverride: String? = null
) {
    val context = LocalContext.current
    val colors = LocalRcrColors.current
    val competingClubs = LocalCompetingClubs.current
    val matchedClub = remember(competingClubs, competingClubId, clubName, shortCode) {
        CompetingClubIdentity.resolveClub(
            clubs = competingClubs,
            competingClubId = competingClubId,
            clubName = clubName,
            shortCode = shortCode
        )
    }
    val effectivePath = logoPathOverride ?: matchedClub?.logoPath.orEmpty()
    val logoFile = remember(effectivePath) {
        if (MediaStorageHelper.isReadableImageFile(context, effectivePath)) {
            MediaStorageHelper.resolveStoredMediaFile(context, effectivePath)
        } else {
            null
        }
    }
    var loadFailed by remember(effectivePath) { mutableStateOf(false) }

    if (logoFile != null && !loadFailed) {
        Box(
            modifier = modifier.size(size),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = logoFile,
                contentDescription = clubName.ifBlank { shortCode },
                contentScale = ContentScale.Fit,
                onError = { loadFailed = true },
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("opponent_badge_logo_${shortCode.trim().uppercase()}")
            )
        }
    } else {
        Surface(
            modifier = modifier.size(size),
            shape = CircleShape,
            color = colors.secondaryBackground,
            border = BorderStroke(1.5.dp, colors.border)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = shortCode.take(3).uppercase(),
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontFamily = BarlowCondensedFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = (size.value * 0.34f).sp
                    ),
                    color = colors.mainText,
                    modifier = Modifier.testTag("opponent_badge_code_${shortCode.trim().uppercase()}")
                )
            }
        }
    }
}

@Composable
fun ClubMediaImage(
    mediaUri: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    when {
        mediaUri == "drawable://img_hero_stadium" -> {
            Image(
                painter = painterResource(id = R.drawable.img_hero_stadium),
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale
            )
        }
        mediaUri == "drawable://img_supporters_banner" -> {
            Image(
                painter = painterResource(id = R.drawable.img_supporters_banner),
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale
            )
        }
        mediaUri == "drawable://img_training_action" -> {
            Image(
                painter = painterResource(id = R.drawable.img_training_action),
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale
            )
        }
        mediaUri == "drawable://img_club_heritage" -> {
            Image(
                painter = painterResource(id = R.drawable.img_club_heritage),
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale
            )
        }
        mediaUri.isNotBlank() && File(mediaUri).exists() -> {
            AsyncImage(
                model = File(mediaUri),
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale
            )
        }
        mediaUri.startsWith("content://") || mediaUri.startsWith("http") -> {
            AsyncImage(
                model = mediaUri,
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale
            )
        }
        else -> {
            Image(
                painter = painterResource(id = R.drawable.img_club_heritage),
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale
            )
        }
    }
}

@Composable
fun PlayerAvatar(
    player: PlayerEntity,
    modifier: Modifier = Modifier,
    size: Dp = 84.dp
) {
    val strings = LocalRcrStrings.current
    val hasPhoto = player.photoUri.isNotBlank() &&
        (File(player.photoUri).exists() || player.photoUri.startsWith("drawable://") || player.photoUri.startsWith("content://"))

    Surface(
        modifier = modifier.size(size),
        shape = CircleShape,
        color = RcrDeepGreen,
        border = BorderStroke(2.5.dp, RcrPrimaryGreen)
    ) {
        if (hasPhoto) {
            ClubMediaImage(
                mediaUri = player.photoUri,
                contentDescription = player.displayFullName(strings.isArabic),
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(RcrPrimaryGreen, RcrDeepGreen)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "#${player.shirtNumber}",
                            fontFamily = BarlowCondensedFamily,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = (size.value * 0.36f).sp,
                            color = RcrPureWhite
                        )
                        Text(
                            text = "RCR",
                            fontFamily = JetBrainsMonoFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = (size.value * 0.14f).sp,
                            color = RcrSoftMint
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RcrTopBar(
    title: String,
    subtitle: String? = null,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    unreadCount: Int = 0,
    onNotificationsClick: () -> Unit = {},
    isAdminActive: Boolean = false,
    onAdminClick: () -> Unit = {},
    isRtl: Boolean = false
) {
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current

    Surface(
        color = if (colors.isDark) colors.secondaryBackground else colors.primaryBackground,
        tonalElevation = 0.dp,
        shadowElevation = if (colors.isDark) 0.dp else 2.dp
    ) {
        Column(modifier = Modifier.statusBarsPadding()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showBackButton) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("top_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = strings.backContentDesc,
                            tint = colors.sectionHeading
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                } else {
                    RcrClubCrest(size = 42.dp)
                    Spacer(modifier = Modifier.width(14.dp))
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 22.sp,
                            lineHeight = 27.sp
                        ),
                        color = colors.sectionHeading,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!subtitle.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.accentHighlight,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                if (isAdminActive) {
                    IconButton(
                        onClick = onAdminClick,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("top_admin_badge_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AdminPanelSettings,
                            contentDescription = strings.adminSpaceDesc,
                            tint = RcrGoldAccent
                        )
                    }
                }

                IconButton(
                    onClick = onNotificationsClick,
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("top_notifications_button")
                ) {
                    BadgedBox(
                        badge = {
                            if (unreadCount > 0) {
                                Badge(
                                    containerColor = RcrLiveRed,
                                    contentColor = RcrPureWhite
                                ) {
                                    Text(
                                        text = if (unreadCount > 9) "9+" else unreadCount.toString(),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (unreadCount > 0) Icons.Filled.Notifications else Icons.Outlined.Notifications,
                            contentDescription = strings.notificationsContentDesc,
                            tint = colors.sectionHeading
                        )
                    }
                }
            }
            HorizontalDivider(thickness = 1.dp, color = colors.border)
        }
    }
}

@Composable
fun RcrBottomBar(
    currentRoute: String?,
    onNavigate: (BottomNavDestination) -> Unit,
    isRtl: Boolean = false
) {
    val colors = LocalRcrColors.current

    Column {
        HorizontalDivider(thickness = 1.dp, color = colors.border)
        NavigationBar(
            windowInsets = WindowInsets.navigationBars,
            containerColor = if (colors.isDark) colors.secondaryBackground else colors.primaryBackground,
            tonalElevation = 0.dp
        ) {
            mainBottomDestinations.forEach { dest ->
                val selected = currentRoute == dest.route
                NavigationBarItem(
                    selected = selected,
                    onClick = { onNavigate(dest) },
                    modifier = Modifier.testTag(dest.testTag),
                    icon = {
                        Icon(
                            imageVector = if (selected) dest.selectedIcon else dest.unselectedIcon,
                            contentDescription = if (isRtl) dest.labelAr else dest.labelFr
                        )
                    },
                    label = {
                        Text(
                            text = if (isRtl) dest.labelAr else dest.labelFr,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RcrPureWhite,
                        selectedTextColor = colors.accentHighlight,
                        indicatorColor = RcrPrimaryGreen,
                        unselectedIconColor = colors.secondaryText,
                        unselectedTextColor = colors.secondaryText
                    )
                )
            }
        }
    }
}

@Composable
fun RcrNavRail(
    currentRoute: String?,
    onNavigate: (BottomNavDestination) -> Unit,
    isRtl: Boolean = false
) {
    val colors = LocalRcrColors.current

    NavigationRail(
        containerColor = if (colors.isDark) colors.secondaryBackground else colors.primaryBackground,
        header = {
            Spacer(modifier = Modifier.height(16.dp))
            RcrClubCrest(size = 48.dp)
            Spacer(modifier = Modifier.height(16.dp))
        }
    ) {
        mainBottomDestinations.forEach { dest ->
            val selected = currentRoute == dest.route
            NavigationRailItem(
                selected = selected,
                onClick = { onNavigate(dest) },
                modifier = Modifier.testTag(dest.testTag),
                icon = {
                    Icon(
                        imageVector = if (selected) dest.selectedIcon else dest.unselectedIcon,
                        contentDescription = if (isRtl) dest.labelAr else dest.labelFr
                    )
                },
                label = {
                    Text(
                        text = if (isRtl) dest.labelAr else dest.labelFr,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = RcrPureWhite,
                    selectedTextColor = colors.accentHighlight,
                    indicatorColor = RcrPrimaryGreen,
                    unselectedIconColor = colors.secondaryText,
                    unselectedTextColor = colors.secondaryText
                )
            )
        }
    }
}

@Composable
fun CountdownTimerDisplay(
    targetTimestampMillis: Long,
    nowMillis: Long,
    modifier: Modifier = Modifier
) {
    val strings = LocalRcrStrings.current
    val diff = (targetTimestampMillis - nowMillis).coerceAtLeast(0L)
    val days = TimeUnit.MILLISECONDS.toDays(diff)
    val hours = TimeUnit.MILLISECONDS.toHours(diff) % 24
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(diff) % 60

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
            val compact = maxWidth < 330.dp
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(if (compact) 4.dp else 8.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CountdownCell(value = days.toString().padStart(2, '0'), unit = strings.daysUnit, compact = compact)
                Text(":", color = RcrSoftMint, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                CountdownCell(value = hours.toString().padStart(2, '0'), unit = strings.hoursUnit, compact = compact)
                Text(":", color = RcrSoftMint, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                CountdownCell(value = minutes.toString().padStart(2, '0'), unit = strings.minsUnit, compact = compact)
                Text(":", color = RcrSoftMint, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                CountdownCell(value = seconds.toString().padStart(2, '0'), unit = strings.secsUnit, compact = compact)
            }
        }
    }
}

@Composable
private fun CountdownCell(value: String, unit: String, compact: Boolean) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.Black.copy(alpha = 0.35f),
        border = BorderStroke(1.dp, RcrPureWhite.copy(alpha = 0.25f))
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = if (compact) 8.dp else 13.dp,
                vertical = if (compact) 6.dp else 8.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontFamily = JetBrainsMonoFamily,
                fontWeight = FontWeight.Bold,
                fontSize = if (compact) 17.sp else 20.sp,
                color = RcrPureWhite
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = unit,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.4.sp,
                color = RcrSoftMint
            )
        }
    }
}

@Composable
fun MatchCard(
    match: MatchEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current
    val isArabic = strings.isArabic

    val isFinished = match.status == "Terminé"
    val isLive = match.status == "En direct"
    val rcrLabel = strings.clubOfficialName
    val opponentLabel = match.displayOpponent(isArabic)
    val homeName = if (match.isHome) rcrLabel else opponentLabel
    val awayName = if (match.isHome) opponentLabel else rcrLabel

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("match_card_${match.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
        border = BorderStroke(1.dp, colors.border),
        elevation = CardDefaults.cardElevation(defaultElevation = if (colors.isDark) 0.dp else 2.dp)
    ) {
        Column(modifier = Modifier.padding(RcrSpacing.CardPadding)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = match.displayCompetition(isArabic),
                    style = MaterialTheme.typography.labelLarge,
                    color = colors.accentHighlight,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(10.dp))
                Surface(
                    shape = RoundedCornerShape(50),
                    color = when {
                        isLive -> RcrLiveRed.copy(alpha = 0.14f)
                        isFinished -> colors.secondaryBackground
                        else -> colors.chipContainer
                    }
                ) {
                    Text(
                        text = strings.localizeMatchStatus(match.status),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = when {
                            isLive -> RcrLiveRed
                            isFinished -> colors.secondaryText
                            else -> colors.accentHighlight
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (match.isHome) {
                        RcrClubCrest(size = 54.dp)
                    } else {
                        OpponentClubBadge(
                            shortCode = match.opponentShort,
                            clubName = match.opponentName,
                            competingClubId = match.competingClubId,
                            size = 54.dp
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = homeName,
                        style = MaterialTheme.typography.titleSmall,
                        color = colors.mainText,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Column(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if ((isFinished || isLive) && match.rcrScore != null && match.opponentScore != null) {
                        val leftScore = if (match.isHome) match.rcrScore else match.opponentScore
                        val rightScore = if (match.isHome) match.opponentScore else match.rcrScore
                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = colors.secondaryBackground,
                                border = BorderStroke(1.dp, colors.border)
                            ) {
                                Text(
                                    text = "$leftScore  -  $rightScore",
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                    fontFamily = BarlowCondensedFamily,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 32.sp,
                                    color = colors.sectionHeading
                                )
                            }
                        }
                    } else {
                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                            Text(
                                text = match.kickoffTimeLabel,
                                fontFamily = JetBrainsMonoFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 21.sp,
                                color = colors.accentHighlight
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = strings.vsLabel,
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.secondaryText
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
                            size = 54.dp
                        )
                    } else {
                        RcrClubCrest(size = 54.dp)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = awayName,
                        style = MaterialTheme.typography.titleSmall,
                        color = colors.mainText,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(thickness = 1.dp, color = colors.border)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = colors.accentHighlight
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        Text(
                            text = match.matchDateLabel,
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.secondaryText
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Stadium,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = colors.secondaryText
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = match.displayStadium(isArabic),
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.secondaryText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun NewsEditorialCard(
    article: NewsArticleEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current
    val isArabic = strings.isArabic
    val title = article.displayTitle(isArabic)
    val summary = article.displaySummary(isArabic)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("news_card_${article.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
        border = BorderStroke(1.dp, colors.border),
        elevation = CardDefaults.cardElevation(defaultElevation = if (colors.isDark) 0.dp else 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
            ) {
                ClubMediaImage(
                    mediaUri = article.imageUri,
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Black.copy(alpha = 0.2f), Color.Transparent, Color.Black.copy(alpha = 0.35f))
                            )
                        )
                )
                Surface(
                    shape = RoundedCornerShape(50),
                    color = RcrDeepGreen,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(14.dp)
                ) {
                    Text(
                        text = strings.localizeNewsCategory(article.category),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = RcrPureWhite
                    )
                }
                if (article.isPlaceholder) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Color.Black.copy(alpha = 0.68f),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(14.dp)
                    ) {
                        Text(
                            text = strings.exampleBadge,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = RcrSoftMint
                        )
                    }
                }
            }
            Column(modifier = Modifier.padding(RcrSpacing.CardPadding)) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Text(
                        text = article.publishedDateLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.accentHighlight
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = colors.mainText,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = summary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.secondaryText,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun PlaceholderInfoBanner(
    text: String? = null,
    modifier: Modifier = Modifier
) {
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current
    val displayText = text ?: strings.placeholderDefaultBanner

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = colors.secondaryBackground,
        border = BorderStroke(1.dp, colors.border)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = colors.accentHighlight,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = displayText,
                style = MaterialTheme.typography.bodySmall,
                color = colors.mainText
            )
        }
    }
}

@Composable
fun EmptyStateCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    val colors = LocalRcrColors.current

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = colors.cardBackground,
        border = BorderStroke(1.dp, colors.border)
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.SportsSoccer,
                contentDescription = null,
                tint = colors.accentHighlight,
                modifier = Modifier.size(46.dp)
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = colors.mainText,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.secondaryText,
                textAlign = TextAlign.Center
            )
        }
    }
}
