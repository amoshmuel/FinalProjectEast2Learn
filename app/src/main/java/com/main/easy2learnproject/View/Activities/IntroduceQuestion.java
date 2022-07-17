package com.main.easy2learnproject.View.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.main.easy2learnproject.Control.FireBase;
import com.main.easy2learnproject.Model.Photo;
import com.main.easy2learnproject.R;

import java.util.ArrayList;
import java.util.Collections;

public class IntroduceQuestion extends AppCompatActivity {
    private TextView TV_select_proffession;
    private TextView TV_select_fav;
    private TextView TV_SeekBar_cost_rating;
    private TextView TV_SeekBar_distance_rating;
    private TextView TV_SeekBar_rating_rating;
    private String chosenPro, pro;
    int selectedItem = -1;
    int selectedFav = -1;
    ArrayList<Integer> proffesion_list = new ArrayList<>();
    String [] proffession_array = {"phisics","math","english","history","civics","bible","grammar"};
    String [] fav_array = {"Lessons for long time","bosst befor exam"};
    private boolean [] selected_proffession = new boolean[proffession_array.length];
    private SeekBar SB_boveable;
    private SeekBar SB_price;
    private SeekBar SB_rating;
    private Button BTN_submit;
    private Photo p = new Photo();
    private String email,pass,name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("amos","im in Activity");
        System.out.println("im in Activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce_question);
        getSupportActionBar().hide();
        findViews();
        initViews();
//        populateRoleSpinner();
    }

    private void initViews() {
        TV_select_proffession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                init alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        IntroduceQuestion.this
                );
//                set title
                builder.setTitle("Select");
//                set dialog non cancel able
                builder.setCancelable(false);
                builder.setSingleChoiceItems(proffession_array, selectedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedItem = Integer.valueOf(i);
                    }
                });
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String stringBuilder = "";
                        stringBuilder = proffession_array[selectedItem];
//                      set text on TV
                        chosenPro = stringBuilder;
                        pro = stringBuilder;
                        TV_select_proffession.setText(stringBuilder);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dismiss dialog
                        dialogInterface.dismiss();
                    }
                });
//                show dialog
                builder.show();
            }
        });

        TV_select_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                init alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        IntroduceQuestion.this
                );
//                set title
                builder.setTitle("Select");
//                set dialog non cancel able
                builder.setCancelable(false);
                builder.setSingleChoiceItems(fav_array, selectedFav, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedFav = Integer.valueOf(i);
                    }
                });
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String stringBuilder = "";
                        stringBuilder = fav_array[selectedFav];
//                      set text on TV
                        TV_select_fav.setText(stringBuilder);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dismiss dialog
                        dialogInterface.dismiss();
                    }
                });
//                show dialog
                builder.show();
            }
        });




        SB_boveable.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                TV_SeekBar_cost_rating.setText("how you moveable?:      "+ String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        SB_price.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                TV_SeekBar_distance_rating.setText("How the price is important:      "+ String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        SB_rating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                TV_SeekBar_rating_rating.setText("How is the rating:      "+ String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        BTN_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForVaildInput();
            }
        });

    }

    private void checkForVaildInput() {

        if(selectedItem==-1) {
            TV_select_proffession.setText("must choose teacher proffesiom");
            TV_select_proffession.setTextColor(Color.red(5));
            return;
        }else if(selectedFav == -1) {
            TV_select_proffession.setText("must choose favorite learning type");
            TV_select_proffession.setTextColor(Color.red(5));
        }else{
            calculateWeight();
            openMainActivity();
        }
    }

    private void openMainActivity() {
        Intent myIntent = new Intent(IntroduceQuestion.this, SignupActivity.class);
//        p = check();
//        updatePhoto();
        myIntent.putExtra("pro",(chosenPro));
        myIntent.putExtra("price",(SB_price.getProgress()));
        myIntent.putExtra("dist",(SB_boveable.getProgress()));
        myIntent.putExtra("rate",(SB_rating.getProgress()));
        myIntent.putExtra("fav",(selectedFav));
        IntroduceQuestion.this.startActivity(myIntent);
        this.finish();

    }

    private void updatePhoto() {
        p.setCostPrecent(SB_price.getProgress());
        p.setDist(SB_boveable.getProgress());
        p.setRate(SB_rating.getProgress());
        FireBase.getInstance().updatePhotoInFireStore(p);
    }

    private float calculateWeight() {
//        double price = (SB_price.getProgress() / 200) * 10; //200 is the max price for lesson
//        double dist = (1-(SB_boveable.getProgress() / 20)) * 10; //20km is the max distance for teacher you can found, we want the lowest distance.
//        double rating = (SB_rating.getProgress() /5)*10; //the top rating is 5
        float weight = SB_rating.getProgress() + SB_price.getProgress() + SB_boveable.getProgress();
        return weight;
    }


//    private void populateRoleSpinner() {
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.spinner_proffesion))
//        {
//            @Override
//            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//                View view = super.getDropDownView(position, convertView, parent);
//                TextView tv = (TextView) view;
//                if (position == 0) {
//                    // Set the hint text color gray
//                    tv.setTextColor(Color.GRAY);
//                    tv.setGravity(Gravity.CENTER);
//                } else {
//                    tv.setTextColor(Color.BLACK);
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
//        SPN_proffession.setAdapter(arrayAdapter);
//        SPN_proffession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//    }


    private void findViews() {
        TV_select_proffession = findViewById(R.id.activity_question_TV_select_proffession);
        SB_boveable = findViewById(R.id.activity_question_SeekBar_cost);
        SB_price = findViewById(R.id.activity_question_SeekBar_distance);
        SB_rating = findViewById(R.id.activity_question_SeekBar_rating);
        TV_SeekBar_cost_rating = findViewById(R.id.activity_question_TV_SeekBar_cost_rating);
        TV_SeekBar_distance_rating = findViewById(R.id.activity_question_TV_SeekBar_distance_rating);
        TV_SeekBar_rating_rating = findViewById(R.id.activity_question_TV_SeekBar_rating_rating);
        BTN_submit = findViewById(R.id.activity_question_BTN_submit);
        TV_select_fav = findViewById(R.id.activity_question_TV_select_long_lesson);
    }
}