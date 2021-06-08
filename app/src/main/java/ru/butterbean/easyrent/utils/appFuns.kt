package ru.butterbean.easyrent.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.butterbean.easyrent.MainActivity
import ru.butterbean.easyrent.R
import java.io.Serializable

fun showToast(message: String) {
    Toast.makeText(APP_ACTIVITY, message, Toast.LENGTH_SHORT).show()
}

fun hideKeyboard() {
    val imm = APP_ACTIVITY.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(APP_ACTIVITY.window.decorView.windowToken, 0)
}

fun createArgsBundle(key:String,value:Serializable):Bundle{
    val args = Bundle()
    args.putSerializable(key, value)
    return args
}