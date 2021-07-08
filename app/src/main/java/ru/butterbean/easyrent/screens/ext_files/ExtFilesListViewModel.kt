package ru.butterbean.easyrent.screens.ext_files

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.butterbean.easyrent.database.repository.ReserveExtFileRepository
import ru.butterbean.easyrent.database.repository.ReserveRepository
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.models.ReserveExtFileData
import ru.butterbean.easyrent.utils.APP_DATABASE

class ExtFilesListViewModel(application: Application): AndroidViewModel(application) {
    private val mRepository = ReserveExtFileRepository()
    private val mRepositoryReserve = ReserveRepository(APP_DATABASE.reserveDao())

    fun getExtFilesByReserveId(reserveId: Long) = mRepository.getExtFilesByReserveId(reserveId)

    fun deleteExtFile(extFile: ReserveExtFileData, onSuccess: (Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.deleteReserveExtFile(extFile)
            val extFilesCount = mRepository.getExtFilesCount(extFile.reserveId)
            withContext(Dispatchers.Main) {
                onSuccess(extFilesCount)
            }
        }
    }

    fun addReserveExtFile(extFile: ReserveExtFileData, onSuccess: (Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.addReserveExtFile(extFile)
            val extFilesCount = mRepository.getExtFilesCount(extFile.reserveId)
            withContext(Dispatchers.Main) {
                onSuccess(extFilesCount)
            }
        }
    }


    fun updateReserve(reserve: ReserveData) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepositoryReserve.updateReserve(reserve)
        }
    }


}