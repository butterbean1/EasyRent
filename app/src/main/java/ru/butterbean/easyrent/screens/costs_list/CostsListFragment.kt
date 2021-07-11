package ru.butterbean.easyrent.screens.costs_list

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.databinding.FragmentExtFilesListBinding
import ru.butterbean.easyrent.models.CostData
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.screens.ext_files.ExtFilesListAdapter
import ru.butterbean.easyrent.screens.ext_files.ExtFilesListViewModel
import ru.butterbean.easyrent.utils.*

class CostsListFragment : Fragment() {
    private lateinit var mViewModel: CostsListViewModel
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mCurrentRoom: RoomData
    private var _binding: FragmentExtFilesListBinding? = null
    private val mBinding get() = _binding!!

    companion object {
        fun clickOnListItem(cost: CostData) {
            APP_ACTIVITY.navController.navigate(R.id.a, createArgsBundle("cost",cost))
        }

        fun longClickOnListItem(cost: CostData, f: CostsListFragment) {
            showDeleteDialog(cost, f)
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
        _binding = FragmentExtFilesListBinding.inflate(layoutInflater, container, false)
        mCurrentRoom = arguments?.getSerializable("room") as RoomData
        return mBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mRecyclerView.adapter = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        extOnActivityResult(resultCode, requestCode, data, this)
    }


    override fun onStart() {
        super.onStart()
        initialization()
    }

    private fun initialization() {
        APP_ACTIVITY.title = "Файлы"
        setHasOptionsMenu(true)

        //Recycler view
        val adapter = ExtFilesListAdapter(this)
        mRecyclerView = mBinding.extFilesRecyclerView
        mRecyclerView.adapter = adapter

        // ViewModel
        mViewModel = ViewModelProvider(APP_ACTIVITY).get(ExtFilesListViewModel::class.java)
        mViewModel.getExtFilesByReserveId(mCurrentReserve.id).observe(viewLifecycleOwner) {
            adapter.setData(it.asReversed())
        }

        mBinding.extFilesBtnAdd.setOnClickListener {
            showAttachFileDialog(this)
        }

    }
}