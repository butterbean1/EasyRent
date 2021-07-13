package ru.butterbean.easyrent.screens.edit_cost

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.databinding.FragmentEditCostBinding
import ru.butterbean.easyrent.models.CostData
import ru.butterbean.easyrent.models.CostItemData
import ru.butterbean.easyrent.utils.*

class EditCostFragment : Fragment() {
    private var mIsNew = false
    private lateinit var mViewModel: EditCostViewModel
    private lateinit var mCurrentCost: CostData
    private var _binding: FragmentEditCostBinding? = null
    private val mBinding get() = _binding!!
    private var mCurrentCostItem: CostItemData? = null
    private var mCurrentDate = "" // date in format yyyy-MM-dd
    private lateinit var mDateSetListener: DatePickerDialog.OnDateSetListener

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
                hideKeyboard()
                change()
                true
            }
            R.id.delete -> {
                mViewModel.deleteCost(mCurrentCost) {
                    goToCostListFragment()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun change() {
        val date = mBinding.editCostDate.text.toString()
        val sum = mBinding.editCostSum.text.toString().trim()
        val descr = mBinding.editCostDescription.text.toString().trim()
        when {
            mCurrentCostItem == null -> showToast(getString(R.string.enter_cost_item))
            date.isEmpty() -> showToast(getString(R.string.enter_cost_date))
            sum.isEmpty() -> showToast(getString(R.string.enter_cost_sum))

            else -> {
                val sumInt = getSumInt(sum)
                val cost = CostData(
                    mCurrentCost.id,
                    mCurrentCost.roomId,
                    mCurrentCostItem!!.id,
                    sumInt,
                    getDateTimeInDatabaseFormat(mCurrentDate, "00:00"),
                    descr
                )
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
    }

    private fun getSumInt(sum: String) = if (sum.isEmpty()) 0 else Integer.parseInt(sum)

    private fun goToCostListFragment() {
        APP_ACTIVITY.navController.popBackStack()
    }

    override fun onStart() {
        super.onStart()
        initialize()
    }

    private fun initialize() {
        mIsNew = mCurrentCost.id == 0.toLong()

        if (mIsNew) APP_ACTIVITY.title = getString(R.string.new_costs)
        else APP_ACTIVITY.title = getString(R.string.costs)

        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mViewModel = ViewModelProvider(APP_ACTIVITY).get(EditCostViewModel::class.java)

        if (mCurrentCost.date.isNotEmpty()) {
            mCurrentDate = mCurrentCost.date.substring(0, 10)
            mBinding.editCostDate.text = mCurrentCost.date.toDateFormat(false)
        }

        mBinding.editCostDescription.setText(mCurrentCost.description)

        if (mCurrentCost.sum != 0) {
            mBinding.editCostSum.setText(mCurrentCost.sum.toString())
        }
        if (mCurrentCost.costItemId != 0L) {
            mViewModel.getCostItemById(mCurrentCost.costItemId) { costItem ->
                mCurrentCostItem = costItem
                mBinding.editCostItem.text = costItem.name
            }
        }
        // date диалог
        mBinding.editCostDate.setOnClickListener {
            hideKeyboard()
            showCalendarDialogFromListener(
                requireContext(),
                mDateSetListener,
                mCurrentDate
            )
        }
        mDateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            mCurrentDate = getDateFormatISO(year, month, dayOfMonth)
            mBinding.editCostDate.text = mCurrentDate.toDateFormat(true)
        }

        mBinding.editCostItem.setOnClickListener {
            val argCost = CostData(
                mCurrentCost.id,
                mCurrentCost.roomId,
                mCurrentCostItem?.id ?: 0,
                getSumInt(mBinding.editCostSum.text.toString()),
                if (mCurrentDate.isEmpty()) "" else getDateTimeInDatabaseFormat(mCurrentDate, "00:00"),
                mBinding.editCostDescription.text.toString()
                )
            APP_ACTIVITY.navController.navigate(
                R.id.action_editCostFragment_to_costItemsListFragment,
                createArgsBundle("cost", argCost)
            )
        }

        // add menu
        setHasOptionsMenu(true)
    }

}