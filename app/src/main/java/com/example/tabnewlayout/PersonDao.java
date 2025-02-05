package com.example.tabnewlayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.OnConflictStrategy;

import java.util.List;

@Dao
public interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPerson(Person person);

    @Delete
    void deletePerson(Person person);

    @Update
    void updatePerson(Person person);


    @Query("SELECT * FROM Person WHERE tab = 1")
    LiveData<List<Person>> getTab1Items();

    @Query("SELECT * FROM Person WHERE tab = 2")
    LiveData<List<Person>> getTab2Items();

    @Query("UPDATE Person SET tab = :newTab WHERE person_id = :personId")
    void moveItem(int personId, int newTab);
}
