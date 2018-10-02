package com.geekbrains.geekbrainskotlin.extensions

import android.content.Context
import android.support.v4.content.ContextCompat
import com.geekbrains.geekbrainskotlin.R
import com.geekbrains.geekbrainskotlin.data.model.Color
import java.text.SimpleDateFormat
import java.util.*

const val DATE_TIME_FORMAT = "dd.MMMyy HH:mm"

fun Date.format(): String =
        SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
                .format(this)

fun Color.getColorInt(context: Context): Int =
        ContextCompat.getColor(context, getColorRes())

fun Color.getColorRes(): Int = when (this) {
    Color.WHITE -> R.color.color_white
    Color.VIOLET -> R.color.color_violet
    Color.YELLOW -> R.color.color_yello
    Color.RED -> R.color.color_red
    Color.PINK -> R.color.color_pink
    Color.GREEN -> R.color.color_green
    Color.BLUE -> R.color.color_blue
}