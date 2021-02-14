package com.project.mytelevet.customer.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.mytelevet.customer.livedata.MyPetsFragmentLiveData;

// Repository is used to get the database path and pass to Live Data

public class MyPetsRepository {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    public MyPetsFragmentLiveData getFirestoreLiveData()
    {
       String userID = firebaseAuth.getCurrentUser().getUid();
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID).collection("pets").document("petList_" + userID);

        return new MyPetsFragmentLiveData(documentReference);
    }
}
