package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.local.ClubInfoEntity
import com.example.data.local.CompetingClubEntity
import com.example.data.local.ContactMessageEntity
import com.example.data.local.GalleryItemEntity
import com.example.data.local.MatchEntity
import com.example.data.local.MediaStorageHelper
import com.example.data.local.NewsArticleEntity
import com.example.data.local.PlayerEntity
import com.example.data.local.StandingRowEntity
import com.example.ui.components.ClubMediaImage
import com.example.ui.components.EmptyStateCard
import com.example.ui.components.OpponentClubBadge
import com.example.ui.components.PlayerAvatar
import com.example.ui.components.RcrClubCrest
import com.example.ui.localization.LocalRcrStrings
import com.example.ui.theme.BarlowCondensedFamily
import com.example.ui.theme.LocalRcrColors
import com.example.ui.theme.RcrGoldAccent
import com.example.ui.theme.RcrLiveRed
import com.example.ui.theme.RcrPrimaryGreen
import com.example.ui.theme.RcrPureWhite
import com.example.ui.theme.RcrSpacing
import kotlinx.coroutines.launch

@Composable
fun AdminScreen(
    isAuthenticated: Boolean,
    allNews: List<NewsArticleEntity>,
    players: List<PlayerEntity>,
    matches: List<MatchEntity>,
    standings: List<StandingRowEntity>,
    galleryItems: List<GalleryItemEntity>,
    clubInfo: ClubInfoEntity?,
    contactMessages: List<ContactMessageEntity>,
    clubLogoPath: String = "",
    competingClubs: List<CompetingClubEntity> = emptyList(),
    onLogin: (String) -> Boolean,
    onLogout: () -> Unit,
    onChangePin: (String) -> Unit,
    onSaveArticle: (NewsArticleEntity) -> Unit,
    onDeleteArticle: (NewsArticleEntity) -> Unit,
    onSavePlayer: (PlayerEntity) -> Unit,
    onDeletePlayer: (PlayerEntity) -> Unit,
    onSaveMatch: (MatchEntity) -> Unit,
    onDeleteMatch: (MatchEntity) -> Unit,
    onSaveStanding: (StandingRowEntity) -> Unit,
    onDeleteStanding: (StandingRowEntity) -> Unit,
    onAddGalleryItem: (GalleryItemEntity) -> Unit,
    onDeleteGalleryItem: (GalleryItemEntity) -> Unit,
    onUpdateClubInfo: (ClubInfoEntity) -> Unit,
    onSaveOfficialLogo: (Uri, (Boolean, String?) -> Unit) -> Unit = { _, cb -> cb(false, null) },
    onResetOfficialLogo: () -> Unit = {},
    onSaveCompetingClubLogo: (Long, String, Uri, (Boolean, String?) -> Unit) -> Unit = { _, _, _, cb -> cb(false, null) },
    onRemoveCompetingClubLogo: (Long) -> Unit = {}
) {
    if (!isAuthenticated) {
        AdminLoginGate(onLogin = onLogin)
        return
    }

    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current

    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val adminTabs = strings.adminTabs

    var editingArticle by remember { mutableStateOf<NewsArticleEntity?>(null) }
    var showNewArticle by remember { mutableStateOf(false) }
    var editingPlayer by remember { mutableStateOf<PlayerEntity?>(null) }
    var showNewPlayer by remember { mutableStateOf(false) }
    var editingMatch by remember { mutableStateOf<MatchEntity?>(null) }
    var showNewMatch by remember { mutableStateOf(false) }
    var editingStanding by remember { mutableStateOf<StandingRowEntity?>(null) }
    var showNewStanding by remember { mutableStateOf(false) }
    var showNewGallery by remember { mutableStateOf(false) }
    var editingClubLogo by remember { mutableStateOf<CompetingClubEntity?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.primaryBackground)
            .testTag("admin_dashboard_screen")
    ) {
        Surface(
            color = colors.secondaryBackground
        ) {
            Column(
                modifier = Modifier.padding(
                    horizontal = RcrSpacing.ScreenHorizontal,
                    vertical = 16.dp
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = strings.adminTitle,
                            style = MaterialTheme.typography.headlineLarge,
                            color = colors.sectionHeading
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = strings.adminSubtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.accentHighlight
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    OutlinedButton(
                        onClick = onLogout,
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, colors.border),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.mainText),
                        modifier = Modifier
                            .heightIn(min = 48.dp)
                            .testTag("admin_logout_button")
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.Logout,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(strings.adminLockBtn, style = MaterialTheme.typography.labelLarge)
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
                BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                    if (maxWidth < 420.dp) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                AdminStatChip(strings.adminStatArticles, allNews.size.toString(), Modifier.weight(1f))
                                AdminStatChip(strings.adminStatPlayers, players.size.toString(), Modifier.weight(1f))
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                AdminStatChip(strings.adminStatMatches, matches.size.toString(), Modifier.weight(1f))
                                AdminStatChip(strings.adminStatMedia, galleryItems.size.toString(), Modifier.weight(1f))
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            AdminStatChip(strings.adminStatArticles, allNews.size.toString(), Modifier.weight(1f))
                            AdminStatChip(strings.adminStatPlayers, players.size.toString(), Modifier.weight(1f))
                            AdminStatChip(strings.adminStatMatches, matches.size.toString(), Modifier.weight(1f))
                            AdminStatChip(strings.adminStatMedia, galleryItems.size.toString(), Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            edgePadding = RcrSpacing.ScreenHorizontal,
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
            adminTabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    selectedContentColor = colors.sectionHeading,
                    unselectedContentColor = colors.secondaryText,
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    },
                    modifier = Modifier.testTag("admin_tab_$index")
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("admin_tab_content_list"),
            contentPadding = PaddingValues(
                start = RcrSpacing.ScreenHorizontal,
                end = RcrSpacing.ScreenHorizontal,
                top = RcrSpacing.ScreenVertical,
                bottom = RcrSpacing.BottomScreenBuffer
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            when (selectedTab) {
                0 -> {
                    item {
                        AdminAddButton(strings.adminAddNewsBtn, "admin_add_news_btn") {
                            showNewArticle = true
                        }
                    }
                    items(allNews, key = { it.id }) { article ->
                        val statusStr = if (article.isPublished) strings.adminPublishedLabel else strings.adminDraftLabel
                        AdminRowCard(
                            title = article.displayTitle(strings.isArabic),
                            subtitle = "${strings.localizeNewsCategory(article.category)} • $statusStr • ${article.publishedDateLabel}",
                            onEdit = { editingArticle = article },
                            onDelete = { onDeleteArticle(article) }
                        )
                    }
                }
                1 -> {
                    item {
                        AdminAddButton(strings.adminAddPlayerBtn, "admin_add_player_btn") {
                            showNewPlayer = true
                        }
                    }
                    items(players, key = { it.id }) { player ->
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
                            border = BorderStroke(1.dp, colors.border)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                PlayerAvatar(player = player, size = 54.dp)
                                Spacer(modifier = Modifier.width(14.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "#${player.shirtNumber} ${player.displayFullName(strings.isArabic)}",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = colors.mainText,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = "${player.displayPosition(strings.isArabic)} (${strings.localizeSquadGroup(player.positionGroup)})",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = colors.accentHighlight
                                    )
                                }
                                IconButton(
                                    onClick = { editingPlayer = player },
                                    modifier = Modifier.minimumInteractiveComponentSize()
                                ) {
                                    Icon(Icons.Outlined.Edit, contentDescription = strings.adminEditDesc, tint = colors.mainText)
                                }
                                IconButton(
                                    onClick = { onDeletePlayer(player) },
                                    modifier = Modifier.minimumInteractiveComponentSize()
                                ) {
                                    Icon(Icons.Outlined.DeleteOutline, contentDescription = strings.adminDeleteDesc, tint = RcrLiveRed)
                                }
                            }
                        }
                    }
                }
                2 -> {
                    item {
                        AdminAddButton(strings.adminAddMatchBtn, "admin_add_match_btn") {
                            showNewMatch = true
                        }
                    }
                    items(matches, key = { it.id }) { match ->
                        val opp = match.displayOpponent(strings.isArabic)
                        val scoreLabel = if (match.rcrScore != null && match.opponentScore != null) {
                            " • ${match.rcrScore}-${match.opponentScore}"
                        } else ""
                        val title = if (match.isHome) {
                            "${strings.clubShortName} ${strings.vsLabel} $opp"
                        } else {
                            "$opp ${strings.vsLabel} ${strings.clubShortName}"
                        }
                        AdminRowCard(
                            title = title,
                            subtitle = "${strings.localizeMatchStatus(match.status)}$scoreLabel • ${match.matchDateLabel} (${match.kickoffTimeLabel})",
                            onEdit = { editingMatch = match },
                            onDelete = { onDeleteMatch(match) }
                        )
                    }
                }
                3 -> {
                    item {
                        AdminAddButton(strings.adminAddStandingBtn, "admin_add_standing_btn") {
                            showNewStanding = true
                        }
                    }
                    items(standings.sortedBy { it.position }, key = { it.id }) { row ->
                        AdminRowCard(
                            title = "${row.position}. ${row.displayClubName(strings.isArabic)} (${row.points} ${strings.colPoints})",
                            subtitle = "${strings.colPlayed} ${row.played} • ${strings.colWins} ${row.wins} • ${strings.colDraws} ${row.draws} • ${strings.colLosses} ${row.losses} • ${strings.colDiff} ${row.goalDifference}",
                            onEdit = { editingStanding = row },
                            onDelete = { onDeleteStanding(row) }
                        )
                    }
                }
                4 -> {
                    item {
                        AdminAddButton(strings.adminAddGalleryBtn, "admin_add_gallery_btn") {
                            showNewGallery = true
                        }
                    }
                    items(galleryItems, key = { it.id }) { item ->
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
                            border = BorderStroke(1.dp, colors.border)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ClubMediaImage(
                                    mediaUri = item.mediaUri,
                                    contentDescription = item.displayTitle(strings.isArabic),
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                )
                                Spacer(modifier = Modifier.width(14.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.displayTitle(strings.isArabic),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = colors.mainText,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "${strings.localizeGalleryCategory(item.category)} • ${item.dateLabel}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = colors.secondaryText
                                    )
                                }
                                IconButton(
                                    onClick = { onDeleteGalleryItem(item) },
                                    modifier = Modifier.minimumInteractiveComponentSize()
                                ) {
                                    Icon(Icons.Outlined.DeleteOutline, contentDescription = strings.adminDeleteDesc, tint = RcrLiveRed)
                                }
                            }
                        }
                    }
                }
                5 -> {
                    item(key = "logos_section_header") {
                        Text(
                            text = strings.adminLogosSectionTitle,
                            style = MaterialTheme.typography.headlineLarge,
                            color = colors.sectionHeading,
                            modifier = Modifier.testTag("admin_logos_section_title")
                        )
                    }
                    item(key = "official_logo_card") {
                        OfficialClubLogoCard(
                            clubLogoPath = clubLogoPath,
                            onSaveOfficialLogo = onSaveOfficialLogo,
                            onResetOfficialLogo = onResetOfficialLogo
                        )
                    }
                    item(key = "competing_logos_header") {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "${strings.adminOpponentLogosTitle} (${competingClubs.size})",
                                style = MaterialTheme.typography.headlineMedium,
                                color = colors.sectionHeading,
                                modifier = Modifier.testTag("admin_competing_logos_header")
                            )
                            Text(
                                text = strings.adminOpponentLogosDesc,
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.secondaryText
                            )
                        }
                    }
                    if (competingClubs.isEmpty()) {
                        item(key = "competing_logos_empty") {
                            EmptyStateCard(
                                title = strings.adminOpponentLogosEmptyTitle,
                                subtitle = strings.adminOpponentLogosEmptySub
                            )
                        }
                    } else {
                        items(competingClubs, key = { it.id }) { club ->
                            CompetingClubLogoRowCard(
                                club = club,
                                onManageLogo = { editingClubLogo = club }
                            )
                        }
                    }
                }
                6 -> {
                    item {
                        ClubAdminEditorSection(
                            clubInfo = clubInfo,
                            contactMessages = contactMessages,
                            onUpdateClubInfo = onUpdateClubInfo,
                            onChangePin = onChangePin
                        )
                    }
                }
            }
        }
    }

    if (showNewArticle || editingArticle != null) {
        ArticleEditorDialog(
            initial = editingArticle,
            onDismiss = { showNewArticle = false; editingArticle = null },
            onSave = {
                onSaveArticle(it)
                showNewArticle = false
                editingArticle = null
            }
        )
    }

    if (showNewPlayer || editingPlayer != null) {
        PlayerEditorDialog(
            initial = editingPlayer,
            onDismiss = { showNewPlayer = false; editingPlayer = null },
            onSave = {
                onSavePlayer(it)
                showNewPlayer = false
                editingPlayer = null
            }
        )
    }

    if (showNewMatch || editingMatch != null) {
        MatchEditorDialog(
            initial = editingMatch,
            onDismiss = { showNewMatch = false; editingMatch = null },
            onSave = {
                onSaveMatch(it)
                showNewMatch = false
                editingMatch = null
            }
        )
    }

    if (showNewStanding || editingStanding != null) {
        StandingEditorDialog(
            initial = editingStanding,
            onDismiss = { showNewStanding = false; editingStanding = null },
            onSave = {
                onSaveStanding(it)
                showNewStanding = false
                editingStanding = null
            }
        )
    }

    if (showNewGallery) {
        GalleryAddDialog(
            onDismiss = { showNewGallery = false },
            onSave = {
                onAddGalleryItem(it)
                showNewGallery = false
            }
        )
    }

    editingClubLogo?.let { selectedClub ->
        val latestClub = competingClubs.find { it.id == selectedClub.id } ?: selectedClub
        CompetingClubLogoDialog(
            club = latestClub,
            onDismiss = { editingClubLogo = null },
            onSaveLogo = { uri, onDone ->
                onSaveCompetingClubLogo(latestClub.id, latestClub.clubShort, uri, onDone)
            },
            onRemoveLogo = {
                onRemoveCompetingClubLogo(latestClub.id)
            }
        )
    }
}

@Composable
private fun OfficialClubLogoCard(
    clubLogoPath: String,
    onSaveOfficialLogo: (Uri, (Boolean, String?) -> Unit) -> Unit,
    onResetOfficialLogo: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current

    var stagedUri by remember { mutableStateOf<Uri?>(null) }
    var isSaving by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf<String?>(null) }

    val hasCustomLogo = remember(clubLogoPath) {
        MediaStorageHelper.isReadableImageFile(context, clubLogoPath)
    }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            scope.launch {
                val valid = MediaStorageHelper.validateImageUri(context, uri)
                if (valid) {
                    stagedUri = uri
                    errorText = null
                } else {
                    errorText = strings.adminLogoInvalidError
                }
            }
        }
    }

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
        border = BorderStroke(1.dp, colors.border),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("official_club_logo_card")
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = strings.adminOfficialLogoTitle,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = colors.sectionHeading
            )
            Text(
                text = strings.adminOfficialLogoDesc,
                style = MaterialTheme.typography.bodySmall,
                color = colors.secondaryText
            )

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = colors.secondaryBackground,
                border = BorderStroke(1.dp, colors.border),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = colors.cardBackground,
                        border = BorderStroke(1.dp, colors.border),
                        modifier = Modifier.size(84.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            RcrClubCrest(size = 68.dp)
                        }
                    }

                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = strings.clubOfficialName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = colors.mainText
                        )
                        Text(
                            text = "${strings.clubShortName} • 1934",
                            style = MaterialTheme.typography.labelMedium,
                            color = colors.secondaryText
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = colors.chipContainer
                        ) {
                            Text(
                                text = if (hasCustomLogo) strings.adminLogoStatusCustom else strings.adminLogoStatusDefault,
                                style = MaterialTheme.typography.labelSmall,
                                color = colors.accentHighlight,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                    enabled = !isSaving,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp)
                        .testTag("official_logo_pick_btn")
                ) {
                    Icon(Icons.Outlined.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (hasCustomLogo) strings.adminLogoReplaceBtn else strings.adminLogoChooseBtn,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (hasCustomLogo) {
                    OutlinedButton(
                        onClick = {
                            stagedUri = null
                            errorText = null
                            onResetOfficialLogo()
                        },
                        enabled = !isSaving,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, colors.border),
                        modifier = Modifier
                            .heightIn(min = 48.dp)
                            .testTag("official_logo_reset_btn")
                    ) {
                        Icon(Icons.Outlined.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(strings.adminLogoResetBtn, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
            }

            AnimatedVisibility(visible = stagedUri != null) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = colors.secondaryBackground,
                    border = BorderStroke(1.5.dp, colors.primaryGreen),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("official_logo_preview_box")
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "${strings.adminLogoPreviewTitle} — ${strings.clubOfficialName}",
                            style = MaterialTheme.typography.labelLarge,
                            color = colors.accentHighlight
                        )
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = colors.cardBackground,
                            border = BorderStroke(1.dp, colors.border),
                            modifier = Modifier.size(120.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                stagedUri?.let { uri ->
                                    AsyncImage(
                                        model = uri,
                                        contentDescription = strings.clubOfficialName,
                                        contentScale = ContentScale.Fit,
                                        onError = {
                                            stagedUri = null
                                            errorText = strings.adminLogoInvalidError
                                        },
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .testTag("official_logo_staged_preview")
                                    )
                                }
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    stagedUri = null
                                    errorText = null
                                },
                                enabled = !isSaving,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = 48.dp)
                                    .testTag("official_logo_cancel_btn")
                            ) {
                                Text(strings.dialogCancel)
                            }
                            Button(
                                onClick = {
                                    val uri = stagedUri ?: return@Button
                                    isSaving = true
                                    onSaveOfficialLogo(uri) { ok, err ->
                                        isSaving = false
                                        if (ok) {
                                            stagedUri = null
                                            errorText = null
                                        } else {
                                            errorText = err ?: strings.adminLogoSaveError
                                        }
                                    }
                                },
                                enabled = !isSaving,
                                colors = ButtonDefaults.buttonColors(containerColor = RcrPrimaryGreen, contentColor = RcrPureWhite),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = 48.dp)
                                    .testTag("official_logo_save_btn")
                            ) {
                                Text(strings.adminLogoSaveBtn)
                            }
                        }
                    }
                }
            }

            errorText?.let { err ->
                Text(
                    text = err,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.testTag("official_logo_error")
                )
            }
        }
    }
}

@Composable
private fun CompetingClubLogoRowCard(
    club: CompetingClubEntity,
    onManageLogo: () -> Unit
) {
    val context = LocalContext.current
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current

    val hasCustomLogo = remember(club.logoPath) {
        MediaStorageHelper.isReadableImageFile(context, club.logoPath)
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
        border = BorderStroke(1.dp, colors.border),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("competing_club_row_${club.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            OpponentClubBadge(
                shortCode = club.clubShort,
                clubName = club.clubName,
                competingClubId = club.id,
                size = 52.dp
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = club.displayName(strings.isArabic),
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.mainText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "${club.clubShort} • ${if (hasCustomLogo) strings.adminLogoStatusCustom else strings.adminOpponentLogoDefaultCode}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (hasCustomLogo) colors.accentHighlight else colors.secondaryText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            OutlinedButton(
                onClick = onManageLogo,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, colors.border),
                modifier = Modifier
                    .heightIn(min = 48.dp)
                    .testTag("manage_club_logo_${club.id}")
            ) {
                Text(
                    text = if (hasCustomLogo) strings.adminLogoReplaceBtn else strings.adminLogoChooseBtn,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Composable
private fun AdminLoginGate(onLogin: (String) -> Boolean) {
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current

    var pin by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.primaryBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = RcrSpacing.ScreenHorizontal, vertical = 28.dp)
            .testTag("admin_login_gate"),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RcrClubCrest(size = 84.dp)
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = strings.adminLoginTitle,
            style = MaterialTheme.typography.displaySmall,
            color = colors.sectionHeading,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = strings.adminLoginDesc,
            style = MaterialTheme.typography.bodyMedium,
            color = colors.secondaryText,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))

        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
            border = BorderStroke(1.dp, colors.border),
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 460.dp)
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = pin,
                    onValueChange = { pin = it; error = null },
                    label = { Text(strings.adminPinLabel) },
                    leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colors.primaryGreen,
                        unfocusedBorderColor = colors.border,
                        focusedLabelColor = colors.accentHighlight,
                        unfocusedLabelColor = colors.secondaryText,
                        focusedTextColor = colors.mainText,
                        unfocusedTextColor = colors.mainText
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_pin_input"),
                    shape = RoundedCornerShape(14.dp)
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = colors.chipContainer
                ) {
                    Text(
                        text = strings.adminDefaultPinHint,
                        modifier = Modifier.padding(14.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.accentHighlight
                    )
                }

                error?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
                }

                Button(
                    onClick = {
                        if (!onLogin(pin)) {
                            error = strings.adminWrongPinError
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RcrPrimaryGreen, contentColor = RcrPureWhite),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 52.dp)
                        .testTag("admin_login_submit")
                ) {
                    Text(strings.adminUnlockBtn, style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Composable
private fun AdminStatChip(label: String, value: String, modifier: Modifier = Modifier) {
    val colors = LocalRcrColors.current
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = colors.cardBackground,
        border = BorderStroke(1.dp, colors.border)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontFamily = BarlowCondensedFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                color = colors.accentHighlight
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = colors.secondaryText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun AdminAddButton(label: String, testTag: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = RcrPrimaryGreen, contentColor = RcrPureWhite),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp)
            .testTag(testTag)
    ) {
        Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
private fun AdminRowCard(
    title: String,
    subtitle: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
        border = BorderStroke(1.dp, colors.border)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.mainText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.secondaryText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onEdit,
                modifier = Modifier.minimumInteractiveComponentSize()
            ) {
                Icon(Icons.Outlined.Edit, contentDescription = strings.adminEditDesc, tint = colors.mainText)
            }
            IconButton(
                onClick = onDelete,
                modifier = Modifier.minimumInteractiveComponentSize()
            ) {
                Icon(Icons.Outlined.DeleteOutline, contentDescription = strings.adminDeleteDesc, tint = RcrLiveRed)
            }
        }
    }
}

@Composable
private fun ClubAdminEditorSection(
    clubInfo: ClubInfoEntity?,
    contactMessages: List<ContactMessageEntity>,
    onUpdateClubInfo: (ClubInfoEntity) -> Unit,
    onChangePin: (String) -> Unit
) {
    if (clubInfo == null) return

    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current

    var officialName by remember(clubInfo) { mutableStateOf(clubInfo.officialName) }
    var officialNameAr by remember(clubInfo) { mutableStateOf(clubInfo.officialNameAr) }
    var history by remember(clubInfo) { mutableStateOf(clubInfo.historyText) }
    var historyAr by remember(clubInfo) { mutableStateOf(clubInfo.historyTextAr) }
    var values by remember(clubInfo) { mutableStateOf(clubInfo.valuesText) }
    var valuesAr by remember(clubInfo) { mutableStateOf(clubInfo.valuesTextAr) }
    var achievements by remember(clubInfo) { mutableStateOf(clubInfo.achievementsText) }
    var achievementsAr by remember(clubInfo) { mutableStateOf(clubInfo.achievementsTextAr) }
    var stadiumName by remember(clubInfo) { mutableStateOf(clubInfo.stadiumName) }
    var stadiumNameAr by remember(clubInfo) { mutableStateOf(clubInfo.stadiumNameAr) }
    var stadiumCapacity by remember(clubInfo) { mutableStateOf(clubInfo.stadiumCapacityLabel) }
    var stadiumDesc by remember(clubInfo) { mutableStateOf(clubInfo.stadiumDescription) }
    var stadiumDescAr by remember(clubInfo) { mutableStateOf(clubInfo.stadiumDescriptionAr) }
    var contactEmail by remember(clubInfo) { mutableStateOf(clubInfo.contactEmail) }
    var contactPhone by remember(clubInfo) { mutableStateOf(clubInfo.contactPhone) }

    var showArabicFields by rememberSaveable { mutableStateOf(false) }
    var newPin by rememberSaveable { mutableStateOf("") }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = colors.primaryGreen,
        unfocusedBorderColor = colors.border,
        focusedLabelColor = colors.accentHighlight,
        unfocusedLabelColor = colors.secondaryText,
        focusedTextColor = colors.mainText,
        unfocusedTextColor = colors.mainText
    )

    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
            border = BorderStroke(1.dp, colors.border)
        ) {
            Column(modifier = Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = strings.adminClubEditorHeader,
                    style = MaterialTheme.typography.headlineLarge,
                    color = colors.sectionHeading
                )
                OutlinedTextField(value = officialName, onValueChange = { officialName = it }, label = { Text(strings.adminClubNameFrLabel) }, colors = fieldColors, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp))
                OutlinedTextField(value = stadiumName, onValueChange = { stadiumName = it }, label = { Text(strings.adminStadiumNameFrLabel) }, colors = fieldColors, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp))
                OutlinedTextField(value = stadiumCapacity, onValueChange = { stadiumCapacity = it }, label = { Text(strings.adminStadiumCapacityLabel) }, colors = fieldColors, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp))
                OutlinedTextField(value = stadiumDesc, onValueChange = { stadiumDesc = it }, label = { Text(strings.adminStadiumDescFrLabel) }, minLines = 3, colors = fieldColors, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp))
                OutlinedTextField(value = history, onValueChange = { history = it }, label = { Text(strings.adminHistoryFrLabel) }, minLines = 3, colors = fieldColors, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp))
                OutlinedTextField(value = values, onValueChange = { values = it }, label = { Text(strings.adminValuesFrLabel) }, minLines = 3, colors = fieldColors, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp))
                OutlinedTextField(value = achievements, onValueChange = { achievements = it }, label = { Text(strings.adminAchievementsFrLabel) }, minLines = 2, colors = fieldColors, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp))
                OutlinedTextField(value = contactEmail, onValueChange = { contactEmail = it }, label = { Text(strings.adminEmailLabel) }, colors = fieldColors, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp))
                OutlinedTextField(value = contactPhone, onValueChange = { contactPhone = it }, label = { Text(strings.adminPhoneLabel) }, colors = fieldColors, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp))

                TextButton(
                    onClick = { showArabicFields = !showArabicFields },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = if (showArabicFields) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                        contentDescription = null,
                        tint = colors.accentHighlight
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(strings.dialogArabicSectionToggle, color = colors.accentHighlight)
                }

                AnimatedVisibility(visible = showArabicFields) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(value = officialNameAr, onValueChange = { officialNameAr = it }, label = { Text(strings.adminClubNameArLabel) }, colors = fieldColors, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp))
                        OutlinedTextField(value = stadiumNameAr, onValueChange = { stadiumNameAr = it }, label = { Text(strings.adminStadiumNameArLabel) }, colors = fieldColors, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp))
                        OutlinedTextField(value = stadiumDescAr, onValueChange = { stadiumDescAr = it }, label = { Text(strings.adminStadiumDescArLabel) }, minLines = 2, colors = fieldColors, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp))
                        OutlinedTextField(value = historyAr, onValueChange = { historyAr = it }, label = { Text(strings.adminHistoryArLabel) }, minLines = 2, colors = fieldColors, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp))
                        OutlinedTextField(value = valuesAr, onValueChange = { valuesAr = it }, label = { Text(strings.adminValuesArLabel) }, minLines = 2, colors = fieldColors, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp))
                        OutlinedTextField(value = achievementsAr, onValueChange = { achievementsAr = it }, label = { Text(strings.adminAchievementsArLabel) }, minLines = 2, colors = fieldColors, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp))
                    }
                }

                Button(
                    onClick = {
                        onUpdateClubInfo(
                            clubInfo.copy(
                                officialName = officialName.trim().ifBlank { clubInfo.officialName },
                                officialNameAr = officialNameAr.trim(),
                                stadiumName = stadiumName.trim(),
                                stadiumNameAr = stadiumNameAr.trim(),
                                stadiumCapacityLabel = stadiumCapacity.trim(),
                                stadiumDescription = stadiumDesc.trim(),
                                stadiumDescriptionAr = stadiumDescAr.trim(),
                                historyText = history.trim(),
                                historyTextAr = historyAr.trim(),
                                valuesText = values.trim(),
                                valuesTextAr = valuesAr.trim(),
                                achievementsText = achievements.trim(),
                                achievementsTextAr = achievementsAr.trim(),
                                contactEmail = contactEmail.trim(),
                                contactPhone = contactPhone.trim()
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RcrPrimaryGreen, contentColor = RcrPureWhite),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 52.dp)
                ) {
                    Text(strings.adminSaveClubBtn, style = MaterialTheme.typography.labelLarge)
                }
            }
        }

        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
            border = BorderStroke(1.dp, RcrGoldAccent.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = strings.adminSecurityHeader,
                    style = MaterialTheme.typography.headlineLarge,
                    color = colors.sectionHeading
                )
                OutlinedTextField(
                    value = newPin,
                    onValueChange = { newPin = it.filter(Char::isDigit).take(8) },
                    label = { Text(strings.adminNewPinLabel) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    singleLine = true,
                    colors = fieldColors,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedButton(
                    onClick = {
                        if (newPin.length >= 4) {
                            onChangePin(newPin)
                            newPin = ""
                        }
                    },
                    enabled = newPin.length >= 4,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 50.dp)
                ) {
                    Text(strings.adminChangePinBtn, style = MaterialTheme.typography.labelLarge)
                }
            }
        }

        Text(
            text = "${strings.adminMessagesHeader} (${contactMessages.size})",
            style = MaterialTheme.typography.headlineLarge,
            color = colors.sectionHeading
        )
        if (contactMessages.isEmpty()) {
            EmptyStateCard(
                title = strings.adminNoMessagesTitle,
                subtitle = strings.adminNoMessagesSub
            )
        } else {
            contactMessages.forEach { msg ->
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
                    border = BorderStroke(1.dp, colors.border)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(msg.subject, style = MaterialTheme.typography.titleMedium, color = colors.mainText)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${msg.senderName} • ${msg.senderEmail}",
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.accentHighlight
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(msg.message, style = MaterialTheme.typography.bodyMedium, color = colors.secondaryText)
                    }
                }
            }
        }
    }
}
