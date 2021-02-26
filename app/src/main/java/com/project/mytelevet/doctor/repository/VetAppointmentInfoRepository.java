package com.project.mytelevet.doctor.repository;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import com.project.mytelevet.doctor.livedata.VetAppointmentInfoLiveData;

public class VetAppointmentInfoRepository {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();



    public VetAppointmentInfoLiveData getFirestoreLiveData(String customerName, String time, String userID)
    {

        CollectionReference collectionReference = firebaseFirestore.collection("appointments");
        Query query = collectionReference.whereEqualTo("VetID", userID).whereEqualTo("CustomerName", customerName).whereEqualTo("AppointmentTime", time);

        return new VetAppointmentInfoLiveData(query);


    }


}
