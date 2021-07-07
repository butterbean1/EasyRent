package ru.butterbean.easyrent.screens.ext_files

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.butterbean.easyrent.database.repository.ReserveExtFileRepository
import ru.butterbean.easyrent.models.ReserveExtFileData

class ExtFilesListViewModel(application: Application): AndroidViewModel(application) {
    private val mRepository = ReserveExtFileRepository()

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

}