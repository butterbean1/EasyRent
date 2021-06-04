package ru.butterbean.easyrent.screens.room

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_room.*
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.database.view_models.ReserveViewModel
import ru.butterbean.easyrent.database.view_models.RoomViewModel
import ru.butterbean.easyrent.database.models.RoomData
import ru.butterbean.easyrent.screens.base.BaseFragment
import ru.butterbean.easyrent.screens.reserves.EditReserveFragment
import ru.butterbean.easyrent.screens.reserves.ReservesListAdapter
import ru.butterbean.easyrent.utils.APP_ACTIVITY
import ru.butterbean.easyrent.utils.deleteRoomWithDialog
import ru.butterbean.easyrent.utils.getEmptyReserve
import ru.butterbean.easyrent.utils.replaceFragment

class RoomFragment() : BaseFragment(R.layout.fragment_room) {
    private lateinit var mReserveViewModel: ReserveViewModel
    private lateinit var mCurrentRoom: RoomData

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.confirm_edit -> {
                replaceFragment(EditRoomFragment())
                true
            }
            R.id.delete -> {
                deleteRoomWithDialog(mCurrentRoom,viewLifecycleOwner)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume(){
        super.onResume()

        //Recycler view
        val adapter = ReservesListAdapter()
        val recyclerView = room_reserves_recycler_view
        recyclerView.adapter = adapter

        // ViewModel
        val roomViewModel = ViewModelProvider(APP_ACTIVITY).get(RoomViewModel::class.java)
        mCurrentRoom = roomViewModel.currentRoom
        APP_ACTIVITY.title = mCurrentRoom.name

        roomViewModel.getReservesCount().observe(viewLifecycleOwner){reservesCount->
            if (reservesCount == 0) room_text_empty_reserves_list.visibility = View.VISIBLE
            else room_text_empty_reserves_list.visibility = View.GONE
        }

        mReserveViewModel = ViewModelProvider(APP_ACTIVITY).get(ReserveViewModel::class.java)
        mReserveViewModel.getReservesByRoomId(mCurrentRoom.id).observe(viewLifecycleOwner, { reserves ->
            adapter.setData(reserves)
        })

        room_name.text = mCurrentRoom.name
        if (mCurrentRoom.address.isEmpty()){
            room_address.text = getString(R.string.empty_address)
        }else {
            room_address.text = mCurrentRoom.address
        }
        roomViewModel.getStatus().observe(viewLifecycleOwner, { status ->
            room_status.text = status
        })

        // add menu
        setHasOptionsMenu(true)

        room_btn_add_reserve.setOnClickListener {
            mReserveViewModel.currentReserve = getEmptyReserve(mCurrentRoom.id)
            replaceFragment(EditReserveFragment())
        }
    }

}