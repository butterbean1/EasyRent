package ru.butterbean.easyrent.screens.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.databinding.FragmentSplashBinding
import ru.butterbean.easyrent.utils.APP_ACTIVITY


open class BaseFragment<Binding : ViewBinding>(layout:Int) : Fragment(layout) {

    var _binding: (fragmentBinding)? = null
    val mBinding get() = _binding!!

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        APP_ACTIVITY.mToolbar.setNavigationOnClickListener {
            APP_ACTIVITY.supportFragmentManager.popBackStack()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = (fragmentBinding::class.java).inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}