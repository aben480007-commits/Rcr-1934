package com.example.ui.localization

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.ui.navigation.Routes

@Immutable
interface RcrStrings {
    val isArabic: Boolean

    // Brand & TopBar
    val clubOfficialName: String
    val clubShortName: String
    val clubLocationBadge: String
    val topBarHomeSubtitle: String
    val backContentDesc: String
    val notificationsContentDesc: String
    val adminSpaceDesc: String

    // Routes Titles & Subtitles
    val titleNews: String
    val subtitleNews: String
    val titleArticle: String
    val subtitleArticle: String
    val titleMatches: String
    val subtitleMatches: String
    val titleMatchDetail: String
    val subtitleMatchDetail: String
    val titleTeam: String
    val subtitleTeam: String
    val titlePlayerProfile: String
    val subtitlePlayerProfile: String
    val titleMore: String
    val subtitleMore: String
    val titleClub: String
    val subtitleClub: String
    val titleStandings: String
    val subtitleStandings: String
    val titleGallery: String
    val subtitleGallery: String
    val titleStadium: String
    val titleContact: String
    val subtitleContact: String
    val titleSettings: String
    val subtitleSettings: String
    val titleAbout: String
    val subtitleAbout: String
    val titleNotifications: String
    val subtitleNotifications: String
    val titleAdmin: String
    val subtitleAdmin: String

    // Home Screen
    val heroHeadline: String
    val heroSubtitle: String
    val discoverClubBtn: String
    val sectionNextMatch: String
    val actionCalendar: String
    val sectionLatestResults: String
    val actionSeeAll: String
    val sectionLatestNews: String
    val actionAllNews: String
    val sectionTeamHighlights: String
    val actionSquad: String
    val sectionStandingsPreview: String
    val actionFullTable: String
    val supportersEyebrow: String
    val supportersMotto: String
    val supportersSubtext: String
    val shirtNumberPrefix: String

    // Countdown & Match
    val daysUnit: String
    val hoursUnit: String
    val minsUnit: String
    val secsUnit: String
    val vsLabel: String
    val exampleBadge: String
    val placeholderDefaultBanner: String
    val matchTabUpcoming: String
    val matchTabResults: String
    val matchTabStandings: String
    val matchEmptyUpcomingTitle: String
    val matchEmptyUpcomingSub: String
    val matchEmptyResultsTitle: String
    val matchEmptyResultsSub: String
    val matchPlaceholderUpcoming: String
    val matchPlaceholderResults: String
    val matchNotFoundTitle: String
    val matchNotFoundSub: String
    val matchSheetHeader: String
    val matchCompetitionLabel: String
    val matchDateLabel: String
    val matchKickoffLabel: String
    val matchStadiumLabel: String
    val matchNotesLabel: String
    val matchPlaceholderDetailBanner: String

    // Standings Table
    val standingsEmptyTitle: String
    val standingsEmptySub: String
    val standingsPlaceholderBanner: String
    val colPos: String
    val colClub: String
    val colPlayed: String
    val colWins: String
    val colDraws: String
    val colLosses: String
    val colGoalsFor: String
    val colGoalsAgainst: String
    val colDiff: String
    val colPoints: String
    val standingsLegend: String

    // News & Article Detail
    val searchPlaceholder: String
    val clearSearchDesc: String
    val searchIconDesc: String
    val newsEmptyTitle: String
    val newsEmptySub: String
    val newsFeaturedHeader: String
    val newsFeaturedPrefix: String
    val newsFeedHeader: String
    val articleNotFoundTitle: String
    val articleNotFoundSub: String
    val articlePlaceholderBanner: String
    val articleFallbackNotice: String
    val articleRelatedHeader: String

    // Team & Player Detail
    val squadPlaceholderBanner: String
    val squadEmptyTitle: String
    val squadEmptySub: String
    val playerNotFoundTitle: String
    val playerNotFoundSub: String
    val playerPlaceholderBanner: String
    val playerStatsAvailableHeader: String
    val playerStatsHeader: String
    val playerNoStatsMsg: String
    val statMatches: String
    val statGoals: String
    val statAssists: String
    val playerBioHeader: String
    val playerNoBioFallback: String
    val playerNationalityPrefix: String

    // More, Club, Stadium, Gallery
    val moreClubTitle: String
    val moreClubSub: String
    val moreStandingsTitle: String
    val moreStandingsSub: String
    val moreGalleryTitle: String
    val moreGallerySub: String
    val moreStadiumTitle: String
    val moreStadiumSub: String
    val moreContactTitle: String
    val moreContactSub: String
    val moreSettingsTitle: String
    val moreSettingsSub: String
    val moreAboutTitle: String
    val moreAboutSub: String
    val moreAdminTitle: String
    val moreAdminSubActive: String
    val moreAdminSubLocked: String
    val foundedPrefix: String
    val clubHistoryTitle: String
    val clubIdentityTitle: String
    val clubValuesTitle: String
    val clubSupportersTitle: String
    val clubAchievementsTitle: String
    val clubViewStadiumBtn: String
    val clubInfoFallback: String
    val stadiumOfficialBadge: String
    val stadiumSpecsHeader: String
    val stadiumNameLabel: String
    val stadiumCityLabel: String
    val stadiumCapacityLabel: String
    val stadiumSurfaceLabel: String
    val stadiumPresentationHeader: String
    val stadiumDescFallback: String
    val galleryEmptyTitle: String
    val galleryEmptySub: String
    val galleryClosePreviewDesc: String

    // Contact
    val contactDetailsHeader: String
    val contactAddressLabel: String
    val contactEmailLabel: String
    val contactPhoneLabel: String
    val contactToConfigure: String
    val contactSendHeader: String
    val contactSendSub: String
    val contactNameField: String
    val contactEmailField: String
    val contactSubjectField: String
    val contactMessageField: String
    val contactErrorRequired: String
    val contactErrorEmail: String
    val contactSubmitBtn: String

    // Notifications
    val notifMarkAllRead: String
    val notifEmptyTitle: String
    val notifEmptySub: String
    val notifDeleteDesc: String

    // Settings
    val settingsAppearanceHeader: String
    val settingsThemeLight: String
    val settingsThemeLightSub: String
    val settingsThemeDark: String
    val settingsThemeDarkSub: String
    val settingsThemeSystem: String
    val settingsThemeSystemSub: String
    val settingsLanguageHeader: String
    val settingsLanguageDesc: String
    val settingsLangFr: String
    val settingsLangAr: String
    val settingsAlertsTitle: String
    val settingsAlertsDesc: String
    val settingsAdminHeader: String
    val settingsAdminDesc: String
    val settingsOpenAdminBtn: String

    // About Application
    val aboutAppName: String
    val aboutHeroTagline: String
    val aboutPresentationTitle: String
    val aboutPresentationBody: String
    val aboutDeveloperSectionTitle: String
    val aboutDeveloperName: String
    val aboutDeveloperRole: String
    val aboutDeveloperBio: String
    val aboutInfoSectionTitle: String
    val aboutInfoAppNameLabel: String
    val aboutInfoClubLabel: String
    val aboutInfoClubValue: String
    val aboutInfoVersionLabel: String
    val aboutInfoVersionUnavailable: String
    val aboutInfoCopyrightLabel: String
    val aboutInfoCopyrightValue: String

    // Admin Dashboard & Dialogs
    val adminTitle: String
    val adminSubtitle: String
    val adminLockBtn: String
    val adminStatArticles: String
    val adminStatPlayers: String
    val adminStatMatches: String
    val adminStatMedia: String
    val adminTabs: List<String>
    val adminAddNewsBtn: String
    val adminAddPlayerBtn: String
    val adminAddMatchBtn: String
    val adminAddStandingBtn: String
    val adminAddGalleryBtn: String
    val adminLogosSectionTitle: String
    val adminOfficialLogoTitle: String
    val adminOfficialLogoDesc: String
    val adminLogoStatusCustom: String
    val adminLogoStatusDefault: String
    val adminLogoChooseBtn: String
    val adminLogoReplaceBtn: String
    val adminLogoResetBtn: String
    val adminLogoPreviewTitle: String
    val adminLogoSaveBtn: String
    val adminOpponentLogosTitle: String
    val adminOpponentLogosDesc: String
    val adminOpponentLogosEmptyTitle: String
    val adminOpponentLogosEmptySub: String
    val adminOpponentLogoDefaultCode: String
    val adminOpponentLogoDialogTitle: String
    val adminOpponentLogoPickBtn: String
    val adminOpponentLogoRemoveBtn: String
    val adminLogoInvalidError: String
    val adminLogoSaveError: String
    val adminEditDesc: String
    val adminDeleteDesc: String
    val adminPublishedLabel: String
    val adminDraftLabel: String
    val adminLoginTitle: String
    val adminLoginDesc: String
    val adminPinLabel: String
    val adminDefaultPinHint: String
    val adminWrongPinError: String
    val adminUnlockBtn: String
    val adminClubEditorHeader: String
    val adminClubNameFrLabel: String
    val adminClubNameArLabel: String
    val adminStadiumNameFrLabel: String
    val adminStadiumNameArLabel: String
    val adminStadiumCapacityLabel: String
    val adminStadiumDescFrLabel: String
    val adminStadiumDescArLabel: String
    val adminHistoryFrLabel: String
    val adminHistoryArLabel: String
    val adminValuesFrLabel: String
    val adminValuesArLabel: String
    val adminAchievementsFrLabel: String
    val adminAchievementsArLabel: String
    val adminEmailLabel: String
    val adminPhoneLabel: String
    val adminSaveClubBtn: String
    val adminSecurityHeader: String
    val adminNewPinLabel: String
    val adminChangePinBtn: String
    val adminMessagesHeader: String
    val adminNoMessagesTitle: String
    val adminNoMessagesSub: String
    val dialogSave: String
    val dialogAdd: String
    val dialogCancel: String
    val dialogArabicSectionToggle: String
    val dialogNewArticle: String
    val dialogEditArticle: String
    val dialogImportCover: String
    val dialogTitleFr: String
    val dialogTitleAr: String
    val dialogSummaryFr: String
    val dialogSummaryAr: String
    val dialogContentFr: String
    val dialogContentAr: String
    val dialogPubDate: String
    val dialogCategory: String
    val dialogPublishNow: String
    val dialogFeatured: String
    val dialogErrorArticle: String
    val dialogNewPlayer: String
    val dialogEditPlayer: String
    val dialogPickPhoto: String
    val dialogChangePhoto: String
    val dialogFullNameFr: String
    val dialogFullNameAr: String
    val dialogShirtNum: String
    val dialogPositionFr: String
    val dialogPositionAr: String
    val dialogPosGroup: String
    val dialogNationalityFr: String
    val dialogNationalityAr: String
    val dialogBioFr: String
    val dialogBioAr: String
    val dialogHighlightHome: String
    val dialogErrorPlayer: String
    val dialogNewMatch: String
    val dialogEditMatch: String
    val dialogOpponentFr: String
    val dialogOpponentAr: String
    val dialogOpponentShort: String
    val dialogCompetitionFr: String
    val dialogCompetitionAr: String
    val dialogDateField: String
    val dialogTimeField: String
    val dialogDaysOffset: String
    val dialogStadiumFr: String
    val dialogStadiumAr: String
    val dialogIsHome: String
    val dialogStatusLabel: String
    val dialogScoreRcr: String
    val dialogScoreOpp: String
    val dialogShowNextMatch: String
    val dialogNotesFr: String
    val dialogNotesAr: String
    val dialogErrorMatch: String
    val dialogAddStanding: String
    val dialogEditStanding: String
    val dialogPosField: String
    val dialogCodeField: String
    val dialogClubNameFr: String
    val dialogClubNameAr: String
    val dialogHighlightRcr: String
    val dialogErrorStanding: String
    val dialogAddGallery: String
    val dialogSelectMedia: String
    val dialogGalleryTitleFr: String
    val dialogGalleryTitleAr: String
    val dialogAlbumLabel: String
    val dialogIsVideo: String
    val dialogDurationLabel: String
    val dialogErrorTitle: String

    // Snackbars
    val msgAllNotifsRead: String
    val msgContactSaved: String
    val msgAdminAuthSuccess: String
    val msgAdminLogout: String
    val msgAdminPinUpdated: String
    val msgArticleSaved: String
    val msgArticleUpdated: String
    val msgArticleDeleted: String
    val msgPlayerSaved: String
    val msgPlayerUpdated: String
    val msgPlayerDeleted: String
    val msgMatchSaved: String
    val msgMatchUpdated: String
    val msgMatchDeleted: String
    val msgStandingUpdated: String
    val msgStandingDeleted: String
    val msgGalleryAdded: String
    val msgGalleryDeleted: String
    val msgClubUpdated: String
    val msgOfficialLogoSaved: String
    val msgOfficialLogoReset: String
    val msgOpponentLogoSaved: String
    val msgOpponentLogoRemoved: String

    fun topBarText(route: String?, stadiumName: String?): Pair<String, String> = when {
        route == Routes.HOME -> clubOfficialName.uppercase() to topBarHomeSubtitle
        route == Routes.NEWS -> titleNews to subtitleNews
        route?.startsWith("news/") == true -> titleArticle to subtitleArticle
        route == Routes.MATCHES -> titleMatches to subtitleMatches
        route?.startsWith("match/") == true -> titleMatchDetail to subtitleMatchDetail
        route == Routes.TEAM -> titleTeam to subtitleTeam
        route?.startsWith("player/") == true -> titlePlayerProfile to subtitlePlayerProfile
        route == Routes.MORE -> titleMore to subtitleMore
        route == Routes.CLUB -> titleClub to clubOfficialName
        route == Routes.STANDINGS -> titleStandings to subtitleStandings
        route == Routes.GALLERY -> titleGallery to subtitleGallery
        route == Routes.STADIUM -> titleStadium to (stadiumName ?: if (isArabic) "ملعب طاهر زوغاري" else "Stade Tahar Zoughari")
        route == Routes.CONTACT -> titleContact to subtitleContact
        route == Routes.SETTINGS -> titleSettings to subtitleSettings
        route == Routes.ABOUT -> titleAbout to subtitleAbout
        route == Routes.NOTIFICATIONS -> titleNotifications to subtitleNotifications
        route == Routes.ADMIN -> titleAdmin to subtitleAdmin
        else -> clubOfficialName.uppercase() to topBarHomeSubtitle
    }

    fun localizeNewsCategory(raw: String): String = if (!isArabic) raw else when (raw) {
        "Toutes" -> "الكل"
        "Équipe A" -> "الفريق الأول"
        "Matchs" -> "المباريات"
        "Club" -> "النادي"
        "Supporters" -> "الأنصار"
        "Académie" -> "الأكاديمية"
        else -> raw
    }

    fun localizeGalleryCategory(raw: String): String = if (!isArabic) raw else when (raw) {
        "Toutes" -> "الكل"
        "Matchs" -> "المباريات"
        "Entraînement" -> "التدريبات"
        "Joueurs" -> "اللاعبون"
        "Supporters" -> "الأنصار"
        "Événements" -> "الفعاليات"
        "Vidéos" -> "فيديوهات"
        else -> raw
    }

    fun localizeMatchStatus(raw: String): String = if (!isArabic) raw else when (raw) {
        "À venir" -> "قادمة"
        "En direct" -> "مباشر"
        "Terminé" -> "انتهت"
        else -> raw
    }

    fun localizeSquadGroup(raw: String): String = if (!isArabic) raw else when (raw) {
        "Tous" -> "الكل"
        "Gardiens" -> "حراس المرمى"
        "Défenseurs" -> "المدافعون"
        "Milieux" -> "وسط الميدان"
        "Attaquants" -> "المهاجمون"
        else -> raw
    }

    fun localizeNotificationType(raw: String): String = if (!isArabic) raw else when (raw) {
        "NEWS" -> "أخبار"
        "MATCH" -> "مباراة"
        "RESULT" -> "نتيجة"
        else -> "النادي"
    }
}

object FrenchRcrStrings : RcrStrings {
    override val isArabic = false
    override val clubOfficialName = "Rapid Club de Relizane"
    override val clubShortName = "RCR"
    override val clubLocationBadge = "Relizane, Algérie • 1934"
    override val topBarHomeSubtitle = "RELIZANE • 1934"
    override val backContentDesc = "Retour"
    override val notificationsContentDesc = "Notifications"
    override val adminSpaceDesc = "Espace Admin"
    override val titleNews = "ACTUALITÉS"
    override val subtitleNews = "RCR Officiel"
    override val titleArticle = "ARTICLE"
    override val subtitleArticle = "Actualités RCR"
    override val titleMatches = "CENTRE DE MATCHS"
    override val subtitleMatches = "Calendrier & Classement"
    override val titleMatchDetail = "DÉTAILS DU MATCH"
    override val subtitleMatchDetail = "Centre de Matchs RCR"
    override val titleTeam = "ÉQUIPE PREMIÈRE"
    override val subtitleTeam = "Effectif RCR"
    override val titlePlayerProfile = "PROFIL JOUEUR"
    override val subtitlePlayerProfile = "Effectif RCR"
    override val titleMore = "PLUS"
    override val subtitleMore = "Club & Services"
    override val titleClub = "LE CLUB"
    override val subtitleClub = "Rapid Club de Relizane"
    override val titleStandings = "CLASSEMENT"
    override val subtitleStandings = "Compétition"
    override val titleGallery = "GALERIE MÉDIA"
    override val subtitleGallery = "Photos & Vidéos"
    override val titleStadium = "LE STADE"
    override val titleContact = "CONTACT"
    override val subtitleContact = "Secrétariat RCR"
    override val titleSettings = "PARAMÈTRES"
    override val subtitleSettings = "Préférences"
    override val titleAbout = "À PROPOS"
    override val subtitleAbout = "RC Relizane • Informations"
    override val titleNotifications = "NOTIFICATIONS"
    override val subtitleNotifications = "Alertes du club"
    override val titleAdmin = "ESPACE ADMIN"
    override val subtitleAdmin = "Administration RCR"
    override val heroHeadline = "ENSEMBLE,\nPLUS FORTS."
    override val heroSubtitle = "L'application officielle des supporters Vert et Blanc : actualités, calendrier, effectif et vie du club."
    override val discoverClubBtn = "Découvrir le Club"
    override val sectionNextMatch = "PROCHAIN MATCH"
    override val actionCalendar = "Calendrier"
    override val sectionLatestResults = "DERNIERS RÉSULTATS"
    override val actionSeeAll = "Voir tout"
    override val sectionLatestNews = "DERNIÈRES ACTUALITÉS"
    override val actionAllNews = "Toutes les actualités"
    override val sectionTeamHighlights = "JOUEURS À LA UNE"
    override val actionSquad = "Effectif"
    override val sectionStandingsPreview = "CLASSEMENT"
    override val actionFullTable = "Classement complet"
    override val supportersEyebrow = "TRIBUNE SUPPORTERS"
    override val supportersMotto = "Notre passion,\nnotre fierté."
    override val supportersSubtext = "Rejoignez l'histoire du Rapid Club de Relizane."
    override val shirtNumberPrefix = "N°"
    override val daysUnit = "JRS"
    override val hoursUnit = "HRS"
    override val minsUnit = "MIN"
    override val secsUnit = "SEC"
    override val vsLabel = "VS"
    override val exampleBadge = "EXEMPLE"
    override val placeholderDefaultBanner = "Contenu d'exemple affiché lorsque les données officielles n'ont pas encore été saisies dans l'Espace Admin."
    override val matchTabUpcoming = "À venir"
    override val matchTabResults = "Résultats"
    override val matchTabStandings = "Classement"
    override val matchEmptyUpcomingTitle = "Aucun match à venir"
    override val matchEmptyUpcomingSub = "Les prochains matchs ajoutés dans l'Espace Admin s'afficheront ici."
    override val matchEmptyResultsTitle = "Aucun résultat enregistré"
    override val matchEmptyResultsSub = "Les scores terminés apparaîtront ici."
    override val matchPlaceholderUpcoming = "Rencontres d'exemple — modifiables dans l'Espace Admin."
    override val matchPlaceholderResults = "Scores d'exemple — remplacez-les par les résultats officiels dans l'Espace Admin."
    override val matchNotFoundTitle = "Match introuvable"
    override val matchNotFoundSub = "Cette rencontre n'est plus disponible."
    override val matchSheetHeader = "FICHE DU MATCH"
    override val matchCompetitionLabel = "Compétition"
    override val matchDateLabel = "Date"
    override val matchKickoffLabel = "Coup d'envoi"
    override val matchStadiumLabel = "Stade"
    override val matchNotesLabel = "Informations"
    override val matchPlaceholderDetailBanner = "Rencontre d'exemple. Mettez à jour l'adversaire, le stade et le score officiel depuis l'Espace Admin."
    override val standingsEmptyTitle = "Classement indisponible"
    override val standingsEmptySub = "Ajoutez les équipes du championnat via l'Espace Admin."
    override val standingsPlaceholderBanner = "Classement d'exemple — toutes les lignes et statistiques sont éditables dans l'Espace Admin."
    override val colPos = "#"
    override val colClub = "CLUB"
    override val colPlayed = "MJ"
    override val colWins = "G"
    override val colDraws = "N"
    override val colLosses = "P"
    override val colGoalsFor = "BP"
    override val colGoalsAgainst = "BC"
    override val colDiff = "DIFF"
    override val colPoints = "PTS"
    override val standingsLegend = "MJ : Matchs joués • G : Gagnés • N : Nuls • P : Perdus • BP:BC : Buts pour/contre"
    override val searchPlaceholder = "Rechercher un article…"
    override val clearSearchDesc = "Effacer la recherche"
    override val searchIconDesc = "Rechercher"
    override val newsEmptyTitle = "Aucun article trouvé"
    override val newsEmptySub = "Essayez un autre mot-clé ou changez de catégorie."
    override val newsFeaturedHeader = "À LA UNE"
    override val newsFeaturedPrefix = "À LA UNE"
    override val newsFeedHeader = "FIL D'ACTUALITÉ"
    override val articleNotFoundTitle = "Article introuvable"
    override val articleNotFoundSub = "Cet article a peut-être été supprimé ou dépublié."
    override val articlePlaceholderBanner = "Article d'exemple modifiable ou supprimable depuis l'Espace Admin."
    override val articleFallbackNotice = ""
    override val articleRelatedHeader = "ARTICLES SIMILAIRES"
    override val squadPlaceholderBanner = "Effectif d'exemple — aucune identité de joueur non vérifiée n'est inventée. Ajoutez les joueurs officiels dans l'Espace Admin."
    override val squadEmptyTitle = "Effectif en cours de saisie"
    override val squadEmptySub = "Ajoutez les joueurs de l'équipe première via l'Espace Admin."
    override val playerNotFoundTitle = "Profil joueur introuvable"
    override val playerNotFoundSub = "Cette fiche joueur n'est plus disponible."
    override val playerPlaceholderBanner = "Fiche joueur d'exemple. Renseignez la photo, la nationalité vérifiée et les statistiques officielles via l'Espace Admin."
    override val playerStatsAvailableHeader = "STATISTIQUES DISPONIBLES"
    override val playerStatsHeader = "STATISTIQUES"
    override val playerNoStatsMsg = "Aucune statistique vérifiée disponible pour ce joueur pour le moment."
    override val statMatches = "MATCHS"
    override val statGoals = "BUTS"
    override val statAssists = "PASSES D."
    override val playerBioHeader = "BIOGRAPHIE"
    override val playerNoBioFallback = "Biographie à compléter par l'administration du club."
    override val playerNationalityPrefix = "Nationalité"
    override val moreClubTitle = "Le Club"
    override val moreClubSub = "Histoire, identité, valeurs & palmarès"
    override val moreStandingsTitle = "Classement"
    override val moreStandingsSub = "Tableau complet de la compétition"
    override val moreGalleryTitle = "Galerie"
    override val moreGallerySub = "Photos des matchs, entraînements & vidéos"
    override val moreStadiumTitle = "Stade"
    override val moreStadiumSub = "Stade Tahar Zoughari • Relizane"
    override val moreContactTitle = "Contact"
    override val moreContactSub = "Contacter le secrétariat du RCR"
    override val moreSettingsTitle = "Paramètres"
    override val moreSettingsSub = "Thème, langue / RTL & notifications"
    override val moreAboutTitle = "À propos de l'application"
    override val moreAboutSub = "Présentation, développeur & version"
    override val moreAdminTitle = "Espace Admin"
    override val moreAdminSubActive = "Session administrateur active"
    override val moreAdminSubLocked = "Gestion sécurisée du contenu du club"
    override val foundedPrefix = "Fondé en"
    override val clubHistoryTitle = "HISTOIRE DU CLUB"
    override val clubIdentityTitle = "IDENTITÉ & TRADITIONS"
    override val clubValuesTitle = "NOS VALEURS"
    override val clubSupportersTitle = "SUPPORTERS"
    override val clubAchievementsTitle = "PALMARÈS"
    override val clubViewStadiumBtn = "Voir la fiche du Stade"
    override val clubInfoFallback = "Information à compléter par l'administration du club."
    override val stadiumOfficialBadge = "STADE OFFICIEL"
    override val stadiumSpecsHeader = "CARACTÉRISTIQUES"
    override val stadiumNameLabel = "Nom du stade"
    override val stadiumCityLabel = "Ville"
    override val stadiumCapacityLabel = "Capacité"
    override val stadiumSurfaceLabel = "Surface"
    override val stadiumPresentationHeader = "PRÉSENTATION"
    override val stadiumDescFallback = "Description du stade à compléter depuis l'Espace Admin."
    override val galleryEmptyTitle = "Aucun média dans cette catégorie"
    override val galleryEmptySub = "Les administrateurs peuvent importer des photos et vidéos depuis l'Espace Admin."
    override val galleryClosePreviewDesc = "Fermer l'aperçu"
    override val contactDetailsHeader = "COORDONNÉES DU CLUB"
    override val contactAddressLabel = "Adresse"
    override val contactEmailLabel = "E-mail"
    override val contactPhoneLabel = "Téléphone"
    override val contactToConfigure = "À configurer"
    override val contactSendHeader = "ENVOYER UN MESSAGE"
    override val contactSendSub = "Votre message est transmis et consultable dans le tableau de bord administratif du club."
    override val contactNameField = "Nom complet *"
    override val contactEmailField = "Adresse e-mail *"
    override val contactSubjectField = "Objet *"
    override val contactMessageField = "Message *"
    override val contactErrorRequired = "Veuillez remplir tous les champs obligatoires."
    override val contactErrorEmail = "Veuillez saisir une adresse e-mail valide."
    override val contactSubmitBtn = "Envoyer le message"
    override val notifMarkAllRead = "Tout marquer comme lu"
    override val notifEmptyTitle = "Aucune notification"
    override val notifEmptySub = "Les annonces du club, nouveaux articles et résultats apparaîtront ici."
    override val notifDeleteDesc = "Supprimer la notification"
    override val settingsAppearanceHeader = "APPARENCE"
    override val settingsThemeLight = "Mode Clair (Vert / Blanc)"
    override val settingsThemeLightSub = "Officiel RCR — Thème principal"
    override val settingsThemeDark = "Mode Sombre (Vert / Noir)"
    override val settingsThemeDarkSub = "Alternative — Thème sombre du club"
    override val settingsThemeSystem = "Suivre le système"
    override val settingsThemeSystemSub = "Adapter automatiquement selon l'appareil"
    override val settingsLanguageHeader = "LANGUE & SUPPORT RTL"
    override val settingsLanguageDesc = "Basculez entre le français (LTR) et l'arabe (RTL). Le changement s'applique immédiatement à toute l'application."
    override val settingsLangFr = "Français (LTR)"
    override val settingsLangAr = "العربية (RTL — من اليمين إلى اليسار)"
    override val settingsAlertsTitle = "Alertes in-app du club"
    override val settingsAlertsDesc = "Afficher le badge des annonces, matchs et résultats dans l'application."
    override val settingsAdminHeader = "ADMINISTRATION"
    override val settingsAdminDesc = "Accédez au tableau de bord protégé pour publier les actualités, gérer l'effectif, les matchs, le classement et la galerie."
    override val settingsOpenAdminBtn = "Ouvrir l'Espace Admin"
    override val aboutAppName = "RC Relizane"
    override val aboutHeroTagline = "Application officielle des supporters • Fondé en 1934"
    override val aboutPresentationTitle = "À propos de l'application"
    override val aboutPresentationBody = "Cette application est le fruit d'une passion pour le football et d'un attachement particulier au Rapid Club de Relizane. Conçue et développée par Mohamed Ala Eddine, elle a pour objectif de rapprocher le club de ses supporters et de réunir son actualité, ses résultats et son histoire dans un seul espace numérique."
    override val aboutDeveloperSectionTitle = "Développeur"
    override val aboutDeveloperName = "Mohamed Ala Eddine"
    override val aboutDeveloperRole = "Conception & Développement"
    override val aboutDeveloperBio = "Cette application a été conçue et développée par Mohamed Ala Eddine, avec la volonté de proposer aux supporters du Rapid Club de Relizane une expérience numérique moderne, pratique et complète."
    override val aboutInfoSectionTitle = "Informations sur l'application"
    override val aboutInfoAppNameLabel = "Nom de l'application"
    override val aboutInfoClubLabel = "Club"
    override val aboutInfoClubValue = "Rapid Club de Relizane"
    override val aboutInfoVersionLabel = "Version de l'application"
    override val aboutInfoVersionUnavailable = "Non disponible"
    override val aboutInfoCopyrightLabel = "Copyright"
    override val aboutInfoCopyrightValue = "© Mohamed Ala Eddine"
    override val adminTitle = "TABLEAU DE BORD ADMIN"
    override val adminSubtitle = "Gestion complète des données RCR"
    override val adminLockBtn = "Verrouiller"
    override val adminStatArticles = "Articles"
    override val adminStatPlayers = "Joueurs"
    override val adminStatMatches = "Matchs"
    override val adminStatMedia = "Médias"
    override val adminTabs = listOf("Actualités", "Équipe", "Matchs", "Classement", "Galerie", "Logos", "Club & Sécurité")
    override val adminAddNewsBtn = "Publier ou créer un article"
    override val adminAddPlayerBtn = "Ajouter un joueur à l'effectif"
    override val adminAddMatchBtn = "Créer un match ou résultat"
    override val adminAddStandingBtn = "Ajouter une équipe au classement"
    override val adminAddGalleryBtn = "Importer photo ou vidéo"
    override val adminLogosSectionTitle = "GESTION DES LOGOS"
    override val adminOfficialLogoTitle = "Logo officiel du club"
    override val adminOfficialLogoDesc = "Le logo actif s'applique automatiquement dans toute l'application (barre supérieure, accueil, matchs, classement, club). L'icône du lanceur et l'écran de démarrage natif restent inchangés."
    override val adminLogoStatusCustom = "Logo personnalisé actif"
    override val adminLogoStatusDefault = "Blason officiel intégré par défaut"
    override val adminLogoChooseBtn = "Importer un logo"
    override val adminLogoReplaceBtn = "Remplacer le logo"
    override val adminLogoResetBtn = "Rétablir par défaut"
    override val adminLogoPreviewTitle = "Aperçu avant enregistrement"
    override val adminLogoSaveBtn = "Enregistrer le logo"
    override val adminOpponentLogosTitle = "Logos des équipes adverses"
    override val adminOpponentLogosDesc = "Chaque logo est lié à son club et synchronisé entre les cartes de match, détails de rencontre et tableau de classement."
    override val adminOpponentLogosEmptyTitle = "Aucun club adverse enregistré"
    override val adminOpponentLogosEmptySub = "Ajoutez un match ou une équipe adverse dans le classement pour configurer son logo."
    override val adminOpponentLogoDefaultCode = "Abréviation par défaut"
    override val adminOpponentLogoDialogTitle = "Logo du club adverse"
    override val adminOpponentLogoPickBtn = "Sélectionner une image"
    override val adminOpponentLogoRemoveBtn = "Supprimer le logo"
    override val adminLogoInvalidError = "Image invalide ou illisible. Veuillez choisir un autre fichier image."
    override val adminLogoSaveError = "Échec de l'enregistrement du logo. Le logo précédent a été conservé."
    override val adminEditDesc = "Modifier"
    override val adminDeleteDesc = "Supprimer"
    override val adminPublishedLabel = "Publié"
    override val adminDraftLabel = "Brouillon"
    override val adminLoginTitle = "ESPACE ADMINISTRATEUR"
    override val adminLoginDesc = "Accès réservé aux administrateurs autorisés du Rapid Club de Relizane."
    override val adminPinLabel = "Code PIN administrateur"
    override val adminDefaultPinHint = "Code PIN initial par défaut : 1934 (modifiable après connexion dans Club & Sécurité)."
    override val adminWrongPinError = "Code PIN incorrect."
    override val adminUnlockBtn = "Déverrouiller le tableau de bord"
    override val adminClubEditorHeader = "INFORMATIONS CLUB & STADE"
    override val adminClubNameFrLabel = "Nom officiel (Français)"
    override val adminClubNameArLabel = "Nom officiel (Arabe — optionnel)"
    override val adminStadiumNameFrLabel = "Nom du stade (Français)"
    override val adminStadiumNameArLabel = "Nom du stade (Arabe — optionnel)"
    override val adminStadiumCapacityLabel = "Capacité du stade"
    override val adminStadiumDescFrLabel = "Description du stade (Français)"
    override val adminStadiumDescArLabel = "Description du stade (Arabe — optionnel)"
    override val adminHistoryFrLabel = "Histoire du club (Français)"
    override val adminHistoryArLabel = "Histoire du club (Arabe — optionnel)"
    override val adminValuesFrLabel = "Valeurs du club (Français)"
    override val adminValuesArLabel = "Valeurs du club (Arabe — optionnel)"
    override val adminAchievementsFrLabel = "Palmarès officiel (Français)"
    override val adminAchievementsArLabel = "Palmarès officiel (Arabe — optionnel)"
    override val adminEmailLabel = "E-mail officiel"
    override val adminPhoneLabel = "Téléphone"
    override val adminSaveClubBtn = "Mettre à jour les infos du club"
    override val adminSecurityHeader = "SÉCURITÉ ADMIN"
    override val adminNewPinLabel = "Nouveau code PIN (min. 4 chiffres)"
    override val adminChangePinBtn = "Modifier le code PIN"
    override val adminMessagesHeader = "MESSAGES SUPPORTERS REÇUS"
    override val adminNoMessagesTitle = "Aucun message reçu"
    override val adminNoMessagesSub = "Les demandes envoyées depuis l'écran Contact apparaîtront ici."
    override val dialogSave = "Enregistrer"
    override val dialogAdd = "Ajouter"
    override val dialogCancel = "Annuler"
    override val dialogArabicSectionToggle = "Version arabe (العربية — optionnel)"
    override val dialogNewArticle = "Nouvel article"
    override val dialogEditArticle = "Modifier l'article"
    override val dialogImportCover = "Importer une image de couverture"
    override val dialogTitleFr = "Titre (Français) *"
    override val dialogTitleAr = "Titre (Arabe — optionnel)"
    override val dialogSummaryFr = "Résumé (Français) *"
    override val dialogSummaryAr = "Résumé (Arabe — optionnel)"
    override val dialogContentFr = "Contenu complet (Français) *"
    override val dialogContentAr = "Contenu complet (Arabe — optionnel)"
    override val dialogPubDate = "Date de publication"
    override val dialogCategory = "Catégorie"
    override val dialogPublishNow = "Publier immédiatement (sinon Brouillon)"
    override val dialogFeatured = "Mettre à la Une"
    override val dialogErrorArticle = "Titre, résumé et contenu requis."
    override val dialogNewPlayer = "Ajouter un joueur"
    override val dialogEditPlayer = "Modifier le joueur"
    override val dialogPickPhoto = "Choisir une photo"
    override val dialogChangePhoto = "Photo sélectionnée (changer)"
    override val dialogFullNameFr = "Nom complet (Français) *"
    override val dialogFullNameAr = "Nom complet (Arabe — optionnel)"
    override val dialogShirtNum = "Numéro de maillot *"
    override val dialogPositionFr = "Poste précis (Français) *"
    override val dialogPositionAr = "Poste précis (Arabe — optionnel)"
    override val dialogPosGroup = "Ligne / Catégorie"
    override val dialogNationalityFr = "Nationalité (Français, optionnel)"
    override val dialogNationalityAr = "Nationalité (Arabe, optionnel)"
    override val dialogBioFr = "Biographie (Français)"
    override val dialogBioAr = "Biographie (Arabe — optionnel)"
    override val dialogHighlightHome = "Mettre en avant sur l'Accueil"
    override val dialogErrorPlayer = "Nom, numéro et poste requis."
    override val dialogNewMatch = "Nouveau match"
    override val dialogEditMatch = "Modifier le match"
    override val dialogOpponentFr = "Adversaire (Français) *"
    override val dialogOpponentAr = "Adversaire (Arabe — optionnel)"
    override val dialogOpponentShort = "Abréviation (3 lettres) *"
    override val dialogCompetitionFr = "Compétition (Français) *"
    override val dialogCompetitionAr = "Compétition (Arabe — optionnel)"
    override val dialogDateField = "Date"
    override val dialogTimeField = "Heure"
    override val dialogDaysOffset = "Compte à rebours (dans X jours)"
    override val dialogStadiumFr = "Stade (Français)"
    override val dialogStadiumAr = "Stade (Arabe — optionnel)"
    override val dialogIsHome = "Match à domicile (RCR reçoit)"
    override val dialogStatusLabel = "Statut"
    override val dialogScoreRcr = "Score RCR"
    override val dialogScoreOpp = "Score Adv."
    override val dialogShowNextMatch = "Afficher comme Prochain Match sur l'Accueil"
    override val dialogNotesFr = "Notes / Détails (Français)"
    override val dialogNotesAr = "Notes / Détails (Arabe — optionnel)"
    override val dialogErrorMatch = "Adversaire, abréviation et compétition requis."
    override val dialogAddStanding = "Ajouter au classement"
    override val dialogEditStanding = "Modifier le classement"
    override val dialogPosField = "Pos *"
    override val dialogCodeField = "Code *"
    override val dialogClubNameFr = "Nom du club (Français) *"
    override val dialogClubNameAr = "Nom du club (Arabe — optionnel)"
    override val dialogHighlightRcr = "Mettre en surbrillance comme RCR"
    override val dialogErrorStanding = "Nom et code du club requis."
    override val dialogAddGallery = "Ajouter à la galerie"
    override val dialogSelectMedia = "Sélectionner depuis l'appareil"
    override val dialogGalleryTitleFr = "Légende / Titre (Français) *"
    override val dialogGalleryTitleAr = "Légende / Titre (Arabe — optionnel)"
    override val dialogAlbumLabel = "Album / Catégorie"
    override val dialogIsVideo = "Élément vidéo"
    override val dialogDurationLabel = "Durée (ex. 02:15)"
    override val dialogErrorTitle = "Veuillez saisir un titre."
    override val msgAllNotifsRead = "Toutes les notifications ont été marquées comme lues."
    override val msgContactSaved = "Message enregistré pour le secrétariat du RCR."
    override val msgAdminAuthSuccess = "Session administrateur activée."
    override val msgAdminLogout = "Déconnexion de l'Espace Admin."
    override val msgAdminPinUpdated = "Code PIN administrateur mis à jour."
    override val msgArticleSaved = "Article enregistré."
    override val msgArticleUpdated = "Article mis à jour."
    override val msgArticleDeleted = "Article supprimé."
    override val msgPlayerSaved = "Joueur ajouté à l'effectif."
    override val msgPlayerUpdated = "Fiche joueur mise à jour."
    override val msgPlayerDeleted = "Joueur retiré de l'effectif."
    override val msgMatchSaved = "Match ajouté."
    override val msgMatchUpdated = "Match mis à jour."
    override val msgMatchDeleted = "Match supprimé."
    override val msgStandingUpdated = "Classement mis à jour."
    override val msgStandingDeleted = "Ligne supprimée du classement."
    override val msgGalleryAdded = "Média ajouté à la galerie."
    override val msgGalleryDeleted = "Média supprimé."
    override val msgClubUpdated = "Informations du club enregistrées."
    override val msgOfficialLogoSaved = "Logo officiel du RCR mis à jour."
    override val msgOfficialLogoReset = "Blason officiel par défaut rétabli."
    override val msgOpponentLogoSaved = "Logo du club adverse enregistré."
    override val msgOpponentLogoRemoved = "Logo supprimé — abréviation rétablie."
}

object ArabicRcrStrings : RcrStrings {
    override val isArabic = true
    override val clubOfficialName = "سريع غليزان"
    override val clubShortName = "RCR"
    override val clubLocationBadge = "غليزان، الجزائر • 1934"
    override val topBarHomeSubtitle = "غليزان • 1934"
    override val backContentDesc = "رجوع"
    override val notificationsContentDesc = "الإشعارات"
    override val adminSpaceDesc = "فضاء الإدارة"
    override val titleNews = "الأخبار"
    override val subtitleNews = "سريع غليزان الرسمي"
    override val titleArticle = "المقال"
    override val subtitleArticle = "أخبار سريع غليزان"
    override val titleMatches = "مركز المباريات"
    override val subtitleMatches = "الجدول والترتيب"
    override val titleMatchDetail = "تفاصيل المباراة"
    override val subtitleMatchDetail = "مركز مباريات سريع غليزان"
    override val titleTeam = "الفريق الأول"
    override val subtitleTeam = "تشكيلة سريع غليزان"
    override val titlePlayerProfile = "ملف اللاعب"
    override val subtitlePlayerProfile = "تشكيلة سريع غليزان"
    override val titleMore = "المزيد"
    override val subtitleMore = "النادي والخدمات"
    override val titleClub = "النادي"
    override val subtitleClub = "سريع غليزان"
    override val titleStandings = "الترتيب"
    override val subtitleStandings = "المسابقة"
    override val titleGallery = "المعرض"
    override val subtitleGallery = "الصور والفيديوهات"
    override val titleStadium = "الملعب"
    override val titleContact = "اتصل بنا"
    override val subtitleContact = "إدارة سريع غليزان"
    override val titleSettings = "الإعدادات"
    override val subtitleSettings = "التفضيلات"
    override val titleAbout = "حول التطبيق"
    override val subtitleAbout = "سريع غليزان • معلومات التطبيق"
    override val titleNotifications = "الإشعارات"
    override val subtitleNotifications = "تنبيهات النادي"
    override val titleAdmin = "فضاء الإدارة"
    override val subtitleAdmin = "إدارة محتوى سريع غليزان"
    override val heroHeadline = "معاً،\nأقوى."
    override val heroSubtitle = "التطبيق الرسمي لأنصار الأخضر والأبيض: آخر الأخبار، جدول المباريات، التشكيلة وحياة النادي."
    override val discoverClubBtn = "اكتشف النادي"
    override val sectionNextMatch = "المباراة القادمة"
    override val actionCalendar = "الجدول"
    override val sectionLatestResults = "أحدث النتائج"
    override val actionSeeAll = "عرض الكل"
    override val sectionLatestNews = "آخر الأخبار"
    override val actionAllNews = "كل الأخبار"
    override val sectionTeamHighlights = "نجوم الفريق"
    override val actionSquad = "التشكيلة"
    override val sectionStandingsPreview = "الترتيب"
    override val actionFullTable = "الترتيب الكامل"
    override val supportersEyebrow = "مدرجات الأنصار"
    override val supportersMotto = "شغفنا،\nفخرنا."
    override val supportersSubtext = "انضم إلى مسيرة وتاريخ سريع غليزان."
    override val shirtNumberPrefix = "رقم"
    override val daysUnit = "أيام"
    override val hoursUnit = "ساعات"
    override val minsUnit = "دقائق"
    override val secsUnit = "ثواني"
    override val vsLabel = "ضد"
    override val exampleBadge = "نموذج"
    override val placeholderDefaultBanner = "محتوى تجريبي معروض إلى حين إدخال البيانات الرسمية عبر فضاء الإدارة."
    override val matchTabUpcoming = "القادمة"
    override val matchTabResults = "النتائج"
    override val matchTabStandings = "الترتيب"
    override val matchEmptyUpcomingTitle = "لا توجد مباريات قادمة"
    override val matchEmptyUpcomingSub = "ستظهر المباريات المضافة عبر فضاء الإدارة هنا."
    override val matchEmptyResultsTitle = "لا توجد نتائج مسجلة"
    override val matchEmptyResultsSub = "ستظهر النتائج المنتهية هنا."
    override val matchPlaceholderUpcoming = "مباريات تجريبية — قابلة للتعديل عبر فضاء الإدارة."
    override val matchPlaceholderResults = "نتائج تجريبية — استبدلها بالنتائج الرسمية عبر فضاء الإدارة."
    override val matchNotFoundTitle = "المباراة غير موجودة"
    override val matchNotFoundSub = "لم تعد هذه المواجهة متاحة."
    override val matchSheetHeader = "بطاقة المباراة"
    override val matchCompetitionLabel = "المسابقة"
    override val matchDateLabel = "التاريخ"
    override val matchKickoffLabel = "صافرة البداية"
    override val matchStadiumLabel = "الملعب"
    override val matchNotesLabel = "معلومات"
    override val matchPlaceholderDetailBanner = "مواجهة تجريبية. يمكنك تحديث المنافس والملعب والنتيجة الرسمية عبر فضاء الإدارة."
    override val standingsEmptyTitle = "الترتيب غير متوفر"
    override val standingsEmptySub = "أضف فرق البطولة عبر فضاء الإدارة."
    override val standingsPlaceholderBanner = "جدول ترتيب تجريبي — جميع الصفوف والإحصائيات قابلة للتعديل من فضاء الإدارة."
    override val colPos = "#"
    override val colClub = "النادي"
    override val colPlayed = "لعب"
    override val colWins = "ف"
    override val colDraws = "ت"
    override val colLosses = "خ"
    override val colGoalsFor = "له"
    override val colGoalsAgainst = "عليه"
    override val colDiff = "فارق"
    override val colPoints = "نقاط"
    override val standingsLegend = "لعب: المباريات • ف: فوز • ت: تعادل • خ: خسارة • له:عليه: الأهداف"
    override val searchPlaceholder = "ابحث عن مقال…"
    override val clearSearchDesc = "مسح البحث"
    override val searchIconDesc = "بحث"
    override val newsEmptyTitle = "لم يتم العثور على مقالات"
    override val newsEmptySub = "جرّب كلمة مفتاحية أخرى أو غيّر التصنيف."
    override val newsFeaturedHeader = "في الواجهة"
    override val newsFeaturedPrefix = "في الواجهة"
    override val newsFeedHeader = "آخر الأخبار"
    override val articleNotFoundTitle = "المقال غير موجود"
    override val articleNotFoundSub = "ربما تم حذف هذا المقال أو إلغاء نشره."
    override val articlePlaceholderBanner = "مقال تجريبي قابل للتعديل أو الحذف عبر فضاء الإدارة."
    override val articleFallbackNotice = "النسخة العربية لهذا المقال غير متوفرة حالياً — يتم عرض النص الأصلي."
    override val articleRelatedHeader = "مقالات ذات صلة"
    override val squadPlaceholderBanner = "تشكيلة تجريبية — لا يتم اختراع هويات لاعبين غير موثقة. أضف اللاعبين الرسميين عبر فضاء الإدارة."
    override val squadEmptyTitle = "التشكيلة قيد الإعداد"
    override val squadEmptySub = "أضف لاعبي الفريق الأول عبر فضاء الإدارة."
    override val playerNotFoundTitle = "ملف اللاعب غير موجود"
    override val playerNotFoundSub = "لم تعد بطاقة هذا اللاعب متاحة."
    override val playerPlaceholderBanner = "بطاقة لاعب تجريبية. أضف الصورة والجنسية الموثقة والإحصائيات الرسمية عبر فضاء الإدارة."
    override val playerStatsAvailableHeader = "الإحصائيات المتوفرة"
    override val playerStatsHeader = "الإحصائيات"
    override val playerNoStatsMsg = "لا توجد إحصائيات موثقة متوفرة لهذا اللاعب حالياً."
    override val statMatches = "المباريات"
    override val statGoals = "الأهداف"
    override val statAssists = "التمريرات"
    override val playerBioHeader = "السيرة الذاتية"
    override val playerNoBioFallback = "السيرة الذاتية قيد الاستكمال من طرف إدارة النادي."
    override val playerNationalityPrefix = "الجنسية"
    override val moreClubTitle = "النادي"
    override val moreClubSub = "التاريخ، الهوية، القيم والألقاب"
    override val moreStandingsTitle = "الترتيب"
    override val moreStandingsSub = "الجدول الكامل للمسابقة"
    override val moreGalleryTitle = "المعرض"
    override val moreGallerySub = "صور المباريات والتدريبات والفيديوهات"
    override val moreStadiumTitle = "الملعب"
    override val moreStadiumSub = "ملعب طاهر زوغاري • غليزان"
    override val moreContactTitle = "اتصل بنا"
    override val moreContactSub = "التواصل مع إدارة سريع غليزان"
    override val moreSettingsTitle = "الإعدادات"
    override val moreSettingsSub = "المظهر، اللغة / الاتجاه والتنبيهات"
    override val moreAboutTitle = "حول التطبيق"
    override val moreAboutSub = "تعريف التطبيق، المطور والإصدار"
    override val moreAdminTitle = "فضاء الإدارة"
    override val moreAdminSubActive = "جلسة الإدارة مفعّلة"
    override val moreAdminSubLocked = "إدارة آمنة لمحتوى النادي"
    override val foundedPrefix = "تأسس سنة"
    override val clubHistoryTitle = "تاريخ النادي"
    override val clubIdentityTitle = "الهوية والتقاليد"
    override val clubValuesTitle = "قيمنا"
    override val clubSupportersTitle = "الأنصار"
    override val clubAchievementsTitle = "سجل الألقاب"
    override val clubViewStadiumBtn = "عرض بطاقة الملعب"
    override val clubInfoFallback = "معلومات قيد الاستكمال من طرف إدارة النادي."
    override val stadiumOfficialBadge = "الملعب الرسمي"
    override val stadiumSpecsHeader = "المواصفات"
    override val stadiumNameLabel = "اسم الملعب"
    override val stadiumCityLabel = "المدينة"
    override val stadiumCapacityLabel = "السعة"
    override val stadiumSurfaceLabel = "الأرضية"
    override val stadiumPresentationHeader = "تقديم الملعب"
    override val stadiumDescFallback = "وصف الملعب قيد الاستكمال عبر فضاء الإدارة."
    override val galleryEmptyTitle = "لا توجد وسائط في هذا التصنيف"
    override val galleryEmptySub = "يمكن للمسؤولين استيراد الصور والفيديوهات عبر فضاء الإدارة."
    override val galleryClosePreviewDesc = "إغلاق المعاينة"
    override val contactDetailsHeader = "بيانات التواصل مع النادي"
    override val contactAddressLabel = "العنوان"
    override val contactEmailLabel = "البريد الإلكتروني"
    override val contactPhoneLabel = "الهاتف"
    override val contactToConfigure = "قيد الضبط"
    override val contactSendHeader = "إرسال رسالة"
    override val contactSendSub = "تُحفظ رسالتك وتُعرض مباشرة في لوحة تحكم إدارة النادي."
    override val contactNameField = "الاسم الكامل *"
    override val contactEmailField = "البريد الإلكتروني *"
    override val contactSubjectField = "الموضوع *"
    override val contactMessageField = "الرسالة *"
    override val contactErrorRequired = "يرجى ملء جميع الحقول المطلوبة."
    override val contactErrorEmail = "يرجى إدخال بريد إلكتروني صحيح."
    override val contactSubmitBtn = "إرسال الرسالة"
    override val notifMarkAllRead = "تحديد الكل كمقروء"
    override val notifEmptyTitle = "لا توجد إشعارات"
    override val notifEmptySub = "ستظهر هنا إعلانات النادي والمقالات الجديدة والنتائج."
    override val notifDeleteDesc = "حذف الإشعار"
    override val settingsAppearanceHeader = "المظهر"
    override val settingsThemeLight = "Mode Clair (Vert / Blanc) — الوضع الفاتح (أخضر / أبيض)"
    override val settingsThemeLightSub = "المظهر الرسمي لسريع غليزان"
    override val settingsThemeDark = "Mode Sombre (Vert / Noir) — الوضع الداكن (أخضر / أسود)"
    override val settingsThemeDarkSub = "المظهر البديل للنادي"
    override val settingsThemeSystem = "اتباع إعدادات النظام"
    override val settingsThemeSystemSub = "التكيّف التلقائي مع مظهر الجهاز"
    override val settingsLanguageHeader = "اللغة ودعم الاتجاه RTL"
    override val settingsLanguageDesc = "بدّل بين الفرنسية (من اليسار لليمين) والعربية (من اليمين لليسار). يُطبّق التغيير فوراً على كامل التطبيق."
    override val settingsLangFr = "Français (LTR)"
    override val settingsLangAr = "العربية (RTL — من اليمين إلى اليسار)"
    override val settingsAlertsTitle = "تنبيهات النادي داخل التطبيق"
    override val settingsAlertsDesc = "إظهار شارة الإعلانات والمباريات والنتائج داخل التطبيق."
    override val settingsAdminHeader = "الإدارة"
    override val settingsAdminDesc = "افتح لوحة التحكم المحمية لنشر الأخبار وإدارة التشكيلة والمباريات والترتيب والمعرض."
    override val settingsOpenAdminBtn = "فتح فضاء الإدارة"
    override val aboutAppName = "RC Relizane"
    override val aboutHeroTagline = "التطبيق الرسمي لأنصار سريع غليزان • تأسس سنة 1934"
    override val aboutPresentationTitle = "حول التطبيق"
    override val aboutPresentationBody = "هذا التطبيق ثمرة شغف بكرة القدم وارتباط خاص بنادي سريع غليزان. صمّمه وطوّره محمد علاء الدين بهدف تقريب النادي من جماهيره، وجمع أخباره ونتائجه وتاريخه في فضاء رقمي واحد."
    override val aboutDeveloperSectionTitle = "المطوّر"
    override val aboutDeveloperName = "محمد علاء الدين (Mohamed Ala Eddine)"
    override val aboutDeveloperRole = "تصميم وتطوير"
    override val aboutDeveloperBio = "تم تصميم وتطوير هذا التطبيق من طرف محمد علاء الدين، بهدف تقديم تجربة رقمية حديثة وعملية ومتكاملة لجماهير نادي سريع غليزان."
    override val aboutInfoSectionTitle = "معلومات التطبيق"
    override val aboutInfoAppNameLabel = "اسم التطبيق"
    override val aboutInfoClubLabel = "النادي"
    override val aboutInfoClubValue = "نادي سريع غليزان"
    override val aboutInfoVersionLabel = "إصدار التطبيق"
    override val aboutInfoVersionUnavailable = "غير متوفر"
    override val aboutInfoCopyrightLabel = "حقوق النشر"
    override val aboutInfoCopyrightValue = "© Mohamed Ala Eddine"
    override val adminTitle = "لوحة تحكم الإدارة"
    override val adminSubtitle = "إدارة كاملة لبيانات سريع غليزان"
    override val adminLockBtn = "قفل"
    override val adminStatArticles = "المقالات"
    override val adminStatPlayers = "اللاعبون"
    override val adminStatMatches = "المباريات"
    override val adminStatMedia = "الوسائط"
    override val adminTabs = listOf("الأخبار", "الفريق", "المباريات", "الترتيب", "المعرض", "الشعارات", "النادي والأمان")
    override val adminAddNewsBtn = "نشر أو إنشاء مقال جديد"
    override val adminAddPlayerBtn = "إضافة لاعب إلى التشكيلة"
    override val adminAddMatchBtn = "إضافة مباراة أو نتيجة"
    override val adminAddStandingBtn = "إضافة فريق إلى الترتيب"
    override val adminAddGalleryBtn = "استيراد صورة أو فيديو"
    override val adminLogosSectionTitle = "إدارة الشعارات"
    override val adminOfficialLogoTitle = "الشعار الرسمي للنادي"
    override val adminOfficialLogoDesc = "يُطبّق الشعار المعتمد تلقائياً في كامل التطبيق (الشريط العلوي، الرئيسية، المباريات، الترتيب والنادي). تبقى أيقونة التطبيق وشاشة البدء الأصلية دون تغيير."
    override val adminLogoStatusCustom = "شعار مخصص مفعّل"
    override val adminLogoStatusDefault = "الشعار الرسمي الافتراضي"
    override val adminLogoChooseBtn = "استيراد شعار"
    override val adminLogoReplaceBtn = "استبدال الشعار"
    override val adminLogoResetBtn = "استعادة الافتراضي"
    override val adminLogoPreviewTitle = "معاينة قبل الحفظ"
    override val adminLogoSaveBtn = "حفظ الشعار"
    override val adminOpponentLogosTitle = "شعارات الفرق المنافسة"
    override val adminOpponentLogosDesc = "يرتبط كل شعار بالنادي المعني ويُعرض موحداً في بطاقات المباريات وتفاصيل اللقاء وجدول الترتيب."
    override val adminOpponentLogosEmptyTitle = "لا توجد فرق منافسة مسجلة"
    override val adminOpponentLogosEmptySub = "أضف مباراة أو فريقاً منافساً في الترتيب لتتمكن من تعيين شعاره."
    override val adminOpponentLogoDefaultCode = "الاختصار الافتراضي"
    override val adminOpponentLogoDialogTitle = "شعار الفريق المنافس"
    override val adminOpponentLogoPickBtn = "اختيار صورة"
    override val adminOpponentLogoRemoveBtn = "إزالة الشعار"
    override val adminLogoInvalidError = "ملف الصورة غير صالح أو تعذر قراءته. يرجى اختيار صورة أخرى."
    override val adminLogoSaveError = "تعذر حفظ الشعار. تم الإبقاء على الشعار السابق."
    override val adminEditDesc = "تعديل"
    override val adminDeleteDesc = "حذف"
    override val adminPublishedLabel = "منشور"
    override val adminDraftLabel = "مسودة"
    override val adminLoginTitle = "فضاء الإدارة"
    override val adminLoginDesc = "دخول مخصص للمسؤولين المخولين في نادي سريع غليزان."
    override val adminPinLabel = "رمز PIN للإدارة"
    override val adminDefaultPinHint = "رمز PIN الافتراضي الأولي: 1934 (يمكن تغييره بعد الدخول من تبويب النادي والأمان)."
    override val adminWrongPinError = "رمز PIN غير صحيح."
    override val adminUnlockBtn = "فتح لوحة التحكم"
    override val adminClubEditorHeader = "معلومات النادي والملعب"
    override val adminClubNameFrLabel = "الاسم الرسمي (بالفرنسية)"
    override val adminClubNameArLabel = "الاسم الرسمي (بالعربية — اختياري)"
    override val adminStadiumNameFrLabel = "اسم الملعب (بالفرنسية)"
    override val adminStadiumNameArLabel = "اسم الملعب (بالعربية — اختياري)"
    override val adminStadiumCapacityLabel = "سعة الملعب"
    override val adminStadiumDescFrLabel = "وصف الملعب (بالفرنسية)"
    override val adminStadiumDescArLabel = "وصف الملعب (بالعربية — اختياري)"
    override val adminHistoryFrLabel = "تاريخ النادي (بالفرنسية)"
    override val adminHistoryArLabel = "تاريخ النادي (بالعربية — اختياري)"
    override val adminValuesFrLabel = "قيم النادي (بالفرنسية)"
    override val adminValuesArLabel = "قيم النادي (بالعربية — اختياري)"
    override val adminAchievementsFrLabel = "سجل الألقاب (بالفرنسية)"
    override val adminAchievementsArLabel = "سجل الألقاب (بالعربية — اختياري)"
    override val adminEmailLabel = "البريد الإلكتروني الرسمي"
    override val adminPhoneLabel = "رقم الهاتف"
    override val adminSaveClubBtn = "تحديث معلومات النادي"
    override val adminSecurityHeader = "أمان الإدارة"
    override val adminNewPinLabel = "رمز PIN جديد (4 أرقام على الأقل)"
    override val adminChangePinBtn = "تغيير رمز PIN"
    override val adminMessagesHeader = "رسائل الأنصار المستلمة"
    override val adminNoMessagesTitle = "لا توجد رسائل مستلمة"
    override val adminNoMessagesSub = "ستظهر هنا الرسائل المرسلة من صفحة اتصل بنا."
    override val dialogSave = "حفظ"
    override val dialogAdd = "إضافة"
    override val dialogCancel = "إلغاء"
    override val dialogArabicSectionToggle = "النسخة العربية (اختياري)"
    override val dialogNewArticle = "مقال جديد"
    override val dialogEditArticle = "تعديل المقال"
    override val dialogImportCover = "استيراد صورة الغلاف"
    override val dialogTitleFr = "العنوان (بالفرنسية) *"
    override val dialogTitleAr = "العنوان (بالعربية — اختياري)"
    override val dialogSummaryFr = "الملخص (بالفرنسية) *"
    override val dialogSummaryAr = "الملخص (بالعربية — اختياري)"
    override val dialogContentFr = "المحتوى الكامل (بالفرنسية) *"
    override val dialogContentAr = "المحتوى الكامل (بالعربية — اختياري)"
    override val dialogPubDate = "تاريخ النشر"
    override val dialogCategory = "التصنيف"
    override val dialogPublishNow = "نشر مباشرة (وإلا يُحفظ كمسودة)"
    override val dialogFeatured = "تثبيت في الواجهة"
    override val dialogErrorArticle = "العنوان والملخص والمحتوى حقول مطلوبة."
    override val dialogNewPlayer = "إضافة لاعب"
    override val dialogEditPlayer = "تعديل بيانات اللاعب"
    override val dialogPickPhoto = "اختيار صورة"
    override val dialogChangePhoto = "تم اختيار الصورة (تغيير)"
    override val dialogFullNameFr = "الاسم الكامل (بالفرنسية) *"
    override val dialogFullNameAr = "الاسم الكامل (بالعربية — اختياري)"
    override val dialogShirtNum = "رقم القميص *"
    override val dialogPositionFr = "المركز الدقيق (بالفرنسية) *"
    override val dialogPositionAr = "المركز الدقيق (بالعربية — اختياري)"
    override val dialogPosGroup = "الخط / الفئة"
    override val dialogNationalityFr = "الجنسية (بالفرنسية، اختياري)"
    override val dialogNationalityAr = "الجنسية (بالعربية، اختياري)"
    override val dialogBioFr = "السيرة الذاتية (بالفرنسية)"
    override val dialogBioAr = "السيرة الذاتية (بالعربية — اختياري)"
    override val dialogHighlightHome = "إبراز اللاعب في الصفحة الرئيسية"
    override val dialogErrorPlayer = "الاسم والرقم والمركز حقول مطلوبة."
    override val dialogNewMatch = "مباراة جديدة"
    override val dialogEditMatch = "تعديل المباراة"
    override val dialogOpponentFr = "المنافس (بالفرنسية) *"
    override val dialogOpponentAr = "المنافس (بالعربية — اختياري)"
    override val dialogOpponentShort = "الاختصار (3 أحرف) *"
    override val dialogCompetitionFr = "المسابقة (بالفرنسية) *"
    override val dialogCompetitionAr = "المسابقة (بالعربية — اختياري)"
    override val dialogDateField = "التاريخ"
    override val dialogTimeField = "التوقيت"
    override val dialogDaysOffset = "العد التنازلي (بعد X أيام)"
    override val dialogStadiumFr = "الملعب (بالفرنسية)"
    override val dialogStadiumAr = "الملعب (بالعربية — اختياري)"
    override val dialogIsHome = "مباراة داخل الديار (سريع غليزان مستضيف)"
    override val dialogStatusLabel = "الحالة"
    override val dialogScoreRcr = "أهداف سريع غليزان"
    override val dialogScoreOpp = "أهداف المنافس"
    override val dialogShowNextMatch = "عرض كمباراة قادمة في الرئيسية"
    override val dialogNotesFr = "ملاحظات / تفاصيل (بالفرنسية)"
    override val dialogNotesAr = "ملاحظات / تفاصيل (بالعربية — اختياري)"
    override val dialogErrorMatch = "المنافس والاختصار والمسابقة حقول مطلوبة."
    override val dialogAddStanding = "إضافة إلى الترتيب"
    override val dialogEditStanding = "تعديل صف الترتيب"
    override val dialogPosField = "المركز *"
    override val dialogCodeField = "الرمز *"
    override val dialogClubNameFr = "اسم النادي (بالفرنسية) *"
    override val dialogClubNameAr = "اسم النادي (بالعربية — اختياري)"
    override val dialogHighlightRcr = "تمييز كفريق سريع غليزان"
    override val dialogErrorStanding = "اسم النادي والرمز مطلوبان."
    override val dialogAddGallery = "إضافة إلى المعرض"
    override val dialogSelectMedia = "اختيار من الجهاز"
    override val dialogGalleryTitleFr = "العنوان / الوصف (بالفرنسية) *"
    override val dialogGalleryTitleAr = "العنوان / الوصف (بالعربية — اختياري)"
    override val dialogAlbumLabel = "الألبوم / التصنيف"
    override val dialogIsVideo = "ملف فيديو"
    override val dialogDurationLabel = "المدة (مثال: 02:15)"
    override val dialogErrorTitle = "يرجى إدخال عنوان."
    override val msgAllNotifsRead = "تم تحديد جميع الإشعارات كمقروءة."
    override val msgContactSaved = "تم تسجيل الرسالة لإدارة سريع غليزان."
    override val msgAdminAuthSuccess = "تم تفعيل جلسة الإدارة."
    override val msgAdminLogout = "تم تسجيل الخروج من فضاء الإدارة."
    override val msgAdminPinUpdated = "تم تحديث رمز PIN للإدارة."
    override val msgArticleSaved = "تم حفظ المقال."
    override val msgArticleUpdated = "تم تحديث المقال."
    override val msgArticleDeleted = "تم حذف المقال."
    override val msgPlayerSaved = "تمت إضافة اللاعب إلى التشكيلة."
    override val msgPlayerUpdated = "تم تحديث بطاقة اللاعب."
    override val msgPlayerDeleted = "تمت إزالة اللاعب من التشكيلة."
    override val msgMatchSaved = "تمت إضافة المباراة."
    override val msgMatchUpdated = "تم تحديث المباراة."
    override val msgMatchDeleted = "تم حذف المباراة."
    override val msgStandingUpdated = "تم تحديث جدول الترتيب."
    override val msgStandingDeleted = "تم حذف الصف من الترتيب."
    override val msgGalleryAdded = "تمت إضافة الوسائط إلى المعرض."
    override val msgGalleryDeleted = "تم حذف الوسائط."
    override val msgClubUpdated = "تم حفظ معلومات النادي."
    override val msgOfficialLogoSaved = "تم تحديث الشعار الرسمي لسريع غليزان."
    override val msgOfficialLogoReset = "تمت استعادة الشعار الرسمي الافتراضي."
    override val msgOpponentLogoSaved = "تم حفظ شعار الفريق المنافس."
    override val msgOpponentLogoRemoved = "تمت إزالة الشعار واستعادة الاختصار."
}

fun stringsForLanguage(languageCode: String): RcrStrings =
    if (languageCode == "ar") ArabicRcrStrings else FrenchRcrStrings

val LocalRcrStrings = staticCompositionLocalOf<RcrStrings> { FrenchRcrStrings }
