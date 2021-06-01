package ru.butterbean.easyrent.database

import ru.butterbean.easyrent.models.GuestData
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.models.RoomData

lateinit var CURRENT_ROOM:RoomData
lateinit var CURRENT_RESERVE: ReserveData
lateinit var CURRENT_GUEST: GuestData

const val DATABASE_NAME = "main_database"

const val TABLE_ROOMS_NAME = "rooms_table"
const val TABLE_RESERVES_NAME = "reserves_table"
const val TABLE_GUESTS_NAME = "guests_table"

const val STATUS_FREE = "Свободно"
const val STATUS_BUSY = "Занято"
const val STATUS_REPAIRS = "Ремонт"