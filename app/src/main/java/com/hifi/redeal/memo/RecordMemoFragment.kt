package com.hifi.redeal.memo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hifi.redeal.MainActivity
import com.hifi.redeal.memo.components.RecordMemoScreen
import com.hifi.redeal.memo.repository.RecordMemoRepository
import com.hifi.redeal.memo.vm.RecordMemoViewModel
import com.hifi.redeal.theme.RedealTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecordMemoFragment : Fragment() {
    @Inject
    lateinit var recordMemoRepository: RecordMemoRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(
            requireContext()
        ).apply {
            val recordMemoViewModel: RecordMemoViewModel by viewModels()
            val mainActivity = activity as MainActivity
            val clientIdx = arguments?.getLong("clientIdx") ?: 1L
            recordMemoViewModel.getRecordMemoList(clientIdx, mainActivity)
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                RedealTheme {
                    RecordMemoScreen(
                        recordMemoViewModel = recordMemoViewModel,
                        mainActivity = mainActivity,
                        clientIdx = clientIdx
                    )
                }
            }
        }
    }
}