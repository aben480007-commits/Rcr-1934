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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.ContactMail
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Leaderboard
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Stadium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.components.RcrClubCrest
import com.example.ui.localization.LocalRcrStrings
import com.example.ui.navigation.Routes
import com.example.ui.theme.LocalRcrColors
import com.example.ui.theme.RcrDarkGreen
import com.example.ui.theme.RcrDeepGreen
import com.example.ui.theme.RcrGoldAccent
import com.example.ui.theme.RcrPureWhite
import com.example.ui.theme.RcrSoftMint
import com.example.ui.theme.RcrSpacing

private data class MoreMenuItem(
    val route: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val testTag: String,
    val isAccent: Boolean = false
)

@Composable
fun MoreScreen(
    isAdminAuthenticated: Boolean,
    onNavigate: (String) -> Unit
) {
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current

    val menuItems = listOf(
        MoreMenuItem(Routes.CLUB, strings.moreClubTitle, strings.moreClubSub, Icons.Outlined.Shield, "more_club"),
        MoreMenuItem(Routes.STANDINGS, strings.moreStandingsTitle, strings.moreStandingsSub, Icons.Outlined.Leaderboard, "more_standings"),
        MoreMenuItem(Routes.GALLERY, strings.moreGalleryTitle, strings.moreGallerySub, Icons.Outlined.PhotoLibrary, "more_gallery"),
        MoreMenuItem(Routes.STADIUM, strings.moreStadiumTitle, strings.moreStadiumSub, Icons.Outlined.Stadium, "more_stadium"),
        MoreMenuItem(Routes.CONTACT, strings.moreContactTitle, strings.moreContactSub, Icons.Outlined.ContactMail, "more_contact"),
        MoreMenuItem(Routes.SETTINGS, strings.moreSettingsTitle, strings.moreSettingsSub, Icons.Outlined.Settings, "more_settings"),
        MoreMenuItem(Routes.ABOUT, strings.moreAboutTitle, strings.moreAboutSub, Icons.Outlined.Info, "more_about"),
        MoreMenuItem(
            route = Routes.ADMIN,
            title = strings.moreAdminTitle,
            subtitle = if (isAdminAuthenticated) strings.moreAdminSubActive else strings.moreAdminSubLocked,
            icon = Icons.Outlined.AdminPanelSettings,
            testTag = "more_admin",
            isAccent = true
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.primaryBackground)
            .testTag("more_screen_list"),
        contentPadding = PaddingValues(
            start = RcrSpacing.ScreenHorizontal,
            end = RcrSpacing.ScreenHorizontal,
            top = RcrSpacing.ScreenVertical,
            bottom = RcrSpacing.BottomScreenBuffer
        ),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                border = BorderStroke(1.dp, colors.deepGreen.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.horizontalGradient(listOf(RcrDeepGreen, RcrDarkGreen)))
                        .padding(22.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RcrClubCrest(size = 64.dp)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = strings.clubOfficialName.uppercase(),
                            style = MaterialTheme.typography.headlineLarge,
                            color = RcrPureWhite
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${strings.clubLocationBadge} • ${strings.foundedPrefix} 1934",
                            style = MaterialTheme.typography.bodyMedium,
                            color = RcrSoftMint
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
        }

        items(menuItems, key = { it.route }) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(item.route) }
                    .testTag(item.testTag),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (item.isAccent) colors.rcrRowHighlight else colors.cardBackground
                ),
                border = BorderStroke(
                    1.dp,
                    if (item.isAccent) RcrGoldAccent.copy(alpha = 0.6f) else colors.border
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (item.isAccent) RcrGoldAccent.copy(alpha = 0.16f) else colors.chipContainer,
                        modifier = Modifier.size(50.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = if (item.isAccent) RcrGoldAccent else colors.accentHighlight,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = colors.mainText
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = item.subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.secondaryText
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = colors.secondaryText
                    )
                }
            }
        }
    }
}
