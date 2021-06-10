package ru.butterbean.easyrent.screens.room

import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.fragment_edit_room.*
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.database.view_models.EditRoomViewModel
import ru.butterbean.easyrent.databinding.FragmentEditRoomBinding
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.utils.*
import java.lang.Appendable

class EditRoomFragment : Fragment() {

    private var mAddFirstRoom = false
    private var mIsNew = false
    private lateinit var mViewModel: EditRoomViewModel
    private lateinit var mCurrentRoom: RoomData
    private var _binding: FragmentEditRoomBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEditRoomBinding.inflate(layoutInflater, container, false)
        mCurrentRoom = arguments?.getSerializable("room") as RoomData
        mAddFirstRoom = arguments?.getBoolean("addFirstRoom") ?: false
        return mBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (mIsNew) inflater.inflate(R.menu.confirm_menu, menu)
        else inflater.inflate(R.menu.confirm_delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> APP_ACTIVITY.navController.popBackStack()
            R.id.confirm_change -> {
                change()
                true
            }
            R.id.delete -> {
                deleteRoomWithDialog(mCurrentRoom, viewLifecycleOwner) { wasDeleted ->
                    if (wasDeleted) APP_ACTIVITY.navController.navigate(R.id.action_editRoomFragment_to_roomsListFragment)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun change() {
        val name = room_change_name.text.toString().trim()
        if (name.isEmpty()) {
            showToast(getString(R.string.enter_room_name))
        } else {
            val room = RoomData(mCurrentRoom.id, name, mCurrentRoom.address, mCurrentRoom.status)
            if (mIsNew) {
                // если новое помещение - добавляем в базу и переходим в карточку помещения
                mViewModel.addRoom(room) { newId ->
                    mViewModel.getRoomsCount().observe(this) { roomsCount ->
                        ONLY_ONE_ROOM = roomsCount == 1
                        goToRoomFragment(RoomData(newId, room.name, room.address, room.status))
                    }
                }
            } else {
                // если редактируем - записываем изменения и переходим в карточку помещения
                mViewModel.updateRoom(room) {
                    goToRoomFragment(room)
                }

            }

        }
    }

    private fun goToRoomFragment(room: RoomData) {
        val actionId = if (ONLY_ONE_ROOM) R.id.action_editRoomFragment_to_roomFragment_as_main
        else R.id.action_editRoomFragment_to_roomFragment
        APP_ACTIVITY.navController.navigate(
            actionId,
            createArgsBundle("room", room)
        )
    }

    override fun onStart() {
        super.onStart()
        initialize()
    }

    private fun initialize() {
        mIsNew = mCurrentRoom.id == 0.toLong()
        val prefs = PreferenceManager.getDefaultSharedPreferences(APP_ACTIVITY)
        val useAddresses = prefs.getBoolean("useRoomAddresses", true)

        if (mIsNew) {
            APP_ACTIVITY.title = getString(R.string.new_room)
        } else {
            APP_ACTIVITY.title = mCurrentRoom.name
        }
        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(!mAddFirstRoom)

        mViewModel = ViewModelProvider(APP_ACTIVITY).get(EditRoomViewModel::class.java)

        mBinding.roomChangeName.setText(mCurrentRoom.name)
        if (useAddresses) mBinding.roomChangeAddress.setText(mCurrentRoom.address)
        else mBinding.roomChangeAddress.visibility = View.GONE

        if (mIsNew) {
            mBinding.roomChangeStatus.visibility = View.INVISIBLE
        } else {
            mBinding.roomChangeStatus.text = mCurrentRoom.status
        }

        mBinding.roomChangeName.requestFocus()
        // add menu
        setHasOptionsMenu(true)
    }

}