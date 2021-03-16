package com.project.mytelevet.customer.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.mytelevet.customer.livedata.ViewMyPetLiveData;
import com.project.mytelevet.customer.repository.ViewMyPetRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewMyPetViewModel extends ViewModel {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore fStore= FirebaseFirestore.getInstance();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();




    private  MutableLiveData<List<String>> myPetList = new MutableLiveData<List<String>>();

    private ViewMyPetRepository repository = new ViewMyPetRepository();
    ViewMyPetLiveData liveData = null;



    public LiveData<Map<String, Object>> getViewMyPetLiveData(String petName, String userID) {


        liveData = repository.getFirestoreLiveData(petName, userID);

        return liveData;
    }


    public LiveData<Map<String, Object>> getMyPetItem() {
        return liveData.item;
    }

    public LiveData<List<String>> getMyPetList()
    {
        addData();
        return myPetList;
    }



    private void addData()
    {
        List<String> list = new ArrayList<>();
        list.add("General Information");
        list.add("Medical Record");
        list.add("Remove This Pet");



        myPetList.setValue(list);
    }

    public boolean updateData(String pet_name,  String breed, String dob, String pet_type,  String size, String condition, String gender)
    {
        boolean state = false;
        String petID = liveData.getPetID( );

        if (!petID.isEmpty())
        {


            DocumentReference documentReference1 = fStore.collection("users").document(firebaseUser.getUid()).collection("pets").document("petList_"+firebaseUser.getUid());
            Map<String, Object> update = new HashMap<>();
            update.put(petID, pet_name);
            documentReference1.update(update).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.i("TAG", pet_name + " is updated from pet list");



                    DocumentReference documentReference = fStore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).collection("pets").document(petID);


                    Map<String, Object> pet = new HashMap<>();

                    pet.put("PetName", pet_name);
                    pet.put("PetType", pet_type);
                    pet.put("DOB", dob);
                    pet.put("Gender", gender);
                    pet.put("Breed", breed);
                    pet.put("Size", size);
                    pet.put("Condition", condition);



                    documentReference.update(pet).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Log.d("TAG", pet_name + " Pet Profile is updated");

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("TAG", e.getMessage());
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("TAG", e.getMessage());

                }
            });

            state = true;
        }
        else {
            Log.e("tag", "PetID is missing in ViewMyPetViewModel.updateData()");
        }
        return  state;
    }

    public void deleteData(String petName)
    {
        String petID = liveData.getPetID( );

        if (!petID.isEmpty()) {
            DocumentReference documentReference1 = fStore.collection("users").document(firebaseUser.getUid()).collection("pets").document("petList_" + firebaseUser.getUid());
            Map<String, Object> remove = new HashMap<>();
            remove.put(petID, FieldValue.delete());
            documentReference1.update(remove).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.i("TAG", petName + " is removed from pet list");
                    DocumentReference documentReference = fStore.collection("users").document(firebaseUser.getUid()).collection("pets").document(petID);
                    documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Log.i("TAG", petName + " pet is removed.");

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("TAG", e.getMessage());

                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("TAG", e.getMessage());

                }
            });

        }
        else
        {
            Log.e("tag", "PetID is missing in ViewMyPetViewModel.deleteData()");
        }
    }



}
