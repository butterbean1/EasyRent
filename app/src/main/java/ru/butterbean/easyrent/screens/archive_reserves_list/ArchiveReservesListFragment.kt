package ru.butterbean.easyrent.screens.archive_reserves_list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.databinding.FragmentArchiveReservesListBinding
import ru.butterbean.easyrent.models.ReserveArchiveData
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.utils.APP_ACTIVITY

class ArchiveReservesListFragment : Fragment() {

    private lateinit var mViewModel: ArchiveReservesListViewModel
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mCurrentRoom: RoomData
    private lateinit var mAdapter: ArchiveReservesListAdapter
    private var _binding: FragmentArchiveReservesListBinding? = null
    private val mBinding get() = _binding!!

    private var mCurrentList = emptyList<ReserveArchiveData>()

    private lateinit var optionsMenu: Menu

    val listMarkedReserves = mutableListOf<ReserveArchiveData>()

   override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArchiveReservesListBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mRecyclerView.adapter = null
    }

    fun setVisibleOptionsMenuItems() {
        val isItemVisible = listMarkedReserves.count() > 0
        optionsMenu.forEach {
            it.isVisible = isItemVisible
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        optionsMenu = menu
        inflater.inflate(R.menu.select_delete_menu, menu)
        setVisibleOptionsMenuItems()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (listMarkedReserves.count() == 0) APP_ACTIVITY.navController.popBackStack()
                else {
                    listMarkedReserves.clear()
                    setVisibleOptionsMenuItems()
                    mAdapter.notifyDataSetChanged()
                }
                true
            }
            R.id.select_all -> {
                listMarkedReserves.clear()
                listMarkedReserves.addAll(mCurrentList)
                setVisibleOptionsMenuItems()
                mAdapter.notifyDataSetChanged()
                true
            }
            R.id.restore -> {
                mViewModel.replaceReservesFromArchive(listMarkedReserves) {
                    listMarkedReserves.clear()
                    setVisibleOptionsMenuItems()
                    setDataToAdapter()
                }
                true
            }
            R.id.delete -> {
                showDeleteDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDeleteDialog() {
        val builder = AlertDialog.Builder(this.context)
        builder.setMessage(getString(R.string.finally_reserves_delete_message))
            .setPositiveButton(APP_ACTIVITY.getString(R.string.yes)) { dialog, _ ->
                dialog.cancel()
                mViewModel.deleteArchiveReserves(listMarkedReserves) {
                    listMarkedReserves.clear()
                    setVisibleOptionsMenuItems()
                    setDataToAdapter()
                }
            }
            .setNegativeButton(APP_ACTIVITY.getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    override fun onStart() {
        super.onStart()
        initialize()
    }

    private fun initialize() {

        listMarkedReserves.clear()

        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // add menu
        setHasOptionsMenu(true)

        //Recycler view
        mAdapter = ArchiveReservesListAdapter(this)
        mRecyclerView = mBinding.archiveReservesRecyclerView
        mRecyclerView.adapter = mAdapter

        // ViewModel
        mViewModel = ViewModelProvider(APP_ACTIVITY).get(ArchiveReservesListViewModel::class.java)
        val roomId = arguments?.getLong("roomId")!!
        mViewModel.getRoomById(roomId) {
            mCurrentRoom = it
            APP_ACTIVITY.title = "${getString(R.string.archive)}. ${mCurrentRoom.name}"
            setDataToAdapter()
        }

    }

    fun setDataToAdapter() {
        mViewModel.getArchiveReservesByRoomId(mCurrentRoom.id).observe(this) { reserves ->
            mCurrentList = reserves
            mAdapter.setData(reserves)
        }
    }

}