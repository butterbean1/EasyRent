package ru.butterbean.easyrent.utils

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.webkit.MimeTypeMap
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.models.ReserveExtFileData
import ru.butterbean.easyrent.screens.ext_files.ExtFilesExtension
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

fun startAnyApp(params: Bundle) {
    try {
        val filePath = params.getString("filePath")!!
        val fileName = params.getString("fileName")
        val fileType = params.getString("fileType")
        val storageDir = APP_ACTIVITY.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val tempFile = File(storageDir, fileName)
        FileInputStream(File(filePath)).use { fis ->
            FileOutputStream(tempFile).use { fos ->
                // Transfer bytes from in to out
                val buf = ByteArray(1024)
                var len: Int
                while (fis.read(buf).also { len = it } > 0) {
                    fos.write(buf, 0, len)
                }
            }
        }
        val uriApp = FileProvider.getUriForFile(
            APP_ACTIVITY,
            "ru.butterbean.easyrent.fileprovider",
            tempFile
        )

        val intent = Intent(Intent.ACTION_VIEW)
        val appType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileType)
        intent.setDataAndType(uriApp, appType)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        APP_ACTIVITY.startActivity(intent)

    } catch (e: Exception) {
        showToast("Не удалось открыть файл: " + e.message.toString())
    }
}

fun ImageView.setExtFileImage(params: Bundle) {
    if (params.getBoolean("isImage")) this.setImageURI(
        params.getString("uriString")?.toUri()!!
    )
    else {
        val fileType = params.getString("fileType")!!
        val resId = getFileIconId(fileType)
        this.setImageResource(resId)
    }
}

fun deleteLocalFile(dirName: String) {
    val file = File(APP_ACTIVITY.filesDir.path + "/" + dirName)
    file.deleteRecursively()
}

fun showDeleteExtFileDialog(extFile: ReserveExtFileData,f: ExtFilesExtension) {
    val actions = arrayOf(
        APP_ACTIVITY.getString(R.string.delete) // 0
    )
    val builder = AlertDialog.Builder(APP_ACTIVITY)
    builder.setItems(actions) { _, i ->
        when (i) {
            0 -> deleteExtFile(extFile,f)
        }
    }
        .show()
}

private fun deleteExtFile(extFile: ReserveExtFileData,f: ExtFilesExtension) {
    deleteLocalFile(extFile.dirName)
    f.deleteReserveExtFile(extFile)
}

fun trimFileName(fileName: String): String {
    val maxLength = 60
    return if (fileName.length > maxLength) fileName.substring(0,maxLength-5) + "..." + fileName.substringAfterLast(".")
    else fileName
}

