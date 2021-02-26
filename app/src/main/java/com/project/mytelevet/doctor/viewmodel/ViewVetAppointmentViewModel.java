package com.project.mytelevet.doctor.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.mytelevet.customer.livedata.ViewAppointmentLiveData;
import com.project.mytelevet.customer.model.ViewAppointmentItem;
import com.project.mytelevet.customer.repository.ViewAppointmentRepository;
import com.project.mytelevet.doctor.livedata.ViewVetAppointmentLiveData;
import com.project.mytelevet.doctor.model.ViewVetAppointmentItem;
import com.project.mytelevet.doctor.repository.ViewVetAppointmentRepository;

import java.util.List;

public class ViewVetAppointmentViewModel extends ViewModel {
    private ViewVetAppointmentRepository repository = new ViewVetAppointmentRepository();

    ViewVetAppointmentLiveData liveData = null;

    public LiveData<List<ViewVetAppointmentItem>> getAppointmentLiveData(String userID) {
        liveData = repository.getFirestoreLiveData(userID);
        return liveData;
    }

    public LiveData<List<ViewVetAppointmentItem>> getItem() {
        return liveData.item;
    }
}
