package com.project.mytelevet.customer.repository;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.project.mytelevet.customer.livedata.AppointmentInfoLiveData;

public class AppointmentInfoRepository {


    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();



    public AppointmentInfoLiveData getFirestoreLiveData(String vetName, String time, String userID)
    {

         CollectionReference collectionReference = firebaseFirestore.collection("appointments");
         Query query = collectionReference.whereEqualTo("CustomerID", userID).whereEqualTo("VetName", vetName).whereEqualTo("AppointmentTime", time);

            return new AppointmentInfoLiveData(query);


    }



}
