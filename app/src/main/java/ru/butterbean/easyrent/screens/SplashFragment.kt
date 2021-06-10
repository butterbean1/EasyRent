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
import ru.butterbean.easyrent.database.view_models.SplashViewModel
import ru.butterbean.easyrent.databinding.FragmentSplashBinding
import ru.butterbean.easyrent.utils.APP_ACTIVITY
import ru.butterbean.easyrent.utils.ONLY_ONE_ROOM
import ru.butterbean.easyrent.utils.createArgsBundle
import ru.butterbean.easyrent.utils.getEmptyRoom

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

        viewModel.getAllRooms().observe(this) { roomsList ->
            Handler(Looper.getMainLooper()).postDelayed({
                when (roomsList.count()) {
                    0 -> {
                        val args = createArgsBundle("room", getEmptyRoom())
                        args.putBoolean("addFirstRoom",true)
                        APP_ACTIVITY.navController.navigate(
                            R.id.action_splashFragment_to_editRoomFragment,
                            args
                        )
                    }
                    1 -> {
                        ONLY_ONE_ROOM = true
                        APP_ACTIVITY.navController.navigate(
                            R.id.action_splashFragment_to_roomFragment,
                            createArgsBundle("room",roomsList[0]))
                    }
                    else -> APP_ACTIVITY.navController.navigate(R.id.action_splashFragment_to_roomsListFragment)
                }
                APP_ACTIVITY.supportActionBar?.show()

            }, 2000)
        }


    }

}