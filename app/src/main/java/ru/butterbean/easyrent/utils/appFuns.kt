package ru.butterbean.easyrent.utils

import android.content.Context
import android.os.Bundle
import android.telephony.TelephonyManager
import android.text.InputFilter
import android.text.Spanned
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import ru.butterbean.easyrent.R
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

fun reserveCompleted(wasCheckOut:Boolean, sum:Int, payment:Int): Boolean {
    return (!PREF_RESERVE_COMPLETE_WAS_CHECK_OUT || wasCheckOut)
            && (!PREF_RESERVE_COMPLETE_WAS_PAID || (sum in 1..payment))
}

fun String.isImageExtension():Boolean{
    return this in APP_ACTIVITY.resources.getStringArray(R.array.image_extensions)
}

fun phoneNumberIsEmpty(pn: String): Boolean {
    return pn.length < 6
}

fun getCountryZipCode(): String {
    var countryZipCode = ""
    val manager = APP_ACTIVITY.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    //getNetworkCountryIso
    val countryID = manager.simCountryIso.uppercase()
    val rl: Array<String> = APP_ACTIVITY.resources.getStringArray(R.array.country_codes)
    for (i in rl.indices) {
        val g = rl[i].split(",").toTypedArray()
        if (g[1].trim { it <= ' ' } == countryID.trim { it <= ' ' }) {
            countryZipCode = g[0]
            break
        }
    }
    return countryZipCode
}