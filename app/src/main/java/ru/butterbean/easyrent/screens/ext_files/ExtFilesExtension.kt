package ru.butterbean.easyrent.screens.ext_files

import ru.butterbean.easyrent.models.ReserveExtFileData

interface ExtFilesExtension {
    fun deleteReserveExtFile(extFile: ReserveExtFileData)
}