package ru.butterbean.easyrent

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import ru.butterbean.easyrent.utils.APP_ACTIVITY

class MainActivity : AppCompatActivity() {

    var backAvailable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        APP_ACTIVITY = this
        setupActionBarWithNavController(findNavController(R.id.fragment))
        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.green)))
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (backAvailable) {super.onBackPressed()}
        else { APP_ACTIVITY.finish()}
    }


}