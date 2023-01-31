package com.example.qrscanlocalisation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PageViewModel extends ViewModel {
    /**
     * Live Data Instance
     */
    private MutableLiveData<String> mCoordinateUrl = new MutableLiveData<>();
    private MutableLiveData<String> mCoordinateLat = new MutableLiveData<>();

    public void setCoordinate(String name) {
        mCoordinateUrl.setValue(name);
    }
    public LiveData<String> getCoordinate() {
        return mCoordinateUrl;
    }

    public void setCoordinateLat(String name) {
        mCoordinateLat.setValue(name);
    }

    public LiveData<String> getCoordinateLat() {
        return mCoordinateLat;
    }
}
