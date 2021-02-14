package com.project.mytelevet.customer.livedata;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.project.mytelevet.customer.model.ViewAppointmentItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewAppointmentLiveData  extends LiveData<List<ViewAppointmentItem>> implements EventListener<DocumentSnapshot> {

    private DocumentReference documentReference;

    private List<ViewAppointmentItem> appTemp = new ArrayList<>();

    public MutableLiveData<List<ViewAppointmentItem>> item = new MutableLiveData<List<ViewAppointmentItem>>();

    private ListenerRegistration listenerRegistration = () -> {};

    public ViewAppointmentLiveData(DocumentReference documentReference) {
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
            appTemp.clear();

            for (Map.Entry<String, Object> entry : items.entrySet()) {
                ViewAppointmentItem itemToAdd = new ViewAppointmentItem();
                itemToAdd.setName(entry.getValue().toString());
                appTemp.add(itemToAdd);
            }

            item.setValue(appTemp);
        }
    }
}

