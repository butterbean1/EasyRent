package ru.butterbean.easyrent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_rooms_list.view.*
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.utils.APP_ACTIVITY

class RoomsListFragment : Fragment() {

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.supportActionBar?.show()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_rooms_list, container, false)
        view.rooms_btn_add.setOnClickListener {
            val nullRoom = RoomData(0,"","","")
            val action = RoomsListFragmentDirections.actionRoomsListFragmentToRoomChangeFragment(nullRoom)
            findNavController().navigate(action)
        }
        return view
    }

}