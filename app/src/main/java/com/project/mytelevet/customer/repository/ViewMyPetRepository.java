package com.project.mytelevet.customer.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.project.mytelevet.customer.livedata.ViewMyPetLiveData;

public class ViewMyPetRepository {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();




    public ViewMyPetLiveData getFirestoreLiveData(String petName, String userID)
    {


        CollectionReference collectionReference = firebaseFirestore.collection("users").document(userID).collection("pets");

        Query query = collectionReference.whereEqualTo("PetName", petName);

        return new ViewMyPetLiveData(query);
    }



}
