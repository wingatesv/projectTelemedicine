package com.project.mytelevet.customer.livedata;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.HashMap;
import java.util.Map;

public class ViewMyProfileLiveData extends LiveData<Map<String, Object>> implements EventListener<DocumentSnapshot> {

    private DocumentReference documentReference;



    public MutableLiveData<Map<String, Object>> item = new MutableLiveData<Map<String, Object>>();

    private ListenerRegistration listenerRegistration = () -> {};

    public ViewMyProfileLiveData(DocumentReference documentReference) {
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

            item.setValue(documentSnapshot.getData());


        }
        else
        {
            Log.e("tag", "DocumentSnapshot is empty at ViewMyProfileLiveData");
        }
    }
}
