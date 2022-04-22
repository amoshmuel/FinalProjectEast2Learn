package com.main.easy2learnproject.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Lesson implements Parcelable {
    private String date;
    private String title;
    private String body;
    private String loc;
    private String userId;
    private String lessonNumber;

    public Lesson() {

    }



    public Lesson(String title, String body, String date, String userId) {
        super();
        this.title = title;
        this.body = body;
        this.date = date;
        this.userId = userId;
    }
    public static final Parcelable.Creator<Lesson> CREATOR
            = new Parcelable.Creator<Lesson>() {
        public Lesson createFromParcel(Parcel in) {
            return new Lesson(in);
        }
        public Lesson[] newArray(int size) {
            return new Lesson[size];
        }
    };

    protected Lesson(Parcel in) {
        this.title = in.readString();
        this.body = in.readString();
        this.date = in.readString();
        this.userId = in.readString();
        lessonNumber =in.readString();

    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(body);
        parcel.writeString(date);
        parcel.writeString(userId);
        parcel.writeString(lessonNumber);

    }


//    public double getLat() {
//        return lat;
//    }

//    public void setLat(double lat) {
//        this.lat = lat;
//    }

//    public double getLon() {
//        return lon;
//    }

//    public void setLon(double lon) {
//        this.lon = lon;
//    }

    public void setDate(String date) {
        this.date = date;
    }


//    public String getPhotoNumber() {
//        return photoNumber;
//    }

//    public void setPhotoNumber(String photoNumber) {
//        this.photoNumber = photoNumber;
//    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

//    public String getImageId() {
//        return imageId;
//    }

//    public void setImageId(String imageId) {
//        this.imageId = imageId;
//    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLessonNumber() {
        return lessonNumber;
    }

    public void setLessonNumber(String lessonNumber) {
        this.lessonNumber = lessonNumber;
    }

}

