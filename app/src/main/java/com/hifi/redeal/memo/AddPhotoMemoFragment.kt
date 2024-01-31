package com.hifi.redeal.memo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hifi.redeal.MainActivity
import com.hifi.redeal.memo.components.AddPhotoMemoScreen
import com.hifi.redeal.memo.repository.PhotoMemoRepository
import com.hifi.redeal.memo.vm.PhotoMemoViewModel
import com.hifi.redeal.theme.RedealTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddPhotoMemoFragment : Fragment() {
    @Inject lateinit var photoMemoRepository: PhotoMemoRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(
            requireContext()
        ).apply{
            val photoMemoViewModel: PhotoMemoViewModel by viewModels()
            val mainActivity = activity as MainActivity
            val clientIdx = arguments?.getLong("clientIdx")?:1L
            photoMemoViewModel.getPhotoMemoList(clientIdx)

            requireActivity().window.apply {
                WindowCompat.setDecorFitsSystemWindows(this, false)
            }

            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                RedealTheme {
                    AddPhotoMemoScreen(
                        clientIdx = clientIdx,
                        repository = photoMemoRepository,
                        mainActivity = mainActivity
                        )
                }
            }
        }
    }
}