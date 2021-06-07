package ru.butterbean.easyrent.screens.room

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_rooms_list.*
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.database.view_models.RoomViewModel
import ru.butterbean.easyrent.databinding.FragmentRoomsListBinding
import ru.butterbean.easyrent.utils.APP_ACTIVITY
import ru.butterbean.easyrent.utils.getEmptyRoom
import ru.butterbean.easyrent.utils.replaceFragment

class RoomsListFragment : Fragment(R.layout.fragment_rooms_list) {

    private lateinit var mRoomViewModel: RoomViewModel
    private var _binding: FragmentRoomsListBinding? = null
    private val mBinding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRoomsListBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart(){
        super.onStart()
        initialization()
    }

    private fun initialization() {
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
            APP_ACTIVITY.navController.navigate(R.id.action_roomsListFragment_to_roomFragment)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
    }
}