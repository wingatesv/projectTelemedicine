package com.project.mytelevet.customer.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.mytelevet.customer.livedata.ViewAppointmentLiveData;
import com.project.mytelevet.customer.model.ViewAppointmentItem;
import com.project.mytelevet.customer.repository.ViewAppointmentRepository;

import java.util.List;

public class ViewAppointmentViewModel extends ViewModel {
    private ViewAppointmentRepository repository = new ViewAppointmentRepository();
    public MutableLiveData<List<ViewAppointmentItem>> shoppingList = new MutableLiveData<List<ViewAppointmentItem>>();
    ViewAppointmentLiveData liveData = null;

    public LiveData<List<ViewAppointmentItem>> getAppointmentLiveData(String userID) {
        liveData = repository.getFirestoreLiveData(userID);
        return liveData;
    }

    public LiveData<List<ViewAppointmentItem>> getItem() {
        return liveData.item;
    }
}