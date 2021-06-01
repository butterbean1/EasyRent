package ru.butterbean.easyrent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import ru.butterbean.easyrent.database.MainDatabase
import ru.butterbean.easyrent.databinding.ActivityMainBinding
import ru.butterbean.easyrent.screens.SplashFragment
import ru.butterbean.easyrent.utils.*

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding:ActivityMainBinding
    lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        APP_ACTIVITY = this
        APP_DATABASE = MainDatabase.getDatabase(applicationContext)
        CURRENT_ROOM = getEmptyRoom()
        CURRENT_RESERVE = getEmptyReserve()
        CURRENT_GUEST = getEmptyGuest()
        mToolbar = mBinding.mainToolbar
        setSupportActionBar(mToolbar)
        replaceFragment(SplashFragment())
    }

}