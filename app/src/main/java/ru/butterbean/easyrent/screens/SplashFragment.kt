package ru.butterbean.easyrent.screens

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.databinding.FragmentSplashBinding
import ru.butterbean.easyrent.screens.room.RoomsListFragment
import ru.butterbean.easyrent.utils.APP_ACTIVITY
import ru.butterbean.easyrent.utils.replaceFragment

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.action_splashFragment_to_roomsListFragment)
           // replaceFragment(RoomsListFragment(), false)
            APP_ACTIVITY.supportActionBar?.show()
        }, 2000)

        APP_ACTIVITY.supportActionBar?.hide()

        _binding = FragmentSplashBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}