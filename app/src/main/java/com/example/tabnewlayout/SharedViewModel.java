package com.example.tabnewlayout;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    public MutableLiveData<List<String>> tab1Items = new MutableLiveData<>(new ArrayList<>());
    public MutableLiveData<List<String>> tab2Items = new MutableLiveData<>(new ArrayList<>());

    public void moveItemToTab2(String item) {
        List<String> currentTab1Items = tab1Items.getValue();
        List<String> currentTab2Items = tab2Items.getValue();

        if (currentTab1Items != null && currentTab2Items != null) {
            currentTab1Items.remove(item);
            currentTab2Items.add(item);

            tab1Items.setValue(currentTab1Items);
            tab2Items.setValue(currentTab2Items);
        }
    }
}
