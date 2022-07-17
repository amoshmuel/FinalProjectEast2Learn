package com.main.easy2learnproject.View.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.main.easy2learnproject.Control.FireBase;
import com.main.easy2learnproject.Model.Photo;
import com.main.easy2learnproject.View.Activities.LoginActivity;
import com.main.easy2learnproject.View.Activities.PhotoActivity;
import com.main.easy2learnproject.R;
import com.main.easy2learnproject.View.Fragments.ListFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CustomAdapter extends RecyclerView.Adapter<ViewHolder> {
    private ArrayList<Photo> localDataSet;
    private Context context;
    private String profileType;
    private String email;



    public CustomAdapter(ArrayList<Photo> list, Context context, String profileType,String email) {
        this.email = email;
        localDataSet=list;
        this.profileType = profileType;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.photo_recycler_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if((localDataSet.get(position).getImageId()!=null)){
            FireBase.getInstance().downloadStorageData(localDataSet.get(position).getImageId(),context,holder.getIMG_holder());
        }
        Log.d("pttt", "onViewHolder: "+localDataSet.get(position).getFullName()+localDataSet.get(position).getImageId()+
                localDataSet.get(position).getPricePerLesson() + localDataSet.get(position).getRate());
        holder.getLBL_name().setText(localDataSet.get(position).getFullName());
        holder.getLBL_email().setText(localDataSet.get(position).getEmail());
        holder.getLBL_price().setText("Price: "+String.valueOf(localDataSet.get(position).getPricePerLesson()));
        holder.getLBL_rating().setText("Rating: "+String.valueOf(localDataSet.get(position).getRate()));
        holder.getLBL_pro().setText("Proffession: "+(localDataSet.get(position).getPro()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("pttt", "onClick: "+localDataSet.get(position).getFullName()+localDataSet.get(position).getImageId());
                if(profileType.equals("Student"))
                    openPhotoActivity(localDataSet.get(position));
                else
                    Toast.makeText(context,"Only Student Can Rate",Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void openPhotoActivity(Photo photo) {
        Log.d("pttt", "openPhotoActivity: ");
        Intent myIntent = new Intent(context, PhotoActivity.class);
        myIntent.putExtra("photo", photo);
        myIntent.putExtra("email", email);
        context.startActivity(myIntent);
    }

    @Override
    public int getItemCount() {
        Log.d("pttt", "getItemCount: ");
        return localDataSet.size();
    }

}
