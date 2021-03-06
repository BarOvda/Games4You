package com.example.games4you;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar mainToolBar;
    DrawerLayout drawerLayout;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser user;
    NavigationView mNavView;
    PS4Fragment psFragment;
    XboxOneFragment xboxOneFragment;
    HomeFragment homeFragment;
    SettingsFragment settingsFragment;
    ImageView userImageView;
    TextView userNameFiled;
    ImageView searchView;
    ImageView filterView;

    boolean fragmentChanged;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);
        mFirebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        psFragment = new PS4Fragment();
        xboxOneFragment = new XboxOneFragment();
        homeFragment = new HomeFragment();
        settingsFragment = new SettingsFragment();

        mNavView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = mNavView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.navMenuUserNameDisplay);
        TextView navEmail = (TextView) headerView.findViewById(R.id.navMenuEmailDisplay);




        userImageView = (ImageView)headerView.findViewById(R.id.user_image_view);
        userNameFiled = (TextView)headerView.findViewById(R.id.navMenuUserNameDisplay);

        String usersEmail;
        if (currentUser != null)
             usersEmail = user.getEmail();
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
                                userNameFiled.setText( userToDisplay.getUserName());
                                String imageUrl = userToDisplay.getImageUrl();
                                if(Patterns.WEB_URL.matcher(imageUrl).matches()){
                                    Picasso.get().load(imageUrl)
                                            .fit()
                                            .centerCrop()
                                            .into(userImageView);}

//                               if(userImageView.getDrawable() == null){
//                                   Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/games4you-d5233.appspot.com/o/users_images%2Fdeafult_icon.png?alt=media&token=3bc92123-7268-447e-9305-d1421ea9dc58")
//                                           .fit()
//                                           .centerCrop()
//                                           .into(userImageView);
//                               }
                            }

                        } else {
                            Log.d("TAG Error", "task.getException()");
                        }
                    }
                });

        navEmail.setText(user.getEmail());

        mNavView.setNavigationItemSelectedListener(this);
        mainToolBar = findViewById(R.id.main_toolbar);

        setSupportActionBar(mainToolBar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, mainToolBar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    homeFragment).commit();
            mNavView.setCheckedItem(R.id.nav_home);
        }


        //PS4 fragment


    }



    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer((GravityCompat.START));
        } else if (count == 0) {
            super.onBackPressed();
        }
            else {
                getSupportFragmentManager().popBackStack();
            }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_ps4:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        psFragment,"PS4_FRAGMENT").addToBackStack("ps4").commit();
                break;
            case R.id.nav_xboxOne:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        xboxOneFragment,"XBOX_ONE_FRAGMENT").addToBackStack("xbox").commit();
                break;
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        homeFragment).addToBackStack("home").commit();
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        settingsFragment).addToBackStack("settings").commit();
                break;
            case R.id.nav_log_out:

                mFirebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(),Login.class));
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);

        return true;
    }




}