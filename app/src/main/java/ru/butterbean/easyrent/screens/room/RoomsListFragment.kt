package ru.butterbean.easyrent.screens.room

import android.view.Menu
import android.view.MenuInflater
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_rooms_list.*
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.database.view_models.RoomViewModel
import ru.butterbean.easyrent.utils.APP_ACTIVITY
import ru.butterbean.easyrent.utils.getEmptyRoom
import ru.butterbean.easyrent.utils.replaceFragment

class RoomsListFragment : Fragment(R.layout.fragment_rooms_list) {

    private lateinit var mRoomViewModel: RoomViewModel

    override fun onResume(){
        super.onResume()

        APP_ACTIVITY.title = getString(R.string.rooms_list)
        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        setHasOptionsMenu(true)

        //Recycler view
        val adapter = RoomsListAdapter(this)
        val recyclerView = rooms_recycler_view
        recyclerView.adapter = adapter

        // ViewModel
        mRoomViewModel = ViewModelProvider(APP_ACTIVITY).get(RoomViewModel::class.java)
        mRoomViewModel.readAllRooms.observe(viewLifecycleOwner, { rooms ->
            adapter.setData(rooms)
        })

        rooms_btn_add.setOnClickListener {
            mRoomViewModel.currentRoom = getEmptyRoom()
            replaceFragment(EditRoomFragment())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
    }
}