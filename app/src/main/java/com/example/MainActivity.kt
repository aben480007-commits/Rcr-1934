package com.example

import android.app.Activity
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.local.PreferencesManager
import com.example.data.local.RcrDatabase
import com.example.data.repository.RcrRepository
import com.example.ui.components.LocalCompetingClubs
import com.example.ui.components.LocalRcrClubLogoPath
import com.example.ui.components.RcrBottomBar
import com.example.ui.components.RcrNavRail
import com.example.ui.components.RcrTopBar
import com.example.ui.localization.LocalRcrStrings
import com.example.ui.localization.stringsForLanguage
import com.example.ui.navigation.Routes
import com.example.ui.navigation.mainBottomDestinations
import com.example.ui.navigation.resolveAdjacentMainRoute
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.ClubScreen
import com.example.ui.screens.ContactScreen
import com.example.ui.screens.GalleryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MatchDetailScreen
import com.example.ui.screens.MatchesScreen
import com.example.ui.screens.MoreScreen
import com.example.ui.screens.NewsDetailScreen
import com.example.ui.screens.NewsScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.PlayerDetailScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.StadiumScreen
import com.example.ui.screens.StandingsScreen
import com.example.ui.screens.TeamScreen
import com.example.ui.theme.LocalRcrColors
import com.example.ui.theme.RcrDeepGreen
import com.example.ui.theme.RcrPrimaryGreen
import com.example.ui.theme.RcrTheme
import com.example.ui.theme.ThemeMode
import com.example.ui.viewmodel.RcrViewModel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.math.abs
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RcrAppRoot(showLaunchSplash = true)
        }
    }
}

@Composable
fun RcrAppRoot(showLaunchSplash: Boolean = false) {
    val context = LocalContext.current.applicationContext
    val viewModel: RcrViewModel = viewModel(
        factory = remember {
            val db = RcrDatabase.getInstance(context)
            val repo = RcrRepository(db)
            val prefs = PreferencesManager(context)
            RcrViewModel.Factory(repo, prefs)
        }
    )

    val preferences by viewModel.preferences.collectAsStateWithLifecycle()
    val publishedNews by viewModel.publishedNews.collectAsStateWithLifecycle()
    val filteredNews by viewModel.filteredNews.collectAsStateWithLifecycle()
    val allNewsForAdmin by viewModel.allNewsForAdmin.collectAsStateWithLifecycle()
    val newsQuery by viewModel.newsQuery.collectAsStateWithLifecycle()
    val selectedNewsCategory by viewModel.selectedNewsCategory.collectAsStateWithLifecycle()
    val matches by viewModel.matches.collectAsStateWithLifecycle()
    val standings by viewModel.standings.collectAsStateWithLifecycle()
    val players by viewModel.players.collectAsStateWithLifecycle()
    val galleryItems by viewModel.galleryItems.collectAsStateWithLifecycle()
    val filteredGallery by viewModel.filteredGalleryItems.collectAsStateWithLifecycle()
    val selectedGalleryCategory by viewModel.selectedGalleryCategory.collectAsStateWithLifecycle()
    val clubInfo by viewModel.clubInfo.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val unreadCount by viewModel.unreadCount.collectAsStateWithLifecycle()
    val contactMessages by viewModel.contactMessages.collectAsStateWithLifecycle()
    val competingClubs by viewModel.competingClubs.collectAsStateWithLifecycle()
    val nowMillis by viewModel.nowMillis.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.snackbarEvents.collect { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    val isRtl = preferences.isRtlLayout || preferences.languageCode == "ar"
    val layoutDir = if (isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr
    val strings = remember(preferences.languageCode) { stringsForLanguage(preferences.languageCode) }

    var splashVisible by remember(showLaunchSplash) { mutableStateOf(showLaunchSplash) }
    LaunchedEffect(showLaunchSplash) {
        if (showLaunchSplash) {
            delay(950L)
            splashVisible = false
        }
    }

    val systemDark = isSystemInDarkTheme()
    val effectiveDark = when (preferences.themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> systemDark
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                val controller = WindowCompat.getInsetsController(window, view)
                val useLightBars = if (splashVisible) true else !effectiveDark
                controller.isAppearanceLightStatusBars = useLightBars
                controller.isAppearanceLightNavigationBars = useLightBars
            }
        }
    }

    CompositionLocalProvider(
        LocalLayoutDirection provides layoutDir,
        LocalRcrStrings provides strings,
        LocalRcrClubLogoPath provides preferences.clubLogoPath,
        LocalCompetingClubs provides competingClubs
    ) {
        RcrTheme(themeMode = preferences.themeMode) {
            val rcrColors = LocalRcrColors.current
            val navController = rememberNavController()
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route

            val mainRoutes = remember { mainBottomDestinations.map { it.route }.toSet() }
            val mainRouteIndices = remember {
                mainBottomDestinations.mapIndexed { index, dest -> dest.route to index }.toMap()
            }
            val isMainTab = currentRoute in mainRoutes
            val dirSign = if (isRtl) -1 else 1
            val animDurationScale = remember(context) {
                runCatching {
                    Settings.Global.getFloat(
                        context.contentResolver,
                        Settings.Global.ANIMATOR_DURATION_SCALE,
                        1f
                    )
                }.getOrDefault(1f)
            }
            val transitionDurationMs = if (animDurationScale <= 0f) {
                0
            } else {
                (260f * animDurationScale.coerceIn(0.25f, 1.5f)).roundToInt()
            }

            val (topTitle, topSubtitle) = strings.topBarText(
                route = currentRoute,
                stadiumName = clubInfo?.displayStadiumName(strings.isArabic)
            )

            fun navigateMainTab(route: String) {
                if (currentRoute == route) return
                navController.navigate(route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }

            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val useNavRail = maxWidth >= 600.dp

                Row(modifier = Modifier.fillMaxSize()) {
                    if (useNavRail) {
                        RcrNavRail(
                            currentRoute = currentRoute,
                            onNavigate = { dest -> navigateMainTab(dest.route) },
                            isRtl = isRtl
                        )
                    }

                    Scaffold(
                        modifier = Modifier.weight(1f),
                        containerColor = rcrColors.primaryBackground,
                        contentWindowInsets = WindowInsets(0, 0, 0, 0),
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                        topBar = {
                            RcrTopBar(
                                title = topTitle,
                                subtitle = topSubtitle,
                                showBackButton = !isMainTab,
                                onBackClick = { navController.popBackStack() },
                                unreadCount = if (preferences.inAppAlertsEnabled) unreadCount else 0,
                                onNotificationsClick = {
                                    if (currentRoute != Routes.NOTIFICATIONS) {
                                        navController.navigate(Routes.NOTIFICATIONS)
                                    }
                                },
                                isAdminActive = preferences.isAdminAuthenticated,
                                onAdminClick = {
                                    if (currentRoute != Routes.ADMIN) {
                                        navController.navigate(Routes.ADMIN)
                                    }
                                },
                                isRtl = isRtl
                            )
                        },
                        bottomBar = {
                            if (!useNavRail) {
                                RcrBottomBar(
                                    currentRoute = currentRoute,
                                    onNavigate = { dest -> navigateMainTab(dest.route) },
                                    isRtl = isRtl
                                )
                            }
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .consumeWindowInsets(innerPadding)
                                .mainTabHorizontalSwipe(
                                    enabled = isMainTab,
                                    isRtl = isRtl,
                                    currentRoute = currentRoute,
                                    onNavigateRoute = ::navigateMainTab
                                )
                                .testTag("main_nav_swipe_container"),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            NavHost(
                                navController = navController,
                                startDestination = Routes.HOME,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .widthIn(max = 860.dp),
                                enterTransition = {
                                    if (transitionDurationMs == 0) {
                                        EnterTransition.None
                                    } else {
                                        val initialIdx = mainRouteIndices[initialState.destination.route]
                                        val targetIdx = mainRouteIndices[targetState.destination.route]
                                        if (initialIdx != null && targetIdx != null && initialIdx != targetIdx) {
                                            val direction = if (targetIdx > initialIdx) dirSign else -dirSign
                                            slideInHorizontally(
                                                animationSpec = tween(transitionDurationMs, easing = FastOutSlowInEasing)
                                            ) { fullWidth -> fullWidth * direction } +
                                                fadeIn(animationSpec = tween(transitionDurationMs), initialAlpha = 0.85f)
                                        } else {
                                            slideInHorizontally(
                                                animationSpec = tween(transitionDurationMs, easing = FastOutSlowInEasing)
                                            ) { fullWidth -> (fullWidth * 0.25f).toInt() * dirSign } +
                                                fadeIn(animationSpec = tween(transitionDurationMs))
                                        }
                                    }
                                },
                                exitTransition = {
                                    if (transitionDurationMs == 0) {
                                        ExitTransition.None
                                    } else {
                                        val initialIdx = mainRouteIndices[initialState.destination.route]
                                        val targetIdx = mainRouteIndices[targetState.destination.route]
                                        if (initialIdx != null && targetIdx != null && initialIdx != targetIdx) {
                                            val direction = if (targetIdx > initialIdx) dirSign else -dirSign
                                            slideOutHorizontally(
                                                animationSpec = tween(transitionDurationMs, easing = FastOutSlowInEasing)
                                            ) { fullWidth -> -fullWidth * direction } +
                                                fadeOut(animationSpec = tween(transitionDurationMs), targetAlpha = 0.85f)
                                        } else {
                                            slideOutHorizontally(
                                                animationSpec = tween(transitionDurationMs, easing = FastOutSlowInEasing)
                                            ) { fullWidth -> -(fullWidth * 0.2f).toInt() * dirSign } +
                                                fadeOut(animationSpec = tween(transitionDurationMs))
                                        }
                                    }
                                },
                                popEnterTransition = {
                                    if (transitionDurationMs == 0) {
                                        EnterTransition.None
                                    } else {
                                        val initialIdx = mainRouteIndices[initialState.destination.route]
                                        val targetIdx = mainRouteIndices[targetState.destination.route]
                                        if (initialIdx != null && targetIdx != null && initialIdx != targetIdx) {
                                            val direction = if (targetIdx > initialIdx) dirSign else -dirSign
                                            slideInHorizontally(
                                                animationSpec = tween(transitionDurationMs, easing = FastOutSlowInEasing)
                                            ) { fullWidth -> fullWidth * direction } +
                                                fadeIn(animationSpec = tween(transitionDurationMs), initialAlpha = 0.85f)
                                        } else {
                                            slideInHorizontally(
                                                animationSpec = tween(transitionDurationMs, easing = FastOutSlowInEasing)
                                            ) { fullWidth -> -(fullWidth * 0.2f).toInt() * dirSign } +
                                                fadeIn(animationSpec = tween(transitionDurationMs))
                                        }
                                    }
                                },
                                popExitTransition = {
                                    if (transitionDurationMs == 0) {
                                        ExitTransition.None
                                    } else {
                                        val initialIdx = mainRouteIndices[initialState.destination.route]
                                        val targetIdx = mainRouteIndices[targetState.destination.route]
                                        if (initialIdx != null && targetIdx != null && initialIdx != targetIdx) {
                                            val direction = if (targetIdx > initialIdx) dirSign else -dirSign
                                            slideOutHorizontally(
                                                animationSpec = tween(transitionDurationMs, easing = FastOutSlowInEasing)
                                            ) { fullWidth -> -fullWidth * direction } +
                                                fadeOut(animationSpec = tween(transitionDurationMs), targetAlpha = 0.85f)
                                        } else {
                                            slideOutHorizontally(
                                                animationSpec = tween(transitionDurationMs, easing = FastOutSlowInEasing)
                                            ) { fullWidth -> (fullWidth * 0.28f).toInt() * dirSign } +
                                                fadeOut(animationSpec = tween(transitionDurationMs))
                                        }
                                    }
                                }
                            ) {
                                composable(Routes.HOME) {
                                    HomeScreen(
                                        clubInfo = clubInfo,
                                        matches = matches,
                                        news = publishedNews,
                                        players = players,
                                        standings = standings,
                                        nowMillis = nowMillis,
                                        onOpenNewsList = { navigateMainTab(Routes.NEWS) },
                                        onOpenArticle = { id -> navController.navigate(Routes.newsDetail(id)) },
                                        onOpenMatches = { navigateMainTab(Routes.MATCHES) },
                                        onOpenMatchDetail = { id -> navController.navigate(Routes.matchDetail(id)) },
                                        onOpenTeam = { navigateMainTab(Routes.TEAM) },
                                        onOpenPlayerDetail = { id -> navController.navigate(Routes.playerDetail(id)) },
                                        onOpenStandings = { navController.navigate(Routes.STANDINGS) },
                                        onOpenClub = { navController.navigate(Routes.CLUB) }
                                    )
                                }

                                composable(Routes.NEWS) {
                                    NewsScreen(
                                        articles = filteredNews,
                                        searchQuery = newsQuery,
                                        selectedCategory = selectedNewsCategory,
                                        onSearchQueryChange = viewModel::updateNewsQuery,
                                        onSelectCategory = viewModel::selectNewsCategory,
                                        onOpenArticle = { id -> navController.navigate(Routes.newsDetail(id)) }
                                    )
                                }

                                composable(
                                    route = Routes.NEWS_DETAIL,
                                    arguments = listOf(navArgument("articleId") { type = NavType.LongType })
                                ) { entry ->
                                    val articleId = entry.arguments?.getLong("articleId") ?: -1L
                                    val article by viewModel.observeNewsById(articleId).collectAsStateWithLifecycle(initialValue = null)
                                    val related = remember(publishedNews, articleId) {
                                        publishedNews.filter { it.id != articleId }.take(3)
                                    }
                                    NewsDetailScreen(
                                        article = article,
                                        relatedArticles = related,
                                        onOpenArticle = { id -> navController.navigate(Routes.newsDetail(id)) }
                                    )
                                }

                                composable(Routes.MATCHES) {
                                    MatchesScreen(
                                        matches = matches,
                                        standings = standings,
                                        onOpenMatchDetail = { id -> navController.navigate(Routes.matchDetail(id)) }
                                    )
                                }

                                composable(
                                    route = Routes.MATCH_DETAIL,
                                    arguments = listOf(navArgument("matchId") { type = NavType.LongType })
                                ) { entry ->
                                    val matchId = entry.arguments?.getLong("matchId") ?: -1L
                                    val match by viewModel.observeMatchById(matchId).collectAsStateWithLifecycle(initialValue = null)
                                    MatchDetailScreen(match = match, nowMillis = nowMillis)
                                }

                                composable(Routes.TEAM) {
                                    TeamScreen(
                                        players = players,
                                        onOpenPlayerDetail = { id -> navController.navigate(Routes.playerDetail(id)) }
                                    )
                                }

                                composable(
                                    route = Routes.PLAYER_DETAIL,
                                    arguments = listOf(navArgument("playerId") { type = NavType.LongType })
                                ) { entry ->
                                    val playerId = entry.arguments?.getLong("playerId") ?: -1L
                                    val player by viewModel.observePlayerById(playerId).collectAsStateWithLifecycle(initialValue = null)
                                    PlayerDetailScreen(player = player)
                                }

                                composable(Routes.MORE) {
                                    MoreScreen(
                                        isAdminAuthenticated = preferences.isAdminAuthenticated,
                                        onNavigate = { route -> navController.navigate(route) }
                                    )
                                }

                                composable(Routes.CLUB) {
                                    ClubScreen(
                                        clubInfo = clubInfo,
                                        onOpenStadium = { navController.navigate(Routes.STADIUM) }
                                    )
                                }

                                composable(Routes.STANDINGS) {
                                    StandingsScreen(standings = standings)
                                }

                                composable(Routes.GALLERY) {
                                    GalleryScreen(
                                        items = filteredGallery,
                                        selectedCategory = selectedGalleryCategory,
                                        onSelectCategory = viewModel::selectGalleryCategory
                                    )
                                }

                                composable(Routes.STADIUM) {
                                    StadiumScreen(clubInfo = clubInfo)
                                }

                                composable(Routes.CONTACT) {
                                    ContactScreen(
                                        clubInfo = clubInfo,
                                        onSubmitMessage = viewModel::submitContactMessage
                                    )
                                }

                                composable(Routes.SETTINGS) {
                                    SettingsScreen(
                                        preferences = preferences,
                                        onSelectTheme = viewModel::setThemeMode,
                                        onSelectLanguageRtl = viewModel::setLanguageAndRtl,
                                        onToggleInAppAlerts = viewModel::setInAppAlertsEnabled,
                                        onOpenAdmin = { navController.navigate(Routes.ADMIN) }
                                    )
                                }

                                composable(Routes.ABOUT) {
                                    AboutScreen()
                                }

                                composable(Routes.NOTIFICATIONS) {
                                    NotificationsScreen(
                                        notifications = notifications,
                                        onNotificationClick = { notif ->
                                            viewModel.markNotificationAsRead(notif.id)
                                            if (notif.targetRoute.isNotBlank()) {
                                                navController.navigate(notif.targetRoute)
                                            }
                                        },
                                        onMarkAllRead = viewModel::markAllNotificationsAsRead,
                                        onDeleteNotification = viewModel::deleteNotification
                                    )
                                }

                                composable(Routes.ADMIN) {
                                    AdminScreen(
                                        isAuthenticated = preferences.isAdminAuthenticated,
                                        allNews = allNewsForAdmin,
                                        players = players,
                                        matches = matches,
                                        standings = standings,
                                        galleryItems = galleryItems,
                                        clubInfo = clubInfo,
                                        contactMessages = contactMessages,
                                        clubLogoPath = preferences.clubLogoPath,
                                        competingClubs = competingClubs,
                                        onLogin = viewModel::loginAdmin,
                                        onLogout = viewModel::logoutAdmin,
                                        onChangePin = viewModel::changeAdminPin,
                                        onSaveArticle = viewModel::saveArticle,
                                        onDeleteArticle = viewModel::deleteArticle,
                                        onSavePlayer = viewModel::savePlayer,
                                        onDeletePlayer = viewModel::deletePlayer,
                                        onSaveMatch = viewModel::saveMatch,
                                        onDeleteMatch = viewModel::deleteMatch,
                                        onSaveStanding = viewModel::saveStandingRow,
                                        onDeleteStanding = viewModel::deleteStandingRow,
                                        onAddGalleryItem = viewModel::addGalleryItem,
                                        onDeleteGalleryItem = viewModel::deleteGalleryItem,
                                        onUpdateClubInfo = viewModel::updateClubInfo,
                                        onSaveOfficialLogo = { uri, onDone ->
                                            viewModel.saveOfficialClubLogo(context, uri, onDone)
                                        },
                                        onResetOfficialLogo = {
                                            viewModel.resetOfficialClubLogo(context)
                                        },
                                        onSaveCompetingClubLogo = { clubId, clubShort, uri, onDone ->
                                            viewModel.saveCompetingClubLogo(context, clubId, clubShort, uri, onDone)
                                        },
                                        onRemoveCompetingClubLogo = { clubId ->
                                            viewModel.removeCompetingClubLogo(context, clubId)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                AnimatedVisibility(
                    visible = splashVisible,
                    exit = fadeOut(animationSpec = tween(durationMillis = 350))
                ) {
                    RcrLaunchSplashScreen()
                }
            }
        }
    }
}

@Composable
fun RcrLaunchSplashScreen(modifier: Modifier = Modifier) {
    val strings = LocalRcrStrings.current
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.rcr_splash_bg))
            .testTag("launch_splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.rcr_crest_original),
                contentDescription = strings.clubOfficialName,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(230.dp)
                    .testTag("launch_splash_logo")
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = strings.clubOfficialName.uppercase(),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.2.sp
                ),
                color = RcrDeepGreen,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "${strings.clubLocationBadge} • 1934",
                style = MaterialTheme.typography.labelLarge,
                color = RcrPrimaryGreen,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun Modifier.mainTabHorizontalSwipe(
    enabled: Boolean,
    isRtl: Boolean,
    currentRoute: String?,
    onNavigateRoute: (String) -> Unit
): Modifier = this.pointerInput(enabled, currentRoute, isRtl) {
    if (!enabled) return@pointerInput
    val touchSlop = viewConfiguration.touchSlop
    val swipeThresholdPx = 64.dp.toPx()
    val currentContext = currentCoroutineContext()

    awaitPointerEventScope {
        while (currentContext.isActive) {
            val down = awaitFirstDown(requireUnconsumed = false)
            var totalDragX = 0f
            var totalDragY = 0f
            var isHorizontalSwipe = false
            var gestureRejected = false

            while (true) {
                val event = awaitPointerEvent(pass = PointerEventPass.Main)
                val change = event.changes.firstOrNull { it.id == down.id }
                    ?: event.changes.firstOrNull()
                    ?: break

                if (change.changedToUpIgnoreConsumed() || !change.pressed) {
                    if (isHorizontalSwipe && !gestureRejected && abs(totalDragX) >= swipeThresholdPx) {
                        val targetRoute = resolveAdjacentMainRoute(
                            currentRoute = currentRoute,
                            dragDeltaX = totalDragX,
                            isRtl = isRtl
                        )
                        if (targetRoute != null) {
                            onNavigateRoute(targetRoute)
                        }
                    }
                    break
                }

                if (gestureRejected) continue

                val delta = change.position - change.previousPosition
                if (delta == Offset.Zero) continue

                if (change.isConsumed) {
                    gestureRejected = true
                    isHorizontalSwipe = false
                    continue
                }

                totalDragX += delta.x
                totalDragY += delta.y

                if (!isHorizontalSwipe) {
                    val absX = abs(totalDragX)
                    val absY = abs(totalDragY)
                    if (absY > touchSlop && absY >= absX * 0.75f) {
                        gestureRejected = true
                    } else if (absX > touchSlop && absX > absY * 1.6f) {
                        val candidateRoute = resolveAdjacentMainRoute(
                            currentRoute = currentRoute,
                            dragDeltaX = totalDragX,
                            isRtl = isRtl
                        )
                        if (candidateRoute != null) {
                            isHorizontalSwipe = true
                            change.consume()
                        } else {
                            gestureRejected = true
                        }
                    }
                } else {
                    change.consume()
                }
            }
        }
    }
}

