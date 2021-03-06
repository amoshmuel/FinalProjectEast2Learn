package com.main.easy2learnproject.View.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.main.easy2learnproject.Control.FireBase;
import com.main.easy2learnproject.Control.OnSuccsessCallBack;
import com.main.easy2learnproject.Control.Permission;
import com.main.easy2learnproject.Model.Photo;
import com.main.easy2learnproject.Control.OnImageSaveListener;
import com.main.easy2learnproject.R;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PhotoActivity extends AppCompatActivity {

    private ImageView IMG_addImage;
//    private TextInputLayout LBL_title, LBL_body;
    private TextView fullName, price, contact;
    private RatingBar ratingBar;
    private TextView textViewShowDate;
    private TextView textView;
    private int rate;
    private ImageView IMG_logout;
//    private TextInputLayout dateTextView;
//    private TextInputLayout timeTextView;
    private Button BTN_save, BTN_delete;
    private Bitmap imageBitmap = null;
    private static final int TAKE_PHOTO_REQUEST_CODE = 0;
    private static final int CHOOSE_FROM_GALLERY_REQUEST_CODE = 1;
    private Photo photo = null;
    private FusedLocationProviderClient fusedLocationClient;
    private String dateTxt = "-1";
    private String timeTxt = "-1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            photo = bundle.getParcelable("photo");
//            Log.d("pttt", "onCreate: PHOTO ID IMG " + photo.getImageId() + " " + photo.getBody() + photo.getDate());
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        setContentView(R.layout.activity_photo);
        findViews();
        initViews();
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

    private void initViews() {
//        textView.setText(photo.getFullName());

        BTN_save.setClickable(true);
//        LBL_date.setClickable(false);
//        ETXT_date.setClickable(false);
        if (photo != null)
            updateViews();
//        else {
//            LBL_date.getEditText().setText(getDateString());
//        }
        BTN_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkForValidInput();
            }
        });

        ratingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rate = ((int) ratingBar.getRating());
            }
        });

        IMG_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogoutDialog();
            }
        });
//        BTN_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(photo !=null && photo.getUserId().equals(FireBase.getInstance().getCurrentUser().getUid())){
//                    FireBase.getInstance().deleteFromStorage(photo.getImageId(), new OnSuccsessCallBack() {
//                        @Override
//                        public void isSuccess(boolean b) {
//                            if(b)
//                                if(dateTxt.equals("-1")||timeTxt.equals("-1")){
//                                    Toast.makeText(PhotoActivity.this,"Enter date and time",Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//
//                                FireBase.getInstance().deleteFromFireStore(photo.getPhotoNumber());
//                            PhotoActivity.this.finish();
//                        }
//                    });
//                }else if(photo.getUserId()!=FireBase.getInstance().getCurrentUser().getUid()){
//                    Toast.makeText(PhotoActivity.this,"Delete Only Photo You Upload",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        IMG_addImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("pttt", "onClick: IMG_addImage");
//                if(photo !=null)
//                    Toast.makeText(PhotoActivity.this,"Image Uneditable !",Toast.LENGTH_SHORT).show();
//                else
//                    openCameraDialog();
//            }
//        });
        populatePhoto();
//        dateTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                handleDateButton();
//            }
//        });

//        timeTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                handleTimeButton();
//            }
//        });

    }

    private void openLogoutDialog() {
        Intent myIntent = new Intent(PhotoActivity.this, MainActivity.class);
        myIntent.setAction("photoActivity");
        myIntent.putExtra("EMAIL",getIntent().getExtras().get("email").toString());
        PhotoActivity.this.startActivity(myIntent);
        PhotoActivity.this.finish();
    }

    private void populatePhoto() {
        String email = getIntent().getExtras().getString("EMAIL");
        FireBase.getInstance().getFromStorageByName(email, this,IMG_addImage);
    }

    private void updateViews() {
//        LBL_date.getEditText().setText(photo.getDate());
//        LBL_title.getEditText().setText(photo.getFullName());
//        LBL_body.getEditText().setText(photo.getEmail());
        fullName.setText("Teacher name: \t" + photo.getFullName());
        price.setText("Price for lesson: \t" + String.valueOf(photo.getPricePerLesson()));
        contact.setText("Contact info: \t" + photo.getEmail());
        if (photo.getImageId() != null) {
            FireBase.getInstance().downloadStorageData(photo.getImageId(), this, IMG_addImage);
        }
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

    public String getDateString() {
        String pattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        Date today = Calendar.getInstance().getTime();
        String todayAsString = df.format(today);
        todayAsString = dateTxt+"\t"+timeTxt;
        return todayAsString;
    }

    private void checkForValidInput() {
//        if (LBL_title.getEditText().getText().toString().trim().length() == 0) {
//            Log.d("pttt", "checkForValidInput:title can't be null! ");
//            LBL_title.getEditText().setError("title can't be null!");
//            return;
//        }
//        if (LBL_body.getEditText().getText().toString().trim().length() == 0) {
//            Log.d("pttt", "checkForValidInput: title can't be null!");
//            LBL_body.getEditText().setError("body can't be null!");
//            return;
//        }
        BTN_save.setClickable(false);
        checkImageForPhoto();


    }

    private void checkImageForPhoto() {
        Log.d("pttt", "checkImageForPhoto: ");
        if (photo == null || photo.getImageId() == null) {
            FireBase.getInstance().uploadImageToCloud(photo.getEmail(), imageBitmap, new OnImageSaveListener() {
                @Override
                public void imageSaved(String imageId) {
                    updatePhoto(imageId);
                }
            });
        } else
            updatePhoto(photo.getImageId());
    }

//    private void addLessomToStore() {
//        lesson = new Lesson();
//        lesson.setDate(getDateString());
//        lesson.setTitle(LBL_title.getEditText().toString());
//        lesson.setBody(LBL_body.getEditText().toString());
//        lesson.setUserId(FireBase.getInstance().getCurrentUser().getUid());
//        FireBase.getInstance().updateLessonInFireStore(lesson);
//        this.finish();
//
//    }

    private void updatePhoto(String imageId) {
        Log.d("pttt", "updatePhoto: ");
//        boolean flag = false;
//        if (photo == null) {
//            photo = new Photo();
//            flag = true;
//        }
        photo.setNumOfRate(photo.getNumOfRate()+1);

        photo.setRate(Double.valueOf(new DecimalFormat("##.##").format(((ratingBar.getRating()+(photo.getRate()*(photo.getNumOfRate()-1)))/photo.getNumOfRate()))));
//        photo.setDate(textViewShowDate.getText().toString());
//        photo.setImageId(imageId);
//        photo.setFullName(LBL_title.getEditText().getText().toString());
//        photo.setEmail(LBL_body.getEditText().getText().toString());
//        photo.setUserId(FireBase.getInstance().getCurrentUser().getUid());
//        if (flag) {
//            updateLocation();
//        }else
        FireBase.getInstance().updatePhotoInFireStore(photo);
        this.finish();
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

    private void findViews() {
        IMG_addImage =(ImageView)findViewById(R.id.photoActivity_IMG_addImage);
        fullName = findViewById(R.id.activity_photo_TXV_full_name);
        price = findViewById(R.id.activity_photo_TXV_price);
        ratingBar = findViewById(R.id.activity_photo_RB_rating);
        IMG_logout = findViewById(R.id.photoActivity_IMG_back);
        contact = findViewById(R.id.activity_photo_TXV_contact);
//        LBL_title = (TextInputLayout)findViewById(R.id.photoActivity_LBL_title);
//        LBL_body = (TextInputLayout)findViewById(R.id.photoActivity_LBL_body);
//        LBL_date = (TextInputLayout)findViewById(R.id.photoActivity_LBL_date);
        BTN_save =(Button)findViewById(R.id.photoActivity_BTN_save);
//        BTN_delete =(Button)findViewById(R.id.photoActivity_BTN_delete);
//        dateTextView =(TextInputLayout) findViewById(R.id.photoActivity_dateTextView);
//        timeTextView =(TextInputLayout) findViewById(R.id.photoActivity_timeTextView);
        textViewShowDate = findViewById(R.id.photoActivity_textViewShowDate);
//        textView = findViewById(R.id.activity_photo_textView);

    }



    private void handleDateButton() {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, date);
                dateTxt = date+"."+month+"."+year;
//                dateTextView.setText(dateText);
                textViewShowDate.setText(getDateString());
            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();




    }

    private void handleTimeButton() {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
//        boolean is24HourFormat = DateFormat.is24HourFormat(this);
        boolean is24HourFormat = true;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
//                Log.i(TAG, "onTimeSet: " + hour + minute);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR, hour);
                calendar1.set(Calendar.MINUTE, minute);
//                String dateText = DateFormat.format("h:mm a", calendar1).toString();

//                timeTextView.setText(hour + ":" + minute);
                timeTxt = hour + ":" + minute;
                textViewShowDate.setText(getDateString());

            }
        }, HOUR, MINUTE, is24HourFormat);

        timePickerDialog.show();

    }



}