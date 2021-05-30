package ru.butterbean.easyrent.screens.base

import androidx.fragment.app.Fragment
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