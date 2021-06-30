package ru.butterbean.easyrent.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE $TABLE_RESERVES_ARCHIVE_NAME(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "roomId INTEGER NOT NULL, " +
                    "guestName TEXT NOT NULL, " +
                    "guestsCount INTEGER NOT NULL, " +
                    "sum INTEGER NOT NULL, " +
                    "payment INTEGER NOT NULL, " +
                    "dateCheckIn TEXT NOT NULL, " +
                    "dateCheckOut TEXT NOT NULL, " +
                    "wasCheckIn INTEGER NOT NULL, " +
                    "wasCheckOut INTEGER NOT NULL," +
                    "FOREIGN KEY(roomId) REFERENCES ${TABLE_ROOMS_NAME}(id) ON UPDATE NO ACTION ON DELETE CASCADE" +
                    ") "
        )
        database.execSQL("CREATE INDEX index_${TABLE_RESERVES_ARCHIVE_NAME}_roomId ON $TABLE_RESERVES_ARCHIVE_NAME (roomId)")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE $TABLE_RESERVES_NAME ADD COLUMN phoneNumber TEXT NOT NULL DEFAULT ''")
        database.execSQL("ALTER TABLE $TABLE_RESERVES_ARCHIVE_NAME ADD COLUMN phoneNumber TEXT NOT NULL DEFAULT ''")
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE ${TABLE_RESERVES_EXT_FILES_NAME}(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "reserveId INTEGER NOT NULL, " +
                    "dirName TEXT NOT NULL, " +
                    "fileName TEXT NOT NULL, " +
                    "fileType TEXT NOT NULL, " +
                    "isImage INTEGER NOT NULL, " +
                    "FOREIGN KEY(reserveId) REFERENCES ${TABLE_RESERVES_NAME}(id) ON UPDATE NO ACTION ON DELETE CASCADE" +
                    ") "
        )
        database.execSQL("CREATE INDEX index_${TABLE_RESERVES_EXT_FILES_NAME}_reserveId ON $TABLE_RESERVES_NAME (reserveId)")
    }
}
