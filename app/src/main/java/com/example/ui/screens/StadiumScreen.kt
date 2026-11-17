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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EventSeat
import androidx.compose.material.icons.outlined.Grass
import androidx.compose.material.icons.outlined.LocationOn
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.local.ClubInfoEntity
import com.example.ui.localization.LocalRcrStrings
import com.example.ui.theme.LocalRcrColors
import com.example.ui.theme.RcrDeepGreen
import com.example.ui.theme.RcrPureWhite
import com.example.ui.theme.RcrSoftMint
import com.example.ui.theme.RcrSpacing

@Composable
fun StadiumScreen(clubInfo: ClubInfoEntity?) {
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current

    val stadiumName = clubInfo?.displayStadiumName(strings.isArabic)
        ?: if (strings.isArabic) "ملعب طاهر زوغاري" else "Stade Tahar Zoughari"
    val stadiumCity = clubInfo?.displayStadiumLocation(strings.isArabic)
        ?: if (strings.isArabic) "غليزان، الجزائر" else "Relizane, Algérie"
    val stadiumCapacity = clubInfo?.displayStadiumCapacity(strings.isArabic)
        ?: strings.contactToConfigure
    val stadiumSurface = clubInfo?.displayStadiumSurface(strings.isArabic)
        ?: strings.contactToConfigure
    val stadiumDescription = clubInfo?.displayStadiumDescription(strings.isArabic).orEmpty()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.primaryBackground)
            .testTag("stadium_screen"),
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
                    painter = painterResource(id = R.drawable.img_hero_stadium),
                    contentDescription = stadiumName,
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
                                    RcrDeepGreen.copy(alpha = 0.6f),
                                    RcrDeepGreen.copy(alpha = 0.96f)
                                )
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(22.dp)
                ) {
                    Surface(shape = RoundedCornerShape(50), color = colors.primaryGreen) {
                        Text(
                            text = strings.stadiumOfficialBadge,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = RcrPureWhite
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stadiumName,
                        style = MaterialTheme.typography.displaySmall,
                        color = RcrPureWhite
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stadiumCity,
                        style = MaterialTheme.typography.bodyMedium,
                        color = RcrSoftMint
                    )
                }
            }
        }

        item {
            Column(
                modifier = Modifier.padding(horizontal = RcrSpacing.ScreenHorizontal),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
                    border = BorderStroke(1.dp, colors.border)
                ) {
                    Column(
                        modifier = Modifier.padding(22.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        Text(
                            text = strings.stadiumSpecsHeader,
                            style = MaterialTheme.typography.headlineLarge,
                            color = colors.sectionHeading
                        )
                        StadiumDetailRow(
                            icon = Icons.Outlined.Stadium,
                            label = strings.stadiumNameLabel,
                            value = stadiumName
                        )
                        StadiumDetailRow(
                            icon = Icons.Outlined.LocationOn,
                            label = strings.stadiumCityLabel,
                            value = stadiumCity
                        )
                        StadiumDetailRow(
                            icon = Icons.Outlined.EventSeat,
                            label = strings.stadiumCapacityLabel,
                            value = stadiumCapacity
                        )
                        StadiumDetailRow(
                            icon = Icons.Outlined.Grass,
                            label = strings.stadiumSurfaceLabel,
                            value = stadiumSurface
                        )
                    }
                }

                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
                    border = BorderStroke(1.dp, colors.border)
                ) {
                    Column(modifier = Modifier.padding(22.dp)) {
                        Text(
                            text = strings.stadiumPresentationHeader,
                            style = MaterialTheme.typography.headlineLarge,
                            color = colors.sectionHeading
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = stadiumDescription.ifBlank { strings.stadiumDescFallback },
                            style = MaterialTheme.typography.bodyLarge,
                            color = colors.mainText
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StadiumDetailRow(icon: ImageVector, label: String, value: String) {
    val colors = LocalRcrColors.current
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = colors.chipContainer,
            modifier = Modifier.size(44.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = colors.accentHighlight
                )
            }
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = colors.secondaryText
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = colors.mainText
            )
        }
    }
}
