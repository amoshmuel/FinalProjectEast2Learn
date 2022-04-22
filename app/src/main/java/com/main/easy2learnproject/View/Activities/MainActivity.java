package com.main.easy2learnproject.View.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.main.easy2learnproject.Control.FireBase;
import com.main.easy2learnproject.Control.GetListListener;
import com.main.easy2learnproject.Model.Photo;
import com.main.easy2learnproject.Model.PhotoComparator;
import com.main.easy2learnproject.View.Fragments.ListFragment;
import com.main.easy2learnproject.View.Fragments.MapFragment;
import com.main.easy2learnproject.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView LBL_hello;
    private Photo photo = null;
    private TextView nameTxt;
    private TextView emailTxt;
    private ImageView IMG_logout;
    private ImageView IMG_holder;
    private FloatingActionButton BTN_floating_addPhoto;
    private BottomNavigationView BTN_navigation;
    private MapFragment mapFragment;
    private ListFragment listFragment;
    private FragmentTransaction ft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        ft = getSupportFragmentManager().beginTransaction();
        mapFragment=new MapFragment();
        listFragment=new ListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainActivity_LAY_container,mapFragment).commit();


        Log.d("pttt", "onCreate: addMap");
        findViews();
        initViews();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initViews() {
        poppulatePhotoProfile();

        BTN_floating_addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPhotoActivity();
            }
        });
        IMG_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogoutDialog();
            }
        });
        BTN_navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                if(item.getTitle().toString().equals( getString(R.string.map_item_label))) {
                    Log.d("pttt", "onNavigationItemSelected: MAP");
                    replaceToMapFragment();

                }else if(item.getTitle().toString().equals( getString(R.string.list_item_label))) {
                    Log.d("pttt", "onNavigationItemSelected: LIST");
                    replaceToListFragment();
                }
                return false;
            }
        });
    }

    private void poppulatePhotoProfile() {
        String email = getIntent().getExtras().getString("EMAIL");
        FireBase.getInstance().getFromStorageByName(email, this,IMG_holder);
        emailTxt.setText(email);

    }


    private void replaceToMapFragment() {
        ft = getSupportFragmentManager().beginTransaction();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainActivity_LAY_container,mapFragment).commit();
    }

    private void replaceToListFragment() {
        ft = getSupportFragmentManager().beginTransaction();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainActivity_LAY_container,listFragment).commit();
    }

    private void openLogoutDialog() {
        FirebaseAuth.getInstance().signOut();
        Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
        MainActivity.this.startActivity(myIntent);
        MainActivity.this.finish();
    }

    private void openPhotoActivity() {
        Log.d("pttt", "openPhotoActivity: ");
        Intent myIntent = new Intent(MainActivity.this, PhotoActivity.class);
        myIntent.putExtra("EMAIL",getIntent().getExtras().getString("EMAIL"));
        MainActivity.this.startActivity(myIntent);
    }

    private void findViews() {
        IMG_logout=(ImageView )findViewById(R.id.mainActivity_IMG_logout);
        BTN_floating_addPhoto =(FloatingActionButton)findViewById(R.id.mainActivity_BTN_floating_addPhoto);
        BTN_navigation=(BottomNavigationView)findViewById(R.id.mainActivity_BTN_navigation);
        IMG_holder=(ImageView) findViewById(R.id.mainActivity_IMG_holder);
        nameTxt= findViewById(R.id.accountRow_LBL_name);
        emailTxt= findViewById(R.id.accountRow_LBL_email);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}