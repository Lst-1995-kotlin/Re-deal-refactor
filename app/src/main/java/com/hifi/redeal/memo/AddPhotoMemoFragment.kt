package com.hifi.redeal.memo

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hifi.redeal.MainActivity
import com.hifi.redeal.R
import com.hifi.redeal.databinding.FragmentAddPhotoMemoBinding
import com.hifi.redeal.memo.components.AddPhotoMemoScreen
import com.hifi.redeal.memo.components.PhotoMemoScreen
import com.hifi.redeal.memo.repository.PhotoMemoRepository
import com.hifi.redeal.memo.utils.dpToPx
import com.hifi.redeal.memo.vm.PhotoMemoViewModel
import com.hifi.redeal.theme.RedealTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddPhotoMemoFragment : Fragment() {
    private lateinit var mainActivity: MainActivity
    private var uriList = mutableListOf<Uri>()
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
//        fragmentAddPhotoMemoBinding.run {
//            addPhotoMemoBtn.run {
//                isEnabled = false
//                setBackgroundResource(R.drawable.add_button_loading_container)
//                text = "사진을 등록해주세요."
//                setOnClickListener {
//                    val photoMemoContext = photoMemoTextInputEditText.text.toString()
//                    addPhotoMemoBtn.isEnabled = false
//                    addPhotoMemoBtn.setBackgroundResource(R.drawable.add_button_loading_container)
//                    addPhotoMemoBtn.text = "등록 중 ..."
//                    addPhotoMemoBtn.setTextColor(
//                        ContextCompat.getColor(
//                            requireContext(),
//                            R.color.primary20
//                        )
//                    )
//                    photoMemoRepository.addPhotoMemo(clientIdx, photoMemoContext, uriList) {
//                        mainActivity.removeFragment(MainActivity.ADD_PHOTO_MEMO_FRAGMENT)
//                    }
//                }
//            }
//        }
//        return fragmentAddPhotoMemoBinding.root
    }
}