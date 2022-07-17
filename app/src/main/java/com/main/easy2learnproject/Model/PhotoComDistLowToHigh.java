package com.main.easy2learnproject.Model;

import java.util.Comparator;

public class PhotoComDistLowToHigh  implements Comparator<Photo> {
    @Override
    public int compare(Photo photo, Photo t1) {
        return Double.compare(photo.getDist(), t1.getDist());
    }
}
