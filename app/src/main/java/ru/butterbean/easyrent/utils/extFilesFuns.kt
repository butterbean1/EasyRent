package ru.butterbean.easyrent.utils

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.webkit.MimeTypeMap
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.core.net.toUri
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

