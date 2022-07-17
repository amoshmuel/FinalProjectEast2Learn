package com.main.easy2learnproject.View.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.main.easy2learnproject.Calendar.ActivityCal;
import com.main.easy2learnproject.Control.FireBase;
import com.main.easy2learnproject.Model.Photo;
import com.main.easy2learnproject.View.Fragments.ListFragment;
import com.main.easy2learnproject.View.Fragments.MapFragment;
import com.main.easy2learnproject.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView LBL_hello;
    private ArrayList<Photo> photosList;
    private Photo photo = null;
    private String email = "";
    private TextView nameTxt;
    private TextView emailTxt;
    private ImageView IMG_logout;
    private ImageView IMG_holder;
    private FloatingActionButton BTN_floating_addPhoto;
    private BottomNavigationView BTN_navigation;
    private MapFragment mapFragment;
    private ListFragment listFragment;
    private FragmentTransaction ft;
    private Spinner SPN_sort;
    private int minRate = 0;
    private int maxDist = 20;
    private int maxPrice = 200;
    private String selectedPro = "";
    private Button BTN_filter;
//    private Spinner SPN_filter;
    private boolean isListFragment;
    private String role = "Student";
    int price, dist, rate;
    private List<Photo> photos;
    HashMap<Double,Photo> photoHashMap = new HashMap<Double, Photo>();
    ArrayList<Double> weightList = new ArrayList<>();
    private boolean isPopulate = true;
    private boolean start = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        findViews();
        getFilterInfo();
        mapFragment=new MapFragment();
        listFragment=new ListFragment(getIntent().getExtras().getString("EMAIL"));
//        if(start)
        replaceToListFragment();
//        start = false;
        Log.d("pttt", "onCreate: addMap");
        SPN_sort.setVisibility(View.VISIBLE);
        populateRoleSpinner();
        initViews();
        poppulatePhotoProfile();

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initViews() {
//        if(isPopulate) {
//            replaceToListFragment();
//            isPopulate = false;
//        }


        BTN_floating_addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCalActivity();
//                openPhotoActivity();
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
                    isListFragment = false;
                    replaceToMapFragment();
                }else if(item.getTitle().toString().equals( getString(R.string.list_item_label))) {
                    Log.d("pttt", "onNavigationItemSelected: LIST");
                    isListFragment =true;
                    replaceToListFragment();
                }
                return false;
            }
        });

        BTN_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFilterActivity();
            }
        });


    }

    private void getFilterInfo() {
        String action = getIntent().getAction();
        Intent intent = getIntent();
        if(action.equals("filter")){
            minRate = (int) intent.getExtras().get("MINRATE");
            maxDist = (int) intent.getExtras().get("MAXDIST");
            maxPrice= (int) intent.getExtras().get("MAXPRICE");
            selectedPro= (String) intent.getExtras().get("PROFESSION");
        }
    }

    private void openFilterActivity() {
        Intent myIntent = new Intent(MainActivity.this, FilterActivity.class);
        myIntent.putExtra("EMAIL",email);
        MainActivity.this.startActivity(myIntent);
        this.finish();
    }


    private double calac(int cost, double rating) {
        double costWeight = (100 /(price + rate)) * price;
        double rateWeight = (100 /(price + rate)) * rate;
        return costWeight*cost + rateWeight*rating;
    }


    private void openCalActivity() {
        Log.d("pttt", "openCalActivity: ");
        Intent myIntent = new Intent(MainActivity.this, ActivityCal.class);
//        myIntent.putExtra("EMAIL",getIntent().getExtras().getString("EMAIL"));
        MainActivity.this.startActivity(myIntent);
    }

    private void poppulatePhotoProfile() {
        email = getIntent().getExtras().getString("EMAIL");
//        String fullName = getSpecificName(email);
        FireBase.getInstance().getFromStorageByName(email, this,IMG_holder);
        emailTxt.setText(email);
//        for (Photo p : photosList) {
//            if (p.getEmail().equals(email)){
//                nameTxt.setText(p.getFullName());
//                break;
//            }
//        }
    }

    private void replaceToMapFragment() {
        ft = getSupportFragmentManager().beginTransaction();
        SPN_sort.setVisibility(View.INVISIBLE);
        BTN_filter.setVisibility(View.INVISIBLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainActivity_LAY_container,mapFragment).commit();
        if(isPopulate){
            photosList = mapFragment.getAllListPhotosMap();
            poppulatePhotoProfile();
            isPopulate=false;
        }
    }

    private void replaceToListFragment() {
        ft = getSupportFragmentManager().beginTransaction();
        SPN_sort.setVisibility(View.VISIBLE);
        BTN_filter.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainActivity_LAY_container,listFragment).commit();
        if(isPopulate){
            photosList = mapFragment.getAllListPhotosMap();
            poppulatePhotoProfile();
            isListFragment = true;
            isPopulate=false;
        }
//        listFragment.populateList(SPN_sort.getSelectedItemPosition());
//        listFragment.filter(SPN_sort.getSelectedItemPosition(),maxDist,maxPrice,minRate, emailTxt.getText().toString(),selectedPro);
        listFragment.newSortAndFilter(SPN_sort.getSelectedItemPosition(), maxDist,maxPrice,minRate, emailTxt.getText().toString(),selectedPro);
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
//        nameTxt= findViewById(R.id.accountRow_LBL_name);
        emailTxt= findViewById(R.id.accountRow_LBL_email);
        SPN_sort = findViewById(R.id.activity_main_SPN_sort);
        BTN_filter = findViewById(R.id.activity_main_BTN_filter);
//        SPN_filter = findViewById(R.id.activity_main_SPN_filter);
    }


    private void populateRoleSpinner() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.spinner_sort)) {
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                    tv.setGravity(Gravity.CENTER);
                } else {
                    tv.setTextColor(Color.WHITE);
                }
                return view;
            }

            @Override
            public boolean isEnabled(int position) {
                if (position == 0)
                    return false;
                return true;
            }
        };
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SPN_sort.setAdapter(arrayAdapter);
        SPN_sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                if(isListFragment) {
//                    checkSort();
                    listFragment.newSortAndFilter(SPN_sort.getSelectedItemPosition(), maxDist,maxPrice,minRate, emailTxt.getText().toString(),selectedPro);
                    updateMyPhoto(email, SPN_sort.getSelectedItemPosition());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void updateMyPhoto(String email, int selectedItemPosition) {
        if(photosList == null)
            return;
        Photo myPhoto= null;
        for (Photo photo1 : photosList) {
            if(photo1.getEmail().equals(email)){
                myPhoto = photo1;
                break;
            }
        }
        Log.d("pttt","There is selected items: "+ selectedItemPosition);
        double result = 0;
        switch (selectedItemPosition){
            case 1:
                result = myPhoto.getCostPrecent();
                myPhoto.setCostPrecent(result+0.1);
                FireBase.getInstance().updatePhotoInFireStore(myPhoto);
                break;
            case 2:
                result = myPhoto.getCostPrecent();
                myPhoto.setCostPrecent(result-0.1);
                FireBase.getInstance().updatePhotoInFireStore(myPhoto);
                break;
            case 3:
                result = myPhoto.getRate();
                myPhoto.setRate(result+0.1);
                FireBase.getInstance().updatePhotoInFireStore(myPhoto);
                break;
            case 4:
                result = myPhoto.getRate();
                myPhoto.setRate(result-0.1);
                FireBase.getInstance().updatePhotoInFireStore(myPhoto);
                break;
            case 5:
                result = myPhoto.getDist();
                myPhoto.setDist(result+0.1);
                FireBase.getInstance().updatePhotoInFireStore(myPhoto);
                break;
            case 6:
                result = myPhoto.getDist();
                myPhoto.setDist(result-0.1);
                FireBase.getInstance().updatePhotoInFireStore(myPhoto);
                break;
        }
    }

//    private void populateFilterSpinner() {
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.spinner_filter)) {
//            @Override
//            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//                View view = super.getDropDownView(position, convertView, parent);
//                TextView tv = (TextView) view;
//                if (position == 0) {
//                    // Set the hint text color gray
//                    tv.setTextColor(Color.GRAY);
//                    tv.setGravity(Gravity.CENTER);
//                } else {
//                    tv.setTextColor(Color.WHITE);
//                }
//                return view;
//            }
//
//            @Override
//            public boolean isEnabled(int position) {
//                if (position == 0)
//                    return false;
//                return true;
//            }
//        };
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        SPN_filter.setAdapter(arrayAdapter);
//        SPN_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
//                if(isListFragment) {
//                    checkSort();
//                    listFragment.newSortAndFilter(SPN_role.getSelectedItemPosition(), SPN_filter.getSelectedItemPosition(),emailTxt.getText().toString());
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//    }

    private void checkSort() {
//        <item>price low to high</item>
//        <item>price high to low</item>
        if(SPN_sort.getSelectedItemPosition() == 2){

        }else if (SPN_sort.getSelectedItemPosition() == 1){

        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}