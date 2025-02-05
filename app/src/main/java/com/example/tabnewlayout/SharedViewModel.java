package com.example.tabnewlayout;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class SharedViewModel extends AndroidViewModel {
    private final PersonRepository repository;
    private final LiveData<List<Person>> tab1Items;
    private final LiveData<List<Person>> tab2Items;

    public SharedViewModel(@NonNull Application application) {
        super(application);
        repository = new PersonRepository(application);
        tab1Items = repository.getTab1Items();  // Ensure Room initializes before access
        tab2Items = repository.getTab2Items();
    }

    public LiveData<List<Person>> getTab1Items() {
        return tab1Items;
    }

    public LiveData<List<Person>> getTab2Items() {
        return tab2Items;
    }

    public void addPersonToTab1(Person person) {
        repository.insertPerson(person);
    }

    // This method moves a person from Tab 1 to Tab 2
    public void moveItemToTab2(Person person) {
        moveItem(person.getId(), 2); // Moves the person to Tab 2 (targetTab = 2)
    }

    public void moveItem(int personId, int targetTab) {
        repository.moveItem(personId, targetTab); // Dynamic target tab
    }

    public void deletePerson(Person person) {
        repository.deletePerson(person);
    }
}
