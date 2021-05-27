package ru.butterbean.easyrent

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_room_change.*
import kotlinx.android.synthetic.main.fragment_room_change.view.*
import ru.butterbean.easyrent.utils.showToast

class ChangeRoomFragment : Fragment() {

    private val args by navArgs<ChangeRoomFragmentArgs>()

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.confirm_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.confirm_change -> change()
        }
        return true
    }

    private fun change() {
        val name = room_change_name.text.toString()
        if (name.isEmpty()){
            showToast("Введите название!")

        }else{
            showToast("Помещение добавлено")
            findNavController().navigate(R.id.action_roomChangeFragment_to_roomsListFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_room_change, container, false)

        if (args.currentRoom != null){
        view.room_change_name.setText(args.currentRoom.name)
        view.room_change_address.setText(args.currentRoom.address)
        view.room_change_status.text = args.currentRoom.status}
        // add menu
        setHasOptionsMenu(true)

        return view
    }

}