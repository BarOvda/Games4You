package com.example.games4you;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;


import com.example.games4you.logic.Categories;
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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar mainToolBar;
    Toolbar searchToolBar;
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
    boolean[] checkedCategories;
    String[] listCategories;
    List<String> mUserCategoriesSelection;
    boolean fragmentChanged;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserCategoriesSelection = new ArrayList<>();
        listCategories = new String[Categories.values().length];
        getAllCategories();

        checkedCategories = new boolean[listCategories.length];

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
                                if(imageUrl ==""){
                                    Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/games4you-d5233.appspot.com/o/users_images%2Fdeafult_icon.png?alt=media&token=3bc92123-7268-447e-9305-d1421ea9dc58")
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

        navEmail.setText(user.getEmail());

        mNavView.setNavigationItemSelectedListener(this);
        mainToolBar = findViewById(R.id.main_toolbar);
        searchToolBar = findViewById(R.id.search_toolbar);
        searchToolBar.setVisibility(View.GONE);
        searchView = findViewById(R.id.search_button_toolbar);
        filterView = findViewById(R.id.search_button_filter);

        searchView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("search","pressed");


            }
        });
        gilterByCatecories();
        setSupportActionBar(mainToolBar);
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

    private void getAllCategories() {
        int i=0;
        for (Categories category : Categories.values()) {
            listCategories[i] = category.toString();
            i++;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer((GravityCompat.START));
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_ps4:
                searchToolBar.setVisibility(View.VISIBLE);
                resetFilterChoices();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        psFragment,"PS4_FRAGMENT").commit();
                break;
            case R.id.nav_xboxOne:
                searchToolBar.setVisibility(View.VISIBLE);
                resetFilterChoices();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        xboxOneFragment,"XBOX_ONE_FRAGMENT").commit();
                break;
            case R.id.nav_home:
                searchToolBar.setVisibility(View.GONE);
                resetFilterChoices();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        homeFragment).commit();
                break;
            case R.id.nav_settings:
                searchToolBar.setVisibility(View.GONE);
                resetFilterChoices();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        settingsFragment).commit();
                break;
            case R.id.nav_log_out:
                searchToolBar.setVisibility(View.GONE);

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
        menuInflater.inflate(R.menu.search_menu,menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                if(psFragment!=null&&psFragment.isVisible())
                    psFragment.searchFilter(newText);
                else if(xboxOneFragment!=null&&xboxOneFragment.isVisible())
                    xboxOneFragment.searchFilter(newText);

                return false;
            }
        });
        return true;
    }
    public void gilterByCatecories(){

        filterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("filter","pressed");
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                mBuilder.setTitle("Games Filter");
                mBuilder.setMultiChoiceItems(listCategories, checkedCategories, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                   //     if(isChecked){
                            if(!mUserCategoriesSelection.contains(listCategories[position])){
                                mUserCategoriesSelection.add(listCategories[position]);
                            }else{
                                mUserCategoriesSelection.remove(listCategories[position]);
                            }
                        }
                    //}
                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("Filter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(psFragment!=null&&psFragment.isVisible())
                            psFragment.filterGamesByCategory(mUserCategoriesSelection);
                        else if(xboxOneFragment!=null&&xboxOneFragment.isVisible())
                            xboxOneFragment.filterGamesByCategory(mUserCategoriesSelection);

                    }
                });
                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                mBuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i=0;i<checkedCategories.length;i++)
                            checkedCategories[i]=false;
                        mUserCategoriesSelection.clear();
                        if(psFragment!=null&&psFragment.isVisible())
                            psFragment.filterGamesByCategory(mUserCategoriesSelection);
                        else if(xboxOneFragment!=null&&xboxOneFragment.isVisible())
                            xboxOneFragment.filterGamesByCategory(mUserCategoriesSelection);

                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

    }
    public void resetFilterChoices(){
        for(int i=0;i<checkedCategories.length;i++)
            checkedCategories[i]=false;
        mUserCategoriesSelection.clear();
    }

}