package com.example.games4you;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import com.example.games4you.logic.Game;
import com.example.games4you.logic.GameAdapter;
import com.example.games4you.logic.User;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {
    NavigationView mNavView;
    ImageView userImageView;
    TextView userNameFiled;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings,container,false);

        mNavView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        View headerView = mNavView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.navMenuUserNameDisplay);
        TextView navEmail = (TextView) headerView.findViewById(R.id.navMenuEmailDisplay);

        userImageView = (ImageView)view.findViewById(R.id.user_photo);
        userNameFiled = (TextView)view.findViewById(R.id.user_name_text);
        String usersEmail;
        if (currentUser != null)
            usersEmail = currentUser.getEmail();
        else
            usersEmail ="";

        db.collection("users").whereEqualTo("email",usersEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User userToDisplay =document.toObject(User.class);
                                userNameFiled.setText(userToDisplay.getUserName());
                                String imageUrl = userToDisplay.getImageUrl();
                                if(imageUrl ==""){
                                    Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/games4you-d5233.appspot.com/o/users_images%2Fperson_icon.png?alt=media&token=76f2c5f4-6302-4777-83da-b51373f45906")
                                            .fit()
                                            .centerCrop()
                                            .into(userImageView);}
                                else{
                                    Picasso.get().load(imageUrl)
                                            .fit()
                                            .centerCrop()
                                            .into(userImageView);}
                            }

                        } else {
                            Log.d("TAG Error", "task.getException()");
                        }
                    }
                });

        return  view;

    }


}


