package com.project.mytelevet.doctor.livedata;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import com.project.mytelevet.doctor.model.ViewVetAppointmentItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewVetAppointmentLiveData  extends LiveData<List<ViewVetAppointmentItem>> implements EventListener<DocumentSnapshot> {

    private DocumentReference documentReference;

    private List<ViewVetAppointmentItem> appTemp = new ArrayList<>();

    public MutableLiveData<List<ViewVetAppointmentItem>> item = new MutableLiveData<List<ViewVetAppointmentItem>>();

    private ListenerRegistration listenerRegistration = () -> {};

    public ViewVetAppointmentLiveData(DocumentReference documentReference) {
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
                ViewVetAppointmentItem itemToAdd = new ViewVetAppointmentItem();
                itemToAdd.setName(entry.getValue().toString());
                appTemp.add(itemToAdd);
            }

            item.setValue(appTemp);
        }
    }
}
