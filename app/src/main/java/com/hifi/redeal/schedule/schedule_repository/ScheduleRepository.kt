package com.hifi.redeal.schedule.schedule_repository

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Transaction
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hifi.redeal.BuildConfig
import com.hifi.redeal.account.repository.RetrofitManager
import com.hifi.redeal.account.repository.model.Coordinate
import com.hifi.redeal.account.repository.model.FullAddrResponse
import com.hifi.redeal.schedule.model.ScheduleData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ScheduleRepository {


    companion object{

        fun getFullAddrGeocoding(fullAddr: String, callback: (Coordinate?) -> Unit) {
            val encodedFullAddr = URLEncoder.encode(fullAddr, "UTF-8")

            RetrofitManager.tMapTapiService.tMapFullTextGeocoding(fullAddr = encodedFullAddr, appKey = BuildConfig.TMAP_APP_KEY)
                .enqueue(object : Callback<FullAddrResponse> {
                    override fun onResponse(
                        call: Call<FullAddrResponse>,
                        response: Response<FullAddrResponse>
                    ) {
                        if (response.isSuccessful) {
                            callback(response.body()?.coordinateInfo?.coordinate?.get(0))
                        }
                    }

                    override fun onFailure(call: Call<FullAddrResponse>, t: Throwable) {

                    }
                })
        }
        fun delSelectSchedule(userIdx: String, scheduleIdx: String, callback1: (Task<Void>) -> Unit){
            val db = Firebase.firestore
            db.collection("userData")
                .document(userIdx)
                .collection("scheduleData")
                .document(scheduleIdx)
                .delete()
                .addOnCompleteListener(callback1)
        }
        fun getSelectScheduleInfo(userIdx: String, scheduleIdx: String, callback1: (Task<DocumentSnapshot>) -> Unit){
            val db = Firebase.firestore
            db.collection("userData")
                .document(userIdx)
                .collection("scheduleData")
                .document(scheduleIdx)
                .get()
                .addOnCompleteListener(callback1)
        }
        fun getSelectClientLastVisitDate(userIdx: String, clientIdx: Long, callback1: (Task<QuerySnapshot>) -> Unit){
            val db = Firebase.firestore
            val scheduleDataRef = db.collection("userData")
                .document(userIdx)
                .collection("scheduleData")

            scheduleDataRef
                .whereEqualTo("clientIdx", clientIdx)
                .whereEqualTo("isScheduleFinish", true)
                .orderBy("scheduleFinishTime",  Query.Direction.DESCENDING).limit(1)
                .get()
                .addOnCompleteListener(callback1)
        }
        fun setUserSchedule(userIdx: String, scheduleData: ScheduleData, callback1: (Task<Void>) -> Unit){
            val db = Firebase.firestore
            db.collection("userData")
                .document(userIdx)
                .collection("scheduleData")
                .document("${scheduleData.scheduleIdx}")
                .set(scheduleData, SetOptions.merge()) // SetOptions.merge() : 없으면 추가, 있다면 덮어쓴다.
                .addOnCompleteListener(callback1)

        }
        fun setUserSchedule(userIdx: String, scheduleData: ScheduleData, callback1: (Task<Void>) -> Unit, callback2: (Task<Void>) -> Unit){
            val db = Firebase.firestore
            db.collection("userData")
                .document(userIdx)
                .collection("scheduleData")
                .document("${scheduleData.scheduleIdx}")
                .set(scheduleData, SetOptions.merge()) // SetOptions.merge() : 없으면 추가, 있다면 덮어쓴다.
                .addOnCompleteListener(callback1)
                .addOnCompleteListener(callback2)
        }
        fun getUserAllSchedule(userIdx: String, callback1: (Task<QuerySnapshot>) -> Unit, callback2: (Task<QuerySnapshot>) -> Unit){
            val db = Firebase.firestore
            val scheduleDataRef = db.collection("userData")
                .document(userIdx)
                .collection("scheduleData")

            scheduleDataRef.orderBy("scheduleIdx", Query.Direction.DESCENDING).limit(1)
                .get()
                .addOnCompleteListener(callback1)
                .addOnCompleteListener(callback2)
        }

        fun getUserSelectClientInfo(userIdx: String, clientIdx: Long, callback1: (Task<DocumentSnapshot>) -> Unit){
            val db = Firebase.firestore
            db.collection("userData")
                .document(userIdx)
                .collection("clientData")
                .document("$clientIdx")
                .get()
                .addOnCompleteListener(callback1)
        }

        fun getUserAllClientInfo(userIdx: String,callback1: (Task<QuerySnapshot>) -> Unit){
            val db = Firebase.firestore
            db.collection("userData")
                .document(userIdx)
                .collection("clientData")
                .get()
                .addOnCompleteListener(callback1)
        }
        fun updateUserDayOfScheduleState(userIdx: String, scheduleIdx: String, callback1: (Task<Unit>) -> Unit){
            val db = Firebase.firestore
            val scheduleRef = db.collection("userData")
                .document(userIdx)
                .collection("scheduleData")
                .document(scheduleIdx)

            db.runTransaction {transaction ->
                val snapshot = transaction.get(scheduleRef)
                val newState = !snapshot.getBoolean("isScheduleFinish")!!
                transaction.update(scheduleRef, "isScheduleFinish", newState)
                if(newState) transaction.update(scheduleRef, "scheduleFinishTime", Timestamp(Date()))
            }.addOnCompleteListener(callback1)

        }

        fun getClientInfo(userIdx: String, clientIdx: Long, callback1: (Task<QuerySnapshot>) -> Unit){
            val db = Firebase.firestore
            db.collection("userData")
                .document(userIdx)
                .collection("clientData")
                .whereEqualTo("clientIdx", clientIdx)
                .get().addOnCompleteListener(callback1)
        }
        fun getUserDayOfSchedule(userIdx: String, date: String, callback1: (Task<QuerySnapshot>) -> Unit, callback2: (Task<QuerySnapshot>) -> Unit){
            val db = Firebase.firestore
            val scheduleRef = db.collection("userData")
                .document(userIdx)
                .collection("scheduleData")

            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = sdf.parse(date)

            val startDate = Timestamp(formattedDate)
            val endDate = Timestamp(Date(formattedDate.time + 24 * 60 * 60 * 1000)) // 다음 날의 timestamp

            val query = scheduleRef
                .whereGreaterThanOrEqualTo("scheduleDeadlineTime", startDate)
                .whereLessThan("scheduleDeadlineTime", endDate)

            query.get().addOnCompleteListener(callback1).addOnCompleteListener(callback2)
        }
    }
}