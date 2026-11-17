package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.data.local.NewsArticleEntity
import com.example.ui.components.ClubMediaImage
import com.example.ui.components.EmptyStateCard
import com.example.ui.components.NewsEditorialCard
import com.example.ui.components.PlaceholderInfoBanner
import com.example.ui.localization.LocalRcrStrings
import com.example.ui.theme.LocalRcrColors
import com.example.ui.theme.RcrDeepGreen
import com.example.ui.theme.RcrPureWhite
import com.example.ui.theme.RcrSpacing

@Composable
fun NewsDetailScreen(
    article: NewsArticleEntity?,
    relatedArticles: List<NewsArticleEntity>,
    onOpenArticle: (Long) -> Unit
) {
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current
    val isArabic = strings.isArabic

    if (article == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.primaryBackground)
                .padding(RcrSpacing.ScreenHorizontal)
        ) {
            EmptyStateCard(
                title = strings.articleNotFoundTitle,
                subtitle = strings.articleNotFoundSub
            )
        }
        return
    }

    val title = article.displayTitle(isArabic)
    val summary = article.displaySummary(isArabic)
    val content = article.displayContent(isArabic)
    val showArabicFallbackNotice = article.isMissingArabicVersion(isArabic) && strings.articleFallbackNotice.isNotBlank()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.primaryBackground)
            .testTag("news_detail_screen"),
        contentPadding = PaddingValues(bottom = RcrSpacing.BottomScreenBuffer)
    ) {
        item {
            ClubMediaImage(
                mediaUri = article.imageUri,
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            )
        }

        item {
            Column(
                modifier = Modifier.padding(horizontal = 22.dp, vertical = 22.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Surface(shape = RoundedCornerShape(50), color = RcrDeepGreen) {
                        Text(
                            text = strings.localizeNewsCategory(article.category),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = RcrPureWhite
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = colors.secondaryBackground
                    ) {
                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                            Text(
                                text = article.publishedDateLabel,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = colors.secondaryText
                            )
                        }
                    }
                }

                Text(
                    text = title,
                    style = MaterialTheme.typography.displaySmall,
                    color = colors.sectionHeading
                )

                Text(
                    text = summary,
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.accentHighlight
                )

                if (showArabicFallbackNotice) {
                    PlaceholderInfoBanner(text = strings.articleFallbackNotice)
                }

                if (article.isPlaceholder) {
                    PlaceholderInfoBanner(text = strings.articlePlaceholderBanner)
                }

                HorizontalDivider(thickness = 1.dp, color = colors.border)

                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyLarge,
                    color = colors.mainText
                )
            }
        }

        if (relatedArticles.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Column(modifier = Modifier.padding(horizontal = 22.dp)) {
                    Text(
                        text = strings.articleRelatedHeader,
                        style = MaterialTheme.typography.headlineLarge,
                        color = colors.sectionHeading
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }
            items(relatedArticles, key = { it.id }) { related ->
                Box(modifier = Modifier.padding(horizontal = 22.dp, vertical = 8.dp)) {
                    NewsEditorialCard(
                        article = related,
                        onClick = { onOpenArticle(related.id) }
                    )
                }
            }
        }
    }
}
