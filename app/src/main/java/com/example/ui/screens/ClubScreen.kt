package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.HistoryEdu
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Stadium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.local.ClubInfoEntity
import com.example.ui.components.RcrClubCrest
import com.example.ui.localization.LocalRcrStrings
import com.example.ui.theme.LocalRcrColors
import com.example.ui.theme.RcrDeepGreen
import com.example.ui.theme.RcrPrimaryGreen
import com.example.ui.theme.RcrPureWhite
import com.example.ui.theme.RcrSoftMint
import com.example.ui.theme.RcrSpacing

@Composable
fun ClubScreen(
    clubInfo: ClubInfoEntity?,
    onOpenStadium: () -> Unit
) {
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current

    val officialName = clubInfo?.displayOfficialName(strings.isArabic) ?: strings.clubOfficialName
    val city = clubInfo?.displayCity(strings.isArabic) ?: if (strings.isArabic) "غليزان، الجزائر" else "Relizane, Algérie"
    val founded = clubInfo?.foundedYear ?: "1934"
    val colorsLabel = clubInfo?.displayColorsLabel(strings.isArabic) ?: if (strings.isArabic) "الأخضر والأبيض" else "Vert & Blanc"
    val stadiumName = clubInfo?.displayStadiumName(strings.isArabic)
        ?: if (strings.isArabic) "ملعب طاهر زوغاري" else "Stade Tahar Zoughari"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.primaryBackground)
            .testTag("club_screen"),
        contentPadding = PaddingValues(bottom = RcrSpacing.BottomScreenBuffer),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(270.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_club_heritage),
                    contentDescription = officialName,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    RcrDeepGreen.copy(alpha = 0.65f),
                                    RcrDeepGreen.copy(alpha = 0.96f)
                                )
                            )
                        )
                )
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(22.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RcrClubCrest(size = 70.dp)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = officialName,
                            style = MaterialTheme.typography.displaySmall,
                            color = RcrPureWhite
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$city • ${strings.foundedPrefix} $founded • $colorsLabel",
                            style = MaterialTheme.typography.bodyMedium,
                            color = RcrSoftMint
                        )
                    }
                }
            }
        }

        item {
            Column(
                modifier = Modifier.padding(horizontal = RcrSpacing.ScreenHorizontal),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                ClubEditorialCard(
                    icon = Icons.Outlined.HistoryEdu,
                    title = strings.clubHistoryTitle,
                    body = clubInfo?.displayHistory(strings.isArabic).orEmpty()
                )
                ClubEditorialCard(
                    icon = Icons.Outlined.Palette,
                    title = strings.clubIdentityTitle,
                    body = clubInfo?.displayIdentity(strings.isArabic).orEmpty()
                )
                ClubEditorialCard(
                    icon = Icons.Outlined.FavoriteBorder,
                    title = strings.clubValuesTitle,
                    body = clubInfo?.displayValues(strings.isArabic).orEmpty()
                )
                ClubEditorialCard(
                    icon = Icons.Outlined.Groups,
                    title = strings.clubSupportersTitle,
                    body = clubInfo?.displaySupporters(strings.isArabic).orEmpty()
                )
                ClubEditorialCard(
                    icon = Icons.Outlined.EmojiEvents,
                    title = strings.clubAchievementsTitle,
                    body = clubInfo?.displayAchievements(strings.isArabic).orEmpty()
                )
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
                    border = BorderStroke(1.dp, colors.border)
                ) {
                    Column(modifier = Modifier.padding(22.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.Stadium,
                                contentDescription = null,
                                tint = colors.accentHighlight,
                                modifier = Modifier.size(26.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = stadiumName,
                                style = MaterialTheme.typography.headlineLarge,
                                color = colors.sectionHeading
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = clubInfo?.displayStadiumDescription(strings.isArabic).orEmpty()
                                .ifBlank { strings.stadiumDescFallback },
                            style = MaterialTheme.typography.bodyLarge,
                            color = colors.secondaryText
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onOpenStadium,
                            colors = ButtonDefaults.buttonColors(containerColor = RcrPrimaryGreen, contentColor = RcrPureWhite),
                            shape = RoundedCornerShape(14.dp),
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                            modifier = Modifier.heightIn(min = 48.dp)
                        ) {
                            Text(strings.clubViewStadiumBtn, style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ClubEditorialCard(
    icon: ImageVector,
    title: String,
    body: String
) {
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
        border = BorderStroke(1.dp, colors.border)
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = colors.chipContainer
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = colors.accentHighlight,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineLarge,
                    color = colors.sectionHeading
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = body.ifBlank { strings.clubInfoFallback },
                style = MaterialTheme.typography.bodyLarge,
                color = colors.mainText
            )
        }
    }
}
