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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.DoneAll
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.SportsSoccer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.local.AppNotificationEntity
import com.example.ui.components.EmptyStateCard
import com.example.ui.localization.LocalRcrStrings
import com.example.ui.theme.LocalRcrColors
import com.example.ui.theme.RcrPrimaryGreen
import com.example.ui.theme.RcrSpacing

@Composable
fun NotificationsScreen(
    notifications: List<AppNotificationEntity>,
    onNotificationClick: (AppNotificationEntity) -> Unit,
    onMarkAllRead: () -> Unit,
    onDeleteNotification: (Long) -> Unit
) {
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.primaryBackground)
            .testTag("notifications_screen"),
        contentPadding = PaddingValues(
            start = RcrSpacing.ScreenHorizontal,
            end = RcrSpacing.ScreenHorizontal,
            top = RcrSpacing.ScreenVertical,
            bottom = RcrSpacing.BottomScreenBuffer
        ),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        if (notifications.any { !it.isRead }) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onMarkAllRead,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("mark_all_read_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.DoneAll,
                            contentDescription = null,
                            tint = colors.accentHighlight,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = strings.notifMarkAllRead,
                            style = MaterialTheme.typography.labelLarge,
                            color = colors.accentHighlight
                        )
                    }
                }
            }
        }

        if (notifications.isEmpty()) {
            item {
                EmptyStateCard(
                    title = strings.notifEmptyTitle,
                    subtitle = strings.notifEmptySub
                )
            }
        } else {
            items(notifications, key = { it.id }) { item ->
                val icon = when (item.type) {
                    "NEWS" -> Icons.Outlined.Newspaper
                    "MATCH" -> Icons.Outlined.SportsSoccer
                    "RESULT" -> Icons.Outlined.EmojiEvents
                    else -> Icons.Outlined.Campaign
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNotificationClick(item) }
                        .testTag("notification_item_${item.id}"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (!item.isRead) {
                            colors.rcrRowHighlight
                        } else {
                            colors.cardBackground
                        }
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (!item.isRead) colors.accentHighlight.copy(alpha = 0.55f) else colors.border
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = colors.chipContainer,
                            modifier = Modifier.size(46.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = strings.localizeNotificationType(item.type),
                                    tint = colors.accentHighlight
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (!item.isRead) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(RcrPrimaryGreen, CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                                Text(
                                    text = item.displayTitle(strings.isArabic),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = colors.mainText
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = item.displayMessage(strings.isArabic),
                                style = MaterialTheme.typography.bodyMedium,
                                color = colors.secondaryText
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "${strings.localizeNotificationType(item.type)} • ${item.displayTimeLabel(strings.isArabic)}",
                                style = MaterialTheme.typography.labelSmall,
                                color = colors.accentHighlight
                            )
                        }
                        IconButton(
                            onClick = { onDeleteNotification(item.id) },
                            modifier = Modifier.minimumInteractiveComponentSize()
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.DeleteOutline,
                                contentDescription = strings.notifDeleteDesc,
                                tint = colors.secondaryText
                            )
                        }
                    }
                }
            }
        }
    }
}
