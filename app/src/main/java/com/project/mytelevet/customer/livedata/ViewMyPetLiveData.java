package com.project.mytelevet.customer.livedata;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ViewMyPetLiveData  extends LiveData<Map<String, Object>> implements EventListener<QuerySnapshot> {


    private  Query query;

    private String petID;


    public MutableLiveData<Map<String, Object>> item = new MutableLiveData<Map<String, Object>>();


    private ListenerRegistration listenerRegistration = () -> {};

    public ViewMyPetLiveData(Query query) {
        this.query = query;
    }


    @Override
    protected void onActive() {
        listenerRegistration = query.addSnapshotListener(this);
        super.onActive();
    }

    @Override
    protected void onInactive() {
        listenerRegistration.remove();
        super.onInactive();
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException error) {



        if (querySnapshot != null && !querySnapshot.isEmpty())
        {
            Map<String, Object> itemTemp = new HashMap<>();

            for (DocumentSnapshot snapshot : querySnapshot.getDocuments()) {

                setPetID(snapshot.getId());
                itemTemp = snapshot.getData();

            }
            item.setValue(itemTemp);
        }

        else
        {
            Log.e("tag", "QuerySnapshot is empty at ViewMyPetLiveData");
        }

    }

    public String getPetID() {
        return petID;
    }

    public void setPetID(String petID) {
        this.petID = petID;
    }

}
