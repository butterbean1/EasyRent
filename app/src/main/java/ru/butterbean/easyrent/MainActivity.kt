package ru.butterbean.easyrent

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import ru.butterbean.easyrent.database.MainDatabase
import ru.butterbean.easyrent.databinding.ActivityMainBinding
import ru.butterbean.easyrent.utils.*


class MainActivity : AppCompatActivity() {

    private var _binding:ActivityMainBinding? = null
    private val mBinding get() = _binding!!
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        setContentView(mBinding.root)

        APP_ACTIVITY = this
        APP_DATABASE = MainDatabase.getDatabase(applicationContext)

        STATUS_FREE = getString(R.string.status_free)
        STATUS_FREE_FROM = getString(R.string.status_free_from)
        STATUS_FREE_TO = getString(R.string.status_free_to)
        STATUS_FREE_ON = getString(R.string.status_free_on)
        STATUS_BUSY = getString(R.string.status_busy)
        STATUS_REPAIRS = getString(R.string.status_on_repair)
        STATUS_UNTIL = getString(R.string.status_until)

        navController = Navigation.findNavController(this,R.id.nav_host_fragment)
        setSupportActionBar(mBinding.mainToolbar)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}