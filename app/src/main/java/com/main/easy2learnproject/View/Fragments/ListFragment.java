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
import com.main.easy2learnproject.Model.PhotoComDistHighToLow;
import com.main.easy2learnproject.Model.PhotoComDistLowToHigh;
import com.main.easy2learnproject.Model.PhotoComHighToLow;
import com.main.easy2learnproject.Model.PhotoComLowToHigh;
import com.main.easy2learnproject.Model.PhotoComRatingHighToLow;
import com.main.easy2learnproject.Model.PhotoComRatingLowToHigh;
import com.main.easy2learnproject.View.Adapters.CustomAdapter;
import com.main.easy2learnproject.Control.FireBase;
import com.main.easy2learnproject.Control.GetListListener;
import com.main.easy2learnproject.Model.PhotoComparator;
import com.main.easy2learnproject.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class ListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private boolean createList = false;
    private TextView textView;
    private CustomAdapter adapter;
    private String email;
    private String profileType;
    private ArrayList<Photo> photosList;
    private ArrayList<Comparator<Photo>> comp = new ArrayList<>();

    public ListFragment(String email) {
        photosList = new ArrayList<Photo>();
        this.email = email;
        comp.add(new PhotoComparator());
        comp.add(new PhotoComLowToHigh());
        comp.add(new PhotoComHighToLow());
        comp.add(new PhotoComRatingLowToHigh());
        comp.add(new PhotoComRatingHighToLow());
        comp.add(new PhotoComDistLowToHigh());
        comp.add(new PhotoComDistHighToLow());
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
        textView = (TextView) rootView.findViewById(R.id.list_fragment_LBL);
        return rootView;
    }


    public void newSortAndFilter(int index, int maxDist, int maxPrice, int minRate, String email, String pro) {
        FireBase.getInstance().
                getListItems(new GetListListener() {
                    @Override
                    public String getList(List<Photo> list) {
                        if (list == null)
                            return null;
                        Log.d("pttt", "getList:SIZE" + list.size());
                        photosList.removeAll(photosList);
                        double distPrec = 0;
                        double costPrec = 0;
                        double ratePrec= 0;
                        double myLat = 0, myLon = 0;
                        String newEmail = "";
                        String myPro = "";
//                        photosList.addAll(list);
                        //teacher doesnt get recommend filter
                        for (Photo onePhoto : list) {
                            if (onePhoto.getprofileType().equals("Teacher")){
                                photosList.add(onePhoto);
                                System.out.println();
                            }else if(onePhoto.getEmail().equals(email)){
                                newEmail = onePhoto.getEmail();
                                myPro = onePhoto.getPro();
                                ratePrec = onePhoto.getRate();
                                distPrec = onePhoto.getDist();
                                costPrec = onePhoto.getCostPrecent();
                                myLat = onePhoto.getLat();
                                myLon = onePhoto.getLon();
                            }
                        }

//                        <item>SORT</item>
//                        <item>Price low to high</item>
//                        <item>Price high to low</item>
//                        <item>Rating low to high</item>
//                        <item>Price high to low</item>
//                        <item>Dist low to high</item>
//                        <item>Dist high to low</item>
//                        <item>Recommended</item>
//

                        System.out.println("AMOS: \n eaneil:\t"+newEmail+"\nratePrec:\t" +ratePrec+"\ndistPrec\t" +distPrec+"\ncostPrec:\t" + costPrec);
                        //cal weight per theacher
                        for (Photo photo : photosList) {
                            photo.setDist(calaDist(myLat,myLon, photo.getLat(), photo.getLon())*10);
                            FireBase.getInstance().updatePhotoInFireStore(photo);
                        }


                        if(index == 7) {
                            photosList = createRecFilter(ratePrec, distPrec, costPrec, myLat, myLon, photosList,myPro);
                        }
                        else {
                            photosList = createFilter(myLat, myLon, photosList, maxDist, maxPrice, minRate, pro);
                        }
                        int newIndex = 0;
                        if(index != -1) {
                            newIndex = index;
                        }

                        if (list.size() >= 2 && index != 7)
                            Collections.sort(photosList, comp.get(newIndex));
                        if (photosList.size() > 0) {
                            textView.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            populateRecycleList();
                        } else {
                            textView.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                        }
                        createList = true;
                        return null;
                    }
                });
    }

    private ArrayList<Photo> createFilter(double myLat, double myLon, ArrayList<Photo> photosList, int maxDist, int maxPrice, int minRate, String pro) {
        ArrayList<Photo> photoArrayList = new ArrayList<>();
        for (Photo photo : photosList) {
//            calaDist(photo.getLat(),photo.getLon(),myLat,myLon)>maxDist
//            ||  !photo.getPro().equals(pro)
            if(pro.equals("")){
                if(photo.getPricePerLesson()>maxPrice || photo.getRate()<minRate ) {
                    photoArrayList.add(photo);
                }
            }else if(photo.getPricePerLesson()>maxPrice || photo.getRate()<minRate ||  !photo.getPro().equals(pro) ) {
                photoArrayList.add(photo);
            }
        }
        photosList.removeAll(photoArrayList);
        return photosList;

    }

    private ArrayList<Photo> createRecFilter(double ratePrec, double distPrec, double costPrec, double myLat,
                                             double myLon, ArrayList<Photo> photosList, String pro) {
        HashMap<Double,Photo> photoHashMap = new HashMap<>();
        ArrayList<Double> doubles = new ArrayList<>();
        ArrayList<Photo> removePhoto = new ArrayList<>();
        for (Photo photo : photosList) {
//            double distance = calaDist(photo.getLat(),photo.getLon(),myLat,myLon);
//            photo.setDist(distance);
//            photo.getDist() > 20 ||
            if(!photo.getPro().equals(pro)){
                continue;
            }
            double weight = calcWeight(ratePrec,distPrec,costPrec,myLat,myLon,photo);
            photoHashMap.put(weight,photo);
            doubles.add(weight);
            Collections.sort(doubles);
        }
        photosList.removeAll(photosList);
        for (Double aDouble : doubles) {
            photosList.add(photoHashMap.get(aDouble));
        }
        return photosList;
    }

    private Double calcWeight(double ratePrecent, double distPrecent, double costPrecent, double myLat, double myLon, Photo photo) {
        double rateNew = ((ratePrecent + distPrecent + costPrecent)/100) * ratePrecent;
        double distNew = ((ratePrecent + distPrecent + costPrecent)/100) * distPrecent;
        double costNew = ((ratePrecent + distPrecent + costPrecent)/100) * costPrecent;
        return (double) (photo.getRate()/5)*rateNew + (calaDist(photo.getLat(),photo.getLon(),myLat,myLon)/20)*distNew +
                (photo.getPricePerLesson()/200)*costNew;
    }

    private double calaDist(double lat, double lon, double myLat, double myLon) {
        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        myLon = Math.toRadians(myLon);
        lon = Math.toRadians(lon);
        lat = Math.toRadians(lat);
        myLat = Math.toRadians(myLat);

        // Haversine formula
        double dlon = lon - myLon;
        double dlat = lat - myLat;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat) * Math.cos(myLat)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return(c * r)/1000;
    }

    public void populateList() {
        Log.d("pttt", "populateList: ");
        FireBase.getInstance().
                getListItems(new GetListListener() {
                    @Override
                    public String getList(List<Photo> list) {
                        if (list == null || createList) {
                            return null;
                        }
                        Log.d("pttt", "getList:SIZE" + list.size());
                        photosList.removeAll(photosList);
//                        photosList.addAll(list);
                        for (Photo onePhoto : list) {
                            if (onePhoto.getprofileType().equals("Teacher")){
                                photosList.add(onePhoto);
                            }if(onePhoto.getEmail().equals(email))
                                profileType = onePhoto.getprofileType();
                        }
                        if (list.size() >= 2)
                            Collections.sort(photosList, comp.get(0));
                        if (photosList.size() > 0) {
                            textView.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            populateRecycleList();
                        } else {
                            textView.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                        }
                        return null;
                    }
                });
    }

    private void onDataChangeListener() {
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
        adapter = new CustomAdapter(photosList, getContext(),profileType,email);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(adapter);
    }

    public void filter(int selectedItemPosition, int maxDist, int maxPrice, int minRate, String toString, String selectedPro) {

    }
}