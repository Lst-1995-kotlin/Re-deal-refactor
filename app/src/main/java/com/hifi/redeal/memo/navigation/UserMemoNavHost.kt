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
import com.hifi.redeal.memo.ui.userMemo.UserMemoDestination
import com.hifi.redeal.memo.ui.userMemo.UserMemoRoute

@Composable
fun UserMemoNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = UserMemoDestination.route,
        modifier = modifier
    ) {
        val onClickPhoto = { imageUris:String, order:Int ->
            navController.navigate("${PhotoDetailDestination.route}/${imageUris}/${order}")
        }
        composable(
            route = UserMemoDestination.route
        ) {
            UserMemoRoute(onClickPhoto = onClickPhoto)
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