package com.hifi.redeal.memo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.compose.rememberNavController
import androidx.navigation.findNavController
import com.hifi.redeal.memo.navigation.RecordMemoNavHost
import com.hifi.redeal.theme.RedealTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordMemoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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
                    RecordMemoNavHost(
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