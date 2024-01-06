package com.hifi.redeal.memo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.hifi.redeal.MainActivity
import com.hifi.redeal.memo.components.PhotoDetailScreen
import com.hifi.redeal.memo.repository.PhotoMemoRepository
import com.hifi.redeal.theme.RedealTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PhotoDetailFragment : Fragment() {
    @Inject
    lateinit var photoMemoRepository: PhotoMemoRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(
            requireContext()
        ).apply {
            val mainActivity = activity as MainActivity
            val imgSrcArr = arguments?.getStringArrayList("srcArr")!!
            val imgOrder = arguments?.getInt("order")!!

            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                RedealTheme {
                    PhotoDetailScreen(
                        imgOrder = imgOrder,
                        imgSrcArr = imgSrcArr,
                        repository = photoMemoRepository,
                        mainActivity = mainActivity
                    )
                }
            }
        }
    }
}
