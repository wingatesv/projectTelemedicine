package com.project.mytelevet.customer.ui.mypets;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.mytelevet.customer.PetInfoActivity;
import com.project.mytelevet.R;
import com.project.mytelevet.customer.ViewMyPetActivity;
import com.project.mytelevet.customer.model.MyPetsItem;

import java.util.ArrayList;

public class MyPetsFragment extends Fragment {

    private MyPetsViewModel myPetsViewModel;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myPetsViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MyPetsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mypets, container, false);
        //final TextView textView = root.findViewById(R.id.text_dashboard);
        ProgressBar progressBar = root.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);


        ArrayList<MyPetsItem> arrayOfItems = new ArrayList<>();
        MyPetsAdapter adapter = new MyPetsAdapter(getActivity(), arrayOfItems);

        myPetsViewModel.getMyPetsFragmentLiveData().observe(getActivity(), Observable -> {});
        myPetsViewModel.getMyPets().observe(getActivity(), myPetsItems -> {
            if (myPetsItems != null ) {
                adapter.clear();
                adapter.addAll(myPetsItems);

                ListView listView = root.findViewById(R.id.list_myPets);
                listView.setAdapter(adapter);

                progressBar.setVisibility(View.GONE);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        //Toast.makeText(getActivity(),  adapter.getItemName(position),Toast.LENGTH_SHORT ).show();

                        //petName = adapter.getItemName(position);

                        Intent intent = new Intent(getActivity(), ViewMyPetActivity.class);
                        intent.putExtra("petName",  adapter.getItemName(position));
                        startActivity(intent);
                    }
                });
            } else {

                Log.d("TAG", "awaiting for info");

            }
        });



        FloatingActionButton btn_addPets = root.findViewById(R.id.btn_addPets);
        btn_addPets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PetInfoActivity.class);
                startActivity(intent);
            }
        });


        /*
        myPetsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

         */
        return root;
    }


}