package com.chordz.eprachar

import com.chordz.eprachar.preferences.AppPreferences
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {
    fun getCurrentDateTime(): String {
        var date = ""
        try {
            val simpleDateFormat: SimpleDateFormat =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

            val time = Calendar.getInstance().getTime()
            date = simpleDateFormat.format(time)
        } catch (_: Exception) {

        }
        return date
    }

    fun needToHourlyMessageUpdated(hourlyMessageUpdateTime: Long, timeInMillis: Long): Boolean {
//        return timeInMillis - hourlyMessageUpdateTime > 3600000
        return timeInMillis - hourlyMessageUpdateTime > 1*60*1000
    }

}