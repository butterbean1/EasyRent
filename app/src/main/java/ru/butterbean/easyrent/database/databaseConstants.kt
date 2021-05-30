package ru.butterbean.easyrent

import ru.butterbean.easyrent.models.RoomData

lateinit var CURRENT_ROOM:RoomData

const val DATABASE_NAME = "main_database"

const val TABLE_ROOMS_NAME = "rooms_table"
const val TABLE_RESERVES_NAME = "reserves_table"
const val TABLE_GUESTS_NAME = "guests_table"

const val STATUS_FREE = "Свободно"
const val STATUS_BUSY = "Занято"
const val STATUS_REPAIRS = "Ремонт"