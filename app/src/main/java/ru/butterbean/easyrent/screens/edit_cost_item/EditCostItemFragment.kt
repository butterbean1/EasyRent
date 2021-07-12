package ru.butterbean.easyrent.screens.edit_cost_item

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.databinding.FragmentEditCostItemBinding
import ru.butterbean.easyrent.models.CostItemData
import ru.butterbean.easyrent.utils.APP_ACTIVITY
import ru.butterbean.easyrent.utils.hideKeyboard
import ru.butterbean.easyrent.utils.showToast

class EditCostItemFragment : Fragment() {
    private var mIsNew = false
    private lateinit var mViewModel: EditCostItemViewModel
    private var _binding: FragmentEditCostItemBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var mCurrentCostItem: CostItemData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEditCostItemBinding.inflate(layoutInflater, container, false)
        mCurrentCostItem = arguments?.getSerializable("costItem") as CostItemData
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
                mViewModel.deleteCostItem(mCurrentCostItem) {
                    goToCostItemsListFragment()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun change() {
        val name = mBinding.editCostItemName.text.toString()
        when {
            name.isEmpty() -> showToast(getString(R.string.enter_cost_item_name))

            else -> {
                val costItem = CostItemData(
                    mCurrentCostItem.id,
                    name
                )
                if (mIsNew) {
                    mViewModel.addCostItem(costItem) {
                        goToCostItemsListFragment()
                    }
                } else {
                    mViewModel.updateCostItem(costItem) {
                        goToCostItemsListFragment()
                    }

                }

            }
        }
    }

    private fun goToCostItemsListFragment() {
        APP_ACTIVITY.navController.popBackStack()
    }

    override fun onStart() {
        super.onStart()
        initialize()
    }

    private fun initialize() {
        mIsNew = mCurrentCostItem.id == 0.toLong()

        APP_ACTIVITY.title = getString(R.string.cost_item)

        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mViewModel = ViewModelProvider(APP_ACTIVITY).get(EditCostItemViewModel::class.java)

        if (!mIsNew){
            mBinding.editCostItemName.setText(mCurrentCostItem.name)
            }

        // add menu
        setHasOptionsMenu(true)
    }

}