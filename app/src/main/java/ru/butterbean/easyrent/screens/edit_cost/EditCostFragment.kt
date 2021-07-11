package ru.butterbean.easyrent.screens.edit_cost

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.database.deleteRoomWithDialog
import ru.butterbean.easyrent.databinding.FragmentEditCostBinding
import ru.butterbean.easyrent.models.CostData
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.screens.edit_room.EditRoomViewModel
import ru.butterbean.easyrent.utils.APP_ACTIVITY
import ru.butterbean.easyrent.utils.ONLY_ONE_ROOM
import ru.butterbean.easyrent.utils.createArgsBundle
import ru.butterbean.easyrent.utils.showToast

class EditCostFragment : Fragment() {
    private var mIsNew = false
    private lateinit var mViewModel: EditCostViewModel
    private lateinit var mCurrentCost: CostData
    private var _binding: FragmentEditCostBinding? = null
    private val mBinding get() = _binding!!
    private var mCurrentCostItemId:Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEditCostBinding.inflate(layoutInflater, container, false)
        mCurrentCost = arguments?.getSerializable("cost") as CostData
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
                showDeleteDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun showDeleteDialog() {
        val builder = AlertDialog.Builder(this.context)
        builder.setMessage(getString(R.string.finally_reserve_delete_message))
            .setPositiveButton(APP_ACTIVITY.getString(R.string.yes)) { dialog, _ ->
                dialog.cancel()
                mViewModel.deleteCost(mCurrentCost) {
                    goToCostListFragment()
                }
            }
            .setNegativeButton(APP_ACTIVITY.getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }


    private fun change() {
        val date = mBinding.editCostDate.text.toString().trim()
//        val address = mBinding.roomChangeAddress.text.toString().trim()
        if (date.isEmpty()) {
            showToast(getString(R.string.enter_checkin_date))
        } else {
            val cost = CostData(mCurrentCost.id, mCurrentCost.roomId, mCurrentCostItemId, TODO("add fields"))
            if (mIsNew) {
                mViewModel.addCost(cost) {
                    goToCostListFragment()
                }
            } else {
                mViewModel.updateCost(cost) {
                    goToCostListFragment()
                }

            }

        }
    }

    private fun goToCostListFragment() {
        APP_ACTIVITY.navController.popBackStack()
    }

    override fun onStart() {
        super.onStart()
        initialize()
    }

    private fun initialize() {
        mIsNew = mCurrentCost.id == 0.toLong()
        val prefs = PreferenceManager.getDefaultSharedPreferences(APP_ACTIVITY)
        val useAddresses = prefs.getBoolean("useRoomAddresses", true)

        APP_ACTIVITY.title = getString(R.string.costs)

        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mViewModel = ViewModelProvider(APP_ACTIVITY).get(EditCostViewModel::class.java)

        mBinding.editCostItem.text = mCurrentCost.costItemId.toString()

        mBinding.editCostSum.requestFocus()
        // add menu
        setHasOptionsMenu(true)
    }

}