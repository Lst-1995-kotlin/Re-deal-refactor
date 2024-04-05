package com.hifi.redeal.memo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.compose.rememberNavController
import androidx.navigation.fragment.findNavController
import com.hifi.redeal.memo.navigation.PhotoMemoNavHost
import com.hifi.redeal.theme.RedealTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoMemoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(
            requireContext()
        ).apply {
            val clientIdx = arguments?.getInt("clientIdx") ?: 1
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                RedealTheme {
                    PhotoMemoNavHost(
                        navController = rememberNavController(),
                        clientId = clientIdx,
                        onClickRemoveFragment = {
                            findNavController().popBackStack()
                        }
                    )
                }
            }
        }
    }
}