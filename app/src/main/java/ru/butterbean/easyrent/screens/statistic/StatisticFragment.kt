package ru.butterbean.easyrent.screens.statistic

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.databinding.FragmentStatisticBinding
import ru.butterbean.easyrent.models.RoomData
import ru.butterbean.easyrent.models.StatisticQueryResult
import ru.butterbean.easyrent.screens.room.RoomViewModel
import ru.butterbean.easyrent.utils.*
import java.util.*

class StatisticFragment : Fragment() {
    private var _binding: FragmentStatisticBinding? = null
    private val mBinding get() = _binding!!
    lateinit var mViewModel: StatisticViewModel
    private var mStartDate = getStartOfDay(Calendar.getInstance())
    private var mEndDate = getStartOfDay(Calendar.getInstance())
    private lateinit var mCurrentRoom: RoomData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticBinding.inflate(layoutInflater, container, false)
        mCurrentRoom = arguments?.getSerializable("room") as RoomData
        return mBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showStatisticPeriodDialog() {
        var actions = emptyArray<String>()
        actions += APP_ACTIVITY.getString(R.string.for_all_time)// 0
        actions += APP_ACTIVITY.getString(R.string.chosen_period)// 1
        actions += APP_ACTIVITY.getString(R.string.from_year_beginning)// 2
        actions += APP_ACTIVITY.getString(R.string.from_month_beginning)// 3
        actions += APP_ACTIVITY.getString(R.string.from_prev_month_beginning)// 4
        val builder = AlertDialog.Builder(APP_ACTIVITY)
        builder.setItems(actions) { _, i ->
            when (i) {
                0 -> clearDates()
                1 -> showPeriodSelect()
                2 -> setPeriodYearBeginning()
                3 -> setPeriodMonthBeginning()
                4 -> setPeriodPrevMonthBeginning()
            }
        }
            .show()
    }

    private fun setPeriodPrevMonthBeginning() {
        mStartDate = getStartOfDay(Calendar.getInstance())
        mStartDate.add(Calendar.MONTH, -1)
        mStartDate.set(Calendar.DAY_OF_MONTH, 1)
        mEndDate = getFarAwayEndDate()
        mBinding.statisticPeriod.text = getString(R.string.from_prev_month_beginning)
        refreshData()
    }

    private fun setPeriodMonthBeginning() {
        mStartDate = getStartOfDay(Calendar.getInstance())
        mStartDate.set(Calendar.DAY_OF_MONTH, 1)
        mEndDate = getFarAwayEndDate()
        mBinding.statisticPeriod.text = getString(R.string.from_month_beginning)
        refreshData()
    }

    private fun setPeriodYearBeginning() {
        mStartDate = getStartOfDay(Calendar.getInstance())
        mStartDate.set(Calendar.MONTH, 0)
        mStartDate.set(Calendar.DAY_OF_MONTH, 1)
        mEndDate = getFarAwayEndDate()
        mBinding.statisticPeriod.text = getString(R.string.from_year_beginning)
        refreshData()
    }

    private fun showPeriodSelect() {
        val tempStartDate =
            if (mStartDate == getFarAwayStartDate()) getStartOfDay(Calendar.getInstance())
            else mStartDate
        var tempEndDate = getStartOfDay(Calendar.getInstance())

        val endDateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                tempEndDate.set(year, month, dayOfMonth)
                if (tempStartDate.after(tempEndDate)) showToast("Некорректный период")
                else {
                    mStartDate = tempStartDate
                    mEndDate = tempEndDate
                    mBinding.statisticPeriod.text = getPeriodPresentation(mStartDate, mEndDate)
                    refreshData()
                }
            }
        val startDateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                tempStartDate.set(year, month, dayOfMonth)

                if (mEndDate == getFarAwayEndDate() || mEndDate.before(tempStartDate)) {
                    tempEndDate.set(year, month, dayOfMonth)
                    tempEndDate.add(Calendar.DAY_OF_MONTH, 1)
                } else tempEndDate = mEndDate

                val dialog = DatePickerDialog(
                    requireContext(),
                    android.R.style.Theme_Material_Light_Dialog_MinWidth,
                    endDateSetListener,
                    tempEndDate.get(Calendar.YEAR),
                    tempEndDate.get(Calendar.MONTH),
                    tempEndDate.get(Calendar.DAY_OF_MONTH)
                )
                dialog.setTitle("Окончание периода")
                dialog.show()
            }
        val dialog = DatePickerDialog(
            requireContext(),
            android.R.style.Theme_Material_Light_Dialog_MinWidth,
            startDateSetListener,
            tempStartDate.get(Calendar.YEAR),
            tempStartDate.get(Calendar.MONTH),
            tempStartDate.get(Calendar.DAY_OF_MONTH)
        )
        dialog.setTitle("Начало периода")
        dialog.show()
    }

    private fun clearDates() {
        mStartDate = getFarAwayStartDate()
        mEndDate = getFarAwayEndDate()
        mBinding.statisticPeriod.text = getString(R.string.for_all_time)
        refreshData()
    }

    private fun refreshData() {
        mViewModel.getStatistic(
            mCurrentRoom.id,
            mStartDate.toDateTimeInDatabaseFormat(),
            getEndOfDay(mEndDate).toDateTimeInDatabaseFormat()
        ) {stat->
            mBinding.statisticSumText.text = (stat.sum?:0).toString()
            mBinding.statisticPaymentText.text = (stat.payment?:0).toString()
            val daysCount = (stat.daysCount?:0)
            val reservesCount = (stat.reservesCount?:0)
            mBinding.statisticDaysText.text = if (daysCount == 0 && reservesCount>0) "1" else daysCount.toString()
            mBinding.statisticReservesText.text = reservesCount.toString()
            mBinding.statisticGuestsText.text = (stat.guestsCount?:0).toString()
            mBinding.statisticCostsText.text = (stat.costs?:0).toString()
        }
    }

    private fun getFarAwayEndDate(): Calendar {
        val cal = getStartOfDay(Calendar.getInstance())
        cal.set(2999, 0, 1)
        return cal
    }

    private fun getFarAwayStartDate(): Calendar {
        val cal = getStartOfDay(Calendar.getInstance())
        cal.set(1800, 0, 1)
        return cal
    }

    override fun onStart() {
        super.onStart()
        initialize()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> APP_ACTIVITY.navController.popBackStack()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initialize() {

        setHasOptionsMenu(true)
        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mStartDate = getFarAwayStartDate()
        mEndDate = getFarAwayEndDate()

        // ViewModel
        mViewModel = ViewModelProvider(this).get(StatisticViewModel::class.java)

        refreshData()

        mBinding.statisticPeriod.setOnClickListener {
            showStatisticPeriodDialog()
        }

    }
}