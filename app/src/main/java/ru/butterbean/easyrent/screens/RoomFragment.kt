package ru.butterbean.easyrent.screens

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_room.view.*
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.database.view_models.RoomViewModel
import ru.butterbean.easyrent.utils.APP_ACTIVITY

class RoomFragment : Fragment() {
    private val args by navArgs<RoomFragmentArgs>()
    private lateinit var mRoomViewModel: RoomViewModel

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.confirm_edit -> {
                val action = RoomFragmentDirections.actionRoomFragmentToRoomChangeFragment(args.currentRoom)
                findNavController().navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRoomViewModel = ViewModelProvider(this).get(RoomViewModel::class.java)

        APP_ACTIVITY.supportActionBar?.title = args.currentRoom.name

        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_room, container, false)

        view.room_name.text = args.currentRoom.name
        if (args.currentRoom.address.isEmpty()){
            view.room_address.text = getString(R.string.empty_address)
        }else {
            view.room_address.text = args.currentRoom.address
        }
        view.room_status.text = args.currentRoom.status

        // add menu
        setHasOptionsMenu(true)

        return view
    }

}