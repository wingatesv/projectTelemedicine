package com.project.mytelevet.doctor.repository;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.mytelevet.customer.livedata.ViewAppointmentLiveData;
import com.project.mytelevet.doctor.livedata.ViewVetAppointmentLiveData;

public class ViewVetAppointmentRepository {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public ViewVetAppointmentLiveData getFirestoreLiveData(String userID) {
        DocumentReference documentReference = firebaseFirestore
                .collection("doctors")
                .document(userID).collection("appointment").document("appointmentList");

        return new ViewVetAppointmentLiveData(documentReference);
    }

}
