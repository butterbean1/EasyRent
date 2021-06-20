package ru.butterbean.easyrent.models

abstract class CommonReserveData(
    open val id: Long,
    open val roomId: Long,
    open val guestName: String = "",
    open val guestsCount: Int = 0,
    open val sum: Int = 0,
    open val payment: Int = 0,
    open val dateCheckIn: String = "",
    open val dateCheckOut: String = "",
    open val wasCheckIn: Boolean = false,
    open val wasCheckOut: Boolean = false
)