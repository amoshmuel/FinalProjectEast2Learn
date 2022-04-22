package com.main.easy2learnproject.View.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.main.easy2learnproject.Model.Photo;
import com.main.easy2learnproject.View.Adapters.CustomAdapter;
import com.main.easy2learnproject.Control.FireBase;
import com.main.easy2learnproject.Control.GetListListener;
import com.main.easy2learnproject.Model.PhotoComparator;
import com.main.easy2learnproject.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private TextView textView;
    private CustomAdapter adapter;
    private ArrayList<Photo> photosList;
    public ListFragment() {
        photosList =new ArrayList<Photo>();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateList();
        Log.d("pttt", "onCreate: listFrag ");
        onDataChangeListener();
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("pttt", "onCreateView: listFrag");
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list_fragment_LST_recyclerview);
        textView=(TextView)rootView.findViewById(R.id.list_fragment_LBL);
        return rootView;
    }

    public void populateList(){
        Log.d("pttt", "populateList: ");
        FireBase.getInstance().
        getListItems(new GetListListener() {
            @Override
            public void getList (List<Photo> list) {
                if(list == null)
                    return;
                Log.d("pttt", "getList:SIZE" + list.size());
                photosList.removeAll(photosList);
                photosList.addAll(list);
                if(list.size() >= 2)
                    Collections.sort(photosList, new PhotoComparator());
                if(photosList.size()>0) {
                    textView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    populateRecycleList();
                }else{
                    textView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }

        }
    });
}
    private void onDataChangeListener(){
        Log.d("pttt", "data: ");
        FireBase.getInstance().getPhotoCollection().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                Log.d("pttt", "onEvent: ");
                populateList();
            }
        });
    }
    private void populateRecycleList() {
        Log.d("pttt", "populateRecycleList: ");
        adapter  = new CustomAdapter(photosList,getContext());
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(adapter);
    }
}