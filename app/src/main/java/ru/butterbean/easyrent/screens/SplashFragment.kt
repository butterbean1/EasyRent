package ru.butterbean.easyrent.screens

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.screens.room.RoomsListFragment
import ru.butterbean.easyrent.utils.APP_ACTIVITY
import ru.butterbean.easyrent.utils.replaceFragment

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Handler(Looper.getMainLooper()).postDelayed({
            replaceFragment(RoomsListFragment(), false)
            APP_ACTIVITY.supportActionBar?.show()
        }, 2000)

        APP_ACTIVITY.supportActionBar?.hide()

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

}