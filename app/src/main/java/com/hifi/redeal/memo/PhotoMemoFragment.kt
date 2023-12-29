package com.hifi.redeal.memo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hifi.redeal.MainActivity
import com.hifi.redeal.databinding.FragmentPhotoMemoBinding
import com.hifi.redeal.memo.components.PhotoMemoScreen
import com.hifi.redeal.memo.repository.PhotoMemoRepository
import com.hifi.redeal.memo.vm.PhotoMemoViewModel
import com.hifi.redeal.theme.RedealTheme
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import javax.inject.Inject

@AndroidEntryPoint
class PhotoMemoFragment : Fragment() {
    @Inject lateinit var photoMemoRepository:PhotoMemoRepository
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return ComposeView(
            requireContext()
        ).apply{
            val photoMemoViewModel: PhotoMemoViewModel by viewModels()
            val mainActivity = activity as MainActivity
            val clientIdx = arguments?.getLong("clientIdx")?:1L
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
                        clientIdx = clientIdx)
                }
            }
        }
//        val binding = DataBindingUtil.inflate<FragmentPhotoMemoBinding>(
//            inflater,
//            R.layout.fragment_photo_memo,
//            container,
//            false
//        ).apply{
//            composeView.apply {
//                setViewCompositionStrategy(
//                    ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
//                )
//                setContent {
//                    MaterialTheme {
//                        PlantDetailDescription(plantDetailViewModel)
//                    }
//                }
//            }
//        }
//        fragmentPhotoMemoBinding = FragmentPhotoMemoBinding.inflate(inflater)
//        mainActivity = activity as MainActivity
//
//        clientIdx = arguments?.getLong("clientIdx")?:1L
//        photoMemoViewModel.run{
//            photoMemoList.observe(viewLifecycleOwner){
//                fragmentPhotoMemoBinding.photoMemoRecyclerView.adapter?.notifyDataSetChanged()
//            }
//        }

//        fragmentPhotoMemoBinding.run{
//            photoMemoViewModel.getPhotoMemoList(clientIdx)
//            photoMemoToolbar.run{
//                setNavigationOnClickListener {
//                    mainActivity.removeFragment(MainActivity.PHOTO_MEMO_FRAGMENT)
//                }
//            }
//            photoMemoAddBtn.setOnClickListener {
//                val newBundle = Bundle()
//                newBundle.putLong("clientIdx", clientIdx)
//                mainActivity.replaceFragment(MainActivity.ADD_PHOTO_MEMO_FRAGMENT, true, newBundle)
//            }
//            photoMemoRecyclerView.run{
//                adapter = PhotoMemoRecyclerAdapter()
//                layoutManager = LinearLayoutManager(context)
//                addItemDecoration(DividerItemDecoration(mainActivity, DividerItemDecoration.VERTICAL))
//            }
//        }
//        return fragmentPhotoMemoBinding.root
        //return binding.root
    }

//    inner class PhotoMemoRecyclerAdapter:RecyclerView.Adapter<PhotoMemoRecyclerAdapter.PhotoMemoViewHolder>(){
//        inner class PhotoMemoViewHolder(rowPhotoMemoBinding: RowPhotoMemoBinding):RecyclerView.ViewHolder(rowPhotoMemoBinding.root){
//            private val imageListLayout = rowPhotoMemoBinding.imageListLayout
//            private val photoDateTextView = rowPhotoMemoBinding.photoDateTextView
//            private val photoMemoTextView = rowPhotoMemoBinding.photoMemoTextView
//            fun bindItem(item:PhotoMemoData){
//                photoDateTextView.text = intervalBetweenDateText(dateFormat.format(item.date.toDate()))
//                photoMemoTextView.text = item.context.ifEmpty { "메모를 등록하지 않았어요" }
//                var linearLayoutHorizontal = LinearLayout(requireContext())
//                linearLayoutHorizontal.orientation = LinearLayout.HORIZONTAL
//                val layoutParams = LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//                )
//                layoutParams.setMargins(0,0,0,dpToPx(requireContext(), 6))
//                linearLayoutHorizontal.layoutParams = layoutParams
//                imageListLayout.removeAllViews()
//                for(i in 0 until item.srcArr.size){
//                    if(i != 0 && i % 3 == 0) {
//                        imageListLayout.addView(linearLayoutHorizontal)
//                        val newLinearLayoutHorizontal = LinearLayout(requireContext())
//                        linearLayoutHorizontal.orientation = LinearLayout.HORIZONTAL
//                        val layoutParams = LinearLayout.LayoutParams(
//                            LinearLayout.LayoutParams.WRAP_CONTENT,
//                            LinearLayout.LayoutParams.WRAP_CONTENT
//                        )
//                        layoutParams.setMargins(0,0,0,dpToPx(requireContext(), 6))
//                        linearLayoutHorizontal.layoutParams = layoutParams
//                        linearLayoutHorizontal = newLinearLayoutHorizontal
//                    }
//                    val imageViewLayoutParams = ViewGroup.MarginLayoutParams(
//                        dpToPx(requireContext(), 100),
//                        dpToPx(requireContext(), 100)
//                    )
//                    if(i%3 != 0) {
//                        imageViewLayoutParams.marginStart = dpToPx(requireContext(), 6)
//                    }
//                    val imageView = ImageView(requireContext())
//                    imageView.layoutParams = imageViewLayoutParams
//                    imageView.setImageResource(R.drawable.empty_photo)
//                    imageView.setOnClickListener {
//                        val newBundle = Bundle()
//                        newBundle.putInt("order", i)
//                        newBundle.putStringArrayList("srcArr", item.srcArr as ArrayList<String>)
//                        mainActivity.replaceFragment(MainActivity.PHOTO_DETAIL_FRAGMENT, true, newBundle)
//                    }
//                    linearLayoutHorizontal.addView(imageView)
//                    photoMemoRepository.getPhotoMemoImgUrl(item.srcArr[i] as String){url ->
//                        Glide.with(imageView)
//                            .load(url)
//                            .into(imageView)
//                    }
//                }
//                imageListLayout.addView(linearLayoutHorizontal)
//            }
//        }
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoMemoViewHolder {
//            val rowPhotoMemoBinding = RowPhotoMemoBinding.inflate(layoutInflater)
//            val photoMemoViewHolder = PhotoMemoViewHolder(rowPhotoMemoBinding)
//
//            rowPhotoMemoBinding.root.layoutParams = ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//            )
//
//            return photoMemoViewHolder
//        }
//
//        override fun getItemCount(): Int {
//            return photoMemoViewModel.photoMemoList.value?.size!!
//        }
//
//        override fun onBindViewHolder(holder: PhotoMemoViewHolder, position: Int) {
//            val item = photoMemoViewModel.photoMemoList.value?.get(position)!!
//            holder.bindItem(item)
//        }
//    }
}