package com.hifi.redeal.account

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResultListener
import com.google.android.material.snackbar.Snackbar
import com.hifi.redeal.MainActivity
import com.hifi.redeal.R
import com.hifi.redeal.account.repository.model.ClientData
import com.hifi.redeal.account.repository.model.ClientInputData
import com.hifi.redeal.account.repository.AccountEditRepository
import com.hifi.redeal.databinding.FragmentAccountEditBinding

class AccountEditFragment : Fragment(){

    lateinit var mainActivity: MainActivity
    lateinit var fragmentAccountEditBinding: FragmentAccountEditBinding

    val accountEditRepository = AccountEditRepository()

    var clientIdx = 0L

    var create = false

    lateinit var client: ClientData

    val items = listOf("거래 중", "거래 시도", "거래 중지")

    var fromNotification = false

    var senderId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentAccountEditBinding = FragmentAccountEditBinding.inflate(layoutInflater)

        if (clientIdx == 0L)
            clientIdx = arguments?.getLong("clientIdx") ?: 0

        senderId = arguments?.getString("senderId") ?: ""

        fromNotification = arguments?.getBoolean("fromNotification") ?: false

        setFragmentResultListener("addressSearchResult") { _, bundle ->
            val data = bundle.getString("address")
            fragmentAccountEditBinding.textEditTextAccountEditZipCode.setText(data.toString())
        }

        val adapter = CustomArrayAdapter(items)

        fragmentAccountEditBinding.run {
            materialToolbarAccountEdit.setNavigationOnClickListener {
                mainActivity.removeFragment(MainActivity.ACCOUNT_EDIT_FRAGMENT)
//                findNavController().popBackStack()
            }

            textEditTextAccountEditState.run {

                setAdapter(adapter)

                setOnItemClickListener { parent, view, position, id ->

                    val scale = resources.displayMetrics.density // 화면 밀도를 가져옴
                    val pixel = (4 * scale + 0.5f).toInt()

                    compoundDrawablePadding = pixel

                    when (position) {
                        0 -> setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.circle_big_16px_primary20, 0, 0, 0)
                        1 -> setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.circle_big_16px_primary50, 0, 0, 0)
                        2 -> setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.circle_big_16px_primary80, 0, 0, 0)
                    }
                }

                addTextChangedListener {
                    val scale = resources.displayMetrics.density // 화면 밀도를 가져옴
                    val pixel = (4 * scale + 0.5f).toInt()

                    compoundDrawablePadding = pixel

                    when (it.toString()) {
                        "거래 중" -> setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.circle_big_16px_primary20, 0, 0, 0)
                        "거래 시도" -> setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.circle_big_16px_primary50, 0, 0, 0)
                        "거래 중지" -> setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.circle_big_16px_primary80, 0, 0, 0)
                    }
                }
            }

            val phoneNumberFormattingTextWatcher = PhoneNumberFormattingTextWatcher()

            textEditTextAccountEditDirectNumber.addTextChangedListener(phoneNumberFormattingTextWatcher)
            textEditTextAccountEditFaxNumber.addTextChangedListener(phoneNumberFormattingTextWatcher)
            textEditTextAccountEditGeneralNumber.addTextChangedListener(phoneNumberFormattingTextWatcher)

//            textEditTextAccountEditZipCode.setOnFocusChangeListener { v, hasFocus ->
//                if (hasFocus) {
//                    textInputLayoutAccountEditZipCode.defaultHintTextColor = resources.getColorStateList(R.color.primary20, null)
//                } else if (textEditTextAccountEditZipCode.text.toString().isEmpty()) {
//                    textInputLayoutAccountEditZipCode.defaultHintTextColor = resources.getColorStateList(android.R.color.transparent, null)
//                } else {
//                    textInputLayoutAccountEditZipCode.defaultHintTextColor = resources.getColorStateList(R.color.text30, null)
//                }
//            }

            textEditTextAccountEditZipCode.addTextChangedListener {
                if (textEditTextAccountEditZipCode.text.toString().isEmpty()) {
                    textInputLayoutAccountEditZipCode.defaultHintTextColor = resources.getColorStateList(android.R.color.transparent, null)
                } else {
                    textInputLayoutAccountEditZipCode.defaultHintTextColor = resources.getColorStateList(R.color.text30, null)
                }
            }

            textEditTextAccountEditZipCode.setOnClickListener {
                mainActivity.replaceFragment(MainActivity.ADDRESS_SEARCH_FRAGMENT, true)
            }

            buttonAccountEditAddressSearch.setOnClickListener {
                mainActivity.replaceFragment(MainActivity.ADDRESS_SEARCH_FRAGMENT, true)
//                mainActivity.navigateTo(R.id.addressSearchFragment)
            }
        }

        if (clientIdx == 0L) {
            registerViewInit()
        } else if (!create) {
            val userId = if (fromNotification) senderId else mainActivity.uid

            accountEditRepository.getClient(userId, clientIdx) {
                client = it
                editViewInit()
                create = true
            }
        } else {
            editViewInit()
        }

        return fragmentAccountEditBinding.root
    }

    inner class CustomArrayAdapter(items: List<String>): ArrayAdapter<String>(
        requireContext(),
        R.layout.dropdown_menu_item_account_edit,
        items
    ) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getView(position, convertView, parent)
            when ((view as TextView).text) {
                "거래 중" -> view.setCompoundDrawablesWithIntrinsicBounds(R.drawable.circle_big_16px_primary20, 0, 0, 0)
                "거래 시도" -> view.setCompoundDrawablesWithIntrinsicBounds(R.drawable.circle_big_16px_primary50, 0, 0, 0)
                "거래 중지" -> view.setCompoundDrawablesWithIntrinsicBounds(R.drawable.circle_big_16px_primary80, 0, 0, 0)
            }
            return view
        }
    }

    fun registerViewInit() {
        fragmentAccountEditBinding.run {
            materialToolbarAccountEdit.run {
                title = "새로운 거래처 등록"
            }

            buttonAccountEditSubmit.setOnClickListener {
                if (!validationCheck()) {
                    return@setOnClickListener
                }

                buttonAccountEditSubmit.text = "거래처 등록"

                val state = when (textEditTextAccountEditState.text.toString()) {
                    "거래 중" -> 1L
                    "거래 시도" -> 2L
                    "거래 중지" -> 3L
                    else -> 1L
                }

                val newClientIdx = System.currentTimeMillis()

                accountEditRepository.registerClient(
                    mainActivity.uid,
                    ClientInputData(
                        textEditTextAccountEditZipCode.text.toString(),
                        textEditTextAccountEditGeneralNumber.text.toString().replace("-",""),
                        textEditTextAccountEditDetailAddress.text.toString(),
                        textEditTextAccountEditShortDescription.text.toString(),
                        textEditTextAccountEditFaxNumber.text.toString().replace("-",""),
                        newClientIdx,
                        textEditTextAccountEditRepresentative.text.toString(),
                        textEditTextAccountEditDirectNumber.text.toString().replace("-",""),
                        textEditTextAccountEditEntireDescription.text.toString(),
                        textEditTextAccountEditAccountName.text.toString(),
                        state,
                        false,
                        emptyList(),
                        emptyList(),
                        emptyList(),
                        0L
                    )
                ) {
                    Snackbar.make(root, "거래처가 등록되었습니다", Snackbar.LENGTH_SHORT).apply {
                        anchorView = mainActivity.activityMainBinding.bottomNavigationViewMain
                    }.show()
                    mainActivity.removeFragment(MainActivity.ACCOUNT_EDIT_FRAGMENT)
//                    findNavController().popBackStack()
                }
            }
        }
    }

    fun editViewInit() {
        fragmentAccountEditBinding.run {
            materialToolbarAccountEdit.title = if (fromNotification) "공유 거래처 정보" else "거래처 정보 수정"

            textEditTextAccountEditState.run {
                val scale = resources.displayMetrics.density // 화면 밀도를 가져옴
                val pixel = (4 * scale + 0.5f).toInt()

                compoundDrawablePadding = pixel

                when (client.clientState) {
                    1L -> {
                        setText("거래 중", false)
                        setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.circle_big_16px_primary20, 0, 0, 0)
                    }
                    2L -> {
                        setText("거래 시도", false)
                        setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.circle_big_16px_primary50, 0, 0, 0)
                    }
                    3L -> {
                        setText("거래 중지", false)
                        setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.circle_big_16px_primary80, 0, 0, 0)
                    }
                }
            }

            textEditTextAccountEditAccountName.setText(client.clientName)
            textEditTextAccountEditZipCode.setText(client.clientAddress)
            textEditTextAccountEditDetailAddress.setText(client.clientDetailAdd)
            textEditTextAccountEditGeneralNumber.setText(client.clientCeoPhone)
            textEditTextAccountEditFaxNumber.setText(client.clientFaxNumber)
            textEditTextAccountEditRepresentative.setText(client.clientManagerName)
            textEditTextAccountEditDirectNumber.setText(client.clientManagerPhone)
            textEditTextAccountEditShortDescription.setText(client.clientExplain)
            textEditTextAccountEditEntireDescription.setText(client.clientMemo)

            buttonAccountEditSubmit.run {
                text = if (fromNotification) "거래처 등록" else "거래처 정보 수정"

                setOnClickListener {
                    if (!validationCheck()) {
                        return@setOnClickListener
                    }

                    val state = when (textEditTextAccountEditState.text.toString()) {
                        "거래 중" -> 1L
                        "거래 시도" -> 2L
                        "거래 중지" -> 3L
                        else -> 1L
                    }

                    val clientInputData = ClientInputData(
                        textEditTextAccountEditZipCode.text.toString(),
                        textEditTextAccountEditGeneralNumber.text.toString().replace("-",""),
                        textEditTextAccountEditDetailAddress.text.toString(),
                        textEditTextAccountEditShortDescription.text.toString(),
                        textEditTextAccountEditFaxNumber.text.toString().replace("-",""),
                        if (fromNotification) System.currentTimeMillis() else clientIdx,
                        textEditTextAccountEditRepresentative.text.toString(),
                        textEditTextAccountEditDirectNumber.text.toString().replace("-",""),
                        textEditTextAccountEditEntireDescription.text.toString(),
                        textEditTextAccountEditAccountName.text.toString(),
                        state,
                        if (fromNotification) false else client.isBookmark,
                        emptyList(),
                        emptyList(),
                        emptyList(),
                        if (fromNotification) 0 else client.viewCount
                    )

                    if (fromNotification) {
                        accountEditRepository.registerClient(
                            mainActivity.uid,
                            clientInputData
                        ) {
                            Snackbar.make(root, "거래처가 등록되었습니다", Snackbar.LENGTH_SHORT).apply {
                                anchorView = mainActivity.activityMainBinding.bottomNavigationViewMain
                            }.show()
                            mainActivity.removeFragment(MainActivity.ACCOUNT_EDIT_FRAGMENT)
                        }
                    } else {
                        accountEditRepository.updateClient(
                            mainActivity.uid,
                            clientInputData
                        ) {
                            Snackbar.make(root, "거래처 정보가 수정되었습니다", Snackbar.LENGTH_SHORT).apply {
                                anchorView = mainActivity.activityMainBinding.bottomNavigationViewMain
                            }.show()
                            mainActivity.removeFragment(MainActivity.ACCOUNT_EDIT_FRAGMENT)
//                        findNavController().popBackStack()
                        }
                    }
                }
            }
        }
    }

    fun validationCheck(): Boolean {
        fragmentAccountEditBinding.run {
            textEditTextAccountEditState.run {
                if (text.toString().isEmpty()) {
                    AlertDialog.Builder(mainActivity)
                        .setTitle("거래 상태")
                        .setMessage("거래 상태를 선택해주세요")
                        .setPositiveButton("확인") { _, _ ->
                            textEditTextAccountEditState.showDropDown()
                            requestFocus()
                        }
                        .show()
                    return false
                }
            }

            textEditTextAccountEditAccountName.run {
                if (text.toString().isEmpty()) {
                    AlertDialog.Builder(mainActivity)
                        .setTitle("거래처명")
                        .setMessage("거래처명을 입력해주세요")
                        .setPositiveButton("확인") { _, _ ->
                            mainActivity.showSoftInput(this)
                        }
                        .show()
                    return false
                }
            }

            textEditTextAccountEditZipCode.run {
                if (text.toString().isEmpty()) {
                    AlertDialog.Builder(mainActivity)
                        .setTitle("거래처 주소")
                        .setMessage("거래처 주소를 입력해주세요")
                        .setPositiveButton("확인") { _, _ ->
                            mainActivity.replaceFragment(MainActivity.ADDRESS_SEARCH_FRAGMENT, true)
                        }
                        .show()
                    return false
                }
            }
        }
        return true
    }
}