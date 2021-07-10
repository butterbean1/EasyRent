package ru.butterbean.easyrent.screens.statistic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.butterbean.easyrent.database.repository.ReserveRepository
import ru.butterbean.easyrent.models.StatisticQueryResult
import ru.butterbean.easyrent.utils.APP_DATABASE

class StatisticViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepository = ReserveRepository(APP_DATABASE.reserveDao())

    fun getStatistic(roomId: Long,startDate:String,endDate:String, onSuccess: (StatisticQueryResult) -> Unit) {

        viewModelScope.launch(Dispatchers.IO) {
            val res = mRepository.getStatistic(roomId,startDate,endDate)
            withContext(Dispatchers.Main) {
                onSuccess(res)
            }
        }
    }

}