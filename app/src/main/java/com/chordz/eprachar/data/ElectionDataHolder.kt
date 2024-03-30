package com.chordz.eprachar.data

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.chordz.eprachar.data.response.ElectionMessageResponse
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ElectionDataHolder {
    var bmpUri: Uri? = null
    lateinit var ImageUrl: String
    var DailyCountUpdateTime: Long? = null
    var hourlyMessageUpdateTime: Long? = null
    var messageBitmap: Bitmap? = null
    var msgDetails: ElectionMessageResponse? = null
    var adminContactNumber = "0";
    var pracharContactsMap = HashMap<String, Int>()
    const val BASE_URL = "http://electionapi.beatsacademy.in"
    val PROVIDER_AUTHORITY: String = "com.chordz.eprachar.provider"


    fun getBitmapUriFromBitmap(context: Context, bitmap: Bitmap): Uri? {
        if (bmpUri != null) {
            return bmpUri
        }
        try {
            val file = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "share_image_${System.currentTimeMillis()}.png"
            )
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.close()
            bmpUri = FileProvider.getUriForFile(context, PROVIDER_AUTHORITY, file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bmpUri
    }
}