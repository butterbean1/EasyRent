package ru.butterbean.easyrent.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import ru.butterbean.easyrent.R
import ru.butterbean.easyrent.models.ReserveExtFileData
import ru.butterbean.easyrent.screens.ext_files.ExtFilesExtensionFragment
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

fun startAnyApp(params: Bundle) {
    try {
        val filePath = params.getString("filePath")!!
        val fileName = params.getString("fileName")!!
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

fun deleteLocalFiles(list:List<String>){
    list.forEach {
        deleteLocalFile(it)
    }
}

fun showDeleteExtFileDialog(extFile: ReserveExtFileData,f: ExtFilesExtensionFragment) {
    val actions = arrayOf(
        APP_ACTIVITY.getString(R.string.delete_file) // 0
    )
    val builder = AlertDialog.Builder(APP_ACTIVITY)
    builder.setItems(actions) { _, i ->
        when (i) {
            0 -> deleteExtFile(extFile,f)
        }
    }
        .show()
}

private fun deleteExtFile(extFile: ReserveExtFileData,f: ExtFilesExtensionFragment) {
    deleteLocalFile(extFile.dirName)
    f.deleteReserveExtFile(extFile)
}

fun trimFileName(fileName: String): String {
    val maxLength = 60
    return if (fileName.length > maxLength) fileName.substring(0,maxLength-5) + "..." + fileName.substringAfterLast(".")
    else fileName
}

fun ImageButton.setSingleExtFileImage(f:ExtFilesExtensionFragment) {
    f.getSingleExtFileParams { params ->
        setExtFileImage(params)
    }
}

fun getSingleExtFileParamsBundle(
    dirName: String,
    fileName: String,
    fileType: String,
    isImage: Boolean
): Bundle {
    val res = Bundle()
    val file = File(
        APP_ACTIVITY.filesDir.path + "/" + dirName,
        fileName
    )
    res.putString("uriString", Uri.fromFile(file).toString())
    res.putString("filePath", file.absolutePath)
    res.putString("fileName", fileName)
    res.putString("fileType", fileType)
    res.putBoolean("isImage", isImage)
    return res
}

fun getFilenameFromUri(uri: Uri): Bundle {
    val result = Bundle()
    val cursor = APP_ACTIVITY.contentResolver.query(uri, null, null, null, null, null)
    try {
        if (cursor != null && cursor.moveToFirst()) {
            result.putString(
                "fileName",
                cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            )
            result.putLong(
                "fileSize",
                cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE))
            )
        }
    } catch (e: Exception) {
        showToast(e.message.toString())
    } finally {
        cursor?.close()
        return result
    }
}

@Throws(IOException::class)
fun createImageFile(): File {
    // Create an image file name
    val storageDir = APP_ACTIVITY.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        "IMG_${getCurrentTimeStamp()}_", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    )
}

fun getCurrentTimeStamp(): String {
    return SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
}


fun showAttachFileDialog(f: ExtFilesExtensionFragment) {
    var actions = emptyArray<String>()
    actions += APP_ACTIVITY.getString(R.string.camera_capture_photo)// 0
    actions += APP_ACTIVITY.getString(R.string.gallery_image)// 1
    actions += APP_ACTIVITY.getString(R.string.local_file)// 2
    val builder = AlertDialog.Builder(APP_ACTIVITY)
    builder.setItems(actions) { _, i ->
        when (i) {
            0 -> attachPhoto(f)
            1 -> attachImage(f)
            2 -> attachFile(f)
        }
    }
        .show()

}

private fun attachFile(f: ExtFilesExtensionFragment) {

    val intent = Intent(Intent.ACTION_GET_CONTENT)
    intent.type = "*/*"
    f.startActivityForResult(intent, FILE_REQUEST_CODE)

}

private fun attachPhoto(f: ExtFilesExtensionFragment) {

    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
        // Ensure that there's a camera activity to handle the intent
        takePictureIntent.resolveActivity(APP_ACTIVITY.packageManager)?.also {
            // Create the File where the photo should go
            val photoFile: File? = try {
                createImageFile()
            } catch (e: IOException) {
                // Error occurred while creating the File
                showToast(e.message.toString())
                null
            }
            // Continue only if the File was successfully created
            photoFile?.also {
                f.photoURI = FileProvider.getUriForFile(
                    APP_ACTIVITY,
                    "ru.butterbean.easyrent.fileprovider",
                    it
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, f.photoURI)
                f.startActivityForResult(takePictureIntent, PHOTO_REQUEST_CODE)
            }
        }
    }
}

private fun attachImage(f: ExtFilesExtensionFragment) {

    val intent = Intent(Intent.ACTION_GET_CONTENT)
    intent.type = "image/* video/*"
    f.startActivityForResult(intent, FILE_REQUEST_CODE)

}

fun extOnActivityResult(
    resultCode: Int,
    requestCode: Int,
    data: Intent?,
    f: ExtFilesExtensionFragment
) {
    if (resultCode == Activity.RESULT_OK) {

        var uri = Uri.EMPTY
        var successfulReq = false
        when (requestCode) {
            PHOTO_REQUEST_CODE -> {
                uri = f.photoURI
                successfulReq = true
            }
            FILE_REQUEST_CODE -> {
                if (data != null) {
                    uri = data.data!!
                    successfulReq = true
                }
            }
            else -> showToast("Неизвестный тип файла!")
        }
        if (successfulReq) {
            try {
                val fileAttr = getFilenameFromUri(uri)
                val fileSize = fileAttr.getLong("fileSize")
                if (fileSize > MAX_FILE_SIZE_BYTES) {
                    showToast("Слишком большой размер файла! Максимальный размер - $MAX_FILE_SIZE_MEGABYTES Мб.")
                } else {
                    val newDirName = getCurrentTimeStamp()
                    val newDir = File(APP_ACTIVITY.filesDir, newDirName)
                    val newFileName = fileAttr.getString("fileName")!!
                    val newFile = File(newDir, newFileName)
                    if (newDir.mkdir()) {
                        val fos = FileOutputStream(newFile)
                        val ins = APP_ACTIVITY.contentResolver.openInputStream(uri)
                        fos.write(ins?.readBytes())
                        fos.close()
                        f.addExtFileToDatabase(newDirName, newFileName, newFile.extension)
                    }
                }
            } catch (e: Exception) {
                showToast(e.message.toString())
            }
        }

    }
}


