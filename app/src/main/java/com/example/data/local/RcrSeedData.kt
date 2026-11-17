package com.example.data.local

import java.util.concurrent.TimeUnit

object RcrSeedData {

    fun defaultClubInfo(): ClubInfoEntity = ClubInfoEntity(
        id = 1,
        officialName = "Rapid Club de Relizane",
        shortName = "RCR",
        city = "Relizane, Algérie",
        foundedYear = "1934",
        colorsLabel = "Vert & Blanc",
        historyText = "Fondé en 1934 à Relizane, le Rapid Club de Relizane (RCR) incarne une histoire populaire ancrée dans la wilaya de Relizane et le football algérien.\n\n[Section historique détaillée et archives officielles à compléter par l'administration du club via l'Espace Admin.]",
        identityText = "Les couleurs Vert et Blanc symbolisent l'attachement à la ville de Relizane, la ferveur populaire et l'esprit de combativité qui animent le club sur et en dehors du terrain.",
        valuesText = "• Engagement & Combativité sur chaque ballon\n• Respect de l'histoire et de la ville de Relizane\n• Formation des jeunes talents locaux\n• Unité entre joueurs, staff et supporters",
        supportersText = "Notre passion, notre fierté. Les supporters du RCR constituent le cœur battant du stade Tahar Zoughari, portant les couleurs vert et blanc à domicile comme à l'extérieur.",
        achievementsText = "[Palmarès officiel vérifié à renseigner par l'administration du club — aucune donnée historique non vérifiée n'est générée automatiquement.]",
        stadiumName = "Stade Tahar Zoughari",
        stadiumLocation = "Relizane, Algérie",
        stadiumCapacityLabel = "À confirmer (Admin)",
        stadiumSurfaceLabel = "Pelouse (à préciser)",
        stadiumDescription = "Enceinte historique du Rapid Club de Relizane située à Relizane. Les informations détaillées sur la capacité, les accès tribunes et la billetterie sont administrables depuis l'Espace Admin.",
        contactEmail = "contact@rcr-exemple.dz (à configurer)",
        contactPhone = "+213 (0)46 XX XX XX (à configurer)",
        contactAddress = "Siège du Rapid Club de Relizane, Relizane 48000, Algérie",
        officialNameAr = "سريع غليزان",
        cityAr = "غليزان، الجزائر",
        colorsLabelAr = "الأخضر والأبيض",
        identityTextAr = "يرمز اللونان الأخضر والأبيض إلى الارتباط بمدينة غليزان، والحماس الجماهيري وروح القتالية التي تميز النادي داخل الملعب وخارجه.",
        supportersTextAr = "شغفنا، فخرنا. يشكل أنصار سريع غليزان القلب النابض لملعب طاهر زوغاري، حاملين الألوان الخضراء والبيضاء في كل المباريات.",
        stadiumNameAr = "ملعب طاهر زوغاري",
        stadiumLocationAr = "غليزان، الجزائر",
        stadiumCapacityLabelAr = "قيد التأكيد (الإدارة)",
        stadiumSurfaceLabelAr = "أرضية اللعب (قيد التحديد)",
        stadiumDescriptionAr = "الملعب التاريخي لنادي سريع غليزان بمدينة غليزان. يمكن تحديث تفاصيل السعة والمدرجات والتذاكر عبر فضاء الإدارة.",
        contactAddressAr = "مقر نادي سريع غليزان، غليزان 48000، الجزائر"
    )

    fun defaultNews(now: Long = System.currentTimeMillis()): List<NewsArticleEntity> = listOf(
        NewsArticleEntity(
            title = "Préparation de la semaine au Stade Tahar Zoughari",
            summary = "Contenu d'exemple : Le groupe poursuit sa préparation athlétique et tactique en vue de la prochaine échéance.",
            content = "Article d'exemple — modifiable et remplaçable depuis l'Espace Admin.\n\nLe staff technique du Rapid Club de Relizane a programmé plusieurs séances axées sur la cohésion collective, la transition rapide et la solidité défensive.\n\nLes supporters peuvent suivre toutes les actualités officielles publiées par le club directement dans cet espace dès leur validation par l'administration.",
            category = "Équipe A",
            imageUri = "drawable://img_training_action",
            publishedDateLabel = "16 Nov 2026",
            createdAtMillis = now - TimeUnit.HOURS.toMillis(2),
            isFeatured = true,
            isPublished = true,
            isPlaceholder = true
        ),
        NewsArticleEntity(
            title = "Supporters du RCR : mobilisation pour le prochain rendez-vous à domicile",
            summary = "Contenu d'exemple : Retour sur la ferveur des tribunes vert et blanc à Relizane.",
            content = "Article d'exemple — modifiable via l'Espace Admin.\n\n« Notre passion, notre fierté. » À chaque sortie au stade Tahar Zoughari, le public relizanais répond présent avec ses écharpes et chants.\n\nLes modalités d'accès et les annonces officielles sont mises à jour régulièrement par l'administration du club.",
            category = "Supporters",
            imageUri = "drawable://img_supporters_banner",
            publishedDateLabel = "14 Nov 2026",
            createdAtMillis = now - TimeUnit.DAYS.toMillis(2),
            isFeatured = false,
            isPublished = true,
            isPlaceholder = true
        ),
        NewsArticleEntity(
            title = "Identité & Héritage : le maillot Vert et Blanc à l'honneur",
            summary = "Contenu d'exemple : Focus sur l'identité visuelle du Rapid Club de Relizane fondé en 1934.",
            content = "Article d'exemple — modifiable via l'Espace Admin.\n\nDepuis 1934, le Rapid Club de Relizane défend fièrement les couleurs vert et blanc.\n\nCette application officielle réunit l'ensemble des actualités, du calendrier des rencontres, de l'effectif et des archives médias du club.",
            category = "Club",
            imageUri = "drawable://img_club_heritage",
            publishedDateLabel = "11 Nov 2026",
            createdAtMillis = now - TimeUnit.DAYS.toMillis(5),
            isFeatured = false,
            isPublished = true,
            isPlaceholder = true
        ),
        NewsArticleEntity(
            title = "Soirée sous les projecteurs : ambiance du stade à Relizane",
            summary = "Contenu d'exemple : Galerie et aperçu de la pelouse avant les grandes affiches.",
            content = "Article d'exemple — modifiable via l'Espace Admin.\n\nRetrouvez dans la section Galerie les clichés des entraînements, des matchs et des tribunes vert et blanc.\n\nLes administrateurs peuvent publier de nouveaux communiqués et photos à tout moment.",
            category = "Matchs",
            imageUri = "drawable://img_hero_stadium",
            publishedDateLabel = "08 Nov 2026",
            createdAtMillis = now - TimeUnit.DAYS.toMillis(8),
            isFeatured = false,
            isPublished = true,
            isPlaceholder = true
        )
    )

    fun defaultMatches(now: Long = System.currentTimeMillis()): List<MatchEntity> = listOf(
        MatchEntity(
            opponentName = "Adversaire Exemple FC",
            opponentShort = "AEF",
            competition = "Championnat (Exemple)",
            matchDateLabel = "22 Nov 2026",
            kickoffTimeLabel = "16:00",
            kickoffTimestampMillis = now + TimeUnit.DAYS.toMillis(5) + TimeUnit.HOURS.toMillis(7),
            stadium = "Stade Tahar Zoughari, Relizane",
            isHome = true,
            rcrScore = null,
            opponentScore = null,
            status = "À venir",
            isFeaturedNext = true,
            matchNotes = "Affiche d'exemple modifiable via l'Espace Admin (adversaire, date, heure, stade et score).",
            isPlaceholder = true,
            opponentNameAr = "نادي منافس (نموذج)",
            competitionAr = "البطولة (نموذج)",
            stadiumAr = "ملعب طاهر زوغاري، غليزان"
        ),
        MatchEntity(
            opponentName = "Club Visité Exemple",
            opponentShort = "CVE",
            competition = "Championnat (Exemple)",
            matchDateLabel = "29 Nov 2026",
            kickoffTimeLabel = "15:00",
            kickoffTimestampMillis = now + TimeUnit.DAYS.toMillis(12) + TimeUnit.HOURS.toMillis(6),
            stadium = "Stade Extérieur (Exemple)",
            isHome = false,
            rcrScore = null,
            opponentScore = null,
            status = "À venir",
            isFeaturedNext = false,
            matchNotes = "Déplacement d'exemple — données officielles à saisir par l'administration.",
            isPlaceholder = true,
            opponentNameAr = "النادي المستضيف (نموذج)",
            competitionAr = "البطولة (نموذج)",
            stadiumAr = "ملعب خارج الديار (نموذج)"
        ),
        MatchEntity(
            opponentName = "Équipe Exemple Sud",
            opponentShort = "EES",
            competition = "Championnat (Exemple)",
            matchDateLabel = "09 Nov 2026",
            kickoffTimeLabel = "16:00",
            kickoffTimestampMillis = now - TimeUnit.DAYS.toMillis(7),
            stadium = "Stade Tahar Zoughari, Relizane",
            isHome = true,
            rcrScore = 2,
            opponentScore = 0,
            status = "Terminé",
            isFeaturedNext = false,
            matchNotes = "Score d'exemple — à remplacer par les résultats officiels vérifiés via Admin.",
            isPlaceholder = true,
            opponentNameAr = "فريق الجنوب (نموذج)",
            competitionAr = "البطولة (نموذج)",
            stadiumAr = "ملعب طاهر زوغاري، غليزان"
        ),
        MatchEntity(
            opponentName = "Union Exemple Nord",
            opponentShort = "UEN",
            competition = "Championnat (Exemple)",
            matchDateLabel = "02 Nov 2026",
            kickoffTimeLabel = "15:30",
            kickoffTimestampMillis = now - TimeUnit.DAYS.toMillis(14),
            stadium = "Stade Municipal (Exemple)",
            isHome = false,
            rcrScore = 1,
            opponentScore = 1,
            status = "Terminé",
            isFeaturedNext = false,
            matchNotes = "Score d'exemple — à remplacer par les résultats officiels vérifiés via Admin.",
            isPlaceholder = true,
            opponentNameAr = "اتحاد الشمال (نموذج)",
            competitionAr = "البطولة (نموذج)",
            stadiumAr = "الملعب البلدي (نموذج)"
        )
    )

    fun defaultStandings(): List<StandingRowEntity> = listOf(
        StandingRowEntity(position = 1, clubName = "Club Leader (Exemple)", clubShort = "CLE", played = 10, wins = 7, draws = 2, losses = 1, goalsFor = 18, goalsAgainst = 7, points = 23, isRcr = false, isPlaceholder = true, clubNameAr = "متصدر الترتيب (نموذج)"),
        StandingRowEntity(position = 2, clubName = "RC Relizane", clubShort = "RCR", played = 10, wins = 6, draws = 3, losses = 1, goalsFor = 16, goalsAgainst = 6, points = 21, isRcr = true, isPlaceholder = true, clubNameAr = "سريع غليزان"),
        StandingRowEntity(position = 3, clubName = "Adversaire Exemple FC", clubShort = "AEF", played = 10, wins = 5, draws = 3, losses = 2, goalsFor = 14, goalsAgainst = 9, points = 18, isRcr = false, isPlaceholder = true, clubNameAr = "نادي منافس (نموذج)"),
        StandingRowEntity(position = 4, clubName = "Union Exemple Nord", clubShort = "UEN", played = 10, wins = 4, draws = 4, losses = 2, goalsFor = 11, goalsAgainst = 8, points = 16, isRcr = false, isPlaceholder = true, clubNameAr = "اتحاد الشمال (نموذج)"),
        StandingRowEntity(position = 5, clubName = "Club Visité Exemple", clubShort = "CVE", played = 10, wins = 4, draws = 2, losses = 4, goalsFor = 12, goalsAgainst = 12, points = 14, isRcr = false, isPlaceholder = true, clubNameAr = "النادي المستضيف (نموذج)"),
        StandingRowEntity(position = 6, clubName = "Équipe Exemple Sud", clubShort = "EES", played = 10, wins = 2, draws = 3, losses = 5, goalsFor = 8, goalsAgainst = 14, points = 9, isRcr = false, isPlaceholder = true, clubNameAr = "فريق الجنوب (نموذج)")
    )

    fun defaultPlayers(): List<PlayerEntity> = listOf(
        PlayerEntity(
            fullName = "Gardien #1 (Exemple)",
            shirtNumber = 1,
            position = "Gardien de but",
            positionGroup = "Gardiens",
            nationality = "",
            biography = "Fiche joueur d'exemple. Les informations vérifiées, la nationalité, la photo officielle et les statistiques sont à compléter dans l'Espace Admin.",
            photoUri = "",
            appearances = null,
            goals = null,
            assists = null,
            isHighlighted = true,
            isPlaceholder = true,
            fullNameAr = "حارس المرمى #1 (نموذج)",
            positionAr = "حارس مرمى"
        ),
        PlayerEntity(
            fullName = "Gardien #16 (Exemple)",
            shirtNumber = 16,
            position = "Gardien de but",
            positionGroup = "Gardiens",
            nationality = "",
            biography = "Fiche joueur d'exemple à mettre à jour par l'administration.",
            photoUri = "",
            isHighlighted = false,
            isPlaceholder = true,
            fullNameAr = "حارس المرمى #16 (نموذج)",
            positionAr = "حارس مرمى"
        ),
        PlayerEntity(
            fullName = "Défenseur Central #4 (Exemple)",
            shirtNumber = 4,
            position = "Défenseur central",
            positionGroup = "Défenseurs",
            nationality = "",
            biography = "Fiche joueur d'exemple à mettre à jour par l'administration.",
            photoUri = "",
            isHighlighted = true,
            isPlaceholder = true,
            fullNameAr = "مدافع محوري #4 (نموذج)",
            positionAr = "مدافع محوري"
        ),
        PlayerEntity(
            fullName = "Latéral Droit #2 (Exemple)",
            shirtNumber = 2,
            position = "Arrière droit",
            positionGroup = "Défenseurs",
            nationality = "",
            biography = "Fiche joueur d'exemple à mettre à jour par l'administration.",
            photoUri = "",
            isHighlighted = false,
            isPlaceholder = true,
            fullNameAr = "ظهير أيمن #2 (نموذج)",
            positionAr = "ظهير أيمن"
        ),
        PlayerEntity(
            fullName = "Milieu Relayeur #8 (Exemple)",
            shirtNumber = 8,
            position = "Milieu central",
            positionGroup = "Milieux",
            nationality = "",
            biography = "Fiche joueur d'exemple à mettre à jour par l'administration.",
            photoUri = "",
            isHighlighted = true,
            isPlaceholder = true,
            fullNameAr = "وسط ميدان #8 (نموذج)",
            positionAr = "وسط ميدان محوري"
        ),
        PlayerEntity(
            fullName = "Meneur de Jeu #10 (Exemple)",
            shirtNumber = 10,
            position = "Milieu offensif",
            positionGroup = "Milieux",
            nationality = "",
            biography = "Fiche joueur d'exemple à mettre à jour par l'administration.",
            photoUri = "",
            isHighlighted = true,
            isPlaceholder = true,
            fullNameAr = "صانع ألعاب #10 (نموذج)",
            positionAr = "وسط ميدان هجومي"
        ),
        PlayerEntity(
            fullName = "Attaquant Pointe #9 (Exemple)",
            shirtNumber = 9,
            position = "Avant-centre",
            positionGroup = "Attaquants",
            nationality = "",
            biography = "Fiche joueur d'exemple à mettre à jour par l'administration.",
            photoUri = "",
            isHighlighted = true,
            isPlaceholder = true,
            fullNameAr = "مهاجم صريح #9 (نموذج)",
            positionAr = "رأس حربة"
        ),
        PlayerEntity(
            fullName = "Ailier Droit #7 (Exemple)",
            shirtNumber = 7,
            position = "Ailier",
            positionGroup = "Attaquants",
            nationality = "",
            biography = "Fiche joueur d'exemple à mettre à jour par l'administration.",
            photoUri = "",
            isHighlighted = false,
            isPlaceholder = true,
            fullNameAr = "جناح أيمن #7 (نموذج)",
            positionAr = "جناح"
        )
    )

    fun defaultGallery(now: Long = System.currentTimeMillis()): List<GalleryItemEntity> = listOf(
        GalleryItemEntity(
            title = "Soirée de match au Stade Tahar Zoughari",
            category = "Matchs",
            mediaUri = "drawable://img_hero_stadium",
            isVideo = false,
            dateLabel = "16 Nov 2026",
            createdAtMillis = now - 10000L,
            isPlaceholder = true,
            titleAr = "أمسية مباراة في ملعب طاهر زوغاري"
        ),
        GalleryItemEntity(
            title = "Séance tactique de l'équipe première",
            category = "Entraînement",
            mediaUri = "drawable://img_training_action",
            isVideo = false,
            dateLabel = "14 Nov 2026",
            createdAtMillis = now - 20000L,
            isPlaceholder = true,
            titleAr = "حصة تكتيكية للفريق الأول"
        ),
        GalleryItemEntity(
            title = "Tribunes Vert et Blanc en fusion",
            category = "Supporters",
            mediaUri = "drawable://img_supporters_banner",
            isVideo = false,
            dateLabel = "12 Nov 2026",
            createdAtMillis = now - 30000L,
            isPlaceholder = true,
            titleAr = "حماس مدرجات الأخضر والأبيض"
        ),
        GalleryItemEntity(
            title = "Maillot & Couleurs du Rapid Club de Relizane",
            category = "Événements",
            mediaUri = "drawable://img_club_heritage",
            isVideo = false,
            dateLabel = "10 Nov 2026",
            createdAtMillis = now - 40000L,
            isPlaceholder = true,
            titleAr = "قميص وألوان نادي سريع غليزان"
        ),
        GalleryItemEntity(
            title = "Résumé d'entraînement — Aperçu média",
            category = "Vidéos",
            mediaUri = "drawable://img_training_action",
            isVideo = true,
            videoDurationLabel = "01:45",
            dateLabel = "09 Nov 2026",
            createdAtMillis = now - 50000L,
            isPlaceholder = true,
            titleAr = "ملخص الحصة التدريبية — معاينة فيديو"
        ),
        GalleryItemEntity(
            title = "Préparation des joueurs sur le terrain",
            category = "Joueurs",
            mediaUri = "drawable://img_club_heritage",
            isVideo = false,
            dateLabel = "07 Nov 2026",
            createdAtMillis = now - 60000L,
            isPlaceholder = true,
            titleAr = "تحضيرات اللاعبين على أرضية الميدان"
        )
    )

    fun defaultNotifications(now: Long = System.currentTimeMillis()): List<AppNotificationEntity> = listOf(
        AppNotificationEntity(
            title = "Bienvenue sur l'application RCR",
            message = "Suivez les actualités, matchs, classement, effectif et galerie du Rapid Club de Relizane.",
            type = "CLUB",
            timestampMillis = now - TimeUnit.MINUTES.toMillis(15),
            timeLabel = "À l'instant",
            isRead = false,
            targetRoute = "club",
            titleAr = "مرحباً بكم في تطبيق سريع غليزان",
            messageAr = "تابعوا أخبار ومباريات وترتيب وتشكيلة ومعرض نادي سريع غليزان.",
            timeLabelAr = "الآن"
        ),
        AppNotificationEntity(
            title = "Prochain match programmé (Exemple)",
            message = "RCR vs Adversaire Exemple FC — 22 Nov 2026 à 16:00 au Stade Tahar Zoughari.",
            type = "MATCH",
            timestampMillis = now - TimeUnit.HOURS.toMillis(3),
            timeLabel = "Il y a 3 h",
            isRead = false,
            targetRoute = "matches",
            titleAr = "موعد المباراة القادمة (نموذج)",
            messageAr = "سريع غليزان ضد نادي منافس — 22 Nov 2026 على الساعة 16:00 بملعب طاهر زوغاري.",
            timeLabelAr = "منذ 3 ساعات"
        ),
        AppNotificationEntity(
            title = "Nouvel article publié (Exemple)",
            message = "Préparation de la semaine au Stade Tahar Zoughari — disponible dans Actualités.",
            type = "NEWS",
            timestampMillis = now - TimeUnit.DAYS.toMillis(1),
            timeLabel = "Hier",
            isRead = true,
            targetRoute = "news",
            titleAr = "نشر مقال جديد (نموذج)",
            messageAr = "تحضيرات الأسبوع في ملعب طاهر زوغاري — متاح الآن في قسم الأخبار.",
            timeLabelAr = "أمس"
        )
    )
}
