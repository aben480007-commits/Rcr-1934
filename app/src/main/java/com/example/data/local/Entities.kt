package com.example.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.text.Normalizer
import java.util.Locale

@Entity(tableName = "news_articles")
data class NewsArticleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val summary: String,
    val content: String,
    val category: String,
    val imageUri: String,
    val publishedDateLabel: String,
    val createdAtMillis: Long = System.currentTimeMillis(),
    val isFeatured: Boolean = false,
    val isPublished: Boolean = true,
    val isPlaceholder: Boolean = false,
    val titleAr: String = "",
    val summaryAr: String = "",
    val contentAr: String = ""
) {
    fun displayTitle(isArabic: Boolean): String = if (isArabic && titleAr.isNotBlank()) titleAr else title
    fun displaySummary(isArabic: Boolean): String = if (isArabic && summaryAr.isNotBlank()) summaryAr else summary
    fun displayContent(isArabic: Boolean): String = if (isArabic && contentAr.isNotBlank()) contentAr else content
    fun isMissingArabicVersion(isArabic: Boolean): Boolean =
        isArabic && titleAr.isBlank() && summaryAr.isBlank() && contentAr.isBlank()
}

@Entity(tableName = "matches")
data class MatchEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val opponentName: String,
    val opponentShort: String,
    val competition: String,
    val matchDateLabel: String,
    val kickoffTimeLabel: String,
    val kickoffTimestampMillis: Long,
    val stadium: String,
    val isHome: Boolean = true,
    val rcrScore: Int? = null,
    val opponentScore: Int? = null,
    val status: String = "À venir", // "À venir", "En direct", "Terminé"
    val isFeaturedNext: Boolean = false,
    val matchNotes: String = "",
    val isPlaceholder: Boolean = false,
    val opponentNameAr: String = "",
    val competitionAr: String = "",
    val stadiumAr: String = "",
    val matchNotesAr: String = "",
    val competingClubId: Long? = null
) {
    fun displayOpponent(isArabic: Boolean): String =
        if (isArabic && opponentNameAr.isNotBlank()) opponentNameAr else opponentName

    fun displayCompetition(isArabic: Boolean): String =
        if (isArabic && competitionAr.isNotBlank()) competitionAr else competition

    fun displayStadium(isArabic: Boolean): String =
        if (isArabic && stadiumAr.isNotBlank()) stadiumAr else stadium

    fun displayNotes(isArabic: Boolean): String =
        if (isArabic && matchNotesAr.isNotBlank()) matchNotesAr else matchNotes
}

@Entity(tableName = "standings")
data class StandingRowEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val position: Int,
    val clubName: String,
    val clubShort: String,
    val played: Int,
    val wins: Int,
    val draws: Int,
    val losses: Int,
    val goalsFor: Int,
    val goalsAgainst: Int,
    val points: Int,
    val isRcr: Boolean = false,
    val isPlaceholder: Boolean = false,
    val clubNameAr: String = "",
    val competingClubId: Long? = null
) {
    val goalDifference: Int get() = goalsFor - goalsAgainst

    fun displayClubName(isArabic: Boolean): String = when {
        isArabic && clubNameAr.isNotBlank() -> clubNameAr
        isArabic && isRcr -> "سريع غليزان"
        else -> clubName
    }
}

@Entity(tableName = "players")
data class PlayerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fullName: String,
    val shirtNumber: Int,
    val position: String,
    val positionGroup: String, // "Gardiens", "Défenseurs", "Milieux", "Attaquants"
    val nationality: String = "",
    val biography: String,
    val photoUri: String = "",
    val appearances: Int? = null,
    val goals: Int? = null,
    val assists: Int? = null,
    val isHighlighted: Boolean = false,
    val isPlaceholder: Boolean = false,
    val fullNameAr: String = "",
    val positionAr: String = "",
    val nationalityAr: String = "",
    val biographyAr: String = ""
) {
    fun displayFullName(isArabic: Boolean): String =
        if (isArabic && fullNameAr.isNotBlank()) fullNameAr else fullName

    fun displayPosition(isArabic: Boolean): String =
        if (isArabic && positionAr.isNotBlank()) positionAr else position

    fun displayNationality(isArabic: Boolean): String =
        if (isArabic && nationalityAr.isNotBlank()) nationalityAr else nationality

    fun displayBiography(isArabic: Boolean): String =
        if (isArabic && biographyAr.isNotBlank()) biographyAr else biography
}

@Entity(tableName = "gallery_items")
data class GalleryItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String, // "Matchs", "Entraînement", "Joueurs", "Supporters", "Événements", "Vidéos"
    val mediaUri: String,
    val isVideo: Boolean = false,
    val videoDurationLabel: String = "",
    val dateLabel: String,
    val createdAtMillis: Long = System.currentTimeMillis(),
    val isPlaceholder: Boolean = false,
    val titleAr: String = ""
) {
    fun displayTitle(isArabic: Boolean): String =
        if (isArabic && titleAr.isNotBlank()) titleAr else title
}

@Entity(tableName = "club_info")
data class ClubInfoEntity(
    @PrimaryKey val id: Int = 1,
    val officialName: String,
    val shortName: String,
    val city: String,
    val foundedYear: String,
    val colorsLabel: String,
    val historyText: String,
    val identityText: String,
    val valuesText: String,
    val supportersText: String,
    val achievementsText: String,
    val stadiumName: String,
    val stadiumLocation: String,
    val stadiumCapacityLabel: String,
    val stadiumSurfaceLabel: String,
    val stadiumDescription: String,
    val contactEmail: String,
    val contactPhone: String,
    val contactAddress: String,
    val officialNameAr: String = "",
    val cityAr: String = "",
    val colorsLabelAr: String = "",
    val historyTextAr: String = "",
    val identityTextAr: String = "",
    val valuesTextAr: String = "",
    val supportersTextAr: String = "",
    val achievementsTextAr: String = "",
    val stadiumNameAr: String = "",
    val stadiumLocationAr: String = "",
    val stadiumCapacityLabelAr: String = "",
    val stadiumSurfaceLabelAr: String = "",
    val stadiumDescriptionAr: String = "",
    val contactAddressAr: String = ""
) {
    fun displayOfficialName(isArabic: Boolean) = if (isArabic && officialNameAr.isNotBlank()) officialNameAr else officialName
    fun displayCity(isArabic: Boolean) = if (isArabic && cityAr.isNotBlank()) cityAr else city
    fun displayColorsLabel(isArabic: Boolean) = if (isArabic && colorsLabelAr.isNotBlank()) colorsLabelAr else colorsLabel
    fun displayHistory(isArabic: Boolean) = if (isArabic && historyTextAr.isNotBlank()) historyTextAr else historyText
    fun displayIdentity(isArabic: Boolean) = if (isArabic && identityTextAr.isNotBlank()) identityTextAr else identityText
    fun displayValues(isArabic: Boolean) = if (isArabic && valuesTextAr.isNotBlank()) valuesTextAr else valuesText
    fun displaySupporters(isArabic: Boolean) = if (isArabic && supportersTextAr.isNotBlank()) supportersTextAr else supportersText
    fun displayAchievements(isArabic: Boolean) = if (isArabic && achievementsTextAr.isNotBlank()) achievementsTextAr else achievementsText
    fun displayStadiumName(isArabic: Boolean) = if (isArabic && stadiumNameAr.isNotBlank()) stadiumNameAr else stadiumName
    fun displayStadiumLocation(isArabic: Boolean) = if (isArabic && stadiumLocationAr.isNotBlank()) stadiumLocationAr else stadiumLocation
    fun displayStadiumCapacity(isArabic: Boolean) = if (isArabic && stadiumCapacityLabelAr.isNotBlank()) stadiumCapacityLabelAr else stadiumCapacityLabel
    fun displayStadiumSurface(isArabic: Boolean) = if (isArabic && stadiumSurfaceLabelAr.isNotBlank()) stadiumSurfaceLabelAr else stadiumSurfaceLabel
    fun displayStadiumDescription(isArabic: Boolean) = if (isArabic && stadiumDescriptionAr.isNotBlank()) stadiumDescriptionAr else stadiumDescription
    fun displayContactAddress(isArabic: Boolean) = if (isArabic && contactAddressAr.isNotBlank()) contactAddressAr else contactAddress
}

@Entity(tableName = "notifications")
data class AppNotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val message: String,
    val type: String, // "NEWS", "MATCH", "RESULT", "CLUB"
    val timestampMillis: Long = System.currentTimeMillis(),
    val timeLabel: String,
    val isRead: Boolean = false,
    val targetRoute: String = "",
    val titleAr: String = "",
    val messageAr: String = "",
    val timeLabelAr: String = ""
) {
    fun displayTitle(isArabic: Boolean) = if (isArabic && titleAr.isNotBlank()) titleAr else title
    fun displayMessage(isArabic: Boolean) = if (isArabic && messageAr.isNotBlank()) messageAr else message
    fun displayTimeLabel(isArabic: Boolean) = if (isArabic && timeLabelAr.isNotBlank()) timeLabelAr else timeLabel
}

@Entity(tableName = "contact_messages")
data class ContactMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val senderName: String,
    val senderEmail: String,
    val subject: String,
    val message: String,
    val createdAtMillis: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "competing_clubs",
    indices = [Index(value = ["clubKey"], unique = true)]
)
data class CompetingClubEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val clubKey: String,
    val clubName: String,
    val clubNameAr: String = "",
    val clubShort: String,
    val logoPath: String = "",
    val updatedAtMillis: Long = System.currentTimeMillis()
) {
    fun displayName(isArabic: Boolean): String =
        if (isArabic && clubNameAr.isNotBlank()) clubNameAr else clubName
}

object CompetingClubIdentity {
    private val diacriticsRegex = Regex("\\p{InCombiningDiacriticalMarks}+")
    private val multiSpaceRegex = Regex("\\s+")

    fun canonicalNameKey(name: String): String {
        val decomposed = Normalizer.normalize(name.trim(), Normalizer.Form.NFD)
        val stripped = diacriticsRegex.replace(decomposed, "")
        return multiSpaceRegex.replace(stripped.lowercase(Locale.ROOT), " ").trim()
    }

    fun canonicalClubKey(clubName: String, clubShort: String): String {
        val shortKey = clubShort.trim().uppercase(Locale.ROOT)
        val nameKey = canonicalNameKey(clubName)
        return "$shortKey|$nameKey"
    }

    /**
     * Resolves a competing club deterministically while avoiding incorrect logo
     * assignments when two distinct clubs happen to share the same 3-letter code.
     */
    fun resolveClub(
        clubs: List<CompetingClubEntity>,
        competingClubId: Long?,
        clubName: String,
        shortCode: String
    ): CompetingClubEntity? {
        val normalizedShort = shortCode.trim().uppercase(Locale.ROOT)
        val normalizedName = canonicalNameKey(clubName)

        if (competingClubId != null) {
            val byId = clubs.firstOrNull { it.id == competingClubId }
            if (byId != null && (normalizedShort.isBlank() || byId.clubShort.equals(normalizedShort, ignoreCase = true))) {
                return byId
            }
        }

        if (normalizedShort.isNotBlank() && normalizedName.isNotBlank()) {
            val targetKey = "$normalizedShort|$normalizedName"
            val byExactKey = clubs.firstOrNull { it.clubKey == targetKey }
            if (byExactKey != null) return byExactKey
        }

        if (normalizedShort.isBlank()) return null

        val sameShortClubs = clubs.filter { it.clubShort.equals(normalizedShort, ignoreCase = true) }
        if (sameShortClubs.size != 1) {
            return null
        }

        val soleCandidate = sameShortClubs.first()
        val candidateName = canonicalNameKey(soleCandidate.clubName)
        return if (normalizedName.isBlank() || candidateName == normalizedName) {
            soleCandidate
        } else {
            // Two different club names share the same abbreviation -> refuse ambiguous match
            null
        }
    }
}
