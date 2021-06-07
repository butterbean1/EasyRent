package ru.butterbean.easyrent.screens.room

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_room.*
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.database.models.RoomData
import ru.butterbean.easyrent.database.view_models.ReserveViewModel
import ru.butterbean.easyrent.database.view_models.RoomViewModel
import ru.butterbean.easyrent.databinding.FragmentRoomBinding
import ru.butterbean.easyrent.databinding.FragmentSplashBinding
import ru.butterbean.easyrent.screens.reserves.EditReserveFragment
import ru.butterbean.easyrent.screens.reserves.ReservesListAdapter
import ru.butterbean.easyrent.utils.*

class RoomFragment : Fragment() {
    private lateinit var mReserveViewModel: ReserveViewModel
    private lateinit var mCurrentRoom: RoomData
    private var _binding: FragmentRoomBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var mAdapter: ReservesListAdapter
    private lateinit var mRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRoomBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mRecyclerView.adapter = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.confirm_edit -> {
                APP_ACTIVITY.navController.navigate(R.id.action_roomFragment_to_editRoomFragment)
                true
            }
            R.id.delete -> {
                deleteRoomWithDialog(mCurrentRoom,viewLifecycleOwner)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStart(){
        super.onStart()

        //Recycler view
        mAdapter = ReservesListAdapter()
        mRecyclerView = mBinding.roomReservesRecyclerView
        mRecyclerView.adapter = mAdapter

        // ViewModel
        val roomViewModel = ViewModelProvider(APP_ACTIVITY).get(RoomViewModel::class.java)
        mCurrentRoom = roomViewModel.currentRoom
        APP_ACTIVITY.title = mCurrentRoom.name

        roomViewModel.getReservesCount().observe(viewLifecycleOwner){reservesCount->
            if (reservesCount == 0) room_text_empty_reserves_list.visibility = View.VISIBLE
            else room_text_empty_reserves_list.visibility = View.GONE
        }

        mReserveViewModel = ViewModelProvider(APP_ACTIVITY).get(ReserveViewModel::class.java)
        mReserveViewModel.getReservesByRoomId(mCurrentRoom.id).observe(viewLifecycleOwner, { reserves ->
            mAdapter.setData(reserves)
        })

        room_name.text = mCurrentRoom.name
        if (mCurrentRoom.address.isEmpty()){
            room_address.text = getString(R.string.empty_address)
        }else {
            room_address.text = mCurrentRoom.address
        }
        roomViewModel.getStatus().observe(viewLifecycleOwner, { status ->
            room_status.text = status
        })

        // add menu
        setHasOptionsMenu(true)

        room_btn_add_reserve.setOnClickListener {
            mReserveViewModel.currentReserve = getEmptyReserve(mCurrentRoom.id)
           APP_ACTIVITY.navController.navigate(R.id.action_roomFragment_to_editReserveFragment)
        }
    }

}