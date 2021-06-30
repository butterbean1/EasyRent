package ru.butterbean.easyrent.utils

import ru.butterbean.easyrent.MainActivity
import ru.butterbean.easyrent.database.MainDatabase

lateinit var APP_ACTIVITY:MainActivity
lateinit var APP_DATABASE:MainDatabase

var ONLY_ONE_ROOM:Boolean = false

lateinit var STATUS_FREE : String
lateinit var STATUS_FREE_FROM : String
lateinit var STATUS_FREE_TO : String
lateinit var STATUS_FREE_ON : String
lateinit var STATUS_BUSY : String
lateinit var STATUS_REPAIRS : String
lateinit var STATUS_UNTIL : String

var PREF_RESERVE_COMPLETE_WAS_PAID : Boolean = false
var PREF_RESERVE_COMPLETE_WAS_CHECK_OUT: Boolean = false
var AUTO_CHECK_IN_CHECK_OUT: Boolean = false


const val DAYS_TO_REPLACE_TO_ARCHIVE = 30
const val FILE_REQUEST_CODE = 301
const val IMAGE_REQUEST_CODE = 302
const val PHOTO_REQUEST_CODE = 303
const val MAX_FILE_SIZE_MEGABYTES = 50 // Мегабайты - менять только здесь!
const val MAX_FILE_SIZE_BYTES = MAX_FILE_SIZE_MEGABYTES * 1024 * 1024 // байты, не трогать!