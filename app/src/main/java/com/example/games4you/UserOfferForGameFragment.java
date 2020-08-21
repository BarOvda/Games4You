package com.example.games4you;

import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.games4you.logic.Game;
import com.example.games4you.logic.GameOffer;
import com.example.games4you.logic.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.net.URI;

import static android.app.Activity.RESULT_OK;

public class UserOfferForGameFragment extends Fragment {
    final static int PICK_IMAGE_REQUEST = 1;

    private Button btnUpload;
    private Game mGame;
    private TextInputEditText description;
    private TextInputEditText title;
    private TextInputEditText mPrice;
    private ImageView pic;
    private Uri mImageUri;
    private String originalTitleImageUrl;

    private FirebaseFirestore db;
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_offer_for_game,container,false);

        db = FirebaseFirestore.getInstance();
        btnUpload = view.findViewById(R.id.upload_offer);
        description = view.findViewById(R.id.description_text);
        title = view.findViewById(R.id.title_text);
        pic = view.findViewById(R.id.game_image);
        mPrice = view.findViewById(R.id.price_text);

        mGame = (Game)getArguments().getSerializable("game");

        pic.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                openFileChooser();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                UploadOffer();
            }
        });


        db.collection(mGame.getmConsole())
                .whereEqualTo("name",mGame.getName())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Game gameFromDB =document.toObject(Game.class);
                                originalTitleImageUrl = gameFromDB.getImageUrl();
                                if(Patterns.WEB_URL.matcher(originalTitleImageUrl).matches()){
                                    Picasso.get().load(originalTitleImageUrl)
                                            .fit()
                                            .centerCrop()
                                            .into(pic);}



                            }

                        } else {
                            Log.d("TAG Error", "task.getException()");
                        }
                    }
                });
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("game",mGame);
                    bundle.putString("console",mGame.getmConsole());
                    GamePageActivity gamePageActivity = new GamePageActivity();
                    gamePageActivity.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,gamePageActivity).commit();


                    return true;
                }
                return false;
            }
        });
        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                &&data!=null&&data.getData()!=null){
            mImageUri = data.getData();
            Picasso.get().load(mImageUri)
                    .into(pic);
        }
    }
    private void UploadOffer() {
        boolean isValid = true;
        if (TextUtils.isEmpty(title.getText())) {
            title.setError("Title is required");
            isValid = false;
        }
        if (TextUtils.isEmpty(mPrice.getText())) {
            mPrice.setError("Price is required");
            isValid = false;
        }
       /* else if (!TextUtils.isDigitsOnly(mPrice.getText())) {
            mPrice.setError("Price must include only digits");
            isValid = false;
        }*/
        if(!isValid)
            return;;
            float price = new Float(mPrice.getText().toString());
            if(mImageUri==null)
                mImageUri= Uri.parse(originalTitleImageUrl);
        Login loginActivity = new Login();
        GameOffer offer = new GameOffer(String.valueOf(title.getText()),String.valueOf(mImageUri)
                ,String.valueOf(description.getText())
                ,price,new GeoPoint(loginActivity.getCurrentUserLong(),loginActivity.getCurrentUserLat()));
        db.collection( mGame.getmConsole())
                .document(mGame.getName())
                .collection("offers")
                .document(currentUser.getEmail())
                .set(offer)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "Your offer was upload", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new HomeFragment(), "XBOX_ONE_FRAGMENT").commit();
                    }
                });

    }
}