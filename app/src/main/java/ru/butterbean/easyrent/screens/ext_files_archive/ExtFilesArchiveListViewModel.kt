package ru.butterbean.easyrent.screens.ext_files_archive

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.butterbean.easyrent.database.repository.ReserveArchiveExtFileRepository
import ru.butterbean.easyrent.utils.APP_DATABASE

class ExtFilesArchiveListViewModel(application: Application): AndroidViewModel(application) {
    private val mRepository = ReserveArchiveExtFileRepository(APP_DATABASE.reserveArchiveExtFilesDao())

    fun getExtFilesByReserveId(reserveId: Long) = mRepository.getExtFilesByReserveId(reserveId)

 }