package com.example.tabnewlayout;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Person.class}, version = 1, exportSchema = false)
public abstract class PersonDatabase extends RoomDatabase {
    private static volatile PersonDatabase instance;

    public abstract PersonDao personDao();

    public static synchronized PersonDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            PersonDatabase.class, "person_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
