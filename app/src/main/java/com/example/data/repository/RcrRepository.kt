package com.example.data.repository

import com.example.data.local.AppNotificationEntity
import com.example.data.local.ClubInfoEntity
import com.example.data.local.CompetingClubEntity
import com.example.data.local.CompetingClubIdentity
import com.example.data.local.ContactMessageEntity
import com.example.data.local.GalleryItemEntity
import com.example.data.local.MatchEntity
import com.example.data.local.NewsArticleEntity
import com.example.data.local.PlayerEntity
import com.example.data.local.RcrDatabase
import com.example.data.local.RcrSeedData
import com.example.data.local.StandingRowEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class RcrRepository(private val database: RcrDatabase) {

    private val newsDao = database.newsDao()
    private val matchDao = database.matchDao()
    private val standingDao = database.standingDao()
    private val playerDao = database.playerDao()
    private val galleryDao = database.galleryDao()
    private val clubInfoDao = database.clubInfoDao()
    private val notificationDao = database.notificationDao()
    private val contactMessageDao = database.contactMessageDao()
    private val competingClubDao = database.competingClubDao()

    val publishedNews: Flow<List<NewsArticleEntity>> = newsDao.observePublishedNews()
    val allNewsForAdmin: Flow<List<NewsArticleEntity>> = newsDao.observeAllNews()
    val matches: Flow<List<MatchEntity>> = matchDao.observeAllMatches()
    val standings: Flow<List<StandingRowEntity>> = standingDao.observeStandings()
    val players: Flow<List<PlayerEntity>> = playerDao.observeAllPlayers()
    val galleryItems: Flow<List<GalleryItemEntity>> = galleryDao.observeGallery()
    val clubInfo: Flow<ClubInfoEntity?> = clubInfoDao.observeClubInfo()
    val notifications: Flow<List<AppNotificationEntity>> = notificationDao.observeNotifications()
    val unreadNotificationsCount: Flow<Int> = notificationDao.observeUnreadCount()
    val contactMessages: Flow<List<ContactMessageEntity>> = contactMessageDao.observeMessages()
    val competingClubs: Flow<List<CompetingClubEntity>> = competingClubDao.observeCompetingClubs()

    fun observeNewsById(id: Long): Flow<NewsArticleEntity?> = newsDao.observeNewsById(id)
    fun observeMatchById(id: Long): Flow<MatchEntity?> = matchDao.observeMatchById(id)
    fun observePlayerById(id: Long): Flow<PlayerEntity?> = playerDao.observePlayerById(id)

    suspend fun ensureSeeded() = withContext(Dispatchers.IO) {
        if (clubInfoDao.count() == 0) {
            clubInfoDao.upsert(RcrSeedData.defaultClubInfo())
        }
        if (newsDao.count() == 0) {
            newsDao.insertAll(RcrSeedData.defaultNews())
        }
        if (matchDao.count() == 0) {
            matchDao.insertAll(RcrSeedData.defaultMatches())
        }
        if (standingDao.count() == 0) {
            standingDao.insertAll(RcrSeedData.defaultStandings())
        }
        if (playerDao.count() == 0) {
            playerDao.insertAll(RcrSeedData.defaultPlayers())
        }
        if (galleryDao.count() == 0) {
            galleryDao.insertAll(RcrSeedData.defaultGallery())
        }
        if (notificationDao.count() == 0) {
            notificationDao.insertAll(RcrSeedData.defaultNotifications())
        }
        syncCompetingClubs()
    }

    suspend fun syncCompetingClubs() = withContext(Dispatchers.IO) {
        val currentStandings = standingDao.getAllOnce()
        for (row in currentStandings) {
            if (!row.isRcr) {
                val clubId = ensureCompetingClub(
                    clubName = row.clubName,
                    clubNameAr = row.clubNameAr,
                    clubShort = row.clubShort
                )
                if (clubId != null && row.competingClubId != clubId) {
                    standingDao.updateCompetingClubId(row.id, clubId)
                }
            }
        }
        val currentMatches = matchDao.getAllOnce()
        for (match in currentMatches) {
            val clubId = ensureCompetingClub(
                clubName = match.opponentName,
                clubNameAr = match.opponentNameAr,
                clubShort = match.opponentShort
            )
            if (clubId != null && match.competingClubId != clubId) {
                matchDao.updateCompetingClubId(match.id, clubId)
            }
        }
    }

    private suspend fun ensureCompetingClub(
        clubName: String,
        clubNameAr: String,
        clubShort: String
    ): Long? {
        val cleanName = clubName.trim()
        val cleanShort = clubShort.trim().uppercase()
        if (cleanName.isBlank() || cleanShort.isBlank()) return null

        val key = CompetingClubIdentity.canonicalClubKey(cleanName, cleanShort)
        val existing = competingClubDao.getByClubKey(key)
        if (existing != null) {
            val cleanAr = clubNameAr.trim()
            if (cleanAr.isNotBlank() && existing.clubNameAr.isBlank()) {
                competingClubDao.update(existing.copy(clubNameAr = cleanAr))
            }
            return existing.id
        }

        val inserted = competingClubDao.insertIgnore(
            CompetingClubEntity(
                clubKey = key,
                clubName = cleanName,
                clubNameAr = clubNameAr.trim(),
                clubShort = cleanShort
            )
        )
        return if (inserted > 0L) {
            inserted
        } else {
            competingClubDao.getByClubKey(key)?.id
        }
    }

    // News CRUD
    suspend fun saveArticle(article: NewsArticleEntity, notifyIfPublished: Boolean = true) {
        val isNew = article.id == 0L
        val savedId = if (isNew) {
            newsDao.insert(article)
        } else {
            newsDao.update(article)
            article.id
        }
        if (notifyIfPublished && article.isPublished && isNew) {
            notificationDao.insert(
                AppNotificationEntity(
                    title = "Nouvelle actualité • ${article.category}",
                    message = article.title,
                    type = "NEWS",
                    timeLabel = "À l'instant",
                    targetRoute = "news/$savedId",
                    titleAr = "خبر جديد • ${article.category}",
                    messageAr = article.titleAr.ifBlank { article.title },
                    timeLabelAr = "الآن"
                )
            )
        }
    }

    suspend fun deleteArticle(article: NewsArticleEntity) = newsDao.delete(article)

    // Match CRUD
    suspend fun saveMatch(match: MatchEntity) {
        if (match.isFeaturedNext) {
            matchDao.clearFeaturedFlags()
        }
        val linkedClubId = ensureCompetingClub(
            clubName = match.opponentName,
            clubNameAr = match.opponentNameAr,
            clubShort = match.opponentShort
        )
        val normalizedMatch = match.copy(competingClubId = linkedClubId)
        val isNew = normalizedMatch.id == 0L
        val savedId = if (isNew) {
            matchDao.insert(normalizedMatch)
        } else {
            matchDao.update(normalizedMatch)
            normalizedMatch.id
        }
        val notifTitle = if (match.status == "Terminé" && match.rcrScore != null && match.opponentScore != null) {
            "Résultat RCR ${match.rcrScore} - ${match.opponentScore} ${match.opponentShort}"
        } else {
            "Calendrier mis à jour : RCR vs ${match.opponentName}"
        }
        val notifTitleAr = if (match.status == "Terminé" && match.rcrScore != null && match.opponentScore != null) {
            "النتيجة: سريع غليزان ${match.rcrScore} - ${match.opponentScore} ${match.opponentShort}"
        } else {
            "تحديث الجدول: سريع غليزان ضد ${match.opponentNameAr.ifBlank { match.opponentName }}"
        }
        notificationDao.insert(
            AppNotificationEntity(
                title = notifTitle,
                message = "${match.competition} • ${match.matchDateLabel} (${match.kickoffTimeLabel})",
                type = if (match.status == "Terminé") "RESULT" else "MATCH",
                timeLabel = "À l'instant",
                targetRoute = "match/$savedId",
                titleAr = notifTitleAr,
                messageAr = "${match.competitionAr.ifBlank { match.competition }} • ${match.matchDateLabel} (${match.kickoffTimeLabel})",
                timeLabelAr = "الآن"
            )
        )
    }

    suspend fun deleteMatch(match: MatchEntity) = matchDao.delete(match)

    // Standings CRUD
    suspend fun saveStandingRow(row: StandingRowEntity) {
        val linkedClubId = if (row.isRcr) {
            null
        } else {
            ensureCompetingClub(
                clubName = row.clubName,
                clubNameAr = row.clubNameAr,
                clubShort = row.clubShort
            )
        }
        val normalizedRow = row.copy(competingClubId = linkedClubId)
        if (normalizedRow.id == 0L) standingDao.insert(normalizedRow) else standingDao.update(normalizedRow)
    }

    suspend fun deleteStandingRow(row: StandingRowEntity) = standingDao.delete(row)

    // Competing Club Logos
    suspend fun updateCompetingClubLogo(clubId: Long, newLogoPath: String): String? =
        withContext(Dispatchers.IO) {
            val existing = competingClubDao.getById(clubId) ?: return@withContext null
            val previousPath = existing.logoPath
            competingClubDao.updateLogoPath(
                clubId = clubId,
                logoPath = newLogoPath,
                updatedAtMillis = System.currentTimeMillis()
            )
            previousPath
        }

    suspend fun activeCompetingClubLogoPaths(): Set<String> =
        withContext(Dispatchers.IO) {
            competingClubDao.getAllActiveLogoPaths().toSet()
        }

    // Player CRUD
    suspend fun savePlayer(player: PlayerEntity) {
        if (player.id == 0L) playerDao.insert(player) else playerDao.update(player)
    }

    suspend fun deletePlayer(player: PlayerEntity) = playerDao.delete(player)

    // Gallery CRUD
    suspend fun addGalleryItem(item: GalleryItemEntity) {
        galleryDao.insert(item)
    }

    suspend fun deleteGalleryItem(item: GalleryItemEntity) = galleryDao.delete(item)

    // Club Info CRUD
    suspend fun updateClubInfo(info: ClubInfoEntity) {
        clubInfoDao.upsert(info)
        notificationDao.insert(
            AppNotificationEntity(
                title = "Informations du club mises à jour",
                message = "Les détails officiels du Rapid Club de Relizane ont été actualisés.",
                type = "CLUB",
                timeLabel = "À l'instant",
                targetRoute = "club",
                titleAr = "تحديث معلومات النادي",
                messageAr = "تم تحديث البيانات الرسمية لنادي سريع غليزان.",
                timeLabelAr = "الآن"
            )
        )
    }

    // Notifications
    suspend fun markNotificationAsRead(id: Long) = notificationDao.markAsRead(id)
    suspend fun markAllNotificationsAsRead() = notificationDao.markAllAsRead()
    suspend fun deleteNotification(id: Long) = notificationDao.deleteById(id)

    // Contact
    suspend fun sendContactMessage(name: String, email: String, subject: String, message: String) {
        contactMessageDao.insert(
            ContactMessageEntity(
                senderName = name.trim(),
                senderEmail = email.trim(),
                subject = subject.trim(),
                message = message.trim()
            )
        )
    }
}
