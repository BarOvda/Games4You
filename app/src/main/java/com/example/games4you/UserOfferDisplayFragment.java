package com.example.games4you;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.games4you.logic.Game;
import com.example.games4you.logic.GameOffer;
import com.example.games4you.logic.GameOfferAdapter;
import com.example.games4you.logic.Review;
import com.example.games4you.logic.User;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class UserOfferDisplayFragment extends Fragment implements OnMapReadyCallback {

    final static int PICK_IMAGE_REQUEST = 1;
    private static final String MAPVIEW_BUNDLE_KEY="MapViewBundleKey";
    private User userDetailes;
    private GameOffer mGame;
    private TextView description;
    private TextView title;
    private TextView mPrice;
    private ImageView offerPic;
    private ImageView userPic;
    private MapView mapView;

    private FirebaseFirestore db;
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_offer_display, container, false);




        mapView = view.findViewById(R.id.user_list_map);
        description = view.findViewById(R.id.description_text_view);
        title = view.findViewById(R.id.title_text_view);
        offerPic = view.findViewById(R.id.game_offer_photo);
        userPic =  view.findViewById(R.id.user_photo);
        mPrice = view.findViewById(R.id.price_text_view);

        db = FirebaseFirestore.getInstance();

        mGame = (GameOffer) getArguments().getSerializable("game");


        db.collection( mGame.getGameConsole())
                .document(mGame.getGameName())
                .collection("offers")
                .document(mGame.getUserEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Log.e("firebasecheck","test");

                        if (task.isSuccessful()) {
                            Log.e("firebasecheck","test");
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                mGame = document.toObject(GameOffer.class);
                                Log.e("firebasecheck","test");
                            }

                        }
                    }}).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("the failur",e.getMessage());
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Log.e("is canseld","test");
            }
        });
        db.collection( "users")
                .document(mGame.getUserEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                userDetailes = document.toObject(User.class);
                            }
                        }
                    }
                });


        title.setText(mGame.getmTitle());
        mPrice.setText(mGame.getmPrice()+"$");
        description.setText(mGame.getmDescription());
        Picasso.get().load(mGame.getmImageUrl()).into(offerPic);

        //       Picasso.get().load(userDetailes.getImageUrl()).into(userPic);

             initGoogleMap(savedInstanceState);

        return view;
    }
    private void initGoogleMap(Bundle instanceState){
        Bundle mapViewBundle = null;
        if (instanceState != null) {
            mapViewBundle = instanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mapView.onCreate(mapViewBundle);

        mapView.getMapAsync(this);

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        URL sellerUrl = null;
        Bitmap sellerBmp = null;

        Login logFragment = new Login();
        LatLng myLocation = new LatLng(logFragment.getCurrentUserLat(),logFragment.getCurrentUserLong());
        Marker meMarker = map.addMarker(new MarkerOptions()
                .position(myLocation)
                .title("Me"));
        LatLng sellerLocation = new LatLng(mGame.getLocation().getLatitude(),mGame.getLocation().getLongitude());
        Marker sellerMarker = map.addMarker(new MarkerOptions()
                .position(sellerLocation)
               /* .icon((BitmapDescriptorFactory.fromBitmap(sellerBmp))))*/
                .title(mGame.getUserEmail()));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        builder.include(meMarker.getPosition());
        builder.include(sellerMarker.getPosition());

        LatLngBounds bounds = builder.build();

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);

        map.animateCamera(cu);

    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}