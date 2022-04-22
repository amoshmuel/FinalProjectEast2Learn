package com.main.easy2learnproject.Model;
import android.os.Parcel;
import android.os.Parcelable;


public class Photo implements Parcelable {

    private String date;
    private String fullName;
    private String email;
    private String imageId;
    private String photoNumber;
    private String userId;
    private double lat,lon;
    public Photo() {

    }



    public Photo(String fullName, String email, String imageId, String userId) {
        super();
        this.fullName = fullName;
        this.email = email;
        this.imageId = imageId;
        this.userId = userId;
    }
    public static final Parcelable.Creator<Photo> CREATOR
            = new Parcelable.Creator<Photo>() {
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    protected Photo(Parcel in) {
        fullName = in.readString();
        email = in.readString();
        imageId = in.readString();
        date=in.readString();
        photoNumber =in.readString();
        userId =in.readString();
        lat=in.readDouble();
        lon=in.readDouble();
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(fullName);
        parcel.writeString(email);
        parcel.writeString(imageId);
        parcel.writeString(date);
        parcel.writeString(photoNumber);
        parcel.writeString(userId);
        parcel.writeDouble(lat);
        parcel.writeDouble(lon);
    }


    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getPhotoNumber() {
        return photoNumber;
    }

    public void setPhotoNumber(String photoNumber) {
        this.photoNumber = photoNumber;
    }

    public String getDate() {
        return date;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
