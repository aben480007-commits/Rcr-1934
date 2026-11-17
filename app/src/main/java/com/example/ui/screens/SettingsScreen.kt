package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.local.UserPreferences
import com.example.ui.localization.LocalRcrStrings
import com.example.ui.theme.LocalRcrColors
import com.example.ui.theme.RcrDarkBg
import com.example.ui.theme.RcrDeepGreen
import com.example.ui.theme.RcrPrimaryGreen
import com.example.ui.theme.RcrPureWhite
import com.example.ui.theme.RcrSpacing
import com.example.ui.theme.ThemeMode

@Composable
fun SettingsScreen(
    preferences: UserPreferences,
    onSelectTheme: (ThemeMode) -> Unit,
    onSelectLanguageRtl: (String, Boolean) -> Unit,
    onToggleInAppAlerts: (Boolean) -> Unit,
    onOpenAdmin: () -> Unit
) {
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.primaryBackground)
            .testTag("settings_screen"),
        contentPadding = PaddingValues(
            start = RcrSpacing.ScreenHorizontal,
            end = RcrSpacing.ScreenHorizontal,
            top = RcrSpacing.ScreenVertical,
            bottom = RcrSpacing.BottomScreenBuffer
        ),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
                border = BorderStroke(1.dp, colors.border)
            ) {
                Column(modifier = Modifier.padding(22.dp)) {
                    SettingsSectionHeader(
                        icon = Icons.Outlined.DarkMode,
                        title = strings.settingsAppearanceHeader
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    // Option 1: Mode Clair (Vert / Blanc) — Default Official Primary Theme
                    ThemeOptionRow(
                        label = strings.settingsThemeLight,
                        subtitle = strings.settingsThemeLightSub,
                        selected = preferences.themeMode == ThemeMode.LIGHT,
                        swatchPrimary = RcrPureWhite,
                        swatchAccent = RcrDeepGreen,
                        testTag = "theme_option_light",
                        onClick = { onSelectTheme(ThemeMode.LIGHT) }
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    // Option 2: Mode Sombre (Vert / Noir)
                    ThemeOptionRow(
                        label = strings.settingsThemeDark,
                        subtitle = strings.settingsThemeDarkSub,
                        selected = preferences.themeMode == ThemeMode.DARK,
                        swatchPrimary = RcrDarkBg,
                        swatchAccent = RcrPrimaryGreen,
                        testTag = "theme_option_dark",
                        onClick = { onSelectTheme(ThemeMode.DARK) }
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    // Option 3: Suivre le système
                    ThemeOptionRow(
                        label = strings.settingsThemeSystem,
                        subtitle = strings.settingsThemeSystemSub,
                        selected = preferences.themeMode == ThemeMode.SYSTEM,
                        swatchPrimary = null,
                        swatchAccent = null,
                        testTag = "theme_option_system",
                        onClick = { onSelectTheme(ThemeMode.SYSTEM) }
                    )
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
                border = BorderStroke(1.dp, colors.border)
            ) {
                Column(modifier = Modifier.padding(22.dp)) {
                    SettingsSectionHeader(
                        icon = Icons.Outlined.Language,
                        title = strings.settingsLanguageHeader
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = strings.settingsLanguageDesc,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.secondaryText
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    ThemeOptionRow(
                        label = strings.settingsLangFr,
                        subtitle = null,
                        selected = preferences.languageCode != "ar",
                        swatchPrimary = null,
                        swatchAccent = null,
                        testTag = "lang_option_fr",
                        onClick = { onSelectLanguageRtl("fr", false) }
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    ThemeOptionRow(
                        label = strings.settingsLangAr,
                        subtitle = null,
                        selected = preferences.languageCode == "ar",
                        swatchPrimary = null,
                        swatchAccent = null,
                        testTag = "lang_option_ar",
                        onClick = { onSelectLanguageRtl("ar", true) }
                    )
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
                border = BorderStroke(1.dp, colors.border)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = colors.chipContainer,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Outlined.NotificationsActive,
                                contentDescription = null,
                                tint = colors.accentHighlight
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = strings.settingsAlertsTitle,
                            style = MaterialTheme.typography.titleMedium,
                            color = colors.mainText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = strings.settingsAlertsDesc,
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.secondaryText
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Switch(
                        checked = preferences.inAppAlertsEnabled,
                        onCheckedChange = onToggleInAppAlerts,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = RcrPureWhite,
                            checkedTrackColor = RcrPrimaryGreen
                        ),
                        modifier = Modifier.testTag("settings_alerts_switch")
                    )
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
                border = BorderStroke(1.dp, colors.border)
            ) {
                Column(modifier = Modifier.padding(22.dp)) {
                    SettingsSectionHeader(
                        icon = Icons.Outlined.AdminPanelSettings,
                        title = strings.settingsAdminHeader
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = strings.settingsAdminDesc,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.secondaryText
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onOpenAdmin,
                        colors = ButtonDefaults.buttonColors(containerColor = RcrPrimaryGreen, contentColor = RcrPureWhite),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 52.dp)
                            .testTag("settings_open_admin_button")
                    ) {
                        Text(strings.settingsOpenAdminBtn, style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsSectionHeader(icon: ImageVector, title: String) {
    val colors = LocalRcrColors.current
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = colors.chipContainer,
            modifier = Modifier.size(40.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(imageVector = icon, contentDescription = null, tint = colors.accentHighlight)
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            color = colors.sectionHeading
        )
    }
}

@Composable
private fun ThemeOptionRow(
    label: String,
    subtitle: String?,
    selected: Boolean,
    swatchPrimary: Color?,
    swatchAccent: Color?,
    testTag: String,
    onClick: () -> Unit
) {
    val colors = LocalRcrColors.current
    val rowBg = if (selected) colors.rcrRowHighlight else colors.secondaryBackground
    val rowBorder = if (selected) colors.primaryGreen else colors.border

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 58.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(rowBg)
            .border(1.dp, rowBorder, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = colors.accentHighlight,
                unselectedColor = colors.secondaryText
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = colors.mainText
            )
            if (!subtitle.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.secondaryText
                )
            }
        }
        if (swatchPrimary != null && swatchAccent != null) {
            Spacer(modifier = Modifier.width(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(swatchPrimary)
                        .border(1.dp, colors.border, CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(swatchAccent)
                        .border(1.dp, colors.border, CircleShape)
                )
            }
        }
    }
}
