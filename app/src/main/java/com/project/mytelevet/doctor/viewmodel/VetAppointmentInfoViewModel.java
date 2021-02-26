package com.project.mytelevet.doctor.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.mytelevet.customer.livedata.AppointmentInfoLiveData;
import com.project.mytelevet.customer.repository.AppointmentInfoRepository;
import com.project.mytelevet.doctor.livedata.VetAppointmentInfoLiveData;
import com.project.mytelevet.doctor.repository.VetAppointmentInfoRepository;

import java.util.Map;

public class VetAppointmentInfoViewModel extends ViewModel {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    private VetAppointmentInfoRepository repository = new VetAppointmentInfoRepository();
    VetAppointmentInfoLiveData liveData = null;


    public LiveData<Map<String, Object>> getAppointmentInfoLiveData(String customerName, String time, String userID) {
        liveData = repository.getFirestoreLiveData(customerName, time, userID);

        return liveData;
    }

    public LiveData<Map<String, Object>> getAppointmentItem() {
        return liveData.item;
    }

    public void confirmApp()
    {
        //DocumentReference documentReference = firebaseFirestore.collection("appointments")
    }

}
