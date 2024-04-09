package com.hifi.redeal.memo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hifi.redeal.memo.ui.recordMemo.RecordMemoDestination
import com.hifi.redeal.memo.ui.recordMemo.RecordMemoEntryDestination
import com.hifi.redeal.memo.ui.recordMemo.RecordMemoEntryRoute
import com.hifi.redeal.memo.ui.recordMemo.RecordMemoRoute

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
            RecordMemoRoute(
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
            RecordMemoEntryRoute(
                onBackClick = navController::popBackStack
            )
        }
    }
}