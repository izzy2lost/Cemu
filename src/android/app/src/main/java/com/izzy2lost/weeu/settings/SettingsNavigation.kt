package com.izzy2lost.weeu.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.izzy2lost.weeu.settings.account.AccountSettingsScreen
import com.izzy2lost.weeu.settings.audio.AudioSettingsScreen
import com.izzy2lost.weeu.settings.customdrivers.CustomDriversScreen
import com.izzy2lost.weeu.settings.gamespath.GamePathsScreen
import com.izzy2lost.weeu.settings.general.GeneralSettingsScreen
import com.izzy2lost.weeu.settings.graphics.GraphicsSettingsScreen
import com.izzy2lost.weeu.settings.input.ControllerInputSettingsScreen
import com.izzy2lost.weeu.settings.input.InputSettingsScreen
import com.izzy2lost.weeu.settings.input.InputSettingsScreenActions
import com.izzy2lost.weeu.settings.inputoverlay.InputOverlaySettingsScreen
import com.izzy2lost.weeu.settings.overlay.OverlaySettingsScreen
import kotlinx.serialization.Serializable

@Serializable
object SettingsRoute

@Serializable
object GeneralSettingsRoute

@Serializable
object InputSettingsRoute

@Serializable
object AudioSettingsRoute

@Serializable
object GraphicsSettingsRoute

@Serializable
object OverlaySettingsRoute

@Serializable
object AccountSettingsRoute

private object SettingsRoutes {
    @Serializable
    object GeneralSettings

    @Serializable
    object GeneralSettingsScreenRoute

    @Serializable
    object InputSettingsRouteInternal

    @Serializable
    object InputSettingsScreenRoute

    @Serializable
    object SettingsHomeScreenRoute

    @Serializable
    object AudioSettingsScreenRoute

    @Serializable
    object GraphicsSettingsScreenRoute

    @Serializable
    object CustomDriversScreenRoute

    @Serializable
    object GamePathsScreenRoute

    @Serializable
    object OverlaySettingsScreenRoute

    @Serializable
    data class ControllerInputSettingsScreenRoute(val index: Int)

    @Serializable
    object InputOverlaySettingsScreenRoute

    @Serializable
    object AccountSettingsScreenRoute
}

fun NavGraphBuilder.settingsNavigation(navController: NavHostController) {
    navigation<SettingsRoute>(startDestination = SettingsRoutes.SettingsHomeScreenRoute) {
        composable<SettingsRoutes.SettingsHomeScreenRoute> {
            SettingsHomeScreen(
                navigateBack = { navController.popBackStack() },
                actions = SettingsHomeScreenActions(
                    goToGeneralSettings = { navController.navigate(GeneralSettingsRoute) },
                    goToInputSettings = { navController.navigate(InputSettingsRoute) },
                    goToGraphicsSettings = { navController.navigate(GraphicsSettingsRoute) },
                    goToAudioSettings = { navController.navigate(AudioSettingsRoute) },
                    goToOverlaySettings = { navController.navigate(OverlaySettingsRoute) },
                    goToAccountSettings = { navController.navigate(AccountSettingsRoute) }
                )
            )
        }
        composable<AudioSettingsRoute> {
            AudioSettingsScreen(
                navigateBack = { navController.popBackStack() },
            )
        }
        composable<SettingsRoutes.AudioSettingsScreenRoute> {
            AudioSettingsScreen(
                navigateBack = { navController.popBackStack() },
            )
        }
        composable<GraphicsSettingsRoute> {
            GraphicsSettingsScreen(
                navigateBack = { navController.popBackStack() },
                goToCustomDriversSettings = {
                    navController.navigate(SettingsRoutes.CustomDriversScreenRoute)
                }
            )
        }
        composable<SettingsRoutes.GraphicsSettingsScreenRoute> {
            GraphicsSettingsScreen(
                navigateBack = { navController.popBackStack() },
                goToCustomDriversSettings = {
                    navController.navigate(SettingsRoutes.CustomDriversScreenRoute)
                }
            )
        }
        composable<SettingsRoutes.CustomDriversScreenRoute> {
            CustomDriversScreen(
                navigateBack = { navController.popBackStack() },
            )
        }
        composable<OverlaySettingsRoute> {
            OverlaySettingsScreen(
                navigateBack = { navController.popBackStack() },
            )
        }
        composable<SettingsRoutes.OverlaySettingsScreenRoute> {
            OverlaySettingsScreen(
                navigateBack = { navController.popBackStack() },
            )
        }
        composable<AccountSettingsRoute> {
            AccountSettingsScreen(
                navigateBack = { navController.popBackStack() },
            )
        }
        composable<InputSettingsRoute> {
            InputSettingsScreen(
                navigateBack = { navController.popBackStack() },
                actions = InputSettingsScreenActions(
                    goToInputOverlaySettings = {
                        navController.navigate(SettingsRoutes.InputOverlaySettingsScreenRoute)
                    },
                    goToControllerSettings = { controllerIndex ->
                        navController.navigate(
                            SettingsRoutes.ControllerInputSettingsScreenRoute(
                                controllerIndex
                            )
                        )
                    },
                )
            )
        }
        navigation<SettingsRoutes.InputSettingsRouteInternal>(startDestination = SettingsRoutes.InputSettingsScreenRoute) {
            composable<SettingsRoutes.ControllerInputSettingsScreenRoute> { navBackStackEntry ->
                val controllerIndex =
                    navBackStackEntry.toRoute<SettingsRoutes.ControllerInputSettingsScreenRoute>().index
                ControllerInputSettingsScreen(
                    navigateBack = { navController.popBackStack() },
                    controllerIndex = controllerIndex,
                )
            }
            composable<SettingsRoutes.InputOverlaySettingsScreenRoute> {
                InputOverlaySettingsScreen(
                    navigateBack = { navController.popBackStack() }
                )
            }
            composable<SettingsRoutes.InputSettingsScreenRoute> {
                InputSettingsScreen(
                    navigateBack = { navController.popBackStack() },
                    actions = InputSettingsScreenActions(
                        goToInputOverlaySettings = {
                            navController.navigate(SettingsRoutes.InputOverlaySettingsScreenRoute)
                        },
                        goToControllerSettings = { controllerIndex ->
                            navController.navigate(
                                SettingsRoutes.ControllerInputSettingsScreenRoute(
                                    controllerIndex
                                )
                            )
                        },
                    )
                )
            }
        }
        composable<GeneralSettingsRoute> {
            GeneralSettingsScreen(
                navigateBack = { navController.popBackStack() },
                goToGamePathsSettings = { navController.navigate(SettingsRoutes.GamePathsScreenRoute) }
            )
        }
        navigation<SettingsRoutes.GeneralSettings>(startDestination = SettingsRoutes.GeneralSettingsScreenRoute) {
            composable<SettingsRoutes.GeneralSettingsScreenRoute> {
                GeneralSettingsScreen(
                    navigateBack = { navController.popBackStack() },
                    goToGamePathsSettings = { navController.navigate(SettingsRoutes.GamePathsScreenRoute) }
                )
            }
            composable<SettingsRoutes.GamePathsScreenRoute> {
                GamePathsScreen(
                    navigateBack = { navController.popBackStack() },
                )
            }
        }

        composable<SettingsRoutes.AccountSettingsScreenRoute> {
            AccountSettingsScreen(
                navigateBack = { navController.popBackStack() },
            )
        }
        composable<SettingsRoutes.AccountSettingsScreenRoute> {
            AccountSettingsScreen(
                navigateBack = { navController.popBackStack() },
            )
        }
    }
}