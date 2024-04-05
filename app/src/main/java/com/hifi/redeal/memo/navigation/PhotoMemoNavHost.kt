package com.hifi.redeal.memo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hifi.redeal.memo.components.AddPhotoMemoDestination
import com.hifi.redeal.memo.components.AddPhotoMemoScreen
import com.hifi.redeal.memo.components.PhotoDetailDestination
import com.hifi.redeal.memo.components.PhotoDetailScreen
import com.hifi.redeal.memo.components.PhotoMemoDestination
import com.hifi.redeal.memo.components.PhotoMemoScreen

@Composable
fun PhotoMemoNavHost(
    navController: NavHostController,
    clientId: Int,
    modifier: Modifier = Modifier
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
                onBackClick = navController::popBackStack,
                onFabClick = {
                    navController.navigate("${AddPhotoMemoDestination.route}/${clientId}")
                },
                onPhotoMemoClick = {
                    navController.navigate("${PhotoDetailDestination.route}/${it}")
                }
            )
        }
        composable(
            route = AddPhotoMemoDestination.routeWithArgs,
            arguments = listOf(navArgument(AddPhotoMemoDestination.clientIdArg) {
                type = NavType.IntType
            })
        ) {
            AddPhotoMemoScreen(
                onBackClick = navController::popBackStack,
            )
        }
        composable(
            route = PhotoDetailDestination.routeWithArgs,
            arguments = listOf(navArgument(PhotoDetailDestination.photoMemoId) {
                type = NavType.IntType
            })
        ) {
            PhotoDetailScreen(
                onBackClick = navController::popBackStack,
                imgOrder = 0,
                imgSrcArr = emptyList(),
            )
        }
    }
}