package com.ferdifir.menitcom.data.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.DateFormatSymbols
import java.time.LocalDate
import java.time.ZoneId

object Helper {

    fun convertDate(date: String): String {
        val dt = date[5].toString() + date[6].toString()
        val dte = DateFormatSymbols().months[dt.toInt()-1]
        val dtt = date[8].toString() + date[9].toString()
        val yrs = date.substringBefore('-')
        return "$dte $dtt, $yrs"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    val today: LocalDate = LocalDate.now(
        ZoneId.of("Asia/Jakarta")
    )

}