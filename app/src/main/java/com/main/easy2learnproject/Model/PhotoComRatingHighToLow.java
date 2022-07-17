package com.main.easy2learnproject.Model;

import java.util.Comparator;

public class PhotoComRatingHighToLow implements Comparator<Photo> {
    @Override
    public int compare(Photo photo, Photo t1) {
        return (Double.compare(t1.getRate(),photo.getRate()));
    }
}