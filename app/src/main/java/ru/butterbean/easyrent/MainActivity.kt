package ru.butterbean.easyrent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.butterbean.easyrent.utils.APP_ACTIVITY

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        APP_ACTIVITY = this
    }
}