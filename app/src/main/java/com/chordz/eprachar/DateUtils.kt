package com.chordz.eprachar

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {
    fun getDateInYYYYMMDDFormat(): String {
        var date = "1990-01-01"
        try {
            val simpleDateFormat: SimpleDateFormat =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val time = Calendar.getInstance().getTime()
            date = simpleDateFormat.format(time)
        } catch (_: Exception) {

        }
        return date
    }
    fun stringToDateConverter(dateInString: String): Date {
        var date = Date();
        try {
            val simpleDateFormat: SimpleDateFormat =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            date = simpleDateFormat.parse(dateInString)
        } catch (_: Exception) {

        }
        return date
    }
}