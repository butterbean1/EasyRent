package ru.butterbean.easyrent.screens.room

import android.view.*
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_edit_room.*
import ru.butterbean.easyrent.CURRENT_ROOM
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.STATUS_FREE
import ru.butterbean.easyrent.database.view_models.RoomViewModel
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.screens.base.BaseFragment
import ru.butterbean.easyrent.utils.APP_ACTIVITY
import ru.butterbean.easyrent.utils.showToast

class EditRoomFragment() : BaseFragment(R.layout.fragment_edit_room) {

    private var mIsNew = false
    private lateinit var mRoomViewModel: RoomViewModel

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.confirm_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.confirm_change -> {
                change()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun change() {
        val name = room_change_name.text.toString()
        val address = room_change_address.text.toString()
        val status = room_change_status.text.toString()
        if (name.isEmpty()){
            showToast("Введите название!")
        }else{
            val room = RoomData(CURRENT_ROOM.id,name,address,status)
            if (mIsNew){
                // если новое помещение - добавляем в базу и переходим в список
                mRoomViewModel.addRoom(room)
            }else{
                // если редактируем - записываем изменения и переходим в карточку помещения
                mRoomViewModel.updateRoom(room)
            }
            APP_ACTIVITY.supportFragmentManager.popBackStack()

        }
    }

    override fun onResume(){
        super.onResume()

        mRoomViewModel = ViewModelProvider(this).get(RoomViewModel::class.java)
        mIsNew = CURRENT_ROOM.name.isEmpty()

        if (mIsNew){
            APP_ACTIVITY.title = getString(R.string.new_room)
        }else{
            APP_ACTIVITY.title = CURRENT_ROOM.name
        }
        room_change_name.setText(CURRENT_ROOM.name)
        room_change_address.setText(CURRENT_ROOM.address)
        if (mIsNew){
            room_change_status.text = STATUS_FREE
            room_change_status.visibility = View.INVISIBLE
        }else{
            room_change_status.text = CURRENT_ROOM.status
        }

        room_change_name.requestFocus()
        // add menu
        setHasOptionsMenu(true)
    }
}