package ru.butterbean.easyrent.screens.rooms_list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.databinding.FragmentRoomsListBinding
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.utils.APP_ACTIVITY
import ru.butterbean.easyrent.utils.createArgsBundle
import ru.butterbean.easyrent.database.deleteRoomWithDialog
import ru.butterbean.easyrent.database.getEmptyRoom

class RoomsListFragment : Fragment() {

    private lateinit var mViewModel: RoomsListViewModel
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mObserverList: Observer<List<RoomData>>
    private var _binding: FragmentRoomsListBinding? = null
    private val mBinding get() = _binding!!

    companion object {
        fun clickOnListItem(room: RoomData) {
            goToRoomFragment(room)
        }

        fun longClickOnListItem(room: RoomData, lo: LifecycleOwner) {
            showEditDeleteRoomDialog(room, lo)
        }

        private fun showEditDeleteRoomDialog(room: RoomData, lo: LifecycleOwner) {
            val actions = arrayOf(
                APP_ACTIVITY.getString(R.string.open), // 0
                APP_ACTIVITY.getString(R.string.delete) // 1
            )
            val builder = AlertDialog.Builder(APP_ACTIVITY)
            builder.setItems(actions) { _, i ->
                when (i) {
                    0 -> goToRoomFragment(room)
                    1 -> deleteRoomWithDialog(room, lo) {}
                }
            }
                .show()
        }

        private fun goToRoomFragment(room: RoomData) {
            val args = createArgsBundle("room", room)
            args.putBoolean("fromRoomsList",true)
            APP_ACTIVITY.navController.navigate(
                R.id.action_roomsListFragment_to_roomFragment,
                args
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoomsListBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mViewModel.readAllRooms.removeObserver(mObserverList)
        mRecyclerView.adapter = null
    }

    override fun onStart() {
        super.onStart()
        initialization()
    }

    private fun initialization() {
        APP_ACTIVITY.title = getString(R.string.rooms_list)
        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        setHasOptionsMenu(true)

        //Recycler view
        val adapter = RoomsListAdapter(viewLifecycleOwner)
        mRecyclerView = mBinding.roomsRecyclerView
        mRecyclerView.adapter = adapter

        // ViewModel
        mObserverList = Observer {
            adapter.setData(it)
        }
        mViewModel = ViewModelProvider(APP_ACTIVITY).get(RoomsListViewModel::class.java)
        mViewModel.readAllRooms.observe(viewLifecycleOwner, mObserverList)

        mBinding.roomsBtnAdd.setOnClickListener {
            APP_ACTIVITY.navController.navigate(
                R.id.action_roomsListFragment_to_editRoomFragment,
                createArgsBundle("room", getEmptyRoom())
            )
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.rooms_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                APP_ACTIVITY.navController.navigate(R.id.action_roomsListFragment_to_settingsFragment)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }
}