package com.example.tabnewlayout;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PersonRepository {
    private final PersonDao personDao;
    private final ExecutorService executorService;

    public PersonRepository(Application application) {
        PersonDatabase db = PersonDatabase.getInstance(application);
        personDao = db.personDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insertPerson(Person person) {
        executorService.execute(() -> personDao.insertPerson(person));
    }

    public void deletePerson(Person person) {
        executorService.execute(() -> personDao.deletePerson(person));
    }

    public void moveItem(int personId, int newTab) {
        executorService.execute(() -> personDao.moveItem(personId, newTab));
    }

    public LiveData<List<Person>> getTab1Items() {
        return personDao.getTab1Items();
    }

    public LiveData<List<Person>> getTab2Items() {
        return personDao.getTab2Items();
    }
}
