package com.project.mytelevet.customer.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.project.mytelevet.customer.livedata.VetInfoLiveData;

public class VetInfoRepository {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();




    public VetInfoLiveData getFirestoreLiveData(String vetName)
    {

        String userID = firebaseAuth.getCurrentUser().getUid();
        CollectionReference collectionReference = firebaseFirestore.collection("doctors");

        Query query = collectionReference.whereEqualTo("FullName", vetName);

        return new VetInfoLiveData(query);
    }

}
