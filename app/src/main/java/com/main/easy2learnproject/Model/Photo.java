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
    private String pro;
    private double weight;
    private String profileType;
    private int pricePerLesson;
    private double dist;
    private double rate;
    private double costPrecent;
    private int numOfRate;

    public Photo() {

    }



    public Photo(String fullName, String email, String imageId, String userId, String pro, double weight,String profileType, int pricePerLesson,
                 double dist, double rate, double costPrecent, int numOfRate) {
        super();
        this.fullName = fullName;
        this.email = email;
        this.imageId = imageId;
        this.userId = userId;
        this.pro = pro;
        this.weight = weight;
        this.profileType = profileType;
        this.pricePerLesson = pricePerLesson;
        this.dist = dist;
        this.rate = rate;
        this.costPrecent = costPrecent;
        this.numOfRate= numOfRate;

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
        pro=in.readString();
        weight=in.readDouble();
        profileType=in.readString();
        pricePerLesson =in.readInt();
        dist=in.readDouble();
        rate=in.readDouble();
        costPrecent =in.readDouble();
        numOfRate =in.readInt();
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
        parcel.writeString(pro);
        parcel.writeDouble(weight);
        parcel.writeString(profileType);
        parcel.writeInt(pricePerLesson);
        parcel.writeDouble(dist);
        parcel.writeDouble(rate);
        parcel.writeDouble(costPrecent);
        parcel.writeInt(numOfRate);
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

    public void setPro(String pro) {
        this.pro = pro;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public String getPro() {
        return pro;
    }

    public String getprofileType() {
        return profileType;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }

    public int getPricePerLesson() {
        return pricePerLesson;
    }
    public void setPricePerLesson(int pricePerLesson) {
        this.pricePerLesson = pricePerLesson;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public void setCostPrecent(double costPrecent) {
        this.costPrecent = costPrecent;
    }


    public double getDist() {
        return dist;
    }

    public double getCostPrecent() {
        return costPrecent;
    }
    public double getRate() {
        return rate;
    }

    public int getNumOfRate(){return numOfRate;}
    public void setNumOfRate(int numOfRate){this.numOfRate = numOfRate;}
}
