package ru.butterbean.easyrent.screens.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.database.getEmptyRoom
import ru.butterbean.easyrent.databinding.FragmentSplashBinding
import ru.butterbean.easyrent.utils.*

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

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

        val prefs = PreferenceManager.getDefaultSharedPreferences(APP_ACTIVITY)
        PREF_RESERVE_COMPLETE_WAS_CHECK_OUT = prefs.getBoolean("reserveCompleteCriteriaWasCheckOut", true)
        PREF_RESERVE_COMPLETE_WAS_PAID = prefs.getBoolean("reserveCompleteCriteriaWasPaid", true)
        AUTO_CHECK_IN_CHECK_OUT = prefs.getBoolean("autoCheckInCheckOut", true)

        val prefAnalyseDepth = prefs.getString("oldReservesAnalyseDepth", DAYS_TO_REPLACE_TO_ARCHIVE.toString())!!
        val analyseDepth = if (prefAnalyseDepth.isEmpty()) DAYS_TO_REPLACE_TO_ARCHIVE else {
            try {
                Integer.parseInt(prefAnalyseDepth)
            }catch(e: NumberFormatException){
                DAYS_TO_REPLACE_TO_ARCHIVE
            }
        }
        viewModel.replaceReservesToArchive(analyseDepth)

        if (AUTO_CHECK_IN_CHECK_OUT) viewModel.setAutoCheckInCheckOut()

        viewModel.getAllRooms(){ roomsList ->
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
                APP_ACTIVITY.window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

                APP_ACTIVITY.supportActionBar?.show()

            }, 1200)
        }

    }

}