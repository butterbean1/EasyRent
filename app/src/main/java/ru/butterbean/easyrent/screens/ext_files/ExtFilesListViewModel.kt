package ru.butterbean.easyrent.screens.ext_files

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.butterbean.easyrent.database.repository.ReserveExtFileRepository
import ru.butterbean.easyrent.utils.APP_DATABASE

class ExtFilesListViewModel(application: Application): AndroidViewModel(application) {
    private val mReserveExtFileRepository = ReserveExtFileRepository(APP_DATABASE.reserveExtFileDao())

    fun getExtFilesByReserveId(reserveId: Long) = mReserveExtFileRepository.getExtFilesByReserveId(reserveId)


}