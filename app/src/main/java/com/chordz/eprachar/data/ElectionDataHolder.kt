package com.chordz.eprachar.data

import android.graphics.Bitmap
import com.chordz.eprachar.data.response.ElectionMessageResponse

object ElectionDataHolder {
    lateinit var ImageUrl: String
    var DailyCountUpdateTime: Long? = null
    var hourlyMessageUpdateTime: Long? =null
    var messageBitmap: Bitmap? = null
    var msgDetails: ElectionMessageResponse? = null
    var adminContactNumber = "0";
    var pracharContactsMap = HashMap<String, Int>()
    const val BASE_URL = "http://electionapi.beatsacademy.in"
}