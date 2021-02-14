package com.project.mytelevet.customer.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.mytelevet.customer.livedata.AppointmentInfoLiveData;
import com.project.mytelevet.customer.repository.AppointmentInfoRepository;

import java.util.Map;

public class AppointmentInfoViewModel extends ViewModel {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    private AppointmentInfoRepository repository = new AppointmentInfoRepository();
    AppointmentInfoLiveData liveData = null;


    public LiveData<Map<String, Object>> getAppointmentInfoLiveData(String vetName, String time, String userID) {
        liveData = repository.getFirestoreLiveData(vetName, time, userID);

        return liveData;
    }

    public LiveData<Map<String, Object>> getAppointmentItem() {
        return liveData.item;
    }

}
