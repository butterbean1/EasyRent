package ru.butterbean.easyrent.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.butterbean.easyrent.database.dao.GuestDao
import ru.butterbean.easyrent.database.dao.ReserveDao
import ru.butterbean.easyrent.database.dao.RoomDao
import ru.butterbean.easyrent.database.models.GuestData
import ru.butterbean.easyrent.database.models.ReserveData
import ru.butterbean.easyrent.database.models.RoomData

@Database(entities=[RoomData::class,GuestData::class,ReserveData::class],version=1,exportSchema = false)
abstract class MainDatabase: RoomDatabase(){
    abstract fun roomDao(): RoomDao
    abstract fun guestDao(): GuestDao
    abstract fun reserveDao(): ReserveDao

    companion object{
        @Volatile
        private var INSTANCE: MainDatabase? = null

        fun getDatabase(context: Context): MainDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }

        }
    }
}