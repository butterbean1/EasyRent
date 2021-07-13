package ru.butterbean.easyrent.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.butterbean.easyrent.database.dao.*
import ru.butterbean.easyrent.models.*

/*
ver. 1 : beginning
ver. 2 : reserves_archive_table added
ver. 3 : field phone_number added to reserves_table & reserves_archive_table
ver. 4 : reserves_ext_files_table & reserves_archive_ext_files_table added
ver. 5 : costs_table & cost_items_table added
 */
@Database(
    entities = [RoomData::class,
        GuestData::class,
        ReserveData::class,
        ReserveArchiveData::class,
        CostData::class,
        CostItemData::class,
        ReserveExtFileData::class,
        ReserveArchiveExtFileData::class
    ],
    version = 5,
    exportSchema = false
)
abstract class MainDatabase : RoomDatabase() {
    abstract fun roomDao(): RoomDao
    abstract fun guestDao(): GuestDao
    abstract fun costDao(): CostDao
    abstract fun costItemDao(): CostItemDao
    abstract fun reserveDao(): ReserveDao
    abstract fun reserveArchiveDao(): ReserveArchiveDao
    abstract fun reserveExtFileDao(): ReserveExtFilesDao
    abstract fun reserveArchiveExtFilesDao(): ReserveArchiveExtFilesDao

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
                    .addMigrations(
                        MIGRATION_1_2,
                        MIGRATION_2_3,
                        MIGRATION_3_4,
                        MIGRATION_4_5,
                    )
                    .build()
                INSTANCE = instance
                return instance
            }

        }
    }
}