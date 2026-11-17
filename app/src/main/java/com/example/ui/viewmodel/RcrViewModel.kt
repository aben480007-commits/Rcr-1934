package com.example.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppNotificationEntity
import com.example.data.local.ClubInfoEntity
import com.example.data.local.CompetingClubEntity
import com.example.data.local.ContactMessageEntity
import com.example.data.local.GalleryItemEntity
import com.example.data.local.LogoPersistResult
import com.example.data.local.MatchEntity
import com.example.data.local.MediaStorageHelper
import com.example.data.local.NewsArticleEntity
import com.example.data.local.PlayerEntity
import com.example.data.local.PreferencesManager
import com.example.data.local.StandingRowEntity
import com.example.data.local.UserPreferences
import com.example.data.repository.RcrRepository
import com.example.ui.localization.RcrStrings
import com.example.ui.localization.stringsForLanguage
import com.example.ui.theme.ThemeMode
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RcrViewModel(
    private val repository: RcrRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _nowMillis = MutableStateFlow(System.currentTimeMillis())
    val nowMillis: StateFlow<Long> = _nowMillis.asStateFlow()

    private val _newsQuery = MutableStateFlow("")
    val newsQuery: StateFlow<String> = _newsQuery.asStateFlow()

    private val _selectedNewsCategory = MutableStateFlow("Toutes")
    val selectedNewsCategory: StateFlow<String> = _selectedNewsCategory.asStateFlow()

    private val _selectedGalleryCategory = MutableStateFlow("Toutes")
    val selectedGalleryCategory: StateFlow<String> = _selectedGalleryCategory.asStateFlow()

    private val _snackbarEvents = MutableSharedFlow<String>()
    val snackbarEvents = _snackbarEvents.asSharedFlow()

    val preferences: StateFlow<UserPreferences> = preferencesManager.preferencesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserPreferences())

    private val currentStrings: RcrStrings
        get() = stringsForLanguage(preferences.value.languageCode)

    val publishedNews: StateFlow<List<NewsArticleEntity>> = repository.publishedNews
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredNews: StateFlow<List<NewsArticleEntity>> = combine(
        repository.publishedNews,
        _newsQuery,
        _selectedNewsCategory
    ) { list, query, category ->
        list.filter { article ->
            val matchesCat = category == "Toutes" || article.category.equals(category, ignoreCase = true)
            val q = query.trim()
            val matchesQuery = q.isEmpty() ||
                article.title.contains(q, ignoreCase = true) ||
                article.summary.contains(q, ignoreCase = true) ||
                article.content.contains(q, ignoreCase = true) ||
                article.titleAr.contains(q, ignoreCase = true) ||
                article.summaryAr.contains(q, ignoreCase = true) ||
                article.contentAr.contains(q, ignoreCase = true)
            matchesCat && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allNewsForAdmin: StateFlow<List<NewsArticleEntity>> = repository.allNewsForAdmin
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val matches: StateFlow<List<MatchEntity>> = repository.matches
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val standings: StateFlow<List<StandingRowEntity>> = repository.standings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val players: StateFlow<List<PlayerEntity>> = repository.players
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val galleryItems: StateFlow<List<GalleryItemEntity>> = repository.galleryItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredGalleryItems: StateFlow<List<GalleryItemEntity>> = combine(
        repository.galleryItems,
        _selectedGalleryCategory
    ) { items, category ->
        if (category == "Toutes") items
        else items.filter { it.category.equals(category, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val clubInfo: StateFlow<ClubInfoEntity?> = repository.clubInfo
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val notifications: StateFlow<List<AppNotificationEntity>> = repository.notifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unreadCount: StateFlow<Int> = repository.unreadNotificationsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val contactMessages: StateFlow<List<ContactMessageEntity>> = repository.contactMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val competingClubs: StateFlow<List<CompetingClubEntity>> = repository.competingClubs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.ensureSeeded()
        }
        viewModelScope.launch {
            while (true) {
                delay(1000L)
                _nowMillis.value = System.currentTimeMillis()
            }
        }
    }

    fun observeNewsById(id: Long): Flow<NewsArticleEntity?> = repository.observeNewsById(id)
    fun observeMatchById(id: Long): Flow<MatchEntity?> = repository.observeMatchById(id)
    fun observePlayerById(id: Long): Flow<PlayerEntity?> = repository.observePlayerById(id)

    fun updateNewsQuery(query: String) {
        _newsQuery.value = query
    }

    fun selectNewsCategory(category: String) {
        _selectedNewsCategory.value = category
    }

    fun selectGalleryCategory(category: String) {
        _selectedGalleryCategory.value = category
    }

    // Settings
    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch { preferencesManager.setThemeMode(mode) }
    }

    fun setLanguageAndRtl(languageCode: String, isRtl: Boolean) {
        viewModelScope.launch { preferencesManager.setLanguageAndRtl(languageCode, isRtl) }
    }

    fun setInAppAlertsEnabled(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setInAppAlertsEnabled(enabled) }
    }

    // Notifications
    fun markNotificationAsRead(id: Long) {
        viewModelScope.launch { repository.markNotificationAsRead(id) }
    }

    fun markAllNotificationsAsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsAsRead()
            _snackbarEvents.emit(currentStrings.msgAllNotifsRead)
        }
    }

    fun deleteNotification(id: Long) {
        viewModelScope.launch { repository.deleteNotification(id) }
    }

    // Contact
    fun submitContactMessage(name: String, email: String, subject: String, message: String) {
        viewModelScope.launch {
            repository.sendContactMessage(name, email, subject, message)
            _snackbarEvents.emit(currentStrings.msgContactSaved)
        }
    }

    // Admin Auth
    fun loginAdmin(pin: String): Boolean {
        val currentPin = preferences.value.adminPin
        return if (pin.trim() == currentPin) {
            viewModelScope.launch {
                preferencesManager.setAdminAuthenticated(true)
                _snackbarEvents.emit(currentStrings.msgAdminAuthSuccess)
            }
            true
        } else {
            false
        }
    }

    fun logoutAdmin() {
        viewModelScope.launch {
            preferencesManager.setAdminAuthenticated(false)
            _snackbarEvents.emit(currentStrings.msgAdminLogout)
        }
    }

    fun changeAdminPin(newPin: String) {
        viewModelScope.launch {
            preferencesManager.updateAdminPin(newPin.trim())
            _snackbarEvents.emit(currentStrings.msgAdminPinUpdated)
        }
    }

    // Admin CRUD
    fun saveArticle(article: NewsArticleEntity) {
        viewModelScope.launch {
            repository.saveArticle(article)
            _snackbarEvents.emit(if (article.id == 0L) currentStrings.msgArticleSaved else currentStrings.msgArticleUpdated)
        }
    }

    fun deleteArticle(article: NewsArticleEntity) {
        viewModelScope.launch {
            repository.deleteArticle(article)
            _snackbarEvents.emit(currentStrings.msgArticleDeleted)
        }
    }

    fun savePlayer(player: PlayerEntity) {
        viewModelScope.launch {
            repository.savePlayer(player)
            _snackbarEvents.emit(if (player.id == 0L) currentStrings.msgPlayerSaved else currentStrings.msgPlayerUpdated)
        }
    }

    fun deletePlayer(player: PlayerEntity) {
        viewModelScope.launch {
            repository.deletePlayer(player)
            _snackbarEvents.emit(currentStrings.msgPlayerDeleted)
        }
    }

    fun saveMatch(match: MatchEntity) {
        viewModelScope.launch {
            repository.saveMatch(match)
            _snackbarEvents.emit(if (match.id == 0L) currentStrings.msgMatchSaved else currentStrings.msgMatchUpdated)
        }
    }

    fun deleteMatch(match: MatchEntity) {
        viewModelScope.launch {
            repository.deleteMatch(match)
            _snackbarEvents.emit(currentStrings.msgMatchDeleted)
        }
    }

    fun saveStandingRow(row: StandingRowEntity) {
        viewModelScope.launch {
            repository.saveStandingRow(row)
            _snackbarEvents.emit(currentStrings.msgStandingUpdated)
        }
    }

    fun deleteStandingRow(row: StandingRowEntity) {
        viewModelScope.launch {
            repository.deleteStandingRow(row)
            _snackbarEvents.emit(currentStrings.msgStandingDeleted)
        }
    }

    fun addGalleryItem(item: GalleryItemEntity) {
        viewModelScope.launch {
            repository.addGalleryItem(item)
            _snackbarEvents.emit(currentStrings.msgGalleryAdded)
        }
    }

    fun deleteGalleryItem(item: GalleryItemEntity) {
        viewModelScope.launch {
            repository.deleteGalleryItem(item)
            _snackbarEvents.emit(currentStrings.msgGalleryDeleted)
        }
    }

    fun updateClubInfo(info: ClubInfoEntity) {
        viewModelScope.launch {
            repository.updateClubInfo(info)
            _snackbarEvents.emit(currentStrings.msgClubUpdated)
        }
    }

    // Dynamic Logo Management (Official RCR + Competing Clubs)
    fun saveOfficialClubLogo(
        context: Context,
        sourceUri: Uri,
        onComplete: (Boolean, String?) -> Unit = { _, _ -> }
    ) {
        viewModelScope.launch {
            val strings = currentStrings
            val previousPath = preferences.value.clubLogoPath
            when (val persisted = MediaStorageHelper.persistValidatedLogo(context, sourceUri, "rcr_official")) {
                is LogoPersistResult.Success -> {
                    val savedOk = runCatching {
                        preferencesManager.setClubLogoPath(persisted.relativePath)
                    }.isSuccess
                    if (savedOk) {
                        val activePaths = repository.activeCompetingClubLogoPaths() + persisted.relativePath
                        MediaStorageHelper.deleteManagedMediaIfUnreferenced(context, previousPath, activePaths)
                        _snackbarEvents.emit(strings.msgOfficialLogoSaved)
                        onComplete(true, null)
                    } else {
                        MediaStorageHelper.deleteManagedMediaIfUnreferenced(
                            context,
                            persisted.relativePath,
                            setOf(previousPath)
                        )
                        _snackbarEvents.emit(strings.adminLogoSaveError)
                        onComplete(false, strings.adminLogoSaveError)
                    }
                }
                LogoPersistResult.InvalidImage -> {
                    _snackbarEvents.emit(strings.adminLogoInvalidError)
                    onComplete(false, strings.adminLogoInvalidError)
                }
                LogoPersistResult.WriteFailed -> {
                    _snackbarEvents.emit(strings.adminLogoSaveError)
                    onComplete(false, strings.adminLogoSaveError)
                }
            }
        }
    }

    fun resetOfficialClubLogo(context: Context) {
        viewModelScope.launch {
            val previousPath = preferences.value.clubLogoPath
            preferencesManager.setClubLogoPath("")
            val activePaths = repository.activeCompetingClubLogoPaths()
            MediaStorageHelper.deleteManagedMediaIfUnreferenced(context, previousPath, activePaths)
            _snackbarEvents.emit(currentStrings.msgOfficialLogoReset)
        }
    }

    fun saveCompetingClubLogo(
        context: Context,
        clubId: Long,
        clubShort: String,
        sourceUri: Uri,
        onComplete: (Boolean, String?) -> Unit = { _, _ -> }
    ) {
        viewModelScope.launch {
            val strings = currentStrings
            val prefix = "opp_${clubShort.trim().lowercase()}"
            when (val persisted = MediaStorageHelper.persistValidatedLogo(context, sourceUri, prefix)) {
                is LogoPersistResult.Success -> {
                    val previousPath = runCatching {
                        repository.updateCompetingClubLogo(clubId, persisted.relativePath)
                    }.getOrNull()
                    if (previousPath != null) {
                        val activePaths = repository.activeCompetingClubLogoPaths() + preferences.value.clubLogoPath
                        MediaStorageHelper.deleteManagedMediaIfUnreferenced(context, previousPath, activePaths)
                        _snackbarEvents.emit(strings.msgOpponentLogoSaved)
                        onComplete(true, null)
                    } else {
                        val activePaths = repository.activeCompetingClubLogoPaths() + preferences.value.clubLogoPath
                        MediaStorageHelper.deleteManagedMediaIfUnreferenced(context, persisted.relativePath, activePaths)
                        _snackbarEvents.emit(strings.adminLogoSaveError)
                        onComplete(false, strings.adminLogoSaveError)
                    }
                }
                LogoPersistResult.InvalidImage -> {
                    _snackbarEvents.emit(strings.adminLogoInvalidError)
                    onComplete(false, strings.adminLogoInvalidError)
                }
                LogoPersistResult.WriteFailed -> {
                    _snackbarEvents.emit(strings.adminLogoSaveError)
                    onComplete(false, strings.adminLogoSaveError)
                }
            }
        }
    }

    fun removeCompetingClubLogo(context: Context, clubId: Long) {
        viewModelScope.launch {
            val previousPath = repository.updateCompetingClubLogo(clubId, "")
            val activePaths = repository.activeCompetingClubLogoPaths() + preferences.value.clubLogoPath
            MediaStorageHelper.deleteManagedMediaIfUnreferenced(context, previousPath, activePaths)
            _snackbarEvents.emit(currentStrings.msgOpponentLogoRemoved)
        }
    }

    class Factory(
        private val repository: RcrRepository,
        private val preferencesManager: PreferencesManager
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RcrViewModel(repository, preferencesManager) as T
        }
    }
}
