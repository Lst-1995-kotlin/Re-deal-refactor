package com.hifi.redeal.memo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hifi.redeal.memo.ui.photoMemo.PhotoDetailDestination
import com.hifi.redeal.memo.ui.photoMemo.PhotoDetailScreen
import com.hifi.redeal.memo.ui.photoMemo.PhotoMemoDestination
import com.hifi.redeal.memo.ui.photoMemo.PhotoMemoEntryDestination
import com.hifi.redeal.memo.ui.photoMemo.PhotoMemoEntryRoute
import com.hifi.redeal.memo.ui.photoMemo.PhotoMemoRoute

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
        val onClickPhoto = { imageUris:String, order:Int ->
            navController.navigate("${PhotoDetailDestination.route}/${imageUris}/${order}")
        }
        composable(
            route = PhotoMemoDestination.routeWithArgs,
            arguments = listOf(navArgument(PhotoMemoDestination.clientIdArg) {
                type = NavType.IntType
                defaultValue = clientId
            })
        ) {
            PhotoMemoRoute(
                onBackClick = onClickRemoveFragment,
                onFabClick = {
                    navController.navigate("${PhotoMemoEntryDestination.route}/${clientId}")
                },
                onClickPhoto = onClickPhoto
            )
        }
        composable(
            route = PhotoMemoEntryDestination.routeWithArgs,
            arguments = listOf(navArgument(PhotoMemoEntryDestination.clientIdArg) {
                type = NavType.IntType
                defaultValue = clientId
            })
        ) {
            PhotoMemoEntryRoute(
                onBackClick = navController::popBackStack,
                onClickPhoto = onClickPhoto
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