package com.example.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.SportsSoccer
import androidx.compose.ui.graphics.vector.ImageVector

object Routes {
    const val HOME = "home"
    const val NEWS = "news"
    const val NEWS_DETAIL = "news/{articleId}"
    const val MATCHES = "matches"
    const val MATCH_DETAIL = "match/{matchId}"
    const val TEAM = "team"
    const val PLAYER_DETAIL = "player/{playerId}"
    const val MORE = "more"

    const val CLUB = "club"
    const val STANDINGS = "standings"
    const val GALLERY = "gallery"
    const val STADIUM = "stadium"
    const val CONTACT = "contact"
    const val SETTINGS = "settings"
    const val ABOUT = "about"
    const val NOTIFICATIONS = "notifications"
    const val ADMIN = "admin"

    fun newsDetail(articleId: Long) = "news/$articleId"
    fun matchDetail(matchId: Long) = "match/$matchId"
    fun playerDetail(playerId: Long) = "player/$playerId"
}

data class BottomNavDestination(
    val route: String,
    val labelFr: String,
    val labelAr: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

val mainBottomDestinations = listOf(
    BottomNavDestination(
        route = Routes.HOME,
        labelFr = "Accueil",
        labelAr = "الرئيسية",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        testTag = "nav_home"
    ),
    BottomNavDestination(
        route = Routes.NEWS,
        labelFr = "Actualités",
        labelAr = "الأخبار",
        selectedIcon = Icons.Filled.Newspaper,
        unselectedIcon = Icons.Outlined.Newspaper,
        testTag = "nav_news"
    ),
    BottomNavDestination(
        route = Routes.MATCHES,
        labelFr = "Matchs",
        labelAr = "المباريات",
        selectedIcon = Icons.Filled.SportsSoccer,
        unselectedIcon = Icons.Outlined.SportsSoccer,
        testTag = "nav_matches"
    ),
    BottomNavDestination(
        route = Routes.TEAM,
        labelFr = "Équipe",
        labelAr = "الفريق",
        selectedIcon = Icons.Filled.Groups,
        unselectedIcon = Icons.Outlined.Groups,
        testTag = "nav_team"
    ),
    BottomNavDestination(
        route = Routes.MORE,
        labelFr = "Plus",
        labelAr = "المزيد",
        selectedIcon = Icons.Filled.Menu,
        unselectedIcon = Icons.Outlined.Menu,
        testTag = "nav_more"
    )
)

fun resolveAdjacentMainRoute(
    currentRoute: String?,
    dragDeltaX: Float,
    isRtl: Boolean
): String? {
    val currentIndex = mainBottomDestinations.indexOfFirst { it.route == currentRoute }
    if (currentIndex == -1 || dragDeltaX == 0f) return null

    // LTR: dragging left (negative X) advances (+1); dragging right (positive X) goes back (-1).
    // RTL: dragging right (positive X) advances (+1); dragging left (negative X) goes back (-1).
    val step = if (isRtl) {
        if (dragDeltaX > 0f) 1 else -1
    } else {
        if (dragDeltaX < 0f) 1 else -1
    }
    val targetIndex = currentIndex + step
    return mainBottomDestinations.getOrNull(targetIndex)?.route
}

