package ru.butterbean.easyrent.screens

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_room_change.*
import kotlinx.android.synthetic.main.fragment_room_change.view.*
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.STATUS_FREE
import ru.butterbean.easyrent.database.view_models.RoomViewModel
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.utils.APP_ACTIVITY
import ru.butterbean.easyrent.utils.showToast

class ChangeRoomFragment : Fragment() {

    private val args by navArgs<ChangeRoomFragmentArgs>()
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
            val room = RoomData(args.currentRoom.id,name,address,status)
            if (mIsNew){
                // если новое помещение - добавляем в базу и переходим в список
                mRoomViewModel.addRoom(room)
                findNavController().navigate(R.id.action_roomChangeFragment_to_roomsListFragment)
            }else{
                // если редактируем - записываем изменения и переходим в карточку помещения
                mRoomViewModel.updateRoom(room)
                val action = ChangeRoomFragmentDirections.actionRoomChangeFragmentToRoomFragment(room)
                findNavController().navigate(action)
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mRoomViewModel = ViewModelProvider(this).get(RoomViewModel::class.java)
        mIsNew = args.currentRoom.name.isEmpty()

        if (mIsNew){
            APP_ACTIVITY.supportActionBar?.title = getString(R.string.new_room)
        }else{
            APP_ACTIVITY.supportActionBar?.title = args.currentRoom.name
        }
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_room_change, container, false)

        view.room_change_name.setText(args.currentRoom.name)
        view.room_change_address.setText(args.currentRoom.address)
        if (mIsNew){
            view.room_change_status.text = STATUS_FREE
            view.room_change_status.visibility = View.INVISIBLE
        }else{
            view.room_change_status.text = args.currentRoom.status
        }

        // add menu
        setHasOptionsMenu(true)

        return view
    }



}