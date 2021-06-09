package ru.butterbean.easyrent.utils

import ru.butterbean.easyrent.MainActivity
import ru.butterbean.easyrent.database.MainDatabase

lateinit var APP_ACTIVITY:MainActivity
lateinit var APP_DATABASE:MainDatabase

var ROOMS_COUNT:Int = 0

lateinit var STATUS_FREE : String
lateinit var STATUS_FREE_FROM : String
lateinit var STATUS_FREE_TO : String
lateinit var STATUS_FREE_ON : String
lateinit var STATUS_BUSY : String
lateinit var STATUS_REPAIRS : String
lateinit var STATUS_UNTIL : String