package com.hifi.redeal.memo

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.hifi.redeal.MainActivity
import com.hifi.redeal.memo.components.AddRecordMemoScreen
import com.hifi.redeal.memo.repository.RecordMemoRepository
import com.hifi.redeal.theme.RedealTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddRecordMemoFragment : Fragment() {
    @Inject
    lateinit var recordMemoRepository: RecordMemoRepository

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(
            requireContext()
        ).apply {
            val clientIdx = arguments?.getLong("clientIdx")!!
            val mainActivity = activity as MainActivity

            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                RedealTheme {
                    AddRecordMemoScreen(
                        clientIdx = clientIdx,
                        mainActivity = mainActivity
                    )
                }
            }
        }
    }
}
