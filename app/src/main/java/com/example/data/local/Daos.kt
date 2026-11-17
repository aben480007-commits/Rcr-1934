package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Query("SELECT * FROM news_articles WHERE isPublished = 1 ORDER BY createdAtMillis DESC")
    fun observePublishedNews(): Flow<List<NewsArticleEntity>>

    @Query("SELECT * FROM news_articles ORDER BY createdAtMillis DESC")
    fun observeAllNews(): Flow<List<NewsArticleEntity>>

    @Query("SELECT * FROM news_articles WHERE id = :id")
    fun observeNewsById(id: Long): Flow<NewsArticleEntity?>

    @Query("SELECT COUNT(*) FROM news_articles")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: NewsArticleEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<NewsArticleEntity>)

    @Update
    suspend fun update(article: NewsArticleEntity)

    @Delete
    suspend fun delete(article: NewsArticleEntity)
}

@Dao
interface MatchDao {
    @Query("SELECT * FROM matches ORDER BY kickoffTimestampMillis ASC")
    fun observeAllMatches(): Flow<List<MatchEntity>>

    @Query("SELECT * FROM matches WHERE id = :id")
    fun observeMatchById(id: Long): Flow<MatchEntity?>

    @Query("SELECT COUNT(*) FROM matches")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(match: MatchEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(matches: List<MatchEntity>)

    @Update
    suspend fun update(match: MatchEntity)

    @Delete
    suspend fun delete(match: MatchEntity)

    @Query("UPDATE matches SET isFeaturedNext = 0")
    suspend fun clearFeaturedFlags()

    @Query("SELECT * FROM matches")
    suspend fun getAllOnce(): List<MatchEntity>

    @Query("UPDATE matches SET competingClubId = :clubId WHERE id = :matchId")
    suspend fun updateCompetingClubId(matchId: Long, clubId: Long)
}

@Dao
interface StandingDao {
    @Query("SELECT * FROM standings ORDER BY position ASC, points DESC")
    fun observeStandings(): Flow<List<StandingRowEntity>>

    @Query("SELECT COUNT(*) FROM standings")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(row: StandingRowEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rows: List<StandingRowEntity>)

    @Update
    suspend fun update(row: StandingRowEntity)

    @Delete
    suspend fun delete(row: StandingRowEntity)

    @Query("SELECT * FROM standings")
    suspend fun getAllOnce(): List<StandingRowEntity>

    @Query("UPDATE standings SET competingClubId = :clubId WHERE id = :rowId")
    suspend fun updateCompetingClubId(rowId: Long, clubId: Long?)
}

@Dao
interface PlayerDao {
    @Query("SELECT * FROM players ORDER BY shirtNumber ASC")
    fun observeAllPlayers(): Flow<List<PlayerEntity>>

    @Query("SELECT * FROM players WHERE id = :id")
    fun observePlayerById(id: Long): Flow<PlayerEntity?>

    @Query("SELECT COUNT(*) FROM players")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(player: PlayerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(players: List<PlayerEntity>)

    @Update
    suspend fun update(player: PlayerEntity)

    @Delete
    suspend fun delete(player: PlayerEntity)
}

@Dao
interface GalleryDao {
    @Query("SELECT * FROM gallery_items ORDER BY createdAtMillis DESC")
    fun observeGallery(): Flow<List<GalleryItemEntity>>

    @Query("SELECT COUNT(*) FROM gallery_items")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: GalleryItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<GalleryItemEntity>)

    @Delete
    suspend fun delete(item: GalleryItemEntity)
}

@Dao
interface ClubInfoDao {
    @Query("SELECT * FROM club_info WHERE id = 1")
    fun observeClubInfo(): Flow<ClubInfoEntity?>

    @Query("SELECT COUNT(*) FROM club_info")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(info: ClubInfoEntity)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY timestampMillis DESC")
    fun observeNotifications(): Flow<List<AppNotificationEntity>>

    @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
    fun observeUnreadCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM notifications")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: AppNotificationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notifications: List<AppNotificationEntity>)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Long)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllAsRead()

    @Query("DELETE FROM notifications WHERE id = :id")
    suspend fun deleteById(id: Long)
}

@Dao
interface ContactMessageDao {
    @Query("SELECT * FROM contact_messages ORDER BY createdAtMillis DESC")
    fun observeMessages(): Flow<List<ContactMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: ContactMessageEntity): Long
}

@Dao
interface CompetingClubDao {
    @Query("SELECT * FROM competing_clubs ORDER BY clubName COLLATE NOCASE ASC")
    fun observeCompetingClubs(): Flow<List<CompetingClubEntity>>

    @Query("SELECT * FROM competing_clubs")
    suspend fun getAllOnce(): List<CompetingClubEntity>

    @Query("SELECT * FROM competing_clubs WHERE id = :id")
    suspend fun getById(id: Long): CompetingClubEntity?

    @Query("SELECT * FROM competing_clubs WHERE clubKey = :clubKey LIMIT 1")
    suspend fun getByClubKey(clubKey: String): CompetingClubEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnore(club: CompetingClubEntity): Long

    @Update
    suspend fun update(club: CompetingClubEntity)

    @Query("UPDATE competing_clubs SET logoPath = :logoPath, updatedAtMillis = :updatedAtMillis WHERE id = :clubId")
    suspend fun updateLogoPath(clubId: Long, logoPath: String, updatedAtMillis: Long)

    @Query("SELECT logoPath FROM competing_clubs WHERE logoPath != ''")
    suspend fun getAllActiveLogoPaths(): List<String>
}
