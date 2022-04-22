package com.main.easy2learnproject.View.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.main.easy2learnproject.Control.FireBase;
import com.main.easy2learnproject.Control.OnImageSaveListener;
import com.main.easy2learnproject.Control.Permission;
import com.main.easy2learnproject.Model.Photo;
import com.main.easy2learnproject.R;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    //views
    private Photo photo = null;
    private Button BTN_submit;
    private TextInputLayout EDT_email;
    private TextInputLayout EDT_password;
    private TextInputLayout EDT_confirm_password;
    private TextInputLayout EDT_fullName;
    private ImageView IMG_addImage;
    private Bitmap imageBitmap = null;
    private Spinner SPN_role;
    private static final int TAKE_PHOTO_REQUEST_CODE = 0;
    private static final int CHOOSE_FROM_GALLERY_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("pttt", "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        findViews();
        populateRoleSpinner();
        initViews();
    }
    private void initViews() {
        Log.d("pttt", "initViews: ");
//        if (photo != null)
//            updateViews();

        BTN_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForVaildInput();
            }
        });


        IMG_addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("pttt", "onClick: IMG_addImage");
                if(photo !=null)
                    Toast.makeText(SignupActivity.this,"Image Uneditable !",Toast.LENGTH_SHORT).show();
                else
                    openCameraDialog();
            }
        });

    }



    private void populateRoleSpinner() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.spinner_role))
        {
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                    tv.setGravity(Gravity.CENTER);
                } else {
                    tv.setTextColor(Color.BLACK);
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
        SPN_role.setAdapter(arrayAdapter);
        SPN_role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void checkForVaildInput() {
        if(!validateLetters(EDT_fullName.getEditText().toString())){
            EDT_fullName.setError("Enter first and last name at least 2 letters");
            BTN_submit.setClickable(true);
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(EDT_email.getEditText().getText().toString()).matches()) {
            Log.d("pttt", "checkForValidInputs: Email invalid");
            EDT_email.setError("pleas enter current email");
            BTN_submit.setClickable(true);
            return;
        }
        if(EDT_password.getEditText().getText().toString().length()<6 || EDT_password.getEditText().getText().toString().equals(" ")) {
            EDT_password.setError("password must contain at least 6 digits .");
            BTN_submit.setClickable(true);
            return;
        }
        if(EDT_password.getEditText().getText().toString().compareTo(EDT_confirm_password.getEditText().getText().toString())!=0) {
            EDT_confirm_password.setError("passwords don't match !");
            BTN_submit.setClickable(true);
            return;
        }
        if(SPN_role.getSelectedItem().toString().equals("Choose Your Role")) {
            Toast.makeText(SignupActivity.this, "Must Choose Role", Toast.LENGTH_SHORT).show();
            return;
        }

        if(imageBitmap == null){
            Toast.makeText(SignupActivity.this,"must upload a photo", Toast.LENGTH_SHORT).show();
            return;
        }
        checkImageForPhoto(EDT_email.getEditText().getText().toString());
        registerToCloudUser();
    }

    private void registerToCloudUser() {
        FireBase.getInstance().
                addUserByEmailAndPassword(EDT_email.getEditText().getText().toString(),
                        EDT_password.getEditText().getText().toString(), SignupActivity.this);
        Toast.makeText(SignupActivity.this,"Register Successed",Toast.LENGTH_SHORT).show();
        Toast.makeText(SignupActivity.this,"Welcome",Toast.LENGTH_SHORT).show();
        this.finish();
    }

    private void findViews() {
        BTN_submit =findViewById(R.id.activity_signup_BTN_submit);
        EDT_email =findViewById(R.id.activity_signup_LBL_email);
        EDT_password =findViewById(R.id.activity_signup_LBL_password);
        EDT_confirm_password =findViewById(R.id.activity_signup_LBL_password_confirm);
        EDT_fullName =findViewById(R.id.activity_signup_LBL_fullName);
        SPN_role =findViewById(R.id.activity_signup_SPN_role);
        IMG_addImage =(ImageView)findViewById(R.id.signup_IMG_addImage);

    }

    private void openCameraDialog() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Log.d("pttt", "onClick: Permission granted");
            chooseImageDialog();
        } else {
            Log.d("TODO", "openCameraPermissionDialog: ");
        }
    }


    public void chooseImageDialog() {
        Log.d("Pttt", "chooseImage: ");
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit"}; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // set the items in builder
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (optionsMenu[i].equals("Take Photo")) {
                    // Open the camera and get the photo
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, TAKE_PHOTO_REQUEST_CODE);
                } else if (optionsMenu[i].equals("Choose from Gallery")) {
                    // choose from  external storage
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, CHOOSE_FROM_GALLERY_REQUEST_CODE);
                } else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == TAKE_PHOTO_REQUEST_CODE) && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            IMG_addImage.setImageBitmap(imageBitmap);
        } else if ((requestCode == CHOOSE_FROM_GALLERY_REQUEST_CODE) && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                IMG_addImage.setImageBitmap(imageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean validateLetters(String txt) {

        String regx = "(^[A-Za-z]{3,16})([ ]{0,1})([A-Za-z]{3,16})?([ ]{0,1})?([A-Za-z]{3,16})?([ ]{0,1})?([A-Za-z]{3,16})";
        Pattern pattern = Pattern.compile(regx,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(txt);
        return matcher.find();

    }


    private void checkImageForPhoto(String email) {
        Log.d("pttt", "checkImageForPhoto: ");
        if (photo == null || photo.getImageId() == null) {
            FireBase.getInstance().uploadImageToCloud(email,imageBitmap, new OnImageSaveListener() {
                @Override
                public void imageSaved(String imageId) {
                    updatePhoto(imageId);
                }
            });
        } else
            updatePhoto(photo.getImageId());
    }


    private void updatePhoto(String imageId) {
        Log.d("pttt", "updatePhoto: ");
        boolean flag = false;
        if (photo == null) {
            photo = new Photo();
            flag = true;
        }
        photo.setDate(getDateString());
        photo.setImageId(imageId);
        photo.setFullName(EDT_fullName.getEditText().getText().toString());
        photo.setEmail(EDT_email.getEditText().getText().toString());
        photo.setUserId(FireBase.getInstance().getCurrentUser().getUid());
        if (flag) {
            updateLocation();
        }else
            FireBase.getInstance().updatePhotoInFireStore(photo);
        this.finish();
    }


//    private void updateViews() {
//        if (photo.getImageId() != null) {
//            FireBase.getInstance().downloadStorageData(photo.getImageId(), this, IMG_addImage);
//        }
//    }


    public String getDateString() {
        String pattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        Date today = Calendar.getInstance().getTime();
        String todayAsString = df.format(today);
        return todayAsString;
    }


    private void updateLocation() {
        Log.d("pttt", "updateLocation: ");
        Permission permission = new Permission(this);
        if (!permission.checkForFineLocationPermission() ||!permission.checkForFineLocationPermission()) {
            permission.requestForLocation();
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Log.d("pttt", "onSuccess: "+location.getLongitude());
                            photo.setLat(location.getLatitude());
                            photo.setLon(location.getLongitude());
                            FireBase.getInstance().addPhotoInFireStore(photo);
                        }
                    }
                });
    }

}