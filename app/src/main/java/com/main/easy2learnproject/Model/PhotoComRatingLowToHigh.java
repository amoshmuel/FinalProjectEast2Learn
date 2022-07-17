package com.main.easy2learnproject.Model;

import java.util.Comparator;

public class PhotoComRatingLowToHigh implements Comparator<Photo> {
    @Override
    public int compare(Photo photo, Photo t1) {
        return (Double.compare(photo.getRate(),t1.getRate()));
    }
}
