package com.project.mytelevet.customer.ui.more;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.project.mytelevet.common.LoginActivity;
import com.project.mytelevet.R;
import com.project.mytelevet.customer.ViewMyProfileActivity;

public class MoreFragment extends Fragment {

    private MoreViewModel moreVewModel;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        moreVewModel = new ViewModelProvider(this).get(MoreViewModel.class);

        View root = inflater.inflate(R.layout.fragment_more, container, false);

        ListView listView = root.findViewById(R.id.listView_more);

        moreVewModel.getData().observe(getActivity(), settingsList ->{

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, settingsList);
            listView.setAdapter(arrayAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String component = listView.getItemAtPosition(position).toString();

                    //Toast.makeText(getActivity(), listView.getItemAtPosition(position).toString(),Toast.LENGTH_SHORT ).show();

                    if (component.matches("My Profile"))
                    {

                        Intent intent = new Intent(getActivity(), ViewMyProfileActivity.class);
                        startActivity(intent);

                    }

                    if (component.matches("Log Out"))
                    {
                        firebaseAuth.signOut();   // logout firebaseUser
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }


                }
            });

        });

        /*
        final TextView textView = root.findViewById(R.id.text_notifications);

        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

         */
        return root;
    }
}