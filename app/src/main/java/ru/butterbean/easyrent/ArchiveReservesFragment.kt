package ru.butterbean.easyrent

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ru.butterbean.easyrent.database.view_models.ArchiveReservesViewModel
import ru.butterbean.easyrent.databinding.FragmentArchiveReservesBinding
import ru.butterbean.easyrent.models.ReserveArchiveData
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.screens.room.ArchiveReservesListAdapter
import ru.butterbean.easyrent.utils.APP_ACTIVITY
import ru.butterbean.easyrent.utils.createArgsBundle

class ArchiveReservesFragment : Fragment() {

    private lateinit var mViewModel: ArchiveReservesViewModel
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mCurrentRoom: RoomData
    private lateinit var mAdapter: ArchiveReservesListAdapter
    private var _binding: FragmentArchiveReservesBinding? = null
    private val mBinding get() = _binding!!

    companion object {
        fun clickOnListItem(reserve: ReserveArchiveData) {
            APP_ACTIVITY.navController.navigate(
                R.id.action_archiveReservesFragment_to_editReserveFragment,
                createArgsBundle("reserve", reserve)
            )
        }

        fun longClickOnListItem(reserve: ReserveArchiveData) {
        }
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                APP_ACTIVITY.navController.popBackStack()
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

        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // add menu
        setHasOptionsMenu(true)

        //Recycler view
        mAdapter = ArchiveReservesListAdapter()
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
            mAdapter.setData(reserves)
        }
    }

}