package ru.butterbean.easyrent.utils

import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import java.io.Serializable

fun showToast(message: String) {
    Toast.makeText(APP_ACTIVITY, message, Toast.LENGTH_SHORT).show()
}

fun hideKeyboard() {
    val imm = APP_ACTIVITY.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(APP_ACTIVITY.window.decorView.windowToken, 0)
}

fun createArgsBundle(key: String, value: Serializable): Bundle {
    val args = Bundle()
    args.putSerializable(key, value)
    return args
}

class NumberEditTextInputFilter(private val minValue: Int = 1, private val maxValue: Int = 99) :
    InputFilter {

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        try {
            val input = (dest?.subSequence(0, dstart).toString() + source + dest?.subSequence(
                dend,
                dest.length
            )).toInt()
            if (isInRange(input))
                return null
        } catch (e: NumberFormatException) {
        }
        return ""
    }

    private fun isInRange(c: Int): Boolean {
        return c in minValue..maxValue
    }
}

