package com.shishusneh

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.shishusneh.data.repository.UserPreferencesRepository
import com.shishusneh.navigation.Screen
import com.shishusneh.navigation.bottomNavItems
import com.shishusneh.ui.feeding.FeedingScreen
import com.shishusneh.ui.feeding.GenAIChatScreen
import com.shishusneh.ui.growth.GrowthScreen
import com.shishusneh.ui.home.HomeScreen
import com.shishusneh.ui.milestone.MilestoneScreen
import com.shishusneh.ui.onboarding.BabyProfileSetupScreen
import com.shishusneh.ui.onboarding.LanguageSelectionScreen
import com.shishusneh.ui.onboarding.OnboardingScreen
import com.shishusneh.ui.onboarding.OnboardingViewModel
import com.shishusneh.ui.profile.EditProfileScreen
import com.shishusneh.ui.settings.SettingsScreen
import com.shishusneh.ui.theme.ShishuSnehTheme
import com.shishusneh.ui.vaccination.VaccinationScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.compose.ui.graphics.Brush
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var prefsRepo: UserPreferencesRepository

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Request notification permission for Android 13+
        requestNotificationPermission()

        setContent {
            val onboardingViewModel: OnboardingViewModel = hiltViewModel()
            val hasProfile by onboardingViewModel.hasProfile.collectAsState()
            val isDarkTheme by prefsRepo.isDarkMode.collectAsState(initial = false)

            // Keep splash screen while checking profile
            splashScreen.setKeepOnScreenCondition { hasProfile == null }

            ShishuSnehTheme(darkTheme = isDarkTheme) {
                hasProfile?.let { profileExists ->
                    val navController = rememberNavController()
                    val startDestination = if (profileExists) {
                        Screen.Home.route
                    } else {
                        Screen.LanguageSelection.route
                    }

                    ShishuSnehApp(
                        navController = navController,
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShishuSnehApp(
    navController: NavHostController,
    startDestination: String
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    // Bottom nav visible only on main screens
    val showBottomBar = currentRoute in bottomNavItems.map { it.screen.route }

    // Only enable drawer gesture on home screen
    val gesturesEnabled = currentRoute == Screen.Home.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                drawerTonalElevation = 0.dp,
                modifier = Modifier.width(300.dp)
            ) {
                // === Drawer Header ===
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primaryContainer.copy(0.5f),
                                    MaterialTheme.colorScheme.surface
                                )
                            )
                        )
                        .padding(24.dp)
                        .padding(top = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Baby avatar
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer.copy(0.4f),
                        modifier = Modifier.size(80.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.img_baby_avatar),
                            contentDescription = "Baby",
                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Shishu-Sneh",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        stringResource(R.string.app_tagline),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(Modifier.height(8.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(0.3f))
                Spacer(Modifier.height(8.dp))

                // === Menu Items ===
                NavigationDrawerItem(
                    icon = { Icon(Icons.Outlined.Person, null) },
                    label = { Text(stringResource(R.string.edit_profile)) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.EditProfile.route)
                    },
                    modifier = Modifier.padding(horizontal = 12.dp),
                    shape = RoundedCornerShape(12.dp)
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Outlined.Settings, null) },
                    label = { Text(stringResource(R.string.settings)) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.Settings.route)
                    },
                    modifier = Modifier.padding(horizontal = 12.dp),
                    shape = RoundedCornerShape(12.dp)
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Outlined.Lock, null) },
                    label = { Text(stringResource(R.string.privacy)) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() } },
                    modifier = Modifier.padding(horizontal = 12.dp),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(Modifier.weight(1f))

                // Footer
                Text(
                    stringResource(R.string.version_label),
                    Modifier.padding(24.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                if (showBottomBar) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        tonalElevation = 0.dp
                    ) {
                        bottomNavItems.forEach { item ->
                            val isSelected = currentRoute == item.screen.route
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = {
                                    if (currentRoute != item.screen.route) {
                                        navController.navigate(item.screen.route) {
                                            popUpTo(Screen.Home.route) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                },
                                icon = { Icon(item.icon, contentDescription = stringResource(item.labelResId)) },
                                label = {
                                    Text(
                                        stringResource(item.labelResId),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                        maxLines = 1
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                                )
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(paddingValues),
                enterTransition = { fadeIn(animationSpec = tween(300)) },
                exitTransition = { fadeOut(animationSpec = tween(300)) }
            ) {
                // Onboarding flow
                composable(Screen.LanguageSelection.route) {
                    LanguageSelectionScreen(
                        onLanguageSelected = { navController.navigate(Screen.Onboarding.route) }
                    )
                }
                composable(Screen.Onboarding.route) {
                    OnboardingScreen(
                        onComplete = { navController.navigate(Screen.ProfileSetup.route) }
                    )
                }
                composable(Screen.ProfileSetup.route) {
                    BabyProfileSetupScreen(
                        onProfileCreated = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.LanguageSelection.route) { inclusive = true }
                            }
                        }
                    )
                }

                // Main screens
                composable(Screen.Home.route) {
                    HomeScreen(
                        onNavigateToGrowth = { navController.navigate(Screen.Growth.route) },
                        onNavigateToVaccination = { navController.navigate(Screen.Vaccination.route) },
                        onNavigateToMilestones = { navController.navigate(Screen.Milestones.route) },
                        onNavigateToFeeding = { navController.navigate(Screen.Feeding.route) },
                        onOpenDrawer = { scope.launch { drawerState.open() } }
                    )
                }
                composable(Screen.Growth.route) { GrowthScreen() }
                composable(Screen.Vaccination.route) { VaccinationScreen() }
                composable(Screen.Milestones.route) { MilestoneScreen() }
                composable(Screen.Feeding.route) {
                    FeedingScreen(
                        onNavigateToChat = { navController.navigate(Screen.GenAIChat.route) }
                    )
                }
                composable(Screen.GenAIChat.route) {
                    GenAIChatScreen(onBack = { navController.popBackStack() })
                }
                composable(Screen.Settings.route) {
                    SettingsScreen(
                        onBack = { navController.popBackStack() },
                        onToggleDarkTheme = { /* handled by DataStore reactively */ },
                        onNavigateToEditProfile = { navController.navigate(Screen.EditProfile.route) }
                    )
                }
                composable(Screen.EditProfile.route) {
                    EditProfileScreen(
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
