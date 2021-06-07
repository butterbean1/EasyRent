package ru.butterbean.easyrent.screens.room

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.database.view_models.RoomViewModel
import ru.butterbean.easyrent.databinding.FragmentRoomBinding
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.screens.reserves.ReservesListAdapter
import ru.butterbean.easyrent.utils.*

class RoomFragment : Fragment() {
    lateinit var viewModel: RoomViewModel
    private lateinit var mCurrentRoom: RoomData
    private var _binding: FragmentRoomBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var mRecyclerView: RecyclerView

    companion object{
        fun clickOnListItem(reserve: ReserveData) {
            goToEditReserveFragment(reserve)
        }

        fun longClickOnListItem(reserve: ReserveData,fragment: RoomFragment) {
            showEditDeleteReserveDialog(reserve,fragment)
        }
        private fun showEditDeleteReserveDialog(reserve: ReserveData,fragment: RoomFragment) {
            val actions = arrayOf(
                APP_ACTIVITY.getString(R.string.edit), // 0
                APP_ACTIVITY.getString(R.string.delete) // 1
            )
            val builder = AlertDialog.Builder(APP_ACTIVITY)
            builder.setItems(actions) { _, i ->
                when (i) {
                    0 -> goToEditReserveFragment(reserve)
                    1 -> fragment.viewModel.deleteReserve(reserve)
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
        return mBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mRecyclerView.adapter = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
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
                deleteRoomWithDialog(mCurrentRoom, viewLifecycleOwner)
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
        //Recycler view
        val adapter = ReservesListAdapter(this)
        mRecyclerView = mBinding.roomReservesRecyclerView
        mRecyclerView.adapter = adapter

        // ViewModel
        viewModel = ViewModelProvider(APP_ACTIVITY).get(RoomViewModel::class.java)
        APP_ACTIVITY.title = mCurrentRoom.name

        viewModel.getReservesCount(mCurrentRoom.id).observe(viewLifecycleOwner) { reservesCount ->
            if (reservesCount == 0) mBinding.roomTextEmptyReservesList.visibility = View.VISIBLE
            else mBinding.roomTextEmptyReservesList.visibility = View.GONE
        }

        viewModel = ViewModelProvider(this).get(RoomViewModel::class.java)
        viewModel.getReservesByRoomId(mCurrentRoom.id).observe(viewLifecycleOwner, { reserves ->
            adapter.setData(reserves)
        })

        mBinding.roomName.text = mCurrentRoom.name
        if (mCurrentRoom.address.isEmpty()) {
            mBinding.roomAddress.text = getString(R.string.empty_address)
        } else {
            mBinding.roomAddress.text = mCurrentRoom.address
        }
        viewModel.getStatus(mCurrentRoom.id).observe(viewLifecycleOwner) { status ->
            mBinding.roomStatus.text = status
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

}