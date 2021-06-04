package ru.butterbean.easyrent.screens.room

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_edit_room.*
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.database.STATUS_FREE
import ru.butterbean.easyrent.database.view_models.RoomViewModel
import ru.butterbean.easyrent.database.models.RoomData
import ru.butterbean.easyrent.screens.base.BaseFragment
import ru.butterbean.easyrent.utils.APP_ACTIVITY
import ru.butterbean.easyrent.utils.deleteRoomWithDialog
import ru.butterbean.easyrent.utils.replaceFragment
import ru.butterbean.easyrent.utils.showToast

class EditRoomFragment() : BaseFragment(R.layout.fragment_edit_room) {

    private var mIsNew = false
    private lateinit var mRoomViewModel: RoomViewModel
    private lateinit var mCurrentRoom: RoomData

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (mIsNew) inflater.inflate(R.menu.confirm_menu, menu)
        else inflater.inflate(R.menu.confirm_delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.confirm_change -> {
                change()
                true
            }
            R.id.delete -> {
                deleteRoomWithDialog(mCurrentRoom, viewLifecycleOwner)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun change() {
        val name = room_change_name.text.toString()
        if (name.isEmpty()) {
            showToast("Введите название!")
        } else {
            val room = RoomData(mCurrentRoom.id, name, mCurrentRoom.address, mCurrentRoom.status)
            if (mIsNew) {
                // если новое помещение - добавляем в базу и переходим в список
                mRoomViewModel.addRoom(room)
            } else {
                // если редактируем - записываем изменения и переходим в карточку помещения
                mRoomViewModel.updateRoom(room)
            }
            if (mIsNew) replaceFragment(RoomFragment(), false)
            else APP_ACTIVITY.supportFragmentManager.popBackStack()
        }
    }

    override fun onResume() {
        super.onResume()

        mRoomViewModel = ViewModelProvider(APP_ACTIVITY).get(RoomViewModel::class.java)
        mCurrentRoom = mRoomViewModel.currentRoom
        mIsNew = mCurrentRoom.name.isEmpty()

        if (mIsNew) {
            APP_ACTIVITY.title = getString(R.string.new_room)
        } else {
            APP_ACTIVITY.title = mCurrentRoom.name
        }
        room_change_name.setText(mCurrentRoom.name)
        room_change_address.setText(mCurrentRoom.address)
        if (mIsNew) {
            room_change_status.text = STATUS_FREE
            room_change_status.visibility = View.INVISIBLE
        } else {
            room_change_status.text = mCurrentRoom.status
        }

        room_change_name.requestFocus()
        // add menu
        setHasOptionsMenu(true)
    }
}