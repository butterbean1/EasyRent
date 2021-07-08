package ru.butterbean.easyrent.screens.ext_files

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import ru.butterbean.easyrent.models.ReserveExtFileData

abstract class ExtFilesExtensionFragment: Fragment() {
    var photoURI: Uri = Uri.EMPTY

    open fun deleteReserveExtFile(extFile: ReserveExtFileData) {}

    open fun getSingleExtFileParams(f: (Bundle) -> Unit) {}

    open fun addExtFileToDatabase(dirName:String, fileName:String, extension:String) {}

}