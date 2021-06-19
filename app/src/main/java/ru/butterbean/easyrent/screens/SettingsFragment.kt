package ru.butterbean.easyrent.screens

import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.*
import androidx.preference.CheckBoxPreference
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.utils.APP_ACTIVITY
import ru.butterbean.easyrent.utils.DAYS_TO_REPLACE_TO_ARCHIVE
import ru.butterbean.easyrent.utils.NumberEditTextInputFilter

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> APP_ACTIVITY.navController.popBackStack()
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.title = getString(R.string.settings)
        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // addPreferencesFromResource(R.xml.root_preferences)
        val e = findPreference<EditTextPreference>("oldReservesAnalyseDepth")

        if (e?.text.isNullOrEmpty()) e?.text = DAYS_TO_REPLACE_TO_ARCHIVE.toString()

        e?.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_CLASS_NUMBER
            editText.filters = arrayOf<InputFilter>(NumberEditTextInputFilter(0, 999))
        }
        e?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue.toString().isNotEmpty()) e.text = newValue.toString().toInt().toString()
            else e.text = DAYS_TO_REPLACE_TO_ARCHIVE.toString()

            false
        }

        val checkGroup = findPreference<Preference>("groupReserveCompleteCriteria")
        val ch1 = findPreference<CheckBoxPreference>("reserveCompleteCriteriaWasCheckOut")
        val ch2 = findPreference<CheckBoxPreference>("reserveCompleteCriteriaWasPaid")
        ch1?.setOnPreferenceChangeListener { _, newValue ->
            val ch1NewIsChecked = newValue as Boolean
            ch2?.isEnabled = ch1NewIsChecked
            ch2?.isChecked = !ch1NewIsChecked || ch2?.isChecked!!
            true
        }

        ch2?.setOnPreferenceChangeListener { _, newValue ->
            val ch2NewIsChecked = newValue as Boolean
            ch1?.isEnabled = ch2NewIsChecked
            ch1?.isChecked = !ch2NewIsChecked || ch1?.isChecked!!
            true
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}