package com.hodiaspora.realtyapplication.Fragments;

import android.R.layout;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hodiaspora.realtyapplication.R;
import com.hodiaspora.realtyapplication.User;
import com.hodiaspora.realtyapplication.userInfo;

import java.util.ArrayList;

import static android.R.layout.*;


public class ProfileFragment extends Fragment {

    private ImageView ProfImg;
    private TextView P_nameTve;

    private static final String TAG = "ProfileFragment";

    private String userID;

    private ListView mListData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ProfImg = view.findViewById(R.id.ProfImg);
        P_nameTve = view.findViewById(R.id.P_nameTv);

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        final FirebaseUser user = fAuth.getCurrentUser();
        mListData = view.findViewById(R.id.ListData);
        userID = user.getUid();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               P_nameTve.setText(user.getDisplayName());
               Glide.with(ProfileFragment.this).load(user.getPhotoUrl()).into(ProfImg);

                readData(dataSnapshot);
                

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void readData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()){
            userInfo uInfo = new userInfo();
            uInfo.setEmail(ds.child(userID).getValue(userInfo.class).getEmail());
            uInfo.setIdentification(ds.child(userID).getValue(userInfo.class).getIdentification());
            uInfo.setPhone(ds.child(userID).getValue(userInfo.class).getPhone());

            Log.d(TAG, "showData: email: " + uInfo.getEmail());
            Log.d(TAG, "showData: identification: " + uInfo.getIdentification());
            Log.d(TAG, "showData: phone: " + uInfo.getPhone());

            ArrayList<String> array = new ArrayList<>();
            array.add(uInfo.getEmail());
            array.add(uInfo.getIdentification());
            array.add(uInfo.getPhone());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getActivity(),
                    simple_list_item_1,
                    array
            );

            mListData.setAdapter(adapter);

        }
    }


}
