package com.hifi.redeal.memo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hifi.redeal.MainActivity
import com.hifi.redeal.R
import com.hifi.redeal.databinding.FragmentMemoBinding
import com.hifi.redeal.memo.adapters.UserPhotoMemoListAdapter
import com.hifi.redeal.memo.adapters.UserRecordMemoListAdapter
import com.hifi.redeal.memo.repository.MemoRepository
import com.hifi.redeal.memo.vm.MemoViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MemoFragment : Fragment() {
    private lateinit var fragmentMemoBinding: FragmentMemoBinding
    private lateinit var mainActivity: MainActivity
    private val memoViewModel: MemoViewModel by viewModels()
    @Inject lateinit var memoRepository: MemoRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentMemoBinding = FragmentMemoBinding.inflate(inflater)
       mainActivity = activity as MainActivity

        val colorPrimary10 = ContextCompat.getColor(requireContext(), R.color.primary10)
        val colorPrimary80 = ContextCompat.getColor(requireContext(), R.color.primary80)

        memoViewModel.run{
            userPhotoMemoList.observe(viewLifecycleOwner){
                fragmentMemoBinding.userPhotoMemoRecyclerView.adapter?.notifyDataSetChanged()
            }
            userRecordMemoList.observe(viewLifecycleOwner){
                fragmentMemoBinding.userRecordMemoRecyclerView.adapter?.notifyDataSetChanged()
            }
        }

        val userPhotoMemoListAdapter = UserPhotoMemoListAdapter(mainActivity, memoViewModel, memoRepository)
        val userRecordMemoListAdapter = UserRecordMemoListAdapter(mainActivity, memoViewModel, memoRepository)

        fragmentMemoBinding.run{
            memoViewModel.getUserPhotoMemoList()
            memoViewModel.getUserRecordMemoList(mainActivity)
            memoViewSwitcher.displayedChild = 0
            memoToolbar.run{
                setNavigationOnClickListener {
                    UserRecordMemoListAdapter.resetAudio()
                    mainActivity.removeFragment(MainActivity.MEMO_FRAGMENT)
                }
            }
            photoMemoTabItem.setOnClickListener {
                UserRecordMemoListAdapter.resetAudio()
                memoViewSwitcher.displayedChild = 0
                photoMemoTabItem.setTextColor(colorPrimary10)
                recordMemoTabItem.setTextColor(colorPrimary80)
            }
            recordMemoTabItem.setOnClickListener {
                photoMemoTabItem.setTextColor(colorPrimary80)
                recordMemoTabItem.setTextColor(colorPrimary10)
                memoViewSwitcher.displayedChild = 1
            }
            userPhotoMemoRecyclerView.run{
                adapter = userPhotoMemoListAdapter
                layoutManager = LinearLayoutManager(context)
                addItemDecoration(DividerItemDecoration(mainActivity, DividerItemDecoration.VERTICAL))
            }
            userRecordMemoRecyclerView.run{
                adapter = userRecordMemoListAdapter
                layoutManager = LinearLayoutManager(context)
                addItemDecoration(DividerItemDecoration(mainActivity, DividerItemDecoration.VERTICAL))
            }
        }
        return fragmentMemoBinding.root
    }
}