package com.project.mytelevet.doctor.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.project.mytelevet.doctor.livedata.ViewVetProfileLiveData;
import com.project.mytelevet.doctor.repository.ViewVetProfileRepository;

import java.util.HashMap;
import java.util.Map;

public class ViewVetProfileViewModel extends ViewModel {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    private ViewVetProfileRepository repository = new ViewVetProfileRepository();
    ViewVetProfileLiveData liveData = null;


    public LiveData<Map<String, Object>> getViewVetProfileLiveData(String userID) {
        liveData = repository.getFirestoreLiveData(userID);

        return liveData;
    }

    public LiveData<Map<String, Object>> getVetProfileItem() {
        return liveData.item;
    }




    public boolean updateData(String update_firstName, String update_lastName, String  update_phoneNumber, String update_avail, String update_description)
    {
        Boolean state = false;
        String userID = firebaseAuth.getCurrentUser().getUid();
        if (!userID.isEmpty())
        {
            DocumentReference documentReference = firebaseFirestore.collection("doctors").document(userID);

            Map<String, Object> update = new HashMap<>();
            update.put("FirstName", update_firstName);
            update.put("LastName", update_lastName);
            update.put("PhoneNumber", update_phoneNumber);
            update.put("Availability", update_avail);
            update.put("Description", update_description);
            update.put("FullName", update_firstName + " " + update_lastName);


            documentReference.update(update).addOnSuccessListener(aVoid ->{Log.i("tag", userID + " Vet Profile is updated");

                        DocumentReference documentReference1 = firebaseFirestore.collection("doctors").document("doctorList");
                        Map<String, Object> userList = new HashMap<>();

                        userList.put(userID, update_firstName + " " + update_lastName);


                        documentReference1.set(userList, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i("tag", userID + " doctor list is updated");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("tag", userID + " doctor List is not updated." + e.getMessage());
                            }
                        });



            }


            ).addOnFailureListener(e ->
                    Log.e("tag", userID + " Vet Profile is not updated." + e.getMessage())
            );
            state = true;
        }
        else {
            Log.e("tag", "User ID is empty at ViewVetProfileViewModel.updateData");
        }

        return state;
    }

}
