package ru.butterbean.easyrent.screens.fullsize_image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.databinding.FragmentFullSizeImageBinding
import ru.butterbean.easyrent.utils.APP_ACTIVITY

class FullSizeImageFragment : Fragment() {
    private var _binding: FragmentFullSizeImageBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFullSizeImageBinding.inflate(layoutInflater, container, false)
        val uri = arguments?.getString("uriString")?.toUri()
        mBinding.fullImage.setImageURI(uri)

        val fileName = arguments?.getString("fileName")
        APP_ACTIVITY.title =
            if (fileName == null || fileName.isEmpty()) getString(R.string.picture)
            else fileName

        setHasOptionsMenu(true)
        return mBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> APP_ACTIVITY.navController.popBackStack()
            else -> return super.onOptionsItemSelected(item)
        }
    }
}