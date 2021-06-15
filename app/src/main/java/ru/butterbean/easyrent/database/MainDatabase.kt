package ru.butterbean.easyrent.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.butterbean.easyrent.database.dao.GuestDao
import ru.butterbean.easyrent.database.dao.ReserveArchiveDao
import ru.butterbean.easyrent.database.dao.ReserveDao
import ru.butterbean.easyrent.database.dao.RoomDao
import ru.butterbean.easyrent.models.GuestData
import ru.butterbean.easyrent.models.ReserveArchiveData
import ru.butterbean.easyrent.models.ReserveData
import ru.butterbean.easyrent.models.RoomData

/*
ver. 1 : beginning
ver. 2 : reserves_archive_table added
 */
@Database(
    entities = [RoomData::class, GuestData::class, ReserveData::class, ReserveArchiveData::class],
    version = 2,
    exportSchema = false
)
abstract class MainDatabase : RoomDatabase() {
    abstract fun roomDao(): RoomDao
    abstract fun guestDao(): GuestDao
    abstract fun reserveDao(): ReserveDao
    abstract fun ReserveArchiveDao(): ReserveArchiveDao

    companion object {
        @Volatile
        private var INSTANCE: MainDatabase? = null

        fun getDatabase(context: Context): MainDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDatabase::class.java,
                    DATABASE_NAME
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                return instance
            }

        }
    }
}