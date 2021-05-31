package ru.butterbean.easyrent.utils

import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.butterbean.easyrent.MainActivity
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.models.GuestData
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.models.RoomData

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
    if (addStack){
        tran = tran.addToBackStack(null)
    }
    tran.replace(R.id.data_container,fragment).commit()
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
