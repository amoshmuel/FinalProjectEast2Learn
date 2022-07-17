package com.main.easy2learnproject.View.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.main.easy2learnproject.R;

public class ViewHolder extends RecyclerView.ViewHolder {
    private ImageView IMG_holder;
    private TextView LBL_name, LBL_email, LBL_price, LBL_rating,LBL_pro;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        initViews(itemView);
    }

    private void initViews(View itemView) {
         IMG_holder=(ImageView)itemView.findViewById(R.id.photoRow_IMG_holder);
         LBL_name =(TextView)itemView.findViewById(R.id.photoRow_LBL_title);
         LBL_email =(TextView)itemView.findViewById(R.id.photoRow_LBL_body);
         LBL_price =(TextView)itemView.findViewById(R.id.photoRow_LBL_price);
         LBL_rating =(TextView)itemView.findViewById(R.id.photoRow_LBL_rating);
         LBL_pro =(TextView)itemView.findViewById(R.id.photoRow_LBL_pro);

         itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

             }
         });
    }

    public ImageView getIMG_holder() {
        return IMG_holder;
    }

    public void setIMG_holder(ImageView IMG_holder) {
        this.IMG_holder = IMG_holder;
    }

    public TextView getLBL_name() {
        return LBL_name;
    }

    public void setLBL_name(TextView LBL_name) {
        this.LBL_name = LBL_name;
    }

    public TextView getLBL_email() {
        return LBL_email;
    }

    public void setLBL_email(TextView LBL_email) {
        this.LBL_email = LBL_email;
    }

    public TextView getLBL_price() {
        return LBL_price;
    }

    public void setLBL_price(TextView LBL_price) {
        this.LBL_price = LBL_price;
    }

    public TextView getLBL_rating() { return LBL_rating; }

    public void setLBL_rating(TextView LBL_rating) {
        this.LBL_rating = LBL_rating;
    }
    public TextView getLBL_pro() { return LBL_pro; }

    public void setLBL_pro(TextView LBL_pro) {
        this.LBL_pro = LBL_pro;
    }
}
