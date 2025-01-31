package com.example.tabnewlayout;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;


@Entity(tableName = "Person")
public class Person {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "person_id")
    private int id;

    @ColumnInfo(name = "person_name")
    private String name;

    @ColumnInfo(name = "person_age")
    private String age;

    @ColumnInfo(name = "tab")
    private int tab;

    public Person(String name, String age, int tab) {
        this.name = name;
        this.age = age;
        this.tab = tab;
    }

    @Ignore
    public Person() {}

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public int getTab() {
        return tab;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setTab(int tab) {
        this.tab = tab;
    }
}
