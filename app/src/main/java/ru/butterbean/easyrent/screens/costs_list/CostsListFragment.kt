package ru.butterbean.easyrent.screens.costs_list

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
import ru.butterbean.easyrent.database.getEmptyCost
import ru.butterbean.easyrent.databinding.FragmentCostsListBinding
import ru.butterbean.easyrent.models.CostData
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.utils.APP_ACTIVITY
import ru.butterbean.easyrent.utils.createArgsBundle

class CostsListFragment : Fragment() {
    private lateinit var mViewModel: CostsListViewModel
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mCurrentRoom: RoomData
    private var _binding: FragmentCostsListBinding? = null
    private val mBinding get() = _binding!!

    companion object {
        fun clickOnListItem(costId: Long, f: CostsListFragment) {
            f.mViewModel.getCostById(costId) { cost ->
                APP_ACTIVITY.navController.navigate(
                    R.id.action_costsListFragment_to_editCostFragment,
                    createArgsBundle("cost", cost)
                )
            }
        }

        fun longClickOnListItem(costId: Long, f: CostsListFragment) {
            f.mViewModel.getCostById(costId) { cost ->
                showDeleteDialog(cost, f)
            }
        }

        private fun showDeleteDialog(cost: CostData, f: CostsListFragment) {
            val actions = arrayOf(
                APP_ACTIVITY.getString(R.string.delete) // 0
            )
            val builder = AlertDialog.Builder(APP_ACTIVITY)
            builder.setItems(actions) { _, i ->
                when (i) {
                    0 -> f.mViewModel.deleteCost(cost)
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
        _binding = FragmentCostsListBinding.inflate(layoutInflater, container, false)
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
        APP_ACTIVITY.title = "${getString(R.string.costs)}. ${mCurrentRoom.name}"

        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // add menu
        setHasOptionsMenu(true)


        //Recycler view
        val adapter = CostsListAdapter(this)
        mRecyclerView = mBinding.costsRecyclerView
        mRecyclerView.adapter = adapter

        // ViewModel
        mViewModel = ViewModelProvider(APP_ACTIVITY).get(CostsListViewModel::class.java)
        mViewModel.getCostsByRoomId(mCurrentRoom.id).observe(viewLifecycleOwner) {
            adapter.setData(it.asReversed())
        }

        mBinding.costsBtnAdd.setOnClickListener {
            APP_ACTIVITY.navController.navigate(R.id.action_costsListFragment_to_editCostFragment,
                createArgsBundle("cost",getEmptyCost(mCurrentRoom.id)))
        }

    }
}