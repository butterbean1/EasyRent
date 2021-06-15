package ru.butterbean.easyrent.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE $TABLE_RESERVES_ARCHIVE_NAME(" +
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
                "FOREIGN KEY(roomId) REFERENCES rooms_table(id) ON UPDATE NO ACTION ON DELETE CASCADE" +
                ");" +
                "CREATE INDEX index_${TABLE_RESERVES_ARCHIVE_NAME}_roomId ON $TABLE_RESERVES_ARCHIVE_NAME (roomId)")
    }
}
