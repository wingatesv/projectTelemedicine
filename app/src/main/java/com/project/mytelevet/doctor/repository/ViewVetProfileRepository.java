package com.project.mytelevet.doctor.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.mytelevet.doctor.livedata.ViewVetProfileLiveData;

public class ViewVetProfileRepository {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();



    public ViewVetProfileLiveData getFirestoreLiveData(String userID)
    {
        DocumentReference documentReference = firebaseFirestore.collection("doctors").document(userID);

        return new ViewVetProfileLiveData(documentReference);
    }

}
