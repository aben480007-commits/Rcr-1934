package com.example.ui.screens

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.pm.PackageInfoCompat
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

fun resolveInstalledAppVersion(context: Context): String {
    return runCatching {
        val pm = context.packageManager
        val pkgInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pm.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION")
            pm.getPackageInfo(context.packageName, 0)
        }
        val versionName = pkgInfo.versionName.orEmpty().trim()
        val versionCode = PackageInfoCompat.getLongVersionCode(pkgInfo)
        when {
            versionName.isNotEmpty() -> versionName
            versionCode > 0L -> versionCode.toString()
            else -> ""
        }
    }.getOrDefault("")
}

@Composable
fun AboutScreen() {
    val context = LocalContext.current
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current

    val installedVersion = remember(context) {
        resolveInstalledAppVersion(context)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.primaryBackground)
            .testTag("about_screen_list"),
        contentPadding = PaddingValues(
            start = RcrSpacing.ScreenHorizontal,
            end = RcrSpacing.ScreenHorizontal,
            top = RcrSpacing.ScreenVertical,
            bottom = RcrSpacing.BottomScreenBuffer
        ),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item(key = "about_hero") {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                border = BorderStroke(1.dp, RcrGoldAccent.copy(alpha = 0.45f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("about_hero_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.verticalGradient(listOf(RcrDeepGreen, RcrDarkGreen)))
                        .padding(horizontal = 24.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = RcrPureWhite.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, RcrPureWhite.copy(alpha = 0.25f)),
                        modifier = Modifier.size(112.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            RcrClubCrest(size = 86.dp)
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = strings.aboutAppName,
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = RcrPureWhite,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.testTag("about_app_title")
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = strings.clubOfficialName,
                        style = MaterialTheme.typography.titleMedium,
                        color = RcrSoftMint,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = RcrPureWhite.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, RcrGoldAccent.copy(alpha = 0.45f))
                    ) {
                        Text(
                            text = strings.aboutHeroTagline,
                            style = MaterialTheme.typography.labelMedium,
                            color = RcrPureWhite,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                        )
                    }
                }
            }
        }

        item(key = "about_presentation") {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
                border = BorderStroke(1.dp, colors.border),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("about_presentation_card")
            ) {
                Column(modifier = Modifier.padding(22.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = colors.chipContainer
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = null,
                                tint = colors.accentHighlight,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = strings.aboutPresentationTitle,
                            style = MaterialTheme.typography.headlineMedium,
                            color = colors.sectionHeading
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = strings.aboutPresentationBody,
                        style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 25.sp),
                        color = colors.mainText
                    )
                }
            }
        }

        item(key = "about_developer") {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
                border = BorderStroke(1.dp, colors.primaryGreen.copy(alpha = 0.42f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("about_developer_card")
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Code,
                            contentDescription = null,
                            tint = colors.accentHighlight,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = strings.aboutDeveloperSectionTitle.uppercase(),
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = colors.accentHighlight
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.Transparent,
                            border = BorderStroke(1.5.dp, RcrGoldAccent.copy(alpha = 0.6f)),
                            modifier = Modifier
                                .size(60.dp)
                                .testTag("about_developer_avatar")
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Brush.linearGradient(listOf(RcrDeepGreen, RcrPrimaryGreen))),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "MA",
                                    fontFamily = BarlowCondensedFamily,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 24.sp,
                                    color = RcrPureWhite
                                )
                            }
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = strings.aboutDeveloperName,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = colors.mainText,
                                modifier = Modifier.testTag("about_developer_name")
                            )
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = colors.chipContainer
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Verified,
                                        contentDescription = null,
                                        tint = colors.accentHighlight,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Text(
                                        text = strings.aboutDeveloperRole,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = colors.accentHighlight,
                                        modifier = Modifier.testTag("about_developer_role")
                                    )
                                }
                            }
                        }
                    }

                    HorizontalDivider(color = colors.border.copy(alpha = 0.75f))

                    Text(
                        text = strings.aboutDeveloperBio,
                        style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 25.sp),
                        color = colors.secondaryText
                    )
                }
            }
        }

        item(key = "about_info") {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
                border = BorderStroke(1.dp, colors.border),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("about_info_card")
            ) {
                Column(modifier = Modifier.padding(22.dp)) {
                    Text(
                        text = strings.aboutInfoSectionTitle,
                        style = MaterialTheme.typography.headlineMedium,
                        color = colors.sectionHeading
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    AboutInfoRow(
                        label = strings.aboutInfoAppNameLabel,
                        value = strings.aboutAppName,
                        testTag = "about_info_app_name"
                    )
                    HorizontalDivider(
                        color = colors.border.copy(alpha = 0.65f),
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                    AboutInfoRow(
                        label = strings.aboutInfoClubLabel,
                        value = strings.aboutInfoClubValue,
                        testTag = "about_info_club"
                    )
                    HorizontalDivider(
                        color = colors.border.copy(alpha = 0.65f),
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                    AboutInfoRow(
                        label = strings.aboutInfoVersionLabel,
                        value = installedVersion.ifBlank { strings.aboutInfoVersionUnavailable },
                        useMonospace = installedVersion.isNotBlank(),
                        testTag = "about_info_version"
                    )
                    HorizontalDivider(
                        color = colors.border.copy(alpha = 0.65f),
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                    AboutInfoRow(
                        label = strings.aboutInfoCopyrightLabel,
                        value = strings.aboutInfoCopyrightValue,
                        testTag = "about_info_copyright"
                    )
                }
            }
        }
    }
}

@Composable
private fun AboutInfoRow(
    label: String,
    value: String,
    useMonospace: Boolean = false,
    testTag: String
) {
    val colors = LocalRcrColors.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = colors.secondaryText,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = value,
            style = if (useMonospace) {
                MaterialTheme.typography.labelLarge.copy(
                    fontFamily = JetBrainsMonoFamily,
                    fontWeight = FontWeight.SemiBold
                )
            } else {
                MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
            },
            color = colors.mainText
        )
    }
}
