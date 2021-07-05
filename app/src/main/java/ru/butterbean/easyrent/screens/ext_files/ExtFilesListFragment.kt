package ru.butterbean.easyrent.screens.ext_files

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.databinding.FragmentExtFilesListBinding
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.models.ReserveExtFileData
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.utils.APP_ACTIVITY
import ru.butterbean.easyrent.utils.createArgsBundle
import ru.butterbean.easyrent.utils.startAnyApp

class ExtFilesListFragment : Fragment() {

    private lateinit var mViewModel: ExtFilesListViewModel
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mCurrentReserve: ReserveData
    private var _binding: FragmentExtFilesListBinding? = null
    private val mBinding get() = _binding!!

    companion object {
        fun clickOnListItem(extFile: ReserveExtFileData) {
            startAnyApp(extFile.getParamsBundle())
        }

        fun longClickOnListItem(extFile: ReserveExtFileData, lo: LifecycleOwner) {
            showEditDeleteRoomDialog(extFile, lo)
        }

        private fun showEditDeleteRoomDialog(extFile: ReserveExtFileData, lo: LifecycleOwner) {
            val actions = arrayOf(
                APP_ACTIVITY.getString(R.string.delete) // 0
            )
            val builder = AlertDialog.Builder(APP_ACTIVITY)
            builder.setItems(actions) { _, i ->
                when (i) {
                    0 -> deleteExtFile(extFile)
                }
            }
                .show()
        }

        private fun deleteExtFile(extFile: ReserveExtFileData) {
            TODO("Not yet implemented")
        }

        private fun goToRoomFragment(room: RoomData) {
            val args = createArgsBundle("room", room)
            args.putBoolean("fromRoomsList",true)
            APP_ACTIVITY.navController.navigate(
                R.id.action_roomsListFragment_to_roomFragment,
                args
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
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
        val adapter = ExtFilesListAdapter(viewLifecycleOwner)
        mRecyclerView = mBinding.extFilesRecyclerView
        mRecyclerView.adapter = adapter

        // ViewModel
        mViewModel = ViewModelProvider(APP_ACTIVITY).get(ExtFilesListViewModel::class.java)
        mViewModel.getExtFilesByReserveId(mCurrentReserve.id).observe(viewLifecycleOwner){
            adapter.setData(it.asReversed())
        }

    }

}