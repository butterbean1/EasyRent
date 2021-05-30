package ru.butterbean.easyrent.screens.room

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_room.*
import ru.butterbean.easyrent.CURRENT_ROOM
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.database.view_models.RoomViewModel
import ru.butterbean.easyrent.screens.base.BaseFragment
import ru.butterbean.easyrent.utils.APP_ACTIVITY
import ru.butterbean.easyrent.utils.replaceFragment

class RoomFragment() : BaseFragment(R.layout.fragment_room) {
    private lateinit var mRoomViewModel: RoomViewModel

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.confirm_edit -> {
                replaceFragment(EditRoomFragment())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume(){
        super.onResume()
        mRoomViewModel = ViewModelProvider(this).get(RoomViewModel::class.java)

        APP_ACTIVITY.title = CURRENT_ROOM.name

        room_name.text = CURRENT_ROOM.name
        if (CURRENT_ROOM.address.isEmpty()){
            room_address.text = getString(R.string.empty_address)
        }else {
            room_address.text = CURRENT_ROOM.address
        }
        room_status.text = CURRENT_ROOM.status

        // add menu
        setHasOptionsMenu(true)
    }

}