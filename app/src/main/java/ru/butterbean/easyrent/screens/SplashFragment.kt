package ru.butterbean.easyrent.screens

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.database.view_models.EditRoomViewModel
import ru.butterbean.easyrent.database.view_models.SplashViewModel
import ru.butterbean.easyrent.databinding.FragmentSplashBinding
import ru.butterbean.easyrent.utils.APP_ACTIVITY
import ru.butterbean.easyrent.utils.ROOMS_COUNT

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSplashBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()

        APP_ACTIVITY.supportActionBar?.hide()

        val viewModel = ViewModelProvider(APP_ACTIVITY).get(SplashViewModel::class.java)

        viewModel.getRoomsCount().observe(this){roomsCount->
            ROOMS_COUNT = roomsCount
            Handler(Looper.getMainLooper()).postDelayed({
                APP_ACTIVITY.supportActionBar?.show()
                APP_ACTIVITY.navController.navigate(R.id.action_splashFragment_to_roomsListFragment)
            }, 2000)
        }


    }

 }