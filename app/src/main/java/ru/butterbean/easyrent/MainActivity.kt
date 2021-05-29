package ru.butterbean.easyrent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import ru.butterbean.easyrent.databinding.ActivityMainBinding
import ru.butterbean.easyrent.screens.SplashFragment
import ru.butterbean.easyrent.utils.APP_ACTIVITY
import ru.butterbean.easyrent.utils.replaceFragment

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding:ActivityMainBinding
    lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        APP_ACTIVITY = this
        mToolbar = mBinding.mainToolbar
        setSupportActionBar(mToolbar)
        replaceFragment(SplashFragment())
    }

}