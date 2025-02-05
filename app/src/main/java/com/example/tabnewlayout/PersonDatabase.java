package com.example.tabnewlayout;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Person.class}, version = 2, exportSchema = false)
public abstract class PersonDatabase extends RoomDatabase {
    private static volatile PersonDatabase instance;
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE person ADD COLUMN base64Image TEXT");
        }
    };

    public abstract PersonDao personDao();

    public static synchronized PersonDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            PersonDatabase.class, "person_database")
                    .addMigrations(MIGRATION_1_2) // Add migration here
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
