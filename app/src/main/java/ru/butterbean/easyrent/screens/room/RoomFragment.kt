package ru.butterbean.easyrent.screens.room

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.databinding.FragmentRoomBinding
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.screens.room.item_models.ArchiveReserveModel
import ru.butterbean.easyrent.screens.room.item_models.FreeReserveModel
import ru.butterbean.easyrent.screens.room.item_models.CommonReserveModel
import ru.butterbean.easyrent.screens.room.item_models.SimpleReserveModel
import ru.butterbean.easyrent.utils.*

class RoomFragment : Fragment() {
    lateinit var mViewModel: RoomViewModel
    private lateinit var mCurrentRoom: RoomData
    private var _binding: FragmentRoomBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var mRecyclerView: RecyclerView
    private var mDoNotShowFreeReserves = false
    private lateinit var mAdapter: ReservesListAdapter
    private var mNavFromRoomsList = false
    private var mHasArchive = false

    companion object {
        fun clickOnListItem(commonReserveModel: CommonReserveModel) {
            when (commonReserveModel.getItemViewType()) {
                CommonReserveModel.SIMPLE -> {
                    val reserveModel = commonReserveModel as SimpleReserveModel
                    goToEditReserveFragment(reserveModel.toReserveData())
                }
                CommonReserveModel.FREE -> {
                    val reserveModel = commonReserveModel as FreeReserveModel
                    goToEditReserveFragment(reserveModel.toReserveData())
                }
                CommonReserveModel.ARCHIVE -> {
                    val reserveModel = commonReserveModel as ArchiveReserveModel
                    val args = Bundle()
                    args.putLong("roomId", reserveModel.roomId)
                    APP_ACTIVITY.navController.navigate(
                        R.id.action_roomFragment_to_archiveReservesFragment,
                        args
                    )
                }
            }

        }

        fun longClickOnListItem(commonReserveModel: CommonReserveModel, fragment: RoomFragment) {
            when (commonReserveModel.getItemViewType()) {
                CommonReserveModel.SIMPLE -> {
                    val reserveModel = commonReserveModel as SimpleReserveModel
                    showEditDeleteReserveDialog(reserveModel.toReserveData(), fragment)
                }
            }
        }

        private fun showEditDeleteReserveDialog(reserve: ReserveData, fragment: RoomFragment) {
            var actions = emptyArray<String>()
            actions += APP_ACTIVITY.getString(R.string.open)// 0
            actions += APP_ACTIVITY.getString(R.string.delete)// 1
            if (reserve.wasCheckOut) actions += APP_ACTIVITY.getString(R.string.replace_to_archive)// 2

            val builder = AlertDialog.Builder(APP_ACTIVITY)
            builder.setItems(actions) { _, i ->
                when (i) {
                    0 -> goToEditReserveFragment(reserve)
                    1 -> {
                        fragment.mViewModel.deleteReserve(reserve)
                        { fragment.setDataToAdapter() }
                    }
                    2 -> {
                        fragment.mViewModel.replaceReserveToArchive(reserve)
                        { fragment.setDataToAdapter() }
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
        mNavFromRoomsList = arguments?.getBoolean("fromRoomsList", false) ?: false
        return mBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mRecyclerView.adapter = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (ONLY_ONE_ROOM) inflater.inflate(R.menu.edit_room_menu, menu)
        else inflater.inflate(R.menu.edit_menu, menu)
        menu.findItem(R.id.go_to_archive).isVisible = mHasArchive
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                goToRoomsList()
                true
            }
            R.id.go_to_archive -> {
                val args = Bundle()
                args.putLong("roomId", mCurrentRoom.id)
                APP_ACTIVITY.navController.navigate(
                    R.id.action_roomFragment_to_archiveReservesFragment,
                    args
                )
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
                mViewModel.getRoomById(mCurrentRoom.id).observe(viewLifecycleOwner) {
                    APP_ACTIVITY.navController.navigate(
                        R.id.action_roomFragment_to_editRoomFragment,
                        createArgsBundle("room", it)
                    )
                }
                true
            }
            R.id.delete -> {
                deleteRoomWithDialog(mCurrentRoom, viewLifecycleOwner) { wasDeleted ->
                    if (wasDeleted) goToRoomsList()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun goToRoomsList() {
        if (mNavFromRoomsList) APP_ACTIVITY.navController.popBackStack()
        else APP_ACTIVITY.navController.navigate(R.id.action_roomFragment_to_roomsListFragment)
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
        if (useAddresses) dontShowAddress =
            useAddresses && prefs.getBoolean("doNotShowAddressInRoomCard", false)

        //Recycler view
        mAdapter = ReservesListAdapter(this)
        mRecyclerView = mBinding.roomReservesRecyclerView
        mRecyclerView.adapter = mAdapter

        // ViewModel
        mViewModel = ViewModelProvider(APP_ACTIVITY).get(RoomViewModel::class.java)

        mViewModel.getReservesArchiveCount(mCurrentRoom.id) { count ->
            mHasArchive = count > 0
            // add menu
            setHasOptionsMenu(true)
        }
        mViewModel.getReservesCount(mCurrentRoom.id).observe(viewLifecycleOwner) { reservesCount ->
            if (reservesCount == 0) mBinding.roomTextEmptyReservesList.visibility = View.VISIBLE
            else mBinding.roomTextEmptyReservesList.visibility = View.GONE
        }

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

        mBinding.roomBtnAddReserve.setOnClickListener {
            APP_ACTIVITY.navController.navigate(
                R.id.action_roomFragment_to_editReserveFragment,
                createArgsBundle("reserve", getEmptyReserve(mCurrentRoom.id))
            )
        }
    }

    fun setDataToAdapter() {
        mViewModel.getReservesByRoomId(mCurrentRoom.id, mDoNotShowFreeReserves) { reserves ->
            mAdapter.setData(reserves)
        }
    }


}