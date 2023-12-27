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
    ): View? {
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
//        fragmentPhotoDetailBinding = FragmentPhotoDetailBinding.inflate(inflater)
//        mainActivity = activity as MainActivity
//        imgOrder = arguments?.getInt("order")!!
//        imgSrcArr = arguments?.getStringArrayList("srcArr")!!
//
//        photoMemoRepository.getPhotoMemoImgUrl(imgSrcArr[imgOrder]) { url ->
//            Glide.with(fragmentPhotoDetailBinding.detailImageView)
//                .load(url)
//                .into(fragmentPhotoDetailBinding.detailImageView)
//        }
//        gestureDetector = GestureDetector(requireContext(), SwipeGestureListener {
//            if (it == SwipeGestureListener.Direction.LEFT) {
//                if (imgOrder + 1 >= imgSrcArr.size) {
//                    return@SwipeGestureListener
//                }
//                imgOrder++
//            } else if (it == SwipeGestureListener.Direction.RIGHT) {
//                if (imgOrder - 1 < 0) {
//                    return@SwipeGestureListener
//                }
//                imgOrder--
//            }
//            photoMemoRepository.getPhotoMemoImgUrl(imgSrcArr[imgOrder]) { url ->
//                Glide.with(fragmentPhotoDetailBinding.detailImageView)
//                    .load(url)
//                    .into(fragmentPhotoDetailBinding.detailImageView)
//            }
//            if (previousView != null) {
//                previousView.background = null
//            }
//            previousView = fragmentPhotoDetailBinding.photoDetailRecyclerView[imgOrder]
//            fragmentPhotoDetailBinding.photoDetailRecyclerView[imgOrder].setBackgroundResource(R.drawable.row_photo_detail_border)
//        })
//
//        fragmentPhotoDetailBinding.run {
//            photoDetailToolbar.run {
//                setNavigationOnClickListener {
//                    mainActivity.removeFragment(MainActivity.PHOTO_DETAIL_FRAGMENT)
//                }
//            }
//            detailImageView.run {
//                setImageResource(R.drawable.empty_photo)
//                setOnTouchListener { _, event ->
//                    gestureDetector.onTouchEvent(event)
//                    true
//                }
//            }
//            photoDetailRecyclerView.run {
//                adapter = PhotoDetailRecyclerAdapter()
//                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//            }
//        }
//        return fragmentPhotoDetailBinding.root
//    }
//
//    inner class PhotoDetailRecyclerAdapter :
//        RecyclerView.Adapter<PhotoDetailRecyclerAdapter.PhotoDetailViewHolder>() {
//        inner class PhotoDetailViewHolder(rowPhotoDetailBinding: RowDetailPhotoBinding) :
//            RecyclerView.ViewHolder(rowPhotoDetailBinding.root) {
//            val rowImageView = rowPhotoDetailBinding.rowImageView
//
//            init {
//                rowImageView.setOnClickListener {
//                    imgOrder = adapterPosition
//                    photoMemoRepository.getPhotoMemoImgUrl(imgSrcArr[adapterPosition]) { url ->
//                        Glide.with(fragmentPhotoDetailBinding.detailImageView)
//                            .load(url)
//                            .into(fragmentPhotoDetailBinding.detailImageView)
//                    }
//                    if (previousView != null) {
//                        previousView.background = null
//                    }
//                    previousView = rowImageView
//                    rowImageView.setBackgroundResource(R.drawable.row_photo_detail_border)
//                }
//            }
//
//            fun bindItem(imgSrc: String) {
//                if (imgOrder == adapterPosition) {
//                    previousView = rowImageView
//                    rowImageView.setBackgroundResource(R.drawable.row_photo_detail_border)
//                }
//                rowImageView.setImageResource(R.drawable.empty_photo)
//                photoMemoRepository.getPhotoMemoImgUrl(imgSrc) { url ->
//                    Glide.with(rowImageView)
//                        .load(url)
//                        .into(rowImageView)
//                }
//            }
//        }
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoDetailViewHolder {
//            val rowPhotoDetailBinding = RowDetailPhotoBinding.inflate(layoutInflater)
//            val photoDetailViewHolder = PhotoDetailViewHolder(rowPhotoDetailBinding)
//
//            rowPhotoDetailBinding.root.layoutParams = ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//            )
//
//            return photoDetailViewHolder
//        }
//
//        override fun getItemCount(): Int {
//            return imgSrcArr.size
//        }
//
//        override fun onBindViewHolder(holder: PhotoDetailViewHolder, position: Int) {
//            val item = imgSrcArr[position]
//            holder.bindItem(item)
//        }
//    }
//}
