package ru.butterbean.easyrent.screens

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_room.*
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.database.view_models.RoomViewModel
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.utils.APP_ACTIVITY
import ru.butterbean.easyrent.utils.replaceFragment

class RoomFragment(private val paramRoom:RoomData) : BaseFragment(R.layout.fragment_room) {
    private lateinit var mRoomViewModel: RoomViewModel

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.confirm_edit -> {
                replaceFragment(EditRoomFragment(paramRoom))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume(){
        super.onResume()
        mRoomViewModel = ViewModelProvider(this).get(RoomViewModel::class.java)

        APP_ACTIVITY.title = paramRoom.name

        room_name.text = paramRoom.name
        if (paramRoom.address.isEmpty()){
            room_address.text = getString(R.string.empty_address)
        }else {
            room_address.text = paramRoom.address
        }
        room_status.text = paramRoom.status

        // add menu
        setHasOptionsMenu(true)
    }

}