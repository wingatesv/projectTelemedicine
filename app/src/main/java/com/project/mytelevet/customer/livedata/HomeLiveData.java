package com.project.mytelevet.customer.livedata;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;


import java.util.Map;

public class HomeLiveData extends LiveData<String> implements EventListener<DocumentSnapshot>
{
    private  DocumentReference documentReference;


    public MutableLiveData<String> username = new MutableLiveData<String>();

    private ListenerRegistration listenerRegistration = () -> {};

    public HomeLiveData(DocumentReference documentReference) {
        this.documentReference = documentReference;
    }

    @Override
    protected void onActive() {
        listenerRegistration = documentReference.addSnapshotListener(this);
        super.onActive();
    }

    @Override
    protected void onInactive() {
        listenerRegistration.remove();
        super.onInactive();
    }

    @Override
    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

        if(documentSnapshot != null && documentSnapshot.exists()) {

            username.setValue(documentSnapshot.getString("FirstName"));


        }
    }

}
