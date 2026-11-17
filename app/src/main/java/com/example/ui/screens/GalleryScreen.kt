package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.local.GalleryItemEntity
import com.example.ui.components.ClubMediaImage
import com.example.ui.components.EmptyStateCard
import com.example.ui.localization.LocalRcrStrings
import com.example.ui.theme.LocalRcrColors
import com.example.ui.theme.RcrDeepGreen
import com.example.ui.theme.RcrPureWhite
import com.example.ui.theme.RcrSoftMint
import com.example.ui.theme.RcrSpacing

private val galleryCategories = listOf(
    "Toutes", "Matchs", "Entraînement", "Joueurs", "Supporters", "Événements", "Vidéos"
)

@Composable
fun GalleryScreen(
    items: List<GalleryItemEntity>,
    selectedCategory: String,
    onSelectCategory: (String) -> Unit
) {
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current

    var previewItem by remember { mutableStateOf<GalleryItemEntity?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.primaryBackground)
            .testTag("gallery_screen")
    ) {
        LazyRow(
            contentPadding = PaddingValues(
                horizontal = RcrSpacing.ScreenHorizontal,
                vertical = 14.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(RcrSpacing.ChipGap)
        ) {
            items(galleryCategories) { category ->
                val isSelected = category == selectedCategory
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectCategory(category) },
                    label = {
                        Text(
                            text = strings.localizeGalleryCategory(category),
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
                    modifier = Modifier.testTag("gallery_chip_$category")
                )
            }
        }

        if (items.isEmpty()) {
            Box(modifier = Modifier.padding(RcrSpacing.ScreenHorizontal)) {
                EmptyStateCard(
                    title = strings.galleryEmptyTitle,
                    subtitle = strings.galleryEmptySub
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 170.dp),
                contentPadding = PaddingValues(
                    start = RcrSpacing.ScreenHorizontal,
                    end = RcrSpacing.ScreenHorizontal,
                    top = 6.dp,
                    bottom = RcrSpacing.BottomScreenBuffer
                ),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(items, key = { it.id }) { media ->
                    GalleryGridCard(
                        item = media,
                        onClick = { previewItem = media }
                    )
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }

    previewItem?.let { item ->
        val displayTitle = item.displayTitle(strings.isArabic)
        val displayCategory = strings.localizeGalleryCategory(item.category)
        Dialog(
            onDismissRequest = { previewItem = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.95f))
                    .padding(20.dp)
            ) {
                IconButton(
                    onClick = { previewItem = null },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = strings.galleryClosePreviewDesc,
                        tint = RcrPureWhite,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(4f / 3f)
                    ) {
                        ClubMediaImage(
                            mediaUri = item.mediaUri,
                            contentDescription = displayTitle,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                        if (item.isVideo) {
                            Surface(
                                shape = CircleShape,
                                color = RcrDeepGreen.copy(alpha = 0.92f),
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(70.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Filled.PlayArrow,
                                        contentDescription = displayCategory,
                                        tint = RcrPureWhite,
                                        modifier = Modifier.size(40.dp)
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = displayTitle,
                        style = MaterialTheme.typography.headlineMedium,
                        color = RcrPureWhite,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "$displayCategory • ${item.dateLabel}" +
                            if (item.isVideo && item.videoDurationLabel.isNotBlank()) " • ${item.videoDurationLabel}" else "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = RcrSoftMint
                    )
                }
            }
        }
    }
}

@Composable
private fun GalleryGridCard(
    item: GalleryItemEntity,
    onClick: () -> Unit
) {
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current
    val displayTitle = item.displayTitle(strings.isArabic)
    val displayCategory = strings.localizeGalleryCategory(item.category)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.88f)
            .clickable(onClick = onClick)
            .testTag("gallery_item_${item.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
        border = BorderStroke(1.dp, colors.border)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            ClubMediaImage(
                mediaUri = item.mediaUri,
                contentDescription = displayTitle,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.55f),
                                Color.Black.copy(alpha = 0.9f)
                            )
                        )
                    )
            )
            if (item.isVideo) {
                Surface(
                    shape = CircleShape,
                    color = RcrDeepGreen.copy(alpha = 0.92f),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(50.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = displayCategory,
                            tint = RcrPureWhite,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
            Surface(
                shape = RoundedCornerShape(50),
                color = Color.Black.copy(alpha = 0.65f),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
            ) {
                Text(
                    text = displayCategory,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = RcrSoftMint
                )
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(14.dp)
            ) {
                Text(
                    text = displayTitle,
                    style = MaterialTheme.typography.titleSmall,
                    color = RcrPureWhite,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.dateLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = RcrSoftMint
                )
            }
        }
    }
}
