package ru.butterbean.easyrent

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.butterbean.easyrent.models.RoomData

@Database(entities=[RoomData::class],version=1,exportSchema = false)
abstract class MainDatabase: RoomDatabase(){
    abstract fun roomDao():RoomDao

    companion object{
        @Volatile
        private var INSTANCE:MainDatabase? = null

        fun getDatabase(context: Context):MainDatabase{
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