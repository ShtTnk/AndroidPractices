package com.example.counterapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<Integer> count = new MutableLiveData<>(0);

    public LiveData<Integer> getCount() {
        return count;
    }

    public void increment() {
        count.setValue(count.getValue() + 1);
    }
}
