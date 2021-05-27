package ru.butterbean.easyrent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_rooms_list.view.*
import ru.butterbean.easyrent.models.Room

class RoomsListFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_rooms_list, container, false)
        view.rooms_btn_add.setOnClickListener {
            findNavController().navigate(R.id.action_roomsListFragment_to_roomChangeFragment)
        }
        return view
    }

}