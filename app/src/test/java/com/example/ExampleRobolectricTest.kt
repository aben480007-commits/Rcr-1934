package com.example

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color as AndroidColor
import android.net.Uri
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.unit.LayoutDirection
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.CompetingClubEntity
import com.example.data.local.CompetingClubIdentity
import com.example.data.local.MatchEntity
import com.example.data.local.MediaStorageHelper
import com.example.data.local.NewsArticleEntity
import com.example.data.local.PreferencesManager
import com.example.data.local.RcrDatabase
import com.example.data.local.StandingRowEntity
import com.example.data.repository.RcrRepository
import com.example.ui.components.LocalCompetingClubs
import com.example.ui.components.LocalRcrClubLogoPath
import com.example.ui.components.MatchCard
import com.example.ui.components.OpponentClubBadge
import com.example.ui.components.RcrClubCrest
import com.example.ui.localization.LocalRcrStrings
import com.example.ui.localization.stringsForLanguage
import com.example.ui.navigation.Routes
import com.example.ui.navigation.resolveAdjacentMainRoute
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.StandingsTableCard
import com.example.ui.screens.resolveInstalledAppVersion
import com.example.ui.theme.RcrTheme
import com.example.ui.theme.ThemeMode
import com.example.ui.viewmodel.RcrViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var context: Context
    private lateinit var database: RcrDatabase
    private lateinit var repository: RcrRepository
    private lateinit var prefs: PreferencesManager

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context, RcrDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = RcrRepository(database)
        prefs = PreferencesManager(context)
        runBlocking {
            prefs.setThemeMode(ThemeMode.LIGHT)
            prefs.setLanguageAndRtl("fr", false)
            prefs.setAdminAuthenticated(false)
            prefs.setClubLogoPath("")
        }
    }

    @After
    fun teardown() {
        runBlocking {
            prefs.setClubLogoPath("")
            prefs.setAdminAuthenticated(false)
            prefs.setThemeMode(ThemeMode.LIGHT)
            prefs.setLanguageAndRtl("fr", false)
        }
        database.close()
    }

    private fun createTestPngFile(
        name: String,
        width: Int = 24,
        height: Int = 24,
        color: Int = AndroidColor.GREEN
    ): File {
        val file = File(context.cacheDir, name)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.eraseColor(color)
        file.outputStream().use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        bitmap.recycle()
        return file
    }

    private fun createInvalidImageFile(name: String): File {
        return File(context.cacheDir, name).apply {
            writeText("NOT_A_VALID_IMAGE_PAYLOAD")
        }
    }

    @Test
    fun readAppNameFromContext() {
        val appName = context.getString(R.string.app_name)
        assertEquals("RC Relizane", appName)
    }

    @Test
    fun repositorySeedsInitialClubDataAndPersistsNewArticle() = runBlocking {
        repository.ensureSeeded()
        val seededNews = repository.publishedNews.first()
        val seededMatches = repository.matches.first()
        val seededPlayers = repository.players.first()
        val seededStandings = repository.standings.first()

        assertTrue(seededNews.isNotEmpty())
        assertTrue(seededMatches.isNotEmpty())
        assertTrue(seededPlayers.isNotEmpty())
        assertTrue(seededStandings.any { it.isRcr })

        repository.saveArticle(
            NewsArticleEntity(
                title = "Communiqué officiel de test",
                summary = "Résumé test",
                content = "Contenu test complet",
                category = "Club",
                imageUri = "drawable://img_club_heritage",
                publishedDateLabel = "16 Nov 2026",
                isPublished = true
            )
        )

        val updatedNews = repository.publishedNews.first()
        assertEquals(seededNews.size + 1, updatedNews.size)
        assertEquals("Communiqué officiel de test", updatedNews.first().title)
    }

    @Test
    fun defaultPreferencesUseOfficialLightThemeAndFrenchLtrAndPersistChanges() = runBlocking {
        val initial = prefs.preferencesFlow.first()
        assertEquals(ThemeMode.LIGHT, initial.themeMode)
        assertEquals("fr", initial.languageCode)
        assertFalse(initial.isRtlLayout)

        prefs.setThemeMode(ThemeMode.DARK)
        prefs.setLanguageAndRtl("ar", true)

        val reloadedManager = PreferencesManager(context)
        val reloaded = reloadedManager.preferencesFlow.first()
        assertEquals(ThemeMode.DARK, reloaded.themeMode)
        assertEquals("ar", reloaded.languageCode)
        assertTrue(reloaded.isRtlLayout)

        prefs.setThemeMode(ThemeMode.SYSTEM)
        assertEquals(ThemeMode.SYSTEM, reloadedManager.preferencesFlow.first().themeMode)
    }

    @Test
    fun bilingualLocalizationAndArticleFallbackWorkWithoutInventedTranslations() {
        val fr = stringsForLanguage("fr")
        val ar = stringsForLanguage("ar")

        assertEquals("Mode Clair (Vert / Blanc)", fr.settingsThemeLight)
        assertEquals("Mode Sombre (Vert / Noir)", fr.settingsThemeDark)
        assertEquals("Suivre le système", fr.settingsThemeSystem)
        assertTrue(ar.isArabic)
        assertFalse(fr.isArabic)

        val frenchOnlyArticle = NewsArticleEntity(
            title = "Préparation d'avant-saison",
            summary = "Résumé original en français",
            content = "Texte complet en français.",
            category = "Équipe A",
            imageUri = "drawable://img_training_action",
            publishedDateLabel = "16 Nov 2026"
        )
        assertTrue(frenchOnlyArticle.isMissingArabicVersion(isArabic = true))
        assertFalse(frenchOnlyArticle.isMissingArabicVersion(isArabic = false))
        assertEquals("Préparation d'avant-saison", frenchOnlyArticle.displayTitle(isArabic = true))
        assertEquals("Texte complet en français.", frenchOnlyArticle.displayContent(isArabic = true))

        val bilingualArticle = frenchOnlyArticle.copy(
            titleAr = "التحضير للموسم الجديد",
            summaryAr = "ملخص رسمي بالعربية",
            contentAr = "النص العربي الكامل."
        )
        assertFalse(bilingualArticle.isMissingArabicVersion(isArabic = true))
        assertEquals("التحضير للموسم الجديد", bilingualArticle.displayTitle(isArabic = true))

        val rcrRow = StandingRowEntity(
            position = 1,
            clubName = "RC Relizane",
            clubShort = "RCR",
            played = 10,
            wins = 7,
            draws = 2,
            losses = 1,
            goalsFor = 18,
            goalsAgainst = 7,
            points = 23,
            isRcr = true
        )
        assertEquals("سريع غليزان", rcrRow.displayClubName(isArabic = true))
        assertEquals("RC Relizane", rcrRow.displayClubName(isArabic = false))

        val match = MatchEntity(
            opponentName = "Adversaire Test",
            opponentNameAr = "المنافس التجريبي",
            opponentShort = "TST",
            competition = "Championnat",
            matchDateLabel = "25 Nov 2026",
            kickoffTimeLabel = "16:00",
            kickoffTimestampMillis = 0L,
            stadium = "Stade Tahar Zoughari",
            stadiumAr = "ملعب طاهر زوغاري"
        )
        assertEquals("المنافس التجريبي", match.displayOpponent(true))
        assertEquals("Adversaire Test", match.displayOpponent(false))
        assertEquals("ملعب طاهر زوغاري", match.displayStadium(true))
    }

    @Test
    fun migration1To2PreservesExistingRowsNonDestructively() {
        val dbName = "migration_test_rcr.db"
        context.deleteDatabase(dbName)

        val callback = object : SupportSQLiteOpenHelper.Callback(1) {
            override fun onCreate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS news_articles (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        title TEXT NOT NULL,
                        summary TEXT NOT NULL,
                        content TEXT NOT NULL,
                        category TEXT NOT NULL,
                        imageUri TEXT NOT NULL,
                        publishedDateLabel TEXT NOT NULL,
                        createdAtMillis INTEGER NOT NULL,
                        isFeatured INTEGER NOT NULL,
                        isPublished INTEGER NOT NULL,
                        isPlaceholder INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS matches (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        opponentName TEXT NOT NULL,
                        opponentShort TEXT NOT NULL,
                        competition TEXT NOT NULL,
                        matchDateLabel TEXT NOT NULL,
                        kickoffTimeLabel TEXT NOT NULL,
                        kickoffTimestampMillis INTEGER NOT NULL,
                        stadium TEXT NOT NULL,
                        isHome INTEGER NOT NULL,
                        rcrScore INTEGER,
                        opponentScore INTEGER,
                        status TEXT NOT NULL,
                        isFeaturedNext INTEGER NOT NULL,
                        matchNotes TEXT NOT NULL,
                        isPlaceholder INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS standings (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        position INTEGER NOT NULL,
                        clubName TEXT NOT NULL,
                        clubShort TEXT NOT NULL,
                        played INTEGER NOT NULL,
                        wins INTEGER NOT NULL,
                        draws INTEGER NOT NULL,
                        losses INTEGER NOT NULL,
                        goalsFor INTEGER NOT NULL,
                        goalsAgainst INTEGER NOT NULL,
                        points INTEGER NOT NULL,
                        isRcr INTEGER NOT NULL,
                        isPlaceholder INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS players (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        fullName TEXT NOT NULL,
                        shirtNumber INTEGER NOT NULL,
                        position TEXT NOT NULL,
                        positionGroup TEXT NOT NULL,
                        nationality TEXT NOT NULL,
                        biography TEXT NOT NULL,
                        photoUri TEXT NOT NULL,
                        appearances INTEGER,
                        goals INTEGER,
                        assists INTEGER,
                        isHighlighted INTEGER NOT NULL,
                        isPlaceholder INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS gallery_items (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        title TEXT NOT NULL,
                        category TEXT NOT NULL,
                        mediaUri TEXT NOT NULL,
                        isVideo INTEGER NOT NULL,
                        videoDurationLabel TEXT NOT NULL,
                        dateLabel TEXT NOT NULL,
                        createdAtMillis INTEGER NOT NULL,
                        isPlaceholder INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS club_info (
                        id INTEGER NOT NULL PRIMARY KEY,
                        officialName TEXT NOT NULL,
                        shortName TEXT NOT NULL,
                        city TEXT NOT NULL,
                        foundedYear TEXT NOT NULL,
                        colorsLabel TEXT NOT NULL,
                        historyText TEXT NOT NULL,
                        identityText TEXT NOT NULL,
                        valuesText TEXT NOT NULL,
                        supportersText TEXT NOT NULL,
                        achievementsText TEXT NOT NULL,
                        stadiumName TEXT NOT NULL,
                        stadiumLocation TEXT NOT NULL,
                        stadiumCapacityLabel TEXT NOT NULL,
                        stadiumSurfaceLabel TEXT NOT NULL,
                        stadiumDescription TEXT NOT NULL,
                        contactEmail TEXT NOT NULL,
                        contactPhone TEXT NOT NULL,
                        contactAddress TEXT NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS notifications (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        title TEXT NOT NULL,
                        message TEXT NOT NULL,
                        type TEXT NOT NULL,
                        timestampMillis INTEGER NOT NULL,
                        timeLabel TEXT NOT NULL,
                        isRead INTEGER NOT NULL,
                        targetRoute TEXT NOT NULL
                    )
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    INSERT INTO news_articles (title, summary, content, category, imageUri, publishedDateLabel, createdAtMillis, isFeatured, isPublished, isPlaceholder)
                    VALUES ('Ancien article v1', 'Résumé v1', 'Contenu v1', 'Club', 'drawable://img_club_heritage', '10 Nov 2026', 1000, 1, 1, 0)
                    """.trimIndent()
                )
            }

            override fun onUpgrade(db: SupportSQLiteDatabase, oldVersion: Int, newVersion: Int) = Unit
        }

        val helper = FrameworkSQLiteOpenHelperFactory().create(
            SupportSQLiteOpenHelper.Configuration.builder(context)
                .name(dbName)
                .callback(callback)
                .build()
        )
        val v1Db = helper.writableDatabase
        RcrDatabase.MIGRATION_1_2.migrate(v1Db)
        v1Db.close()

        val sqlite = SQLiteDatabase.openDatabase(
            context.getDatabasePath(dbName).path,
            null,
            SQLiteDatabase.OPEN_READONLY
        )
        val cursor = sqlite.rawQuery("SELECT title, titleAr FROM news_articles", null)
        assertTrue(cursor.moveToFirst())
        assertEquals("Ancien article v1", cursor.getString(0))
        assertEquals("", cursor.getString(1))
        cursor.close()
        sqlite.close()
        context.deleteDatabase(dbName)
    }

    @Test
    fun composeUiSwitchesThemesAndLanguagesAndNavigatesScreens() {
        composeRule.setContent {
            RcrAppRoot()
        }

        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithText("ENSEMBLE,\nPLUS FORTS.").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("home_screen_list").assertIsDisplayed()

        // Navigate across bottom tabs
        composeRule.onNodeWithTag("nav_news").performClick()
        composeRule.onNodeWithTag("news_screen_list").assertIsDisplayed()

        composeRule.onNodeWithTag("nav_matches").performClick()
        composeRule.onNodeWithTag("matches_screen").assertIsDisplayed()

        composeRule.onNodeWithTag("nav_team").performClick()
        composeRule.onNodeWithTag("team_screen_list").assertIsDisplayed()

        composeRule.onNodeWithTag("nav_more").performClick()
        composeRule.onNodeWithTag("more_screen_list").assertIsDisplayed()

        // Open Settings and verify exact theme labels in exact order + bilingual switch
        composeRule.onNodeWithTag("more_screen_list")
            .performScrollToNode(hasTestTag("more_settings"))
        composeRule.onNodeWithTag("more_settings").performClick()
        composeRule.onNodeWithTag("settings_screen").assertIsDisplayed()
        composeRule.onNodeWithText("Mode Clair (Vert / Blanc)").assertIsDisplayed()
        composeRule.onNodeWithText("Mode Sombre (Vert / Noir)").assertIsDisplayed()
        composeRule.onNodeWithTag("settings_screen")
            .performScrollToNode(hasTestTag("theme_option_system"))
        composeRule.onNodeWithText("Suivre le système").assertIsDisplayed()

        // Switch theme to Dark
        composeRule.onNodeWithTag("settings_screen")
            .performScrollToNode(hasTestTag("theme_option_dark"))
        composeRule.onNodeWithTag("theme_option_dark").performClick()
        composeRule.waitForIdle()

        // Switch language to Arabic (RTL)
        composeRule.onNodeWithTag("settings_screen")
            .performScrollToNode(hasTestTag("lang_option_ar"))
        composeRule.onNodeWithTag("lang_option_ar").performClick()
        composeRule.waitUntil(timeoutMillis = 3_000) {
            composeRule.onAllNodesWithText("اللغة ودعم الاتجاه RTL").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("اللغة ودعم الاتجاه RTL").assertIsDisplayed()

        // Unlock Admin Dashboard in Arabic
        composeRule.onNodeWithTag("settings_screen")
            .performScrollToNode(hasTestTag("settings_open_admin_button"))
        composeRule.onNodeWithTag("settings_open_admin_button").performClick()
        composeRule.onNodeWithTag("admin_login_gate").assertIsDisplayed()
        composeRule.onNodeWithTag("admin_pin_input").performScrollTo().performTextInput("1934")
        composeRule.onNodeWithTag("admin_login_submit").performScrollTo().performClick()
        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithText("لوحة تحكم الإدارة").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("admin_dashboard_screen").assertIsDisplayed()
    }

    @Test
    fun officialCrestAssetsAndSplashScreenMeetBackgroundSpecs() {
        // 1. Original crest preserves opaque off-white background
        val originalBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.rcr_crest_original)
        assertTrue(originalBitmap.width > 0 && originalBitmap.height > 0)
        val originalCornerAlpha = AndroidColor.alpha(originalBitmap.getPixel(0, 0))
        assertEquals(255, originalCornerAlpha)

        // 2. Transparent in-app crest has transparent exterior corners and opaque white crest interior
        val transparentBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.rcr_crest_transparent)
        assertTrue(transparentBitmap.width > 0 && transparentBitmap.height > 0)
        assertEquals(0, AndroidColor.alpha(transparentBitmap.getPixel(0, 0)))
        assertEquals(0, AndroidColor.alpha(transparentBitmap.getPixel(transparentBitmap.width - 1, 0)))
        // Center band around R.C.R / white shield background must remain fully opaque
        val centerPixel = transparentBitmap.getPixel(transparentBitmap.width / 2, (transparentBitmap.height * 0.49f).toInt())
        assertEquals(255, AndroidColor.alpha(centerPixel))

        // 3. Splash screen renders with original uncropped logo
        composeRule.setContent {
            CompositionLocalProvider(LocalRcrStrings provides stringsForLanguage("fr")) {
                RcrTheme(themeMode = ThemeMode.LIGHT) {
                    RcrLaunchSplashScreen()
                }
            }
        }
        composeRule.onNodeWithTag("launch_splash_screen").assertIsDisplayed()
        composeRule.onNodeWithTag("launch_splash_logo").assertIsDisplayed()
    }

    @Test
    fun migration2To3PreservesRecordsMapsCompetingClubsAndHandlesAmbiguousCodes(): Unit = runBlocking {
        val dbName = "migration_v2_v3_rcr.db"
        context.deleteDatabase(dbName)

        val callback = object : SupportSQLiteOpenHelper.Callback(2) {
            override fun onCreate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS news_articles (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        title TEXT NOT NULL,
                        summary TEXT NOT NULL,
                        content TEXT NOT NULL,
                        category TEXT NOT NULL,
                        imageUri TEXT NOT NULL,
                        publishedDateLabel TEXT NOT NULL,
                        createdAtMillis INTEGER NOT NULL,
                        isFeatured INTEGER NOT NULL,
                        isPublished INTEGER NOT NULL,
                        isPlaceholder INTEGER NOT NULL,
                        titleAr TEXT NOT NULL DEFAULT '',
                        summaryAr TEXT NOT NULL DEFAULT '',
                        contentAr TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS matches (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        opponentName TEXT NOT NULL,
                        opponentShort TEXT NOT NULL,
                        competition TEXT NOT NULL,
                        matchDateLabel TEXT NOT NULL,
                        kickoffTimeLabel TEXT NOT NULL,
                        kickoffTimestampMillis INTEGER NOT NULL,
                        stadium TEXT NOT NULL,
                        isHome INTEGER NOT NULL,
                        rcrScore INTEGER,
                        opponentScore INTEGER,
                        status TEXT NOT NULL,
                        isFeaturedNext INTEGER NOT NULL,
                        matchNotes TEXT NOT NULL,
                        isPlaceholder INTEGER NOT NULL,
                        opponentNameAr TEXT NOT NULL DEFAULT '',
                        competitionAr TEXT NOT NULL DEFAULT '',
                        stadiumAr TEXT NOT NULL DEFAULT '',
                        matchNotesAr TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS standings (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        position INTEGER NOT NULL,
                        clubName TEXT NOT NULL,
                        clubShort TEXT NOT NULL,
                        played INTEGER NOT NULL,
                        wins INTEGER NOT NULL,
                        draws INTEGER NOT NULL,
                        losses INTEGER NOT NULL,
                        goalsFor INTEGER NOT NULL,
                        goalsAgainst INTEGER NOT NULL,
                        points INTEGER NOT NULL,
                        isRcr INTEGER NOT NULL,
                        isPlaceholder INTEGER NOT NULL,
                        clubNameAr TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS players (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        fullName TEXT NOT NULL,
                        shirtNumber INTEGER NOT NULL,
                        position TEXT NOT NULL,
                        positionGroup TEXT NOT NULL,
                        nationality TEXT NOT NULL,
                        biography TEXT NOT NULL,
                        photoUri TEXT NOT NULL,
                        appearances INTEGER,
                        goals INTEGER,
                        assists INTEGER,
                        isHighlighted INTEGER NOT NULL,
                        isPlaceholder INTEGER NOT NULL,
                        fullNameAr TEXT NOT NULL DEFAULT '',
                        positionAr TEXT NOT NULL DEFAULT '',
                        nationalityAr TEXT NOT NULL DEFAULT '',
                        biographyAr TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS gallery_items (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        title TEXT NOT NULL,
                        category TEXT NOT NULL,
                        mediaUri TEXT NOT NULL,
                        isVideo INTEGER NOT NULL,
                        videoDurationLabel TEXT NOT NULL,
                        dateLabel TEXT NOT NULL,
                        createdAtMillis INTEGER NOT NULL,
                        isPlaceholder INTEGER NOT NULL,
                        titleAr TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS club_info (
                        id INTEGER NOT NULL PRIMARY KEY,
                        officialName TEXT NOT NULL,
                        shortName TEXT NOT NULL,
                        city TEXT NOT NULL,
                        foundedYear TEXT NOT NULL,
                        colorsLabel TEXT NOT NULL,
                        historyText TEXT NOT NULL,
                        identityText TEXT NOT NULL,
                        valuesText TEXT NOT NULL,
                        supportersText TEXT NOT NULL,
                        achievementsText TEXT NOT NULL,
                        stadiumName TEXT NOT NULL,
                        stadiumLocation TEXT NOT NULL,
                        stadiumCapacityLabel TEXT NOT NULL,
                        stadiumSurfaceLabel TEXT NOT NULL,
                        stadiumDescription TEXT NOT NULL,
                        contactEmail TEXT NOT NULL,
                        contactPhone TEXT NOT NULL,
                        contactAddress TEXT NOT NULL,
                        officialNameAr TEXT NOT NULL DEFAULT '',
                        cityAr TEXT NOT NULL DEFAULT '',
                        colorsLabelAr TEXT NOT NULL DEFAULT '',
                        historyTextAr TEXT NOT NULL DEFAULT '',
                        identityTextAr TEXT NOT NULL DEFAULT '',
                        valuesTextAr TEXT NOT NULL DEFAULT '',
                        supportersTextAr TEXT NOT NULL DEFAULT '',
                        achievementsTextAr TEXT NOT NULL DEFAULT '',
                        stadiumNameAr TEXT NOT NULL DEFAULT '',
                        stadiumLocationAr TEXT NOT NULL DEFAULT '',
                        stadiumCapacityLabelAr TEXT NOT NULL DEFAULT '',
                        stadiumSurfaceLabelAr TEXT NOT NULL DEFAULT '',
                        stadiumDescriptionAr TEXT NOT NULL DEFAULT '',
                        contactAddressAr TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS notifications (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        title TEXT NOT NULL,
                        message TEXT NOT NULL,
                        type TEXT NOT NULL,
                        timestampMillis INTEGER NOT NULL,
                        timeLabel TEXT NOT NULL,
                        isRead INTEGER NOT NULL,
                        targetRoute TEXT NOT NULL,
                        titleAr TEXT NOT NULL DEFAULT '',
                        messageAr TEXT NOT NULL DEFAULT '',
                        timeLabelAr TEXT NOT NULL DEFAULT ''
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS contact_messages (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        senderName TEXT NOT NULL,
                        senderEmail TEXT NOT NULL,
                        subject TEXT NOT NULL,
                        message TEXT NOT NULL,
                        createdAtMillis INTEGER NOT NULL
                    )
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    INSERT INTO news_articles (title, summary, content, category, imageUri, publishedDateLabel, createdAtMillis, isFeatured, isPublished, isPlaceholder)
                    VALUES ('Ancien article v2', 'Résumé v2', 'Contenu v2', 'Club', 'drawable://img_club_heritage', '12 Nov 2026', 2000, 1, 1, 0)
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT INTO standings (position, clubName, clubShort, played, wins, draws, losses, goalsFor, goalsAgainst, points, isRcr, isPlaceholder, clubNameAr)
                    VALUES (1, 'RC Relizane', 'RCR', 10, 7, 2, 1, 18, 6, 23, 1, 0, 'سريع غليزان')
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT INTO standings (position, clubName, clubShort, played, wins, draws, losses, goalsFor, goalsAgainst, points, isRcr, isPlaceholder, clubNameAr)
                    VALUES (2, 'Club Sportif AEF', 'AEF', 10, 6, 2, 2, 14, 9, 20, 0, 0, '')
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT INTO standings (position, clubName, clubShort, played, wins, draws, losses, goalsFor, goalsAgainst, points, isRcr, isPlaceholder, clubNameAr)
                    VALUES (3, 'US Chlefien', 'USC', 10, 5, 3, 2, 12, 8, 18, 0, 0, '')
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT INTO matches (opponentName, opponentShort, competition, matchDateLabel, kickoffTimeLabel, kickoffTimestampMillis, stadium, isHome, status, isFeaturedNext, matchNotes, isPlaceholder)
                    VALUES ('Club Sportif AEF', 'AEF', 'Ligue', '20 Nov 2026', '15:00', 5000, 'Stade Tahar Zoughari', 1, 'À venir', 1, '', 0)
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT INTO matches (opponentName, opponentShort, competition, matchDateLabel, kickoffTimeLabel, kickoffTimestampMillis, stadium, isHome, status, isFeaturedNext, matchNotes, isPlaceholder)
                    VALUES ('US Chlefien', 'USC', 'Ligue', '27 Nov 2026', '15:00', 6000, 'Stade Externe', 0, 'À venir', 0, '', 0)
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT INTO matches (opponentName, opponentShort, competition, matchDateLabel, kickoffTimeLabel, kickoffTimestampMillis, stadium, isHome, status, isFeaturedNext, matchNotes, isPlaceholder)
                    VALUES ('US Constantine B', 'USC', 'Coupe', '04 Déc 2026', '16:00', 7000, 'Stade Constantine', 0, 'À venir', 0, '', 0)
                    """.trimIndent()
                )
            }

            override fun onUpgrade(db: SupportSQLiteDatabase, oldVersion: Int, newVersion: Int) = Unit
        }

        val helper = FrameworkSQLiteOpenHelperFactory().create(
            SupportSQLiteOpenHelper.Configuration.builder(context)
                .name(dbName)
                .callback(callback)
                .build()
        )
        val v2Db = helper.writableDatabase
        RcrDatabase.MIGRATION_2_3.migrate(v2Db)
        v2Db.close()

        val migratedRoomDb = Room.databaseBuilder(context, RcrDatabase::class.java, dbName)
            .addMigrations(RcrDatabase.MIGRATION_1_2, RcrDatabase.MIGRATION_2_3)
            .allowMainThreadQueries()
            .build()

        val preservedArticles = migratedRoomDb.newsDao().observeAllNews().first()
        assertEquals(1, preservedArticles.size)
        assertEquals("Ancien article v2", preservedArticles.first().title)

        val migratedClubs = migratedRoomDb.competingClubDao().getAllOnce()
        // RCR excluded, shared Club Sportif AEF deduplicated, distinct USC clubs separated cleanly
        assertEquals(3, migratedClubs.size)
        assertFalse(migratedClubs.any { it.clubShort == "RCR" })

        val aefClub = migratedClubs.first { it.clubShort == "AEF" }
        val chlefClub = migratedClubs.first { it.clubName == "US Chlefien" }
        val constClub = migratedClubs.first { it.clubName == "US Constantine B" }
        assertNotEquals(chlefClub.id, constClub.id)

        val migratedStandings = migratedRoomDb.standingDao().getAllOnce()
        val migratedMatches = migratedRoomDb.matchDao().getAllOnce()
        val aefStanding = migratedStandings.first { it.clubShort == "AEF" }
        val aefMatch = migratedMatches.first { it.opponentShort == "AEF" }
        assertEquals(aefClub.id, aefStanding.competingClubId)
        assertEquals(aefClub.id, aefMatch.competingClubId)
        assertNull(migratedStandings.first { it.isRcr }.competingClubId)

        // Verify ambiguity protection: resolving US Constantine B never grabs US Chlefien's logo
        val clubsWithOneLogo = migratedClubs.map {
            if (it.id == chlefClub.id) it.copy(logoPath = "rcr_media/logos/chlef.png") else it
        }
        val resolvedConst = CompetingClubIdentity.resolveClub(
            clubs = clubsWithOneLogo,
            competingClubId = null,
            clubName = "US Constantine B",
            shortCode = "USC"
        )
        assertEquals(constClub.id, resolvedConst?.id)
        assertEquals("", resolvedConst?.logoPath)

        // Ambiguous lookup with unknown club name and shared USC code refuses wrong match
        val resolvedAmbiguous = CompetingClubIdentity.resolveClub(
            clubs = clubsWithOneLogo,
            competingClubId = null,
            clubName = "Autre Club Inconnu",
            shortCode = "USC"
        )
        assertNull(resolvedAmbiguous)

        migratedRoomDb.close()
        context.deleteDatabase(dbName)
    }

    @Test
    fun officialClubLogoPersistsCentralizesCrestAndFallsBackSafely(): Unit = runBlocking {
        repository.ensureSeeded()
        val vm = RcrViewModel(repository, prefs)

        // 1. Default state renders bundled transparent crest
        var activeLogoPath = prefs.preferencesFlow.first().clubLogoPath
        assertEquals("", activeLogoPath)

        val validPng = createTestPngFile("test_official_logo.png", color = AndroidColor.GREEN)
        val saveResult = CompletableDeferred<Pair<Boolean, String?>>()
        vm.saveOfficialClubLogo(context, Uri.fromFile(validPng)) { ok, err ->
            saveResult.complete(ok to err)
        }
        val (savedOk, savedErr) = saveResult.await()
        assertTrue(savedOk)
        assertNull(savedErr)

        // 2. Restart simulation: fresh PreferencesManager reads persisted relative path
        val restartedPrefs = PreferencesManager(context)
        activeLogoPath = restartedPrefs.preferencesFlow.first().clubLogoPath
        assertTrue(activeLogoPath.startsWith("rcr_media/logos/"))
        val savedFile = MediaStorageHelper.resolveStoredMediaFile(context, activeLogoPath)
        assertNotNull(savedFile)
        assertTrue(MediaStorageHelper.isReadableImageFile(context, activeLogoPath))

        // 3. Invalid image fails safely and preserves the active custom logo
        val invalidFile = createInvalidImageFile("bad_logo.txt")
        val invalidResult = CompletableDeferred<Pair<Boolean, String?>>()
        vm.saveOfficialClubLogo(context, Uri.fromFile(invalidFile)) { ok, err ->
            invalidResult.complete(ok to err)
        }
        val (invalidOk, invalidErr) = invalidResult.await()
        assertFalse(invalidOk)
        assertNotNull(invalidErr)
        assertEquals(activeLogoPath, restartedPrefs.preferencesFlow.first().clubLogoPath)

        // 4. Centralized RcrClubCrest updates everywhere when path is provided
        composeRule.setContent {
            CompositionLocalProvider(
                LocalRcrStrings provides stringsForLanguage("fr"),
                LocalRcrClubLogoPath provides activeLogoPath
            ) {
                RcrTheme(themeMode = ThemeMode.LIGHT) {
                    RcrClubCrest()
                }
            }
        }
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("rcr_club_crest_custom").assertIsDisplayed()

        // 5. Missing/deleted file on disk triggers automatic fallback to default crest
        savedFile!!.delete()
        assertFalse(MediaStorageHelper.isReadableImageFile(context, activeLogoPath))

        // 6. Reset clears preference cleanly
        vm.resetOfficialClubLogo(context)
        composeRule.waitUntil(timeoutMillis = 3_000) {
            runBlocking { restartedPrefs.preferencesFlow.first().clubLogoPath.isEmpty() }
        }
        assertEquals("", restartedPrefs.preferencesFlow.first().clubLogoPath)
    }

    @Test
    fun competingClubLogosSyncAcrossMatchesAndStandingsAndFallbackSafely(): Unit = runBlocking {
        repository.ensureSeeded()
        val vm = RcrViewModel(repository, prefs)
        val initialClubs = repository.competingClubs.first()
        assertTrue(initialClubs.size >= 3)

        val clubA = initialClubs[0]
        val clubB = initialClubs[1]
        val clubC = initialClubs[2]

        val pngA = createTestPngFile("opp_a.png", color = AndroidColor.RED)
        val pngB = createTestPngFile("opp_b.png", color = AndroidColor.BLUE)

        val resA = CompletableDeferred<Boolean>()
        vm.saveCompetingClubLogo(context, clubA.id, clubA.clubShort, Uri.fromFile(pngA)) { ok, _ ->
            resA.complete(ok)
        }
        assertTrue(resA.await())

        val resB = CompletableDeferred<Boolean>()
        vm.saveCompetingClubLogo(context, clubB.id, clubB.clubShort, Uri.fromFile(pngB)) { ok, _ ->
            resB.complete(ok)
        }
        assertTrue(resB.await())

        val updatedClubs = repository.competingClubs.first()
        val updatedA = updatedClubs.first { it.id == clubA.id }
        val updatedB = updatedClubs.first { it.id == clubB.id }
        assertTrue(MediaStorageHelper.isReadableImageFile(context, updatedA.logoPath))
        assertTrue(MediaStorageHelper.isReadableImageFile(context, updatedB.logoPath))
        assertNotEquals(updatedA.logoPath, updatedB.logoPath)

        val sampleMatchA = MatchEntity(
            opponentName = updatedA.clubName,
            opponentShort = updatedA.clubShort,
            competingClubId = updatedA.id,
            competition = "Championnat",
            matchDateLabel = "30 Nov 2026",
            kickoffTimeLabel = "15:00",
            kickoffTimestampMillis = System.currentTimeMillis() + 86_400_000L,
            stadium = "Stade Tahar Zoughari"
        )
        val sampleStandings = listOf(
            StandingRowEntity(
                position = 1,
                clubName = updatedA.clubName,
                clubShort = updatedA.clubShort,
                competingClubId = updatedA.id,
                played = 5, wins = 4, draws = 0, losses = 1, goalsFor = 9, goalsAgainst = 3, points = 12
            ),
            StandingRowEntity(
                position = 2,
                clubName = updatedB.clubName,
                clubShort = updatedB.clubShort,
                competingClubId = updatedB.id,
                played = 5, wins = 3, draws = 1, losses = 1, goalsFor = 7, goalsAgainst = 4, points = 10
            ),
            StandingRowEntity(
                position = 3,
                clubName = clubC.clubName,
                clubShort = clubC.clubShort,
                competingClubId = clubC.id,
                played = 5, wins = 2, draws = 2, losses = 1, goalsFor = 5, goalsAgainst = 4, points = 8
            )
        )

        composeRule.setContent {
            CompositionLocalProvider(
                LocalRcrStrings provides stringsForLanguage("fr"),
                LocalCompetingClubs provides updatedClubs
            ) {
                RcrTheme(themeMode = ThemeMode.LIGHT) {
                    androidx.compose.foundation.layout.Column {
                        MatchCard(match = sampleMatchA, onClick = {})
                        StandingsTableCard(standings = sampleStandings)
                    }
                }
            }
        }
        composeRule.waitForIdle()

        // Both MatchCard AND StandingsTableCard display clubA custom logo
        assertEquals(
            2,
            composeRule.onAllNodesWithTag("opponent_badge_logo_${updatedA.clubShort}").fetchSemanticsNodes().size
        )
        // clubB displays its own logo; clubC falls back to its 3-letter code
        assertTrue(
            composeRule.onAllNodesWithTag("opponent_badge_logo_${updatedB.clubShort}").fetchSemanticsNodes().isNotEmpty()
        )
        assertTrue(
            composeRule.onAllNodesWithTag("opponent_badge_code_${clubC.clubShort}").fetchSemanticsNodes().isNotEmpty()
        )

        // Removing clubA's logo cleans up the file and returns badge fallback path
        val oldPathA = updatedA.logoPath
        vm.removeCompetingClubLogo(context, updatedA.id)
        composeRule.waitUntil(timeoutMillis = 3_000) {
            runBlocking {
                repository.competingClubs.first().first { it.id == clubA.id }.logoPath.isEmpty()
            }
        }
        assertNull(MediaStorageHelper.resolveStoredMediaFile(context, oldPathA))
    }

    @Test
    fun adminLogosTabOpensPreviewDialogAndSupportsFrenchLtrAndArabicRtlDarkMode() {
        composeRule.setContent {
            RcrAppRoot()
        }

        // Wait for Home screen to finish seeding
        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithText("ENSEMBLE,\nPLUS FORTS.").fetchSemanticsNodes().isNotEmpty()
        }

        // Navigate More -> Admin Dashboard and unlock with PIN 1934
        composeRule.onNodeWithTag("nav_more").performClick()
        composeRule.onNodeWithTag("more_screen_list")
            .performScrollToNode(hasTestTag("more_admin"))
        composeRule.onNodeWithTag("more_admin").performClick()
        composeRule.onNodeWithTag("admin_pin_input").performScrollTo().performTextInput("1934")
        composeRule.onNodeWithTag("admin_login_submit").performScrollTo().performClick()

        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithTag("admin_dashboard_screen").fetchSemanticsNodes().isNotEmpty()
        }

        // Open Logos tab (index 5) in French LTR + Light theme
        composeRule.onNodeWithTag("admin_tab_5").performClick()
        composeRule.waitUntil(timeoutMillis = 3_000) {
            composeRule.onAllNodesWithTag("admin_logos_section_title").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("GESTION DES LOGOS").assertIsDisplayed()
        composeRule.onNodeWithTag("official_club_logo_card").assertIsDisplayed()

        // Scroll to competing club list and open club #1 logo dialog
        composeRule.onNodeWithTag("admin_tab_content_list")
            .performScrollToNode(hasTestTag("admin_competing_logos_header"))
        composeRule.onNodeWithTag("admin_competing_logos_header").assertIsDisplayed()
        composeRule.onNodeWithTag("admin_tab_content_list")
            .performScrollToNode(hasTestTag("competing_club_row_1"))
        composeRule.onNodeWithTag("manage_club_logo_1").performClick()
        composeRule.waitUntil(timeoutMillis = 3_000) {
            composeRule.onAllNodesWithTag("competing_club_logo_dialog").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("competing_logo_pick_btn").assertIsDisplayed()
        // Save button disabled until an image is staged/previewed
        composeRule.onNodeWithTag("competing_logo_save_btn").assertIsNotEnabled()
        composeRule.onNodeWithTag("competing_logo_cancel_btn").performClick()
        composeRule.waitForIdle()

        // Switch to Dark Mode + Arabic RTL and verify Logos tab localizes cleanly
        composeRule.onNodeWithTag("nav_more").performClick()
        composeRule.onNodeWithTag("more_screen_list")
            .performScrollToNode(hasTestTag("more_settings"))
        composeRule.onNodeWithTag("more_settings").performClick()
        composeRule.onNodeWithTag("settings_screen")
            .performScrollToNode(hasTestTag("theme_option_dark"))
        composeRule.onNodeWithTag("theme_option_dark").performClick()
        composeRule.onNodeWithTag("settings_screen")
            .performScrollToNode(hasTestTag("lang_option_ar"))
        composeRule.onNodeWithTag("lang_option_ar").performClick()
        composeRule.onNodeWithTag("settings_screen")
            .performScrollToNode(hasTestTag("settings_open_admin_button"))
        composeRule.onNodeWithTag("settings_open_admin_button").performClick()

        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithTag("admin_tab_5").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("admin_tab_5").performClick()
        composeRule.waitUntil(timeoutMillis = 3_000) {
            composeRule.onAllNodesWithText("إدارة الشعارات").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("إدارة الشعارات").assertIsDisplayed()
        composeRule.onNodeWithText("الشعار الرسمي للنادي").assertIsDisplayed()
    }

    @Test
    fun aboutScreenNavigatesFromMoreDisplaysDynamicVersionAndTranslatesBilingually() {
        val expectedVersion = resolveInstalledAppVersion(context)
        assertTrue(expectedVersion.isNotBlank())

        composeRule.setContent {
            RcrAppRoot()
        }

        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithText("ENSEMBLE,\nPLUS FORTS.").fetchSemanticsNodes().isNotEmpty()
        }

        // Open More -> À propos de l'application (French LTR)
        composeRule.onNodeWithTag("nav_more").performClick()
        composeRule.onNodeWithTag("more_screen_list")
            .performScrollToNode(hasTestTag("more_about"))
        composeRule.onNodeWithTag("more_about").performClick()

        composeRule.waitUntil(timeoutMillis = 3_000) {
            composeRule.onAllNodesWithTag("about_screen_list").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithTag("rcr_club_crest_default").assertIsDisplayed()
        composeRule.onNodeWithTag("about_app_title").assertIsDisplayed()
        composeRule.onNodeWithTag("about_screen_list")
            .performScrollToNode(hasTestTag("about_presentation_card"))
        composeRule.onNodeWithText(
            "Cette application est le fruit d'une passion pour le football et d'un attachement particulier au Rapid Club de Relizane. Conçue et développée par Mohamed Ala Eddine, elle a pour objectif de rapprocher le club de ses supporters et de réunir son actualité, ses résultats et son histoire dans un seul espace numérique."
        ).assertIsDisplayed()

        composeRule.onNodeWithTag("about_screen_list")
            .performScrollToNode(hasTestTag("about_developer_card"))
        composeRule.onNodeWithText("MA").assertIsDisplayed()
        composeRule.onNodeWithText("Mohamed Ala Eddine").assertIsDisplayed()
        composeRule.onNodeWithText("Conception & Développement").assertIsDisplayed()
        composeRule.onNodeWithText(
            "Cette application a été conçue et développée par Mohamed Ala Eddine, avec la volonté de proposer aux supporters du Rapid Club de Relizane une expérience numérique moderne, pratique et complète."
        ).assertIsDisplayed()

        composeRule.onNodeWithTag("about_screen_list")
            .performScrollToNode(hasTestTag("about_info_card"))
        composeRule.onNodeWithText(expectedVersion).assertIsDisplayed()
        composeRule.onNodeWithText("© Mohamed Ala Eddine").assertIsDisplayed()

        // Switch to Arabic (RTL) in Settings and open About page again from More
        composeRule.onNodeWithTag("nav_more").performClick()
        composeRule.onNodeWithTag("more_screen_list")
            .performScrollToNode(hasTestTag("more_settings"))
        composeRule.onNodeWithTag("more_settings").performClick()
        composeRule.onNodeWithTag("settings_screen")
            .performScrollToNode(hasTestTag("lang_option_ar"))
        composeRule.onNodeWithTag("lang_option_ar").performClick()
        composeRule.waitUntil(timeoutMillis = 3_000) {
            composeRule.onAllNodesWithText("الإعدادات").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithTag("nav_more").performClick()
        composeRule.onNodeWithTag("more_screen_list")
            .performScrollToNode(hasTestTag("more_about"))
        composeRule.onNodeWithTag("more_about").performClick()
        composeRule.waitUntil(timeoutMillis = 3_000) {
            composeRule.onAllNodesWithTag("about_screen_list").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithTag("about_screen_list")
            .performScrollToNode(hasTestTag("about_presentation_card"))
        composeRule.onNodeWithText(
            "هذا التطبيق ثمرة شغف بكرة القدم وارتباط خاص بنادي سريع غليزان. صمّمه وطوّره محمد علاء الدين بهدف تقريب النادي من جماهيره، وجمع أخباره ونتائجه وتاريخه في فضاء رقمي واحد."
        ).assertIsDisplayed()

        composeRule.onNodeWithTag("about_screen_list")
            .performScrollToNode(hasTestTag("about_developer_card"))
        composeRule.onNodeWithText("تصميم وتطوير").assertIsDisplayed()
        composeRule.onNodeWithText(
            "تم تصميم وتطوير هذا التطبيق من طرف محمد علاء الدين، بهدف تقديم تجربة رقمية حديثة وعملية ومتكاملة لجماهير نادي سريع غليزان."
        ).assertIsDisplayed()

        composeRule.onNodeWithTag("about_screen_list")
            .performScrollToNode(hasTestTag("about_info_card"))
        composeRule.onNodeWithText("إصدار التطبيق").assertIsDisplayed()
        composeRule.onNodeWithText(expectedVersion).assertIsDisplayed()
        composeRule.onNodeWithText("© Mohamed Ala Eddine").assertIsDisplayed()
    }

    @Test
    fun aboutScreenSupportsLightDarkSystemThemesAndRtlLtrDirection() {
        var currentTheme by mutableStateOf(ThemeMode.LIGHT)
        var currentLang by mutableStateOf("fr")
        var observedDir: LayoutDirection? = null
        val expectedVersion = resolveInstalledAppVersion(context)

        composeRule.setContent {
            val dir = if (currentLang == "ar") LayoutDirection.Rtl else LayoutDirection.Ltr
            CompositionLocalProvider(
                LocalLayoutDirection provides dir,
                LocalRcrStrings provides stringsForLanguage(currentLang)
            ) {
                observedDir = LocalLayoutDirection.current
                RcrTheme(themeMode = currentTheme) {
                    AboutScreen()
                }
            }
        }

        // Light + French LTR
        composeRule.waitForIdle()
        assertEquals(LayoutDirection.Ltr, observedDir)
        composeRule.onNodeWithTag("about_hero_card").assertIsDisplayed()

        // Dark + Arabic RTL
        currentTheme = ThemeMode.DARK
        currentLang = "ar"
        composeRule.waitForIdle()
        assertEquals(LayoutDirection.Rtl, observedDir)
        composeRule.onNodeWithTag("about_screen_list")
            .performScrollToNode(hasTestTag("about_info_card"))
        composeRule.onNodeWithText(expectedVersion).assertIsDisplayed()

        // System + French LTR
        currentTheme = ThemeMode.SYSTEM
        currentLang = "fr"
        composeRule.waitForIdle()
        assertEquals(LayoutDirection.Ltr, observedDir)
        composeRule.onNodeWithTag("about_screen_list")
            .performScrollToNode(hasTestTag("about_developer_card"))
        composeRule.onNodeWithText("Mohamed Ala Eddine").assertIsDisplayed()
    }

    @Test
    fun resolveAdjacentMainRouteRespectsBoundariesAndDirection() {
        // LTR boundaries & adjacent steps
        assertNull(resolveAdjacentMainRoute(Routes.HOME, dragDeltaX = 120f, isRtl = false))
        assertEquals(Routes.NEWS, resolveAdjacentMainRoute(Routes.HOME, dragDeltaX = -120f, isRtl = false))
        assertEquals(Routes.HOME, resolveAdjacentMainRoute(Routes.NEWS, dragDeltaX = 120f, isRtl = false))
        assertEquals(Routes.MATCHES, resolveAdjacentMainRoute(Routes.NEWS, dragDeltaX = -120f, isRtl = false))
        assertEquals(Routes.TEAM, resolveAdjacentMainRoute(Routes.MATCHES, dragDeltaX = -120f, isRtl = false))
        assertEquals(Routes.MORE, resolveAdjacentMainRoute(Routes.TEAM, dragDeltaX = -120f, isRtl = false))
        assertNull(resolveAdjacentMainRoute(Routes.MORE, dragDeltaX = -120f, isRtl = false))
        assertEquals(Routes.TEAM, resolveAdjacentMainRoute(Routes.MORE, dragDeltaX = 120f, isRtl = false))

        // RTL reverses horizontal swipe direction naturally
        assertNull(resolveAdjacentMainRoute(Routes.HOME, dragDeltaX = -120f, isRtl = true))
        assertEquals(Routes.NEWS, resolveAdjacentMainRoute(Routes.HOME, dragDeltaX = 120f, isRtl = true))
        assertEquals(Routes.HOME, resolveAdjacentMainRoute(Routes.NEWS, dragDeltaX = -120f, isRtl = true))
        assertNull(resolveAdjacentMainRoute(Routes.MORE, dragDeltaX = 120f, isRtl = true))
        assertEquals(Routes.TEAM, resolveAdjacentMainRoute(Routes.MORE, dragDeltaX = -120f, isRtl = true))

        // Secondary screens do not participate in main-tab swipe
        assertNull(resolveAdjacentMainRoute(Routes.ABOUT, dragDeltaX = -120f, isRtl = false))
        assertNull(resolveAdjacentMainRoute(Routes.SETTINGS, dragDeltaX = 120f, isRtl = true))
    }

    @Test
    fun swipeNavigationSwitchesMainTabsSyncsBottomBarRespectsRtlAndAvoidsScrollConflicts() {
        composeRule.setContent {
            RcrAppRoot()
        }

        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithText("ENSEMBLE,\nPLUS FORTS.").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("nav_home").assertIsSelected()
        composeRule.onNodeWithTag("home_screen_list").assertIsDisplayed()

        // 1. Vertical scrolling stays on Home without triggering horizontal tab switch
        composeRule.onNodeWithTag("home_screen_list").performTouchInput { swipeUp() }
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("nav_home").assertIsSelected()
        assertTrue(composeRule.onAllNodesWithTag("news_screen_list").fetchSemanticsNodes().isEmpty())

        // 2. Horizontal carousel swipe on highlighted players LazyRow scrolls carousel without changing tab
        composeRule.onNodeWithTag("home_screen_list")
            .performScrollToNode(hasTestTag("home_players_row"))
        composeRule.onNodeWithTag("home_players_row").performTouchInput { swipeLeft() }
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("nav_home").assertIsSelected()
        assertTrue(composeRule.onAllNodesWithTag("news_screen_list").fetchSemanticsNodes().isEmpty())

        composeRule.onNodeWithTag("home_screen_list").performTouchInput { swipeDown() }
        composeRule.waitForIdle()

        // 3. LTR boundary clamp: swiping right on Home does nothing
        composeRule.onNodeWithTag("main_nav_swipe_container").performTouchInput { swipeRight() }
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("nav_home").assertIsSelected()

        // 4. Swipe through all 5 tabs LTR: Home -> News -> Matches -> Team -> More
        composeRule.onNodeWithTag("main_nav_swipe_container").performTouchInput { swipeLeft() }
        composeRule.waitUntil(timeoutMillis = 3_000) {
            composeRule.onAllNodesWithTag("news_screen_list").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("nav_news").assertIsSelected()

        composeRule.onNodeWithTag("main_nav_swipe_container").performTouchInput { swipeLeft() }
        composeRule.waitUntil(timeoutMillis = 3_000) {
            composeRule.onAllNodesWithTag("matches_screen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("nav_matches").assertIsSelected()

        composeRule.onNodeWithTag("main_nav_swipe_container").performTouchInput { swipeLeft() }
        composeRule.waitUntil(timeoutMillis = 3_000) {
            composeRule.onAllNodesWithTag("team_screen_list").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("nav_team").assertIsSelected()

        composeRule.onNodeWithTag("main_nav_swipe_container").performTouchInput { swipeLeft() }
        composeRule.waitUntil(timeoutMillis = 3_000) {
            composeRule.onAllNodesWithTag("more_screen_list").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("nav_more").assertIsSelected()

        // 5. LTR end clamp & reverse swipe: swipe left stays on More; swipe right goes to Team
        composeRule.onNodeWithTag("main_nav_swipe_container").performTouchInput { swipeLeft() }
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("nav_more").assertIsSelected()

        composeRule.onNodeWithTag("main_nav_swipe_container").performTouchInput { swipeRight() }
        composeRule.waitUntil(timeoutMillis = 3_000) {
            composeRule.onAllNodesWithTag("team_screen_list").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("nav_team").assertIsSelected()

        // 6. Switch to Arabic (RTL) and verify reversed horizontal swipe + button navigation
        composeRule.onNodeWithTag("nav_more").performClick()
        composeRule.onNodeWithTag("more_screen_list")
            .performScrollToNode(hasTestTag("more_settings"))
        composeRule.onNodeWithTag("more_settings").performClick()
        composeRule.onNodeWithTag("settings_screen")
            .performScrollToNode(hasTestTag("lang_option_ar"))
        composeRule.onNodeWithTag("lang_option_ar").performClick()
        composeRule.waitUntil(timeoutMillis = 3_000) {
            composeRule.onAllNodesWithText("الإعدادات").fetchSemanticsNodes().isNotEmpty()
        }

        // Swiping on a secondary screen (Settings) must NOT switch main tabs
        composeRule.onNodeWithTag("main_nav_swipe_container").performTouchInput { swipeRight() }
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("settings_screen").assertIsDisplayed()

        // Return to Home via bottom bar click
        composeRule.onNodeWithTag("nav_home").performClick()
        composeRule.waitUntil(timeoutMillis = 3_000) {
            composeRule.onAllNodesWithTag("home_screen_list").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("nav_home").assertIsSelected()

        // In RTL: swiping right moves forward Home -> News; swiping left returns News -> Home
        composeRule.onNodeWithTag("main_nav_swipe_container").performTouchInput { swipeRight() }
        composeRule.waitUntil(timeoutMillis = 3_000) {
            composeRule.onAllNodesWithTag("news_screen_list").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("nav_news").assertIsSelected()

        composeRule.onNodeWithTag("main_nav_swipe_container").performTouchInput { swipeLeft() }
        composeRule.waitUntil(timeoutMillis = 3_000) {
            composeRule.onAllNodesWithTag("home_screen_list").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("nav_home").assertIsSelected()
    }
}
