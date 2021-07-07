package ru.butterbean.easyrent.screens.ext_files

import android.os.Bundle
import ru.butterbean.easyrent.models.ReserveExtFileData

interface ExtFilesExtension {
    fun deleteReserveExtFile(extFile: ReserveExtFileData)

    fun getSingleExtFileParams(f: (Bundle) -> Unit)

}