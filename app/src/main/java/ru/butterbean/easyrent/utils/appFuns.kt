package ru.butterbean.easyrent.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.butterbean.easyrent.MainActivity
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.models.GuestData
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.models.RoomData
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

fun showToast(message: String) {
    Toast.makeText(APP_ACTIVITY, message, Toast.LENGTH_SHORT).show()
}

fun hideKeyboard() {
    val imm = APP_ACTIVITY.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(APP_ACTIVITY.window.decorView.windowToken, 0)
}

fun restartActivity() {
    APP_ACTIVITY.startActivity(Intent(APP_ACTIVITY, MainActivity::class.java))
    APP_ACTIVITY.finish()
}

fun replaceFragment(fragment: Fragment, addStack: Boolean = true) {
    var tran = APP_ACTIVITY.supportFragmentManager.beginTransaction()
    if (addStack) {
        tran = tran.addToBackStack(null)
    }
    tran.replace(R.id.data_container, fragment).commit()
}

fun getEmptyRoom(): RoomData {
    return RoomData(0)
}

fun getEmptyReserve(): ReserveData {
    return ReserveData(0)
}

fun getEmptyGuest(): GuestData {
    return GuestData(0)
}

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

        val inst = SimpleDateFormat.getDateInstance() as SimpleDateFormat
        val formatter = SimpleDateFormat(inst.toLocalizedPattern(), Locale.getDefault())
        formatter.format(parser.parse(this) ?: Date(0))
    }

}

fun String.toTimeFormat(): String {

    return if (Build.VERSION.SDK_INT > 25) {
        val parsedTime = LocalTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
        parsedTime.format(DateTimeFormatter.ofPattern("HH:ss"))

    } else {

        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

        val formatter = SimpleDateFormat("HH:ss", Locale.getDefault())
        formatter.format(parser.parse(this) ?: Date(0))
    }

}
