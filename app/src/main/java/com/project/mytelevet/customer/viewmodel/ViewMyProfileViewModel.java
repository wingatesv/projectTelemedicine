package com.project.mytelevet.customer.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.mytelevet.customer.livedata.ViewMyProfileLiveData;
import com.project.mytelevet.customer.repository.ViewMyProfileRepository;

import java.util.HashMap;
import java.util.Map;

public class ViewMyProfileViewModel extends ViewModel {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    private ViewMyProfileRepository repository = new ViewMyProfileRepository();
    ViewMyProfileLiveData liveData = null;


    public LiveData<Map<String, Object>> getViewMyProfileLiveData(String userID) {
        liveData = repository.getFirestoreLiveData(userID);

        return liveData;
    }

    public LiveData<Map<String, Object>> getMyProfileItem() {
        return liveData.item;
    }




    public boolean updateData(String update_firstName, String update_lastName, String  update_phoneNumber, String update_state)
    {
        Boolean state = false;
        String userID = firebaseAuth.getCurrentUser().getUid();
        if (!userID.isEmpty())
        {
            DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);

            Map<String, Object> update = new HashMap<>();
            update.put("FirstName", update_firstName);
            update.put("LastName", update_lastName);
            update.put("PhoneNumber", update_phoneNumber);
            update.put("State", update_state);
            update.put("FullName", update_firstName + " " + update_lastName);


            documentReference.update(update).addOnSuccessListener(aVoid ->
                    Log.i("tag", userID + " Profile is updated")

            ).addOnFailureListener(e ->
                    Log.e("tag", userID + " Profile is not updated." + e.getMessage())
            );
            state = true;
        }
        else {
            Log.e("tag", "User ID is empty at ViewMyProfileViewModel.updateData");
        }

        return state;
    }

}
