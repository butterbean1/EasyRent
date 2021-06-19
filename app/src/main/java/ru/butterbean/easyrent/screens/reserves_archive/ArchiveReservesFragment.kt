package ru.butterbean.easyrent.screens.reserves_archive

import android.os.Bundle
import android.view.*
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.databinding.FragmentArchiveReservesBinding
import ru.butterbean.easyrent.models.ReserveArchiveData
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.utils.APP_ACTIVITY
import ru.butterbean.easyrent.utils.createArgsBundle

class ArchiveReservesFragment : Fragment() {

    private lateinit var mViewModel: ArchiveReservesViewModel
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mCurrentRoom: RoomData
    private lateinit var mAdapter: ArchiveReservesListAdapter
    private var _binding: FragmentArchiveReservesBinding? = null
    private val mBinding get() = _binding!!

    private var mCurrentList = emptyList<ReserveArchiveData>()

    private lateinit var optionsMenu: Menu

    val listMarkedReserves = mutableListOf<ReserveArchiveData>()

    fun goToEditReserveFragment(reserve: ReserveArchiveData) {
        APP_ACTIVITY.navController.navigate(
            R.id.action_archiveReservesFragment_to_editReserveFragment,
            createArgsBundle("reserve", reserve)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArchiveReservesBinding.inflate(layoutInflater, container, false)
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
                APP_ACTIVITY.navController.popBackStack()
                true
            }
            R.id.select_all->{
                listMarkedReserves.clear()
                listMarkedReserves.addAll(mCurrentList)
                setVisibleOptionsMenuItems()
                mAdapter.notifyDataSetChanged()
                true
            }
            R.id.cancel_selection->{
                listMarkedReserves.clear()
                setVisibleOptionsMenuItems()
                mAdapter.notifyDataSetChanged()
                true}
            R.id.delete -> {
                mViewModel.deleteReserves(listMarkedReserves) {
                    listMarkedReserves.clear()
                    setDataToAdapter()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
        mViewModel = ViewModelProvider(APP_ACTIVITY).get(ArchiveReservesViewModel::class.java)
        val roomId = arguments?.getLong("roomId")!!
        mViewModel.getRoomById(roomId).observe(this) {
            mCurrentRoom = it
            APP_ACTIVITY.title = "${getString(R.string.archive)}. ${mCurrentRoom.name}"
            setDataToAdapter()
        }

    }

    fun setDataToAdapter() {
        mViewModel.getReservesByRoomId(mCurrentRoom.id).observe(this) { reserves ->
            mCurrentList = reserves
            mAdapter.setData(reserves)
        }
    }

}