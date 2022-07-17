package com.main.easy2learnproject.View.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.main.easy2learnproject.R;

public class FilterActivity extends AppCompatActivity {

    private SeekBar SB_dist;
    private SeekBar SB_price;
    private SeekBar SB_rating;
    private TextView TV_SeekBar_rating;
    private TextView TV_SeekBar_price;
    private TextView TV_SeekBar_dist;
    private Button BTN_submit;
    private TextView TV_select_proffession;
    int selectedItem = -1;
    String [] proffession_array = {"phisics","math","english","history","civics","bible","grammar"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        findViews();
        initViews();
    }

    private void initViews(){
        SB_rating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                TV_SeekBar_rating.setText("Select the min rate for teacher: "+ String.valueOf(i));
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
                TV_SeekBar_price.setText("Select the max price for lesson: "+ String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        SB_dist.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                TV_SeekBar_dist.setText("Select the max distance from teacher: "+ String.valueOf(i));
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
            public void onClick(View view) {
                backToMainActivity();
            }
        });

        TV_select_proffession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                init alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        FilterActivity.this
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
    }

    private void backToMainActivity() {
        String email = getIntent().getExtras().getString("EMAIL");
        Intent myIntent = new Intent(FilterActivity.this, MainActivity.class);
        myIntent.putExtra("MAXPRICE",SB_price.getProgress());
        myIntent.putExtra("MAXDIST",SB_dist.getProgress());
        myIntent.putExtra("MINRATE",SB_rating.getProgress());
        myIntent.putExtra("PROFESSION",TV_select_proffession.getText().toString());
        myIntent.putExtra("EMAIL",email);
        myIntent.setAction("filter");
        FilterActivity.this.startActivity(myIntent);
        this.finish();
    }

    private void findViews() {
        SB_price = findViewById(R.id.activity_filter_SeekBar_price);
        SB_dist = findViewById(R.id.activity_filter_SeekBar_dist);
        SB_rating = findViewById(R.id.activity_filter_SeekBar_rating);
        TV_SeekBar_rating = findViewById(R.id.activity_filter_TV_SeekBar_title_rating);
        TV_SeekBar_dist = findViewById(R.id.activity_filter_TV_SeekBar_title_dist);
        TV_SeekBar_price = findViewById(R.id.activity_filter_TV_SeekBar_title_price);
        BTN_submit = findViewById(R.id.activity_filter_BTN_submit);
        TV_select_proffession = findViewById(R.id.activity_filter_TV_select_proffession);

    }
}