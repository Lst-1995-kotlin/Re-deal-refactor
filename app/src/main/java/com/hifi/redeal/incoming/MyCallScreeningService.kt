package com.hifi.redeal.incoming

import android.telecom.Call
import android.telecom.CallScreeningService
import android.util.Log

class MyCallScreeningService : CallScreeningService() {

    override fun onScreenCall(callDetails: Call.Details) {
        Log.d("tttt", "${callDetails.handle.schemeSpecificPart}")
        val isIncoming = callDetails.callDirection == Call.Details.DIRECTION_INCOMING
        if (isIncoming) { // 저장된 번호가 아니라면....
            val response = CallResponse.Builder()
            // 차단할 번호인 경우 아래와 같이 설정
            if (callDetails.handle.schemeSpecificPart == "123456789") {
            }
            respondToCall(callDetails, response.build())
        }
    }
}
