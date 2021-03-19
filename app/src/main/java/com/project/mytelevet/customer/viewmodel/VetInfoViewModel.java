package com.project.mytelevet.customer.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.mytelevet.customer.livedata.VetInfoLiveData;
import com.project.mytelevet.customer.repository.VetInfoRepository;

import java.util.Map;

public class VetInfoViewModel extends ViewModel {


    private VetInfoRepository repository = new VetInfoRepository();
    VetInfoLiveData liveData = null;



    public LiveData<Map<String, Object>> getVetInfoLiveData(String vetName) {


        liveData = repository.getFirestoreLiveData(vetName);

        return liveData;
    }


    public LiveData<Map<String, Object>> getVetInfoItem() {
        return liveData.item;
    }
}
