package com.hifi.redeal.memo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hifi.redeal.memo.components.PhotoDetailDestination
import com.hifi.redeal.memo.components.PhotoDetailScreen
import com.hifi.redeal.memo.components.PhotoMemoDestination
import com.hifi.redeal.memo.components.PhotoMemoEntryDestination
import com.hifi.redeal.memo.components.PhotoMemoEntryScreen
import com.hifi.redeal.memo.components.PhotoMemoScreen

@Composable
fun PhotoMemoNavHost(
    navController: NavHostController,
    clientId: Int,
    modifier: Modifier = Modifier,
    onClickRemoveFragment: () -> Unit = {},
) {
    NavHost(
        navController = navController,
        startDestination = PhotoMemoDestination.routeWithArgs,
        modifier = modifier
    ) {
        composable(
            route = PhotoMemoDestination.routeWithArgs,
            arguments = listOf(navArgument(PhotoMemoDestination.clientIdArg) {
                type = NavType.IntType
                defaultValue = clientId
            })
        ) {
            PhotoMemoScreen(
                onBackClick = onClickRemoveFragment,
                onFabClick = {
                    navController.navigate("${PhotoMemoEntryDestination.route}/${clientId}")
                },
                onClickPhoto = { imageUris, order ->
                    navController.navigate("${PhotoDetailDestination.route}/${imageUris}/${order}")
                }
            )
        }
        composable(
            route = PhotoMemoEntryDestination.routeWithArgs,
            arguments = listOf(navArgument(PhotoMemoEntryDestination.clientIdArg) {
                type = NavType.IntType
                defaultValue = clientId
            })
        ) {
            PhotoMemoEntryScreen(
                onBackClick = navController::popBackStack,
            )
        }
        composable(
            route = PhotoDetailDestination.routeWithArgs,
            arguments = listOf(
                navArgument(PhotoDetailDestination.imageUris) {
                    type = NavType.StringType
                },
                navArgument(PhotoDetailDestination.initialOrder) {
                    type = NavType.IntType
                }
            )
        ) {
            val imageUris: List<String> =
                it.arguments?.getString(PhotoDetailDestination.imageUris)?.split(",") ?: emptyList()
            val initialOrder: Int = it.arguments?.getInt(PhotoDetailDestination.initialOrder) ?: 0
            PhotoDetailScreen(
                onBackClick = navController::popBackStack,
                initialOrder = initialOrder,
                imageUris = imageUris,
            )
        }
    }
}