package com.project.mytelevet.customer.livedata;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.project.mytelevet.customer.model.FindVetItem;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class FindVetLiveData  extends LiveData<List<FindVetItem>> implements EventListener<DocumentSnapshot> {

    private DocumentReference documentReference;

    private List<FindVetItem> findVetTemp = new ArrayList<>();

    public MutableLiveData<List<FindVetItem>> findVetItem = new MutableLiveData<List<FindVetItem>>();

    private ListenerRegistration listenerRegistration = () -> {};

    public FindVetLiveData(DocumentReference documentReference) {
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
            Map<String, Object> items = documentSnapshot.getData();
            findVetTemp.clear();

            for (Map.Entry<String, Object> entry : items.entrySet()) {
                FindVetItem itemToAdd = new FindVetItem();
                itemToAdd.setName(entry.getValue().toString());
                findVetTemp.add(itemToAdd);
            }

            findVetItem.setValue(findVetTemp);
        }
    }
}
