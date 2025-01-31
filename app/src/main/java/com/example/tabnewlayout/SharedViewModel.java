package com.example.tabnewlayout;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class SharedViewModel extends AndroidViewModel {
    private final PersonRepository repository;
    public LiveData<List<Person>> tab1Items;
    public LiveData<List<Person>> tab2Items;

    public SharedViewModel(@NonNull Application application) {
        super(application);
        repository = new PersonRepository(application);
        tab1Items = repository.getTab1Items();
        tab2Items = repository.getTab2Items();
    }

    public void addPersonToTab1(Person person) {
        repository.insertPerson(person);
    }

    public void moveItemToTab2(Person person) {
        repository.moveItem(person.getId(), 2);
    }

    public void deletePerson(Person person) {
        repository.deletePerson(person);
    }
}
