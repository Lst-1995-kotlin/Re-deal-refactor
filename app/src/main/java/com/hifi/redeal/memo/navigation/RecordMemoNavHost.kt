package com.hifi.redeal.memo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hifi.redeal.memo.components.RecordMemoDestination
import com.hifi.redeal.memo.components.RecordMemoEntryDestination
import com.hifi.redeal.memo.components.RecordMemoEntryScreen
import com.hifi.redeal.memo.components.RecordMemoScreen

@Composable
fun RecordMemoNavHost(
    navController: NavHostController,
    clientId:Int,
    modifier: Modifier = Modifier,
    onClickRemoveFragment: () -> Unit = {}
){
    NavHost(
        navController = navController,
        startDestination = RecordMemoDestination.routeWithArgs,
        modifier = modifier
    ){
        composable(
            route = RecordMemoDestination.routeWithArgs,
            arguments = listOf(navArgument(RecordMemoDestination.clientId){
                type = NavType.IntType
                defaultValue = clientId
            })
        ){
            RecordMemoScreen(
                onBackClick = onClickRemoveFragment,
                onFabClick = {
                    navController.navigate("${RecordMemoEntryDestination.route}/${clientId}")
                }
            )
        }

        composable(
            route = RecordMemoEntryDestination.routeWithArgs,
            arguments = listOf(navArgument(RecordMemoEntryDestination.clientId){
                type = NavType.IntType
            })
        ){
            RecordMemoEntryScreen(
                onBackClick = navController::popBackStack
            )
        }
    }
}