package com.main.easy2learnproject.Model;

import java.util.Comparator;

public class PhotoComLowToHigh implements Comparator<Photo> {
    @Override
    public int compare(Photo photo, Photo t1) {
        return photo.getPricePerLesson()- t1.getPricePerLesson();
    }
}