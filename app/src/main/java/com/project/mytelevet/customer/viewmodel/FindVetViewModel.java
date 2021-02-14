package com.project.mytelevet.customer.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.mytelevet.customer.livedata.FindVetLiveData;
import com.project.mytelevet.customer.model.FindVetItem;
import com.project.mytelevet.customer.repository.FindVetRepository;

import java.util.List;

public class FindVetViewModel extends ViewModel {
    private FindVetRepository repository = new FindVetRepository();
    public MutableLiveData<List<FindVetItem>> shoppingList = new MutableLiveData<List<FindVetItem>>();
    FindVetLiveData liveData = null;

    public LiveData<List<FindVetItem>> getVetItemLiveData() {
        liveData = repository.getFirestoreLiveData();
        return liveData;
    }

    public LiveData<List<FindVetItem>> getVetItem() {
        return liveData.findVetItem;
    }
}

