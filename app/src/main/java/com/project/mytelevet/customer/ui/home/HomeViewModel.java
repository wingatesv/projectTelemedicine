package com.project.mytelevet.customer.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.mytelevet.customer.livedata.HomeLiveData;
import com.project.mytelevet.customer.repository.HomeRepository;

import java.util.ArrayList;
import java.util.List;

//Email is not verified. Click to verify now.

public class HomeViewModel extends ViewModel {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    private HomeRepository repository = new HomeRepository();
    HomeLiveData liveData = null;

    //private MutableLiveData<String> userName = new MutableLiveData<String>();      // make object observable
    private  MutableLiveData<String> verifyEmailText;

    private  MutableLiveData<List<String>> homeList = new MutableLiveData<List<String>>();



    public  HomeViewModel()
    {
        verifyEmailText = new MutableLiveData<>();
        verifyEmailText.setValue("");

        if (!firebaseUser.isEmailVerified())
        {
            verifyEmailText.postValue("Email is not verified. Click to verify now.");
        }

        else
        {
            verifyEmailText.postValue("");
        }
    }


    public LiveData<String> getHomeLiveData() {
        liveData = repository.getFirestoreLiveData();

        return liveData;
    }


    public LiveData<String> getText() {
        return liveData.username;
    }       // observer (send back data object)

    public LiveData<String> getVerifyEmail()
    {
        return verifyEmailText;
    }



    public LiveData<List<String>> getHomeList()
    {
        addData();
        return homeList;
    }



    private void addData()
    {
        List<String> list = new ArrayList<>();
        list.add("Find a Veterinarian");
        list.add("Upcoming Appointment");



        homeList.setValue(list);
    }


     /*

    public LiveData<String> isEmailVerified()
    {
        if (!firebaseUser.isEmailVerified())
        {
            verifyEmailText.setValue("Email is not verified. Click to verify now.");
        }

        else
        {
            verifyEmailText.setValue("Email is verified.");
        }

        return  verifyEmailText;
    }



     */


}