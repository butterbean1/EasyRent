package ru.butterbean.easyrent.screens.ext_files

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.butterbean.easyrent.database.repository.ReserveExtFileRepository
import ru.butterbean.easyrent.models.ReserveExtFileData
import ru.butterbean.easyrent.utils.APP_DATABASE

class ExtFilesListViewModel(application: Application): AndroidViewModel(application) {
    private val mRepository = ReserveExtFileRepository(APP_DATABASE.reserveExtFileDao())

    fun getExtFilesByReserveId(reserveId: Long) = mRepository.getExtFilesByReserveId(reserveId)

    fun deleteExtFile(extFile: ReserveExtFileData, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.deleteReserveExtFile(extFile)
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        }
    }

}