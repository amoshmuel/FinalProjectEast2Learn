package com.main.easy2learnproject.Model;

import java.util.Comparator;

public class PhotoComHighToLow implements Comparator<Photo> {
    @Override
    public int compare(Photo photo, Photo t1) {
        return  t1.getPricePerLesson() - photo.getPricePerLesson();
    }
}