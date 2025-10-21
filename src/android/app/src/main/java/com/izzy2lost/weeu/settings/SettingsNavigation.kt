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

private object SettingsRoutes {
    @Serializable
    object GeneralSettings

    @Serializable
    object GeneralSettingsScreenRoute

    @Serializable
    object InputSettingsRoute

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
    object InputSettingsScreenRoute

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
                    goToGeneralSettings = { navController.navigate(SettingsRoutes.GeneralSettings) },
                    goToInputSettings = { navController.navigate(SettingsRoutes.InputSettingsRoute) },
                    goToGraphicsSettings = { navController.navigate(SettingsRoutes.GraphicsSettingsScreenRoute) },
                    goToAudioSettings = { navController.navigate(SettingsRoutes.AudioSettingsScreenRoute) },
                    goToOverlaySettings = { navController.navigate(SettingsRoutes.OverlaySettingsScreenRoute) },
                    goToAccountSettings = { navController.navigate(SettingsRoutes.AccountSettingsScreenRoute) }
                )
            )
        }
        composable<SettingsRoutes.AudioSettingsScreenRoute> {
            AudioSettingsScreen(
                navigateBack = { navController.popBackStack() },
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
        composable<SettingsRoutes.OverlaySettingsScreenRoute> {
            OverlaySettingsScreen(
                navigateBack = { navController.popBackStack() },
            )
        }
        navigation<SettingsRoutes.InputSettingsRoute>(startDestination = SettingsRoutes.InputSettingsScreenRoute) {
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
    }
}