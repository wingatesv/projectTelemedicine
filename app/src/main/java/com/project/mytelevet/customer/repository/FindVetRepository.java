package com.project.mytelevet.customer.repository;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.mytelevet.customer.livedata.FindVetLiveData;

public class FindVetRepository {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public FindVetLiveData getFirestoreLiveData() {
        DocumentReference documentReference = firebaseFirestore
                .collection("doctors")
                .document("doctorList");

        return new FindVetLiveData(documentReference);
    }

}
