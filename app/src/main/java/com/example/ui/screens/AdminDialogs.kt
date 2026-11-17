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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.data.local.CompetingClubEntity
import com.example.data.local.GalleryItemEntity
import com.example.data.local.MatchEntity
import com.example.data.local.MediaStorageHelper
import com.example.data.local.NewsArticleEntity
import com.example.data.local.PlayerEntity
import com.example.data.local.StandingRowEntity
import com.example.ui.components.ClubMediaImage
import com.example.ui.components.OpponentClubBadge
import com.example.ui.localization.LocalRcrStrings
import com.example.ui.theme.LocalRcrColors
import com.example.ui.theme.RcrLiveRed
import com.example.ui.theme.RcrPrimaryGreen
import com.example.ui.theme.RcrPureWhite
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
private fun ArabicFieldsAccordion(
    expanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current

    TextButton(
        onClick = onToggle,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
            contentDescription = null,
            tint = colors.accentHighlight
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(strings.dialogArabicSectionToggle, color = colors.accentHighlight)
    }

    AnimatedVisibility(visible = expanded) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            content()
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ArticleEditorDialog(
    initial: NewsArticleEntity?,
    onDismiss: () -> Unit,
    onSave: (NewsArticleEntity) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current
    val todayLabel = remember {
        SimpleDateFormat("dd MMM yyyy", Locale.FRENCH).format(Date())
    }

    var title by remember { mutableStateOf(initial?.title.orEmpty()) }
    var summary by remember { mutableStateOf(initial?.summary.orEmpty()) }
    var content by remember { mutableStateOf(initial?.content.orEmpty()) }
    var titleAr by remember { mutableStateOf(initial?.titleAr.orEmpty()) }
    var summaryAr by remember { mutableStateOf(initial?.summaryAr.orEmpty()) }
    var contentAr by remember { mutableStateOf(initial?.contentAr.orEmpty()) }
    var showArabic by remember {
        mutableStateOf(
            strings.isArabic || !initial?.titleAr.isNullOrBlank() || !initial?.contentAr.isNullOrBlank()
        )
    }
    var category by remember { mutableStateOf(initial?.category ?: "Équipe A") }
    var imageUri by remember { mutableStateOf(initial?.imageUri ?: "drawable://img_training_action") }
    var publishedDate by remember { mutableStateOf(initial?.publishedDateLabel ?: todayLabel) }
    var isFeatured by remember { mutableStateOf(initial?.isFeatured ?: false) }
    var isPublished by remember { mutableStateOf(initial?.isPublished ?: true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            scope.launch {
                val savedPath = MediaStorageHelper.persistPickedMedia(context, uri, "news")
                if (savedPath != null) imageUri = savedPath
            }
        }
    }

    val categories = listOf("Équipe A", "Matchs", "Club", "Supporters", "Académie")

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = colors.cardBackground,
        titleContentColor = colors.sectionHeading,
        textContentColor = colors.mainText,
        title = {
            Text(
                text = if (initial == null) strings.dialogNewArticle else strings.dialogEditArticle,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                ClubMediaImage(
                    mediaUri = imageUri,
                    contentDescription = strings.dialogImportCover,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(14.dp))
                )
                OutlinedButton(
                    onClick = {
                        photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                ) {
                    Icon(Icons.Outlined.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(strings.dialogImportCover)
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it; errorMsg = null },
                    label = { Text(strings.dialogTitleFr) },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = summary,
                    onValueChange = { summary = it; errorMsg = null },
                    label = { Text(strings.dialogSummaryFr) },
                    minLines = 2,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it; errorMsg = null },
                    label = { Text(strings.dialogContentFr) },
                    minLines = 4,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                ArabicFieldsAccordion(expanded = showArabic, onToggle = { showArabic = !showArabic }) {
                    OutlinedTextField(
                        value = titleAr,
                        onValueChange = { titleAr = it },
                        label = { Text(strings.dialogTitleAr) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = summaryAr,
                        onValueChange = { summaryAr = it },
                        label = { Text(strings.dialogSummaryAr) },
                        minLines = 2,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = contentAr,
                        onValueChange = { contentAr = it },
                        label = { Text(strings.dialogContentAr) },
                        minLines = 3,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                OutlinedTextField(
                    value = publishedDate,
                    onValueChange = { publishedDate = it },
                    label = { Text(strings.dialogPubDate) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(strings.dialogCategory, style = MaterialTheme.typography.labelLarge)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        categories.forEach { cat ->
                            FilterChip(
                                selected = category == cat,
                                onClick = { category = cat },
                                label = { Text(strings.localizeNewsCategory(cat)) }
                            )
                        }
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(checked = isPublished, onCheckedChange = { isPublished = it })
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(strings.dialogPublishNow, style = MaterialTheme.typography.bodyMedium)
                    }
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(checked = isFeatured, onCheckedChange = { isFeatured = it })
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(strings.dialogFeatured, style = MaterialTheme.typography.bodyMedium)
                    }
                }

                errorMsg?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isBlank() || summary.isBlank() || content.isBlank()) {
                        errorMsg = strings.dialogErrorArticle
                    } else {
                        onSave(
                            NewsArticleEntity(
                                id = initial?.id ?: 0L,
                                title = title.trim(),
                                summary = summary.trim(),
                                content = content.trim(),
                                category = category,
                                imageUri = imageUri,
                                publishedDateLabel = publishedDate.ifBlank { todayLabel },
                                createdAtMillis = initial?.createdAtMillis ?: System.currentTimeMillis(),
                                isFeatured = isFeatured,
                                isPublished = isPublished,
                                isPlaceholder = false,
                                titleAr = titleAr.trim(),
                                summaryAr = summaryAr.trim(),
                                contentAr = contentAr.trim()
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = RcrPrimaryGreen, contentColor = RcrPureWhite),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(strings.dialogSave)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(strings.dialogCancel) }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlayerEditorDialog(
    initial: PlayerEntity?,
    onDismiss: () -> Unit,
    onSave: (PlayerEntity) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current

    var fullName by remember { mutableStateOf(initial?.fullName.orEmpty()) }
    var fullNameAr by remember { mutableStateOf(initial?.fullNameAr.orEmpty()) }
    var shirtNumberText by remember { mutableStateOf(initial?.shirtNumber?.toString() ?: "") }
    var position by remember { mutableStateOf(initial?.position.orEmpty()) }
    var positionAr by remember { mutableStateOf(initial?.positionAr.orEmpty()) }
    var positionGroup by remember { mutableStateOf(initial?.positionGroup ?: "Milieux") }
    var nationality by remember { mutableStateOf(initial?.nationality.orEmpty()) }
    var nationalityAr by remember { mutableStateOf(initial?.nationalityAr.orEmpty()) }
    var biography by remember { mutableStateOf(initial?.biography.orEmpty()) }
    var biographyAr by remember { mutableStateOf(initial?.biographyAr.orEmpty()) }
    var showArabic by remember {
        mutableStateOf(
            strings.isArabic || !initial?.fullNameAr.isNullOrBlank() || !initial?.positionAr.isNullOrBlank()
        )
    }
    var photoUri by remember { mutableStateOf(initial?.photoUri.orEmpty()) }
    var appearancesText by remember { mutableStateOf(initial?.appearances?.toString().orEmpty()) }
    var goalsText by remember { mutableStateOf(initial?.goals?.toString().orEmpty()) }
    var assistsText by remember { mutableStateOf(initial?.assists?.toString().orEmpty()) }
    var isHighlighted by remember { mutableStateOf(initial?.isHighlighted ?: false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            scope.launch {
                val savedPath = MediaStorageHelper.persistPickedMedia(context, uri, "player")
                if (savedPath != null) photoUri = savedPath
            }
        }
    }

    val groups = listOf("Gardiens", "Défenseurs", "Milieux", "Attaquants")

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = colors.cardBackground,
        titleContentColor = colors.sectionHeading,
        textContentColor = colors.mainText,
        title = {
            Text(
                text = if (initial == null) strings.dialogNewPlayer else strings.dialogEditPlayer,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                ) {
                    Icon(Icons.Outlined.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (photoUri.isBlank()) strings.dialogPickPhoto else strings.dialogChangePhoto)
                }

                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it; errorMsg = null },
                    label = { Text(strings.dialogFullNameFr) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = shirtNumberText,
                    onValueChange = { shirtNumberText = it.filter { ch -> ch.isDigit() }; errorMsg = null },
                    label = { Text(strings.dialogShirtNum) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = position,
                    onValueChange = { position = it; errorMsg = null },
                    label = { Text(strings.dialogPositionFr) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(strings.dialogPosGroup, style = MaterialTheme.typography.labelLarge)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        groups.forEach { group ->
                            FilterChip(
                                selected = positionGroup == group,
                                onClick = { positionGroup = group },
                                label = { Text(strings.localizeSquadGroup(group)) }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = nationality,
                    onValueChange = { nationality = it },
                    label = { Text(strings.dialogNationalityFr) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = biography,
                    onValueChange = { biography = it },
                    label = { Text(strings.dialogBioFr) },
                    minLines = 3,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                ArabicFieldsAccordion(expanded = showArabic, onToggle = { showArabic = !showArabic }) {
                    OutlinedTextField(
                        value = fullNameAr,
                        onValueChange = { fullNameAr = it },
                        label = { Text(strings.dialogFullNameAr) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = positionAr,
                        onValueChange = { positionAr = it },
                        label = { Text(strings.dialogPositionAr) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = nationalityAr,
                        onValueChange = { nationalityAr = it },
                        label = { Text(strings.dialogNationalityAr) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = biographyAr,
                        onValueChange = { biographyAr = it },
                        label = { Text(strings.dialogBioAr) },
                        minLines = 2,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = appearancesText,
                        onValueChange = { appearancesText = it.filter(Char::isDigit) },
                        label = { Text(strings.statMatches) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = goalsText,
                        onValueChange = { goalsText = it.filter(Char::isDigit) },
                        label = { Text(strings.statGoals) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = assistsText,
                        onValueChange = { assistsText = it.filter(Char::isDigit) },
                        label = { Text(strings.statAssists) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = isHighlighted, onCheckedChange = { isHighlighted = it })
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(strings.dialogHighlightHome, style = MaterialTheme.typography.bodyMedium)
                }

                errorMsg?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val number = shirtNumberText.toIntOrNull()
                    if (fullName.isBlank() || position.isBlank() || number == null) {
                        errorMsg = strings.dialogErrorPlayer
                    } else {
                        onSave(
                            PlayerEntity(
                                id = initial?.id ?: 0L,
                                fullName = fullName.trim(),
                                shirtNumber = number,
                                position = position.trim(),
                                positionGroup = positionGroup,
                                nationality = nationality.trim(),
                                biography = biography.trim(),
                                photoUri = photoUri,
                                appearances = appearancesText.toIntOrNull(),
                                goals = goalsText.toIntOrNull(),
                                assists = assistsText.toIntOrNull(),
                                isHighlighted = isHighlighted,
                                isPlaceholder = false,
                                fullNameAr = fullNameAr.trim(),
                                positionAr = positionAr.trim(),
                                nationalityAr = nationalityAr.trim(),
                                biographyAr = biographyAr.trim()
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = RcrPrimaryGreen, contentColor = RcrPureWhite),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(strings.dialogSave)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(strings.dialogCancel) }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MatchEditorDialog(
    initial: MatchEntity?,
    onDismiss: () -> Unit,
    onSave: (MatchEntity) -> Unit
) {
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current

    var opponentName by remember { mutableStateOf(initial?.opponentName.orEmpty()) }
    var opponentNameAr by remember { mutableStateOf(initial?.opponentNameAr.orEmpty()) }
    var opponentShort by remember { mutableStateOf(initial?.opponentShort.orEmpty()) }
    var competition by remember { mutableStateOf(initial?.competition ?: "Championnat") }
    var competitionAr by remember { mutableStateOf(initial?.competitionAr.orEmpty()) }
    var dateLabel by remember { mutableStateOf(initial?.matchDateLabel ?: "28 Nov 2026") }
    var timeLabel by remember { mutableStateOf(initial?.kickoffTimeLabel ?: "16:00") }
    var daysOffsetText by remember { mutableStateOf("5") }
    var stadium by remember { mutableStateOf(initial?.stadium ?: "Stade Tahar Zoughari, Relizane") }
    var stadiumAr by remember { mutableStateOf(initial?.stadiumAr.orEmpty()) }
    var isHome by remember { mutableStateOf(initial?.isHome ?: true) }
    var status by remember { mutableStateOf(initial?.status ?: "À venir") }
    var rcrScoreText by remember { mutableStateOf(initial?.rcrScore?.toString().orEmpty()) }
    var oppScoreText by remember { mutableStateOf(initial?.opponentScore?.toString().orEmpty()) }
    var isFeaturedNext by remember { mutableStateOf(initial?.isFeaturedNext ?: false) }
    var notes by remember { mutableStateOf(initial?.matchNotes.orEmpty()) }
    var notesAr by remember { mutableStateOf(initial?.matchNotesAr.orEmpty()) }
    var showArabic by remember {
        mutableStateOf(strings.isArabic || !initial?.opponentNameAr.isNullOrBlank())
    }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val statuses = listOf("À venir", "En direct", "Terminé")

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = colors.cardBackground,
        titleContentColor = colors.sectionHeading,
        textContentColor = colors.mainText,
        title = {
            Text(
                text = if (initial == null) strings.dialogNewMatch else strings.dialogEditMatch,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                OutlinedTextField(
                    value = opponentName,
                    onValueChange = { opponentName = it; errorMsg = null },
                    label = { Text(strings.dialogOpponentFr) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = opponentShort,
                    onValueChange = { opponentShort = it.take(3).uppercase(); errorMsg = null },
                    label = { Text(strings.dialogOpponentShort) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = competition,
                    onValueChange = { competition = it },
                    label = { Text(strings.dialogCompetitionFr) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = dateLabel,
                        onValueChange = { dateLabel = it },
                        label = { Text(strings.dialogDateField) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = timeLabel,
                        onValueChange = { timeLabel = it },
                        label = { Text(strings.dialogTimeField) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    )
                }
                if (initial == null && status == "À venir") {
                    OutlinedTextField(
                        value = daysOffsetText,
                        onValueChange = { daysOffsetText = it.filter(Char::isDigit) },
                        label = { Text(strings.dialogDaysOffset) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                OutlinedTextField(
                    value = stadium,
                    onValueChange = { stadium = it },
                    label = { Text(strings.dialogStadiumFr) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = strings.dialogIsHome,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Switch(checked = isHome, onCheckedChange = { isHome = it })
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(strings.dialogStatusLabel, style = MaterialTheme.typography.labelLarge)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        statuses.forEach { st ->
                            FilterChip(
                                selected = status == st,
                                onClick = { status = st },
                                label = { Text(strings.localizeMatchStatus(st)) }
                            )
                        }
                    }
                }

                if (status == "Terminé" || status == "En direct") {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = rcrScoreText,
                            onValueChange = { rcrScoreText = it.filter(Char::isDigit) },
                            label = { Text(strings.dialogScoreRcr) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = oppScoreText,
                            onValueChange = { oppScoreText = it.filter(Char::isDigit) },
                            label = { Text(strings.dialogScoreOpp) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                if (status != "Terminé") {
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(checked = isFeaturedNext, onCheckedChange = { isFeaturedNext = it })
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(strings.dialogShowNextMatch, style = MaterialTheme.typography.bodyMedium)
                    }
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text(strings.dialogNotesFr) },
                    minLines = 2,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                ArabicFieldsAccordion(expanded = showArabic, onToggle = { showArabic = !showArabic }) {
                    OutlinedTextField(
                        value = opponentNameAr,
                        onValueChange = { opponentNameAr = it },
                        label = { Text(strings.dialogOpponentAr) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = competitionAr,
                        onValueChange = { competitionAr = it },
                        label = { Text(strings.dialogCompetitionAr) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = stadiumAr,
                        onValueChange = { stadiumAr = it },
                        label = { Text(strings.dialogStadiumAr) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = notesAr,
                        onValueChange = { notesAr = it },
                        label = { Text(strings.dialogNotesAr) },
                        minLines = 2,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                errorMsg?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (opponentName.isBlank() || opponentShort.isBlank() || competition.isBlank()) {
                        errorMsg = strings.dialogErrorMatch
                    } else {
                        val computedTimestamp = initial?.kickoffTimestampMillis ?: run {
                            val days = daysOffsetText.toLongOrNull() ?: 5L
                            if (status == "Terminé") {
                                System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)
                            } else {
                                System.currentTimeMillis() + TimeUnit.DAYS.toMillis(days)
                            }
                        }
                        onSave(
                            MatchEntity(
                                id = initial?.id ?: 0L,
                                opponentName = opponentName.trim(),
                                opponentShort = opponentShort.trim().uppercase(),
                                competition = competition.trim(),
                                matchDateLabel = dateLabel.trim(),
                                kickoffTimeLabel = timeLabel.trim(),
                                kickoffTimestampMillis = computedTimestamp,
                                stadium = stadium.trim(),
                                isHome = isHome,
                                rcrScore = rcrScoreText.toIntOrNull(),
                                opponentScore = oppScoreText.toIntOrNull(),
                                status = status,
                                isFeaturedNext = isFeaturedNext && status != "Terminé",
                                matchNotes = notes.trim(),
                                isPlaceholder = false,
                                opponentNameAr = opponentNameAr.trim(),
                                competitionAr = competitionAr.trim(),
                                stadiumAr = stadiumAr.trim(),
                                matchNotesAr = notesAr.trim()
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = RcrPrimaryGreen, contentColor = RcrPureWhite),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(strings.dialogSave)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(strings.dialogCancel) }
        }
    )
}

@Composable
fun StandingEditorDialog(
    initial: StandingRowEntity?,
    onDismiss: () -> Unit,
    onSave: (StandingRowEntity) -> Unit
) {
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current

    var positionText by remember { mutableStateOf(initial?.position?.toString() ?: "1") }
    var clubName by remember { mutableStateOf(initial?.clubName.orEmpty()) }
    var clubNameAr by remember { mutableStateOf(initial?.clubNameAr.orEmpty()) }
    var clubShort by remember { mutableStateOf(initial?.clubShort.orEmpty()) }
    var playedText by remember { mutableStateOf(initial?.played?.toString() ?: "0") }
    var winsText by remember { mutableStateOf(initial?.wins?.toString() ?: "0") }
    var drawsText by remember { mutableStateOf(initial?.draws?.toString() ?: "0") }
    var lossesText by remember { mutableStateOf(initial?.losses?.toString() ?: "0") }
    var gfText by remember { mutableStateOf(initial?.goalsFor?.toString() ?: "0") }
    var gaText by remember { mutableStateOf(initial?.goalsAgainst?.toString() ?: "0") }
    var pointsText by remember { mutableStateOf(initial?.points?.toString() ?: "0") }
    var isRcr by remember { mutableStateOf(initial?.isRcr ?: false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = colors.cardBackground,
        titleContentColor = colors.sectionHeading,
        textContentColor = colors.mainText,
        title = {
            Text(
                text = if (initial == null) strings.dialogAddStanding else strings.dialogEditStanding,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = positionText,
                        onValueChange = { positionText = it.filter(Char::isDigit) },
                        label = { Text(strings.dialogPosField) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(0.38f)
                    )
                    OutlinedTextField(
                        value = clubShort,
                        onValueChange = { clubShort = it.take(3).uppercase() },
                        label = { Text(strings.dialogCodeField) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(0.62f)
                    )
                }
                OutlinedTextField(
                    value = clubName,
                    onValueChange = { clubName = it; errorMsg = null },
                    label = { Text(strings.dialogClubNameFr) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = clubNameAr,
                    onValueChange = { clubNameAr = it },
                    label = { Text(strings.dialogClubNameAr) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = playedText,
                        onValueChange = { playedText = it.filter(Char::isDigit) },
                        label = { Text(strings.colPlayed) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = winsText,
                        onValueChange = { winsText = it.filter(Char::isDigit) },
                        label = { Text(strings.colWins) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = drawsText,
                        onValueChange = { drawsText = it.filter(Char::isDigit) },
                        label = { Text(strings.colDraws) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = lossesText,
                        onValueChange = { lossesText = it.filter(Char::isDigit) },
                        label = { Text(strings.colLosses) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = gfText,
                        onValueChange = { gfText = it.filter(Char::isDigit) },
                        label = { Text(strings.colGoalsFor) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = gaText,
                        onValueChange = { gaText = it.filter(Char::isDigit) },
                        label = { Text(strings.colGoalsAgainst) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = pointsText,
                        onValueChange = { pointsText = it.filter(Char::isDigit) },
                        label = { Text(strings.colPoints) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = isRcr, onCheckedChange = { isRcr = it })
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(strings.dialogHighlightRcr, style = MaterialTheme.typography.bodyMedium)
                }
                errorMsg?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (clubName.isBlank() || clubShort.isBlank()) {
                        errorMsg = strings.dialogErrorStanding
                    } else {
                        onSave(
                            StandingRowEntity(
                                id = initial?.id ?: 0L,
                                position = positionText.toIntOrNull() ?: 1,
                                clubName = clubName.trim(),
                                clubShort = clubShort.trim().uppercase(),
                                played = playedText.toIntOrNull() ?: 0,
                                wins = winsText.toIntOrNull() ?: 0,
                                draws = drawsText.toIntOrNull() ?: 0,
                                losses = lossesText.toIntOrNull() ?: 0,
                                goalsFor = gfText.toIntOrNull() ?: 0,
                                goalsAgainst = gaText.toIntOrNull() ?: 0,
                                points = pointsText.toIntOrNull() ?: 0,
                                isRcr = isRcr,
                                isPlaceholder = false,
                                clubNameAr = clubNameAr.trim()
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = RcrPrimaryGreen, contentColor = RcrPureWhite),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(strings.dialogSave)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(strings.dialogCancel) }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GalleryAddDialog(
    onDismiss: () -> Unit,
    onSave: (GalleryItemEntity) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current
    val todayLabel = remember {
        SimpleDateFormat("dd MMM yyyy", Locale.FRENCH).format(Date())
    }

    var title by remember { mutableStateOf("") }
    var titleAr by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Matchs") }
    var mediaUri by remember { mutableStateOf("drawable://img_hero_stadium") }
    var isVideo by remember { mutableStateOf(false) }
    var duration by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val mediaPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            scope.launch {
                val savedPath = MediaStorageHelper.persistPickedMedia(context, uri, "gallery")
                if (savedPath != null) mediaUri = savedPath
            }
        }
    }

    val categories = listOf("Matchs", "Entraînement", "Joueurs", "Supporters", "Événements", "Vidéos")

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = colors.cardBackground,
        titleContentColor = colors.sectionHeading,
        textContentColor = colors.mainText,
        title = {
            Text(
                text = strings.dialogAddGallery,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                ClubMediaImage(
                    mediaUri = mediaUri,
                    contentDescription = strings.dialogSelectMedia,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(14.dp))
                )
                OutlinedButton(
                    onClick = {
                        val requestType = if (isVideo) {
                            ActivityResultContracts.PickVisualMedia.ImageAndVideo
                        } else {
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        }
                        mediaPicker.launch(PickVisualMediaRequest(requestType))
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                ) {
                    Icon(Icons.Outlined.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(strings.dialogSelectMedia)
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it; errorMsg = null },
                    label = { Text(strings.dialogGalleryTitleFr) },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = titleAr,
                    onValueChange = { titleAr = it },
                    label = { Text(strings.dialogGalleryTitleAr) },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(strings.dialogAlbumLabel, style = MaterialTheme.typography.labelLarge)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        categories.forEach { cat ->
                            FilterChip(
                                selected = category == cat,
                                onClick = {
                                    category = cat
                                    if (cat == "Vidéos") isVideo = true
                                },
                                label = { Text(strings.localizeGalleryCategory(cat)) }
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = isVideo, onCheckedChange = { isVideo = it })
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(strings.dialogIsVideo, style = MaterialTheme.typography.bodyMedium)
                }

                if (isVideo) {
                    OutlinedTextField(
                        value = duration,
                        onValueChange = { duration = it },
                        label = { Text(strings.dialogDurationLabel) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                errorMsg?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isBlank()) {
                        errorMsg = strings.dialogErrorTitle
                    } else {
                        onSave(
                            GalleryItemEntity(
                                title = title.trim(),
                                category = category,
                                mediaUri = mediaUri,
                                isVideo = isVideo,
                                videoDurationLabel = if (isVideo) duration.trim() else "",
                                dateLabel = todayLabel,
                                isPlaceholder = false,
                                titleAr = titleAr.trim()
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = RcrPrimaryGreen, contentColor = RcrPureWhite),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(strings.dialogAdd)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(strings.dialogCancel) }
        }
    )
}

@Composable
fun CompetingClubLogoDialog(
    club: CompetingClubEntity,
    onDismiss: () -> Unit,
    onSaveLogo: (Uri, (Boolean, String?) -> Unit) -> Unit,
    onRemoveLogo: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current

    var stagedUri by remember(club.id) { mutableStateOf<Uri?>(null) }
    var isSaving by remember(club.id) { mutableStateOf(false) }
    var errorMsg by remember(club.id) { mutableStateOf<String?>(null) }

    val hasActiveLogo = remember(club.logoPath) {
        MediaStorageHelper.isReadableImageFile(context, club.logoPath)
    }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            scope.launch {
                val isValid = MediaStorageHelper.validateImageUri(context, uri)
                if (isValid) {
                    stagedUri = uri
                    errorMsg = null
                } else {
                    errorMsg = strings.adminLogoInvalidError
                }
            }
        }
    }

    AlertDialog(
        onDismissRequest = { if (!isSaving) onDismiss() },
        containerColor = colors.cardBackground,
        titleContentColor = colors.sectionHeading,
        textContentColor = colors.mainText,
        modifier = Modifier.testTag("competing_club_logo_dialog"),
        title = {
            Text(
                text = strings.adminOpponentLogoDialogTitle,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = colors.secondaryBackground,
                    border = BorderStroke(1.dp, colors.border),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
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
                                fontWeight = FontWeight.Bold,
                                color = colors.mainText
                            )
                            Text(
                                text = "${club.clubShort} • ${if (hasActiveLogo) strings.adminLogoStatusCustom else strings.adminOpponentLogoDefaultCode}",
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.accentHighlight
                            )
                        }
                    }
                }

                if (stagedUri != null) {
                    Text(
                        text = strings.adminLogoPreviewTitle,
                        style = MaterialTheme.typography.labelLarge,
                        color = colors.accentHighlight
                    )
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = colors.secondaryBackground,
                        border = BorderStroke(1.5.dp, colors.primaryGreen),
                        modifier = Modifier.size(124.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = stagedUri,
                                contentDescription = club.displayName(strings.isArabic),
                                contentScale = ContentScale.Fit,
                                onError = {
                                    stagedUri = null
                                    errorMsg = strings.adminLogoInvalidError
                                },
                                modifier = Modifier
                                    .fillMaxSize()
                                    .testTag("competing_logo_preview")
                            )
                        }
                    }
                }

                OutlinedButton(
                    onClick = {
                        photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                    enabled = !isSaving,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                        .testTag("competing_logo_pick_btn")
                ) {
                    Icon(Icons.Outlined.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(strings.adminOpponentLogoPickBtn)
                }

                if (hasActiveLogo) {
                    OutlinedButton(
                        onClick = {
                            onRemoveLogo()
                            onDismiss()
                        },
                        enabled = !isSaving,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, RcrLiveRed.copy(alpha = 0.55f)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = RcrLiveRed),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp)
                            .testTag("competing_logo_remove_btn")
                    ) {
                        Icon(Icons.Outlined.DeleteOutline, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(strings.adminOpponentLogoRemoveBtn)
                    }
                }

                errorMsg?.let { err ->
                    Text(
                        text = err,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.testTag("competing_logo_error")
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val uri = stagedUri ?: return@Button
                    isSaving = true
                    onSaveLogo(uri) { ok, err ->
                        isSaving = false
                        if (ok) {
                            onDismiss()
                        } else {
                            errorMsg = err ?: strings.adminLogoSaveError
                        }
                    }
                },
                enabled = stagedUri != null && !isSaving,
                colors = ButtonDefaults.buttonColors(containerColor = RcrPrimaryGreen, contentColor = RcrPureWhite),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("competing_logo_save_btn")
            ) {
                Text(strings.adminLogoSaveBtn)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isSaving,
                modifier = Modifier.testTag("competing_logo_cancel_btn")
            ) {
                Text(strings.dialogCancel)
            }
        }
    )
}

