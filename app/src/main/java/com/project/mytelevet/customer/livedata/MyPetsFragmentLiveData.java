package com.project.mytelevet.customer.livedata;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.project.mytelevet.customer.model.MyPetsItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;

/*

 final DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                lbl_welcome_user.setText("Hello " + value.getString("FirstName") + "!");
            }
        });

 */

// similar to code above which to obtain data from database

public class MyPetsFragmentLiveData extends LiveData<List<MyPetsItem>> implements
        EventListener<DocumentSnapshot> {

    private DocumentReference documentReference;



    private List<MyPetsItem> myPetsTemp = new ArrayList<>();
    public MutableLiveData<List<MyPetsItem>> myPets = new MutableLiveData<List<MyPetsItem>>();

    private ListenerRegistration listenerRegistration = () -> {};

    public MyPetsFragmentLiveData(DocumentReference documentReference) {
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


    @Override public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

        if(documentSnapshot != null && documentSnapshot.exists()) {

            Map<String, Object> myPetsItems = documentSnapshot.getData();

            myPetsTemp.clear();

            for (Map.Entry<String, Object> entry : myPetsItems.entrySet()) {
                MyPetsItem itemToAdd = new MyPetsItem();
                itemToAdd.setName(entry.getValue().toString());
                myPetsTemp.add(itemToAdd);
            }

            myPets.setValue(myPetsTemp);
        } else {

            Log.d("TAG", "error");
        }
    }
}
