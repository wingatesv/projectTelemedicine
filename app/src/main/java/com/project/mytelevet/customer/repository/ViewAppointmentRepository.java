package com.project.mytelevet.customer.repository;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.mytelevet.customer.livedata.ViewAppointmentLiveData;

public class ViewAppointmentRepository {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public ViewAppointmentLiveData getFirestoreLiveData(String userID) {
        DocumentReference documentReference = firebaseFirestore
                .collection("users")
                .document(userID).collection("appointment").document("appointmentList");

        return new ViewAppointmentLiveData(documentReference);
    }

}
