package com.project.mytelevet.customer.ui.mypets;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.project.mytelevet.customer.livedata.MyPetsFragmentLiveData;
import com.project.mytelevet.customer.model.MyPetsItem;
import com.project.mytelevet.customer.repository.MyPetsRepository;

import java.util.List;

public class MyPetsViewModel extends ViewModel {

    private MyPetsRepository repository = new MyPetsRepository();
    //public MutableLiveData<List<MyPetsItem>> myPet = new MutableLiveData<List<MyPetsItem>>();
    MyPetsFragmentLiveData liveData = null;

    public LiveData<List<MyPetsItem>> getMyPetsFragmentLiveData()
    {
        liveData = repository.getFirestoreLiveData();
        return liveData;

    }

   public LiveData<List<MyPetsItem>> getMyPets() {return liveData.myPets;}


}