package com.example.ui.screens

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.data.local.ClubInfoEntity
import com.example.ui.localization.LocalRcrStrings
import com.example.ui.theme.LocalRcrColors
import com.example.ui.theme.RcrPrimaryGreen
import com.example.ui.theme.RcrPureWhite
import com.example.ui.theme.RcrSpacing

@Composable
fun ContactScreen(
    clubInfo: ClubInfoEntity?,
    onSubmitMessage: (String, String, String, String) -> Unit
) {
    val colors = LocalRcrColors.current
    val strings = LocalRcrStrings.current

    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var subject by rememberSaveable { mutableStateOf("") }
    var message by rememberSaveable { mutableStateOf("") }
    var validationError by rememberSaveable { mutableStateOf<String?>(null) }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = colors.primaryGreen,
        unfocusedBorderColor = colors.border,
        focusedLabelColor = colors.accentHighlight,
        unfocusedLabelColor = colors.secondaryText,
        focusedTextColor = colors.mainText,
        unfocusedTextColor = colors.mainText
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.primaryBackground)
            .testTag("contact_screen"),
        contentPadding = PaddingValues(
            start = RcrSpacing.ScreenHorizontal,
            end = RcrSpacing.ScreenHorizontal,
            top = RcrSpacing.ScreenVertical,
            bottom = RcrSpacing.BottomScreenBuffer
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
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
                        text = strings.contactDetailsHeader,
                        style = MaterialTheme.typography.headlineLarge,
                        color = colors.sectionHeading
                    )
                    ContactInfoRow(
                        icon = Icons.Outlined.LocationOn,
                        label = strings.contactAddressLabel,
                        value = clubInfo?.displayContactAddress(strings.isArabic)
                            ?: if (strings.isArabic) "غليزان، الجزائر" else "Relizane, Algérie"
                    )
                    ContactInfoRow(
                        icon = Icons.Outlined.Email,
                        label = strings.contactEmailLabel,
                        value = clubInfo?.contactEmail?.takeIf { it.isNotBlank() } ?: strings.contactToConfigure,
                        forceLtrValue = clubInfo?.contactEmail?.isNotBlank() == true
                    )
                    ContactInfoRow(
                        icon = Icons.Outlined.Phone,
                        label = strings.contactPhoneLabel,
                        value = clubInfo?.contactPhone?.takeIf { it.isNotBlank() } ?: strings.contactToConfigure,
                        forceLtrValue = clubInfo?.contactPhone?.isNotBlank() == true
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
                Column(
                    modifier = Modifier.padding(22.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = strings.contactSendHeader,
                            style = MaterialTheme.typography.headlineLarge,
                            color = colors.sectionHeading
                        )
                        Text(
                            text = strings.contactSendSub,
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.secondaryText
                        )
                    }

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it; validationError = null },
                        label = { Text(strings.contactNameField) },
                        singleLine = true,
                        colors = fieldColors,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("contact_name_input"),
                        shape = RoundedCornerShape(14.dp)
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it; validationError = null },
                        label = { Text(strings.contactEmailField) },
                        singleLine = true,
                        colors = fieldColors,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("contact_email_input"),
                        shape = RoundedCornerShape(14.dp)
                    )
                    OutlinedTextField(
                        value = subject,
                        onValueChange = { subject = it; validationError = null },
                        label = { Text(strings.contactSubjectField) },
                        singleLine = true,
                        colors = fieldColors,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("contact_subject_input"),
                        shape = RoundedCornerShape(14.dp)
                    )
                    OutlinedTextField(
                        value = message,
                        onValueChange = { message = it; validationError = null },
                        label = { Text(strings.contactMessageField) },
                        minLines = 4,
                        colors = fieldColors,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("contact_message_input"),
                        shape = RoundedCornerShape(14.dp)
                    )

                    validationError?.let { err ->
                        Text(
                            text = err,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    Button(
                        onClick = {
                            when {
                                name.isBlank() || subject.isBlank() || message.isBlank() -> {
                                    validationError = strings.contactErrorRequired
                                }
                                !email.contains("@") -> {
                                    validationError = strings.contactErrorEmail
                                }
                                else -> {
                                    onSubmitMessage(name, email, subject, message)
                                    name = ""
                                    email = ""
                                    subject = ""
                                    message = ""
                                    validationError = null
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RcrPrimaryGreen, contentColor = RcrPureWhite),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 52.dp)
                            .testTag("contact_submit_button")
                    ) {
                        Text(strings.contactSubmitBtn, style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}

@Composable
private fun ContactInfoRow(
    icon: ImageVector,
    label: String,
    value: String,
    forceLtrValue: Boolean = false
) {
    val colors = LocalRcrColors.current
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = colors.chipContainer,
            modifier = Modifier.size(44.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(imageVector = icon, contentDescription = null, tint = colors.accentHighlight)
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
            if (forceLtrValue) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyLarge,
                        color = colors.mainText
                    )
                }
            } else {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    color = colors.mainText
                )
            }
        }
    }
}
