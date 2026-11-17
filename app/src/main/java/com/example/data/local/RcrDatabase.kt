package com.example.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        NewsArticleEntity::class,
        MatchEntity::class,
        StandingRowEntity::class,
        PlayerEntity::class,
        GalleryItemEntity::class,
        ClubInfoEntity::class,
        AppNotificationEntity::class,
        ContactMessageEntity::class,
        CompetingClubEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class RcrDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
    abstract fun matchDao(): MatchDao
    abstract fun standingDao(): StandingDao
    abstract fun playerDao(): PlayerDao
    abstract fun galleryDao(): GalleryDao
    abstract fun clubInfoDao(): ClubInfoDao
    abstract fun notificationDao(): NotificationDao
    abstract fun contactMessageDao(): ContactMessageDao
    abstract fun competingClubDao(): CompetingClubDao

    companion object {
        @Volatile
        private var INSTANCE: RcrDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE news_articles ADD COLUMN titleAr TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE news_articles ADD COLUMN summaryAr TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE news_articles ADD COLUMN contentAr TEXT NOT NULL DEFAULT ''")

                db.execSQL("ALTER TABLE matches ADD COLUMN opponentNameAr TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE matches ADD COLUMN competitionAr TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE matches ADD COLUMN stadiumAr TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE matches ADD COLUMN matchNotesAr TEXT NOT NULL DEFAULT ''")

                db.execSQL("ALTER TABLE standings ADD COLUMN clubNameAr TEXT NOT NULL DEFAULT ''")

                db.execSQL("ALTER TABLE players ADD COLUMN fullNameAr TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE players ADD COLUMN positionAr TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE players ADD COLUMN nationalityAr TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE players ADD COLUMN biographyAr TEXT NOT NULL DEFAULT ''")

                db.execSQL("ALTER TABLE gallery_items ADD COLUMN titleAr TEXT NOT NULL DEFAULT ''")

                db.execSQL("ALTER TABLE club_info ADD COLUMN officialNameAr TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE club_info ADD COLUMN cityAr TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE club_info ADD COLUMN colorsLabelAr TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE club_info ADD COLUMN historyTextAr TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE club_info ADD COLUMN identityTextAr TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE club_info ADD COLUMN valuesTextAr TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE club_info ADD COLUMN supportersTextAr TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE club_info ADD COLUMN achievementsTextAr TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE club_info ADD COLUMN stadiumNameAr TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE club_info ADD COLUMN stadiumLocationAr TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE club_info ADD COLUMN stadiumCapacityLabelAr TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE club_info ADD COLUMN stadiumSurfaceLabelAr TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE club_info ADD COLUMN stadiumDescriptionAr TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE club_info ADD COLUMN contactAddressAr TEXT NOT NULL DEFAULT ''")

                db.execSQL("ALTER TABLE notifications ADD COLUMN titleAr TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE notifications ADD COLUMN messageAr TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE notifications ADD COLUMN timeLabelAr TEXT NOT NULL DEFAULT ''")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS competing_clubs (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        clubKey TEXT NOT NULL,
                        clubName TEXT NOT NULL,
                        clubNameAr TEXT NOT NULL DEFAULT '',
                        clubShort TEXT NOT NULL,
                        logoPath TEXT NOT NULL DEFAULT '',
                        updatedAtMillis INTEGER NOT NULL DEFAULT 0
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    "CREATE UNIQUE INDEX IF NOT EXISTS index_competing_clubs_clubKey ON competing_clubs (clubKey)"
                )
                db.execSQL("ALTER TABLE matches ADD COLUMN competingClubId INTEGER DEFAULT NULL")
                db.execSQL("ALTER TABLE standings ADD COLUMN competingClubId INTEGER DEFAULT NULL")

                val now = System.currentTimeMillis()
                val clubKeyToId = mutableMapOf<String, Long>()

                fun ensureClubRow(name: String, nameAr: String, shortCode: String): Long? {
                    val cleanName = name.trim()
                    val cleanShort = shortCode.trim().uppercase()
                    if (cleanName.isBlank() || cleanShort.isBlank()) return null
                    val key = CompetingClubIdentity.canonicalClubKey(cleanName, cleanShort)
                    val existing = clubKeyToId[key]
                    if (existing != null) {
                        if (nameAr.isNotBlank()) {
                            val cv = ContentValues().apply {
                                put("clubNameAr", nameAr.trim())
                            }
                            db.update(
                                "competing_clubs",
                                SQLiteDatabase.CONFLICT_IGNORE,
                                cv,
                                "id = ? AND clubNameAr = ''",
                                arrayOf(existing.toString())
                            )
                        }
                        return existing
                    }

                    val cv = ContentValues().apply {
                        put("clubKey", key)
                        put("clubName", cleanName)
                        put("clubNameAr", nameAr.trim())
                        put("clubShort", cleanShort)
                        put("logoPath", "")
                        put("updatedAtMillis", now)
                    }
                    val insertedId = db.insert("competing_clubs", SQLiteDatabase.CONFLICT_IGNORE, cv)
                    val finalId = if (insertedId > 0L) {
                        insertedId
                    } else {
                        db.query("SELECT id FROM competing_clubs WHERE clubKey = ?", arrayOf(key)).use { c ->
                            if (c.moveToFirst()) c.getLong(0) else -1L
                        }
                    }
                    if (finalId > 0L) {
                        clubKeyToId[key] = finalId
                        return finalId
                    }
                    return null
                }

                // Backfill competing_clubs and competingClubId from existing standings rows (non-RCR)
                val standingUpdates = mutableListOf<Pair<Long, Long>>()
                db.query("SELECT id, clubName, clubNameAr, clubShort, isRcr FROM standings").use { cursor ->
                    while (cursor.moveToNext()) {
                        val rowId = cursor.getLong(0)
                        val clubName = cursor.getString(1)
                        val clubNameAr = cursor.getString(2)
                        val clubShort = cursor.getString(3)
                        val isRcr = cursor.getInt(4) == 1
                        if (!isRcr) {
                            val clubId = ensureClubRow(clubName, clubNameAr, clubShort)
                            if (clubId != null) {
                                standingUpdates.add(rowId to clubId)
                            }
                        }
                    }
                }
                standingUpdates.forEach { (rowId, clubId) ->
                    val cv = ContentValues().apply { put("competingClubId", clubId) }
                    db.update("standings", SQLiteDatabase.CONFLICT_IGNORE, cv, "id = ?", arrayOf(rowId.toString()))
                }

                // Backfill competing_clubs and competingClubId from existing matches
                val matchUpdates = mutableListOf<Pair<Long, Long>>()
                db.query("SELECT id, opponentName, opponentNameAr, opponentShort FROM matches").use { cursor ->
                    while (cursor.moveToNext()) {
                        val matchId = cursor.getLong(0)
                        val opponentName = cursor.getString(1)
                        val opponentNameAr = cursor.getString(2)
                        val opponentShort = cursor.getString(3)
                        val clubId = ensureClubRow(opponentName, opponentNameAr, opponentShort)
                        if (clubId != null) {
                            matchUpdates.add(matchId to clubId)
                        }
                    }
                }
                matchUpdates.forEach { (matchId, clubId) ->
                    val cv = ContentValues().apply { put("competingClubId", clubId) }
                    db.update("matches", SQLiteDatabase.CONFLICT_IGNORE, cv, "id = ?", arrayOf(matchId.toString()))
                }
            }
        }

        fun getInstance(context: Context): RcrDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    RcrDatabase::class.java,
                    "rcr_club_database.db"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
