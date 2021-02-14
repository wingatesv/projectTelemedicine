package com.project.mytelevet.customer.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.mytelevet.customer.livedata.ViewMyProfileLiveData;

public class ViewMyProfileRepository {


    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();



    public ViewMyProfileLiveData getFirestoreLiveData(String userID)
    {
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);

        return new ViewMyProfileLiveData(documentReference);
    }

}
