package ru.butterbean.easyrent.screens.ext_files

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ru.butterbean.easyrent.databinding.FragmentExtFilesListBinding
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.models.ReserveExtFileData
import ru.butterbean.easyrent.utils.*

class ExtFilesListFragment : Fragment(), ExtFilesExtension {

    private lateinit var mViewModel: ExtFilesListViewModel
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mCurrentReserve: ReserveData
    private var _binding: FragmentExtFilesListBinding? = null
    private val mBinding get() = _binding!!

    companion object {
        fun clickOnListItem(extFile: ReserveExtFileData) {
            startAnyApp(extFile.getParamsBundle())
        }

        fun longClickOnListItem(extFile: ReserveExtFileData,f:ExtFilesListFragment) {
            showDeleteExtFileDialog(extFile,f)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> APP_ACTIVITY.navController.popBackStack()
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExtFilesListBinding.inflate(layoutInflater, container, false)
        mCurrentReserve = arguments?.getSerializable("reserve") as ReserveData
        return mBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mRecyclerView.adapter = null
    }

    override fun onStart() {
        super.onStart()
        initialization()
    }

    private fun initialization() {
        APP_ACTIVITY.title = "Файлы"
        setHasOptionsMenu(true)

        //Recycler view
        val adapter = ExtFilesListAdapter(this)
        mRecyclerView = mBinding.extFilesRecyclerView
        mRecyclerView.adapter = adapter

        // ViewModel
        mViewModel = ViewModelProvider(APP_ACTIVITY).get(ExtFilesListViewModel::class.java)
        mViewModel.getExtFilesByReserveId(mCurrentReserve.id).observe(viewLifecycleOwner){
            adapter.setData(it.asReversed())
        }

    }

    override fun deleteReserveExtFile(extFile: ReserveExtFileData) {
        mViewModel.deleteExtFile(extFile){extFilesCount ->
            mCurrentReserve.extFilesCount = extFilesCount
        }
    }

    override fun getSingleExtFileParams(f: (Bundle) -> Unit) {}

}