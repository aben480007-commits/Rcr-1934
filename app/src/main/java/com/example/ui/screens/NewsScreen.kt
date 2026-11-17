package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.data.local.NewsArticleEntity
import com.example.ui.components.ClubMediaImage
import com.example.ui.components.EmptyStateCard
import com.example.ui.components.NewsEditorialCard
import com.example.ui.localization.LocalRcrStrings
import com.example.ui.theme.LocalRcrColors
import com.example.ui.theme.RcrDeepGreen
import com.example.ui.theme.RcrPrimaryGreen
import com.example.ui.theme.RcrPureWhite
import com.example.ui.theme.RcrSoftMint
import com.example.ui.theme.RcrSpacing

private val newsCategories = listOf("Toutes", "Équipe A", "Matchs", "Club", "Supporters", "Académie")

@Composable
fun NewsScreen(
    articles: List<NewsArticleEntity>,
    searchQuery: String,
    selectedCategory: String,
    onSearchQueryChange: (String) -> Unit,
    onSelectCategory: (String) -> Unit,
    onOpenArticle: (Long) -> Unit
) {
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current

    val featured = articles.firstOrNull { it.isFeatured } ?: articles.firstOrNull()
    val feedArticles = if (featured != null && searchQuery.isBlank() && selectedCategory == "Toutes") {
        articles.filterNot { it.id == featured.id }
    } else {
        articles
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.primaryBackground)
            .testTag("news_screen_list"),
        contentPadding = PaddingValues(
            start = RcrSpacing.ScreenHorizontal,
            end = RcrSpacing.ScreenHorizontal,
            top = RcrSpacing.ScreenVertical,
            bottom = RcrSpacing.BottomScreenBuffer
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("news_search_input"),
                placeholder = { Text(strings.searchPlaceholder, style = MaterialTheme.typography.bodyMedium) },
                leadingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = strings.searchIconDesc)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(Icons.Filled.Close, contentDescription = strings.clearSearchDesc)
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(RcrSpacing.ChipGap)
            ) {
                items(newsCategories) { category ->
                    val selected = category == selectedCategory
                    FilterChip(
                        selected = selected,
                        onClick = { onSelectCategory(category) },
                        label = {
                            Text(
                                text = strings.localizeNewsCategory(category),
                                modifier = Modifier.padding(vertical = 4.dp),
                                style = MaterialTheme.typography.labelLarge
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = RcrPrimaryGreen,
                            selectedLabelColor = RcrPureWhite
                        ),
                        modifier = Modifier.testTag("news_category_$category")
                    )
                }
            }
        }

        if (articles.isEmpty()) {
            item {
                EmptyStateCard(
                    title = strings.newsEmptyTitle,
                    subtitle = strings.newsEmptySub
                )
            }
        } else {
            if (featured != null && searchQuery.isBlank() && selectedCategory == "Toutes") {
                item {
                    Column {
                        Text(
                            text = strings.newsFeaturedHeader,
                            style = MaterialTheme.typography.headlineLarge,
                            color = colors.sectionHeading
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        FeaturedArticleCard(
                            article = featured,
                            onClick = { onOpenArticle(featured.id) }
                        )
                    }
                }
            }

            if (feedArticles.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = strings.newsFeedHeader,
                        style = MaterialTheme.typography.headlineLarge,
                        color = colors.sectionHeading
                    )
                }
                items(feedArticles, key = { it.id }) { article ->
                    NewsEditorialCard(
                        article = article,
                        onClick = { onOpenArticle(article.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FeaturedArticleCard(
    article: NewsArticleEntity,
    onClick: () -> Unit
) {
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current
    val isArabic = strings.isArabic
    val title = article.displayTitle(isArabic)
    val summary = article.displaySummary(isArabic)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(290.dp)
            .clickable(onClick = onClick)
            .testTag("news_featured_card"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
        border = BorderStroke(1.dp, colors.border),
        elevation = CardDefaults.cardElevation(defaultElevation = if (colors.isDark) 0.dp else 3.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
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
                            colors = listOf(Color.Transparent, RcrDeepGreen.copy(alpha = 0.65f), Color.Black.copy(alpha = 0.90f))
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(22.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Surface(shape = RoundedCornerShape(50), color = RcrPrimaryGreen) {
                        Text(
                            text = "${strings.newsFeaturedPrefix} • ${strings.localizeNewsCategory(article.category)}",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = RcrPureWhite
                        )
                    }
                    Surface(shape = RoundedCornerShape(50), color = Color.Black.copy(alpha = 0.65f)) {
                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                            Text(
                                text = article.publishedDateLabel,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = RcrSoftMint
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = RcrPureWhite,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = summary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = RcrSoftMint,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
