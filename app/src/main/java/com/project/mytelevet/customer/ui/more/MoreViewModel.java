package com.project.mytelevet.customer.ui.more;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class MoreViewModel extends ViewModel {

   // private MutableLiveData<String> mText;

    private  MutableLiveData<List<String>> settingsList = new MutableLiveData<>();



    public MoreViewModel() {





        /*
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");

         */
    }

    public LiveData<List<String>> getData()
    {
        addData();
        return settingsList;
    }

    private void addData()
    {
        List<String> list = new ArrayList<>();
        list.add("My Profile");
        list.add("Terms of Service");
        list.add("Contact Us");
        list.add("Log Out");

        settingsList.setValue(list);
    }
/*
    public LiveData<String> getText() {
        return mText;
    }

 */
}