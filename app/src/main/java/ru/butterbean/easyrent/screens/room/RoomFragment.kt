package ru.butterbean.easyrent.screens.room

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.database.view_models.RoomViewModel
import ru.butterbean.easyrent.databinding.FragmentRoomBinding
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.screens.reserves.*
import ru.butterbean.easyrent.utils.*

class RoomFragment : Fragment() {
    lateinit var viewModel: RoomViewModel
    private lateinit var mCurrentRoom: RoomData
    private var _binding: FragmentRoomBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var mRecyclerView: RecyclerView
    private var mDoNotShowFreeReserves = false
    private lateinit var mAdapter: ReservesListAdapter

        companion object {
        fun clickOnListItem(reserveType: ReserveType) {
            when (reserveType.getItemViewType()) {
                ReserveType.SIMPLE -> {
                    val reserveModel = reserveType as SimpleReserveModel
                    goToEditReserveFragment(reserveModel.toReserveData())
                }
                ReserveType.FREE -> {
                    val reserveModel = reserveType as FreeReserveModel
                    goToEditReserveFragment(reserveModel.toReserveData())
                }
            }

        }

        fun longClickOnListItem(reserveType: ReserveType, fragment: RoomFragment) {
            when (reserveType.getItemViewType()) {
                ReserveType.SIMPLE -> {
                    val reserveModel = reserveType as SimpleReserveModel
                    showEditDeleteReserveDialog(reserveModel.toReserveData(), fragment)
                }
            }
        }

        private fun showEditDeleteReserveDialog(reserve: ReserveData, fragment: RoomFragment) {
            val actions = arrayOf(
                APP_ACTIVITY.getString(R.string.open), // 0
                APP_ACTIVITY.getString(R.string.delete) // 1
            )
            val builder = AlertDialog.Builder(APP_ACTIVITY)
            builder.setItems(actions) { _, i ->
                when (i) {
                    0 -> goToEditReserveFragment(reserve)
                    1 -> {
                        fragment.viewModel.deleteReserve(reserve)
                        fragment.setDataToAdapter()
                    }
                }
            }
                .show()
        }

        private fun goToEditReserveFragment(reserve: ReserveData) {
            APP_ACTIVITY.navController.navigate(
                R.id.action_roomFragment_to_editReserveFragment,
                createArgsBundle("reserve", reserve)
            )
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRoomBinding.inflate(layoutInflater, container, false)
        mCurrentRoom = arguments?.getSerializable("room") as RoomData
        APP_ACTIVITY.onBackPressedDispatcher.addCallback {
            if (ONLY_ONE_ROOM) APP_ACTIVITY.finish()
            else APP_ACTIVITY.navController.navigate(R.id.action_roomFragment_to_roomsListFragment)

        }
        return mBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mRecyclerView.adapter = null
        APP_ACTIVITY.onBackPressedDispatcher.addCallback { APP_ACTIVITY.navController.popBackStack() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (ONLY_ONE_ROOM) inflater.inflate(R.menu.edit_room_menu, menu)
        else inflater.inflate(R.menu.edit_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                APP_ACTIVITY.navController.navigate(R.id.action_roomFragment_to_roomsListFragment)
                true
            }
            R.id.settings -> {
                APP_ACTIVITY.navController.navigate(R.id.action_roomFragment_to_settingsFragment)
                true
            }
            R.id.new_room -> {
                APP_ACTIVITY.navController.navigate(
                    R.id.action_roomFragment_to_editRoomFragment,
                    createArgsBundle("room", getEmptyRoom())
                )
                true
            }
            R.id.confirm_edit -> {
                viewModel.getRoomById(mCurrentRoom.id).observe(viewLifecycleOwner) {
                    APP_ACTIVITY.navController.navigate(
                        R.id.action_roomFragment_to_editRoomFragment,
                        createArgsBundle("room", it)
                    )
                }
                true
            }
            R.id.delete -> {
                deleteRoomWithDialog(mCurrentRoom, viewLifecycleOwner) { wasDeleted ->
                    if (wasDeleted) APP_ACTIVITY.navController.navigate(R.id.action_roomFragment_to_roomsListFragment)
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
        APP_ACTIVITY.title = mCurrentRoom.name

        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(!ONLY_ONE_ROOM)
        val prefs = PreferenceManager.getDefaultSharedPreferences(APP_ACTIVITY)
        mDoNotShowFreeReserves = prefs.getBoolean("doNotShowFreeReserves", false)
        val useAddresses = prefs.getBoolean("useRoomAddresses", true)
        var dontShowAddress = true
            if (useAddresses) dontShowAddress = useAddresses&&prefs.getBoolean("doNotShowAddressInRoomCard", false)

        //Recycler view
        mAdapter = ReservesListAdapter(this)
        mRecyclerView = mBinding.roomReservesRecyclerView
        mRecyclerView.adapter = mAdapter

        // ViewModel
        viewModel = ViewModelProvider(APP_ACTIVITY).get(RoomViewModel::class.java)

        viewModel.getReservesCount(mCurrentRoom.id).observe(viewLifecycleOwner) { reservesCount ->
            if (reservesCount == 0) mBinding.roomTextEmptyReservesList.visibility = View.VISIBLE
            else mBinding.roomTextEmptyReservesList.visibility = View.GONE
        }

        viewModel = ViewModelProvider(this).get(RoomViewModel::class.java)
        setDataToAdapter()

        if (dontShowAddress) {
            mBinding.roomAddress.visibility = View.GONE
            mBinding.roomSeparator.visibility = View.GONE
        } else {
            if (mCurrentRoom.address.isEmpty()) {
                mBinding.roomAddress.text = getString(R.string.empty_address)
            } else {
                mBinding.roomAddress.text = mCurrentRoom.address
            }
        }

        // add menu
        setHasOptionsMenu(true)

        mBinding.roomBtnAddReserve.setOnClickListener {
            APP_ACTIVITY.navController.navigate(
                R.id.action_roomFragment_to_editReserveFragment,
                createArgsBundle("reserve", getEmptyReserve(mCurrentRoom.id))
            )
        }
    }

    fun setDataToAdapter() {
        viewModel.getReservesByRoomId(mCurrentRoom.id, mDoNotShowFreeReserves) { reserves ->
            mAdapter.setData(reserves)
        }
    }


}