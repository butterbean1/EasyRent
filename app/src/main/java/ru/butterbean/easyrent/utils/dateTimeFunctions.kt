package ru.butterbean.easyrent.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

fun String.toDateTimeFormat(): String {

    return if (Build.VERSION.SDK_INT > 25) {
        val formatStyle = FormatStyle.SHORT
        val parsedDateTime = LocalDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
        parsedDateTime.format(DateTimeFormatter.ofLocalizedDateTime(formatStyle))


    } else {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

        val inst = SimpleDateFormat.getDateTimeInstance() as SimpleDateFormat

        val formatter = SimpleDateFormat(inst.toLocalizedPattern(), Locale.getDefault())
        formatter.format(parser.parse(this) ?: Date(0))
    }

}

fun getCalendarFromString(date: String,pattern:String = "yyyy-MM-dd'T'HH:mm:ss"): Calendar {
    val d = SimpleDateFormat(pattern, Locale.getDefault()).parse(date)
    val cal = Calendar.getInstance()
    cal.time = d!!
    return cal
}

fun getStartOfDay(date: Calendar): Calendar {
    val cal = Calendar.getInstance()
    cal.set(
        date.get(Calendar.YEAR),
        date.get(Calendar.MONTH),
        date.get(Calendar.DAY_OF_MONTH),
        0,
        0,
        0
    )
    cal.set(Calendar.MILLISECOND, 0)
    return cal
}

fun Calendar.toDateFormat(): String {
    val formatter = SimpleDateFormat("d MMMM", Locale.getDefault())
    return formatter.format(this.time)
}

fun Calendar.toDateTimeInDatabaseFormat(): String {
    val d = getDateFormatISO(
        this.get(Calendar.YEAR),
        this.get(Calendar.MONTH),
        this.get(Calendar.DAY_OF_MONTH)
    )
    val t = getTimeString(
        this.get(Calendar.HOUR_OF_DAY),
        this.get(Calendar.MINUTE)
    )
    return getDateTimeInDatabaseFormat(d,t)
}

fun String.toDateFormat(onlyDate: Boolean = false): String {

    return if (Build.VERSION.SDK_INT > 25) {
        val formatStyle = FormatStyle.SHORT
        when {
            onlyDate -> {
                LocalDate.parse(this, DateTimeFormatter.ISO_DATE)
                    .format(DateTimeFormatter.ofLocalizedDate(formatStyle))
            }
            else -> {
                LocalDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
                    .format(DateTimeFormatter.ofLocalizedDate(formatStyle))
            }
        }

    } else {
        val patternParser = if (onlyDate) {
            "yyyy-MM-dd"
        } else {
            "yyyy-MM-dd'T'HH:mm:ss"
        }
        val parser = SimpleDateFormat(patternParser, Locale.getDefault())

        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        formatter.format(parser.parse(this) ?: Date(0))
    }
}

fun String.toTimeFormat(): String {

    return if (Build.VERSION.SDK_INT > 25) {
        val parsedTime = LocalTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
        parsedTime.format(DateTimeFormatter.ofPattern("HH:mm"))

    } else {

        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        formatter.format(parser.parse(this) ?: Date(0))
    }

}

fun getDateTimeInDatabaseFormat(date: String, time: String) =
    "${date}T${time}:00"


fun getDateFormatISO(year: Int, month: Int, dayOfMonth: Int): String {
    val monthText = (month + 1).toString().padStart(2, '0')
    val dayText = dayOfMonth.toString().padStart(2, '0')
    return "$year-$monthText-$dayText"
}

fun getTimeString(hour: Int, minute: Int): String {
    val hourText = hour.toString().padStart(2, '0')
    val minuteText = minute.toString().padStart(2, '0')
    return "$hourText:$minuteText"
}

fun showCalendarDialogFromListener(
    context: Context,
    listener: DatePickerDialog.OnDateSetListener,
    date: String, // в формате yyyy-MM-dd
    minDate: String="" // в формате yyyy-MM-dd
) {

    val cal = if (date.isEmpty()) {
        if (minDate.isEmpty()) Calendar.getInstance()
        else {
            val c = getCalendarFromString(minDate, "yyyy-MM-dd")
            c.add(Calendar.DAY_OF_MONTH,1)
            c
        }
    }
    else getCalendarFromString(date,"yyyy-MM-dd")

    val dialog = DatePickerDialog(
        context,
        android.R.style.Theme_Material_Light_Dialog_MinWidth,
        listener,
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH),
        cal.get(Calendar.DAY_OF_MONTH)
    )
    dialog.show()
}

fun showTimeDialogFromListener(
    context: Context,
    listener: TimePickerDialog.OnTimeSetListener,
    time: String // в формате HH:mm
) {
    val cal = (if (time.isEmpty()) {
        val c = Calendar.getInstance()
        c.set(0, 0, 0, 12, 0, 0)
        c
    } else getCalendarFromString(time,"HH:mm"))

    val dialog = TimePickerDialog(
        context,
        android.R.style.Theme_Material_Light_Dialog,
        listener,
        cal.get(Calendar.HOUR_OF_DAY),
        cal.get(Calendar.MINUTE),
        true
    )
    dialog.show()
}
