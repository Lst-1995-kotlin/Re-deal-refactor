package com.hifi.redeal.memo

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class CurrentUserClass @Inject constructor() {
    val userIdx: String = Firebase.auth.uid!!
}