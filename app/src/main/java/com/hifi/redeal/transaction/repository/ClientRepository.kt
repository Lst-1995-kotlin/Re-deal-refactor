package com.hifi.redeal.transaction.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hifi.redeal.CurrentUserClass
import javax.inject.Inject

class ClientRepository @Inject constructor(
    private val currentUser: CurrentUserClass,
) {
    private val uid = currentUser.userIdx
    private val db = Firebase.firestore
    private val dbClientRef = db
        .collection("userData")
        .document(uid)
        .collection("clientData")

    fun getUserAllClient(callback1: (Task<QuerySnapshot>) -> Unit) {
        dbClientRef
            .get()
            .addOnCompleteListener(callback1)
    }
}