package ru.butterbean.easyrent.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.utils.APP_ACTIVITY

open class BaseFragment(layout:Int) : Fragment(layout) {

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        APP_ACTIVITY.mToolbar.setNavigationOnClickListener {
            APP_ACTIVITY.supportFragmentManager.popBackStack()
        }
    }
}