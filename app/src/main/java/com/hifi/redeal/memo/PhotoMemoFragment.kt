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
import com.hifi.redeal.memo.components.PhotoMemoScreen
import com.hifi.redeal.memo.repository.PhotoMemoRepository
import com.hifi.redeal.memo.vm.PhotoMemoViewModel
import com.hifi.redeal.theme.RedealTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PhotoMemoFragment : Fragment() {
    @Inject
    lateinit var photoMemoRepository: PhotoMemoRepository
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(
            requireContext()
        ).apply {
            val photoMemoViewModel: PhotoMemoViewModel by viewModels()
            val mainActivity = activity as MainActivity
            val clientIdx = arguments?.getLong("clientIdx") ?: 1L
            photoMemoViewModel.getPhotoMemoList(clientIdx)
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                RedealTheme {
                    PhotoMemoScreen(
                        photoMemoViewModel = photoMemoViewModel,
                        repository = photoMemoRepository,
                        mainActivity = mainActivity,
                        clientIdx = clientIdx
                    )
                }
            }
        }
    }
}