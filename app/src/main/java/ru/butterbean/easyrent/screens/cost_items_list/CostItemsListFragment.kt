package ru.butterbean.easyrent.screens.cost_items_list

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.database.getEmptyCostItem
import ru.butterbean.easyrent.databinding.FragmentCostItemsListBinding
import ru.butterbean.easyrent.models.CostItemData
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.utils.APP_ACTIVITY
import ru.butterbean.easyrent.utils.createArgsBundle


class CostItemsListFragment : Fragment() {
    private lateinit var mViewModel: CostItemsListViewModel
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mCurrentRoom: RoomData
    private var _binding: FragmentCostItemsListBinding? = null
    private val mBinding get() = _binding!!

    companion object {
        fun clickOnListItem(costItemId: Long, f: CostItemsListFragment) {
            f.mViewModel.getCostItemById(costItemId) { costItem ->
                APP_ACTIVITY.navController.navigate(
                    R.id.action_costItemsListFragment_to_editCostFragment,
                    createArgsBundle("costItem", costItem)
                )
            }
        }

        fun longClickOnListItem(costId: Long, f: CostItemsListFragment) {
            f.mViewModel.getCostItemById(costId) { costItem ->
                showDeleteDialog(costItem, f)
            }
        }

        private fun showDeleteDialog(costItem: CostItemData, f: CostItemsListFragment) {
            val actions = arrayOf(
                APP_ACTIVITY.getString(R.string.edit), // 0
                APP_ACTIVITY.getString(R.string.delete) // 1
            )
            val builder = AlertDialog.Builder(APP_ACTIVITY)
            builder.setItems(actions) { _, i ->
                when (i) {
                    0 -> APP_ACTIVITY.navController.navigate(
                        R.id.action_costItemsListFragment_to_editCostItemFragment,
                        createArgsBundle("costItem", costItem)
                    )
                    1 -> f.mViewModel.deleteCostItem(costItem)
                }
            }
                .show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> APP_ACTIVITY.navController.popBackStack()
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCostItemsListBinding.inflate(layoutInflater, container, false)
        mCurrentRoom = arguments?.getSerializable("room") as RoomData
        return mBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mRecyclerView.adapter = null
    }


    override fun onStart() {
        super.onStart()
        initialization()
    }

    private fun initialization() {
        APP_ACTIVITY.title = getString(R.string.select_cost_item)

        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // add menu
        setHasOptionsMenu(true)


        //Recycler view
        val adapter = CostItemsListAdapter(this)
        mRecyclerView = mBinding.costItemsRecyclerView
        mRecyclerView.adapter = adapter

        // ViewModel
        mViewModel = ViewModelProvider(APP_ACTIVITY).get(CostItemsListViewModel::class.java)
        mViewModel.getAllCostItems().observe(viewLifecycleOwner) {
            adapter.setData(it.asReversed())
        }

        mBinding.costItemsBtnAdd.setOnClickListener {
            APP_ACTIVITY.navController.navigate(R.id.action_costItemsListFragment_to_editCostItemFragment,
                createArgsBundle("costItem", getEmptyCostItem())
            )
        }

    }
}