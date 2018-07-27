package com.example.standard.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Step implements Parcelable {

    private String mShortDescription, mDescription, mVideoURL, mThumbnailUrl;
    private int mIndex;

    public Step (){}

    public Step(int index, String mShortDescription, String mDescription, String mVideoURL, String mThumbnailUrl) {
        this.mIndex = index;
        this.mShortDescription = mShortDescription;
        this.mDescription = mDescription;
        this.mVideoURL = mVideoURL;
        this.mThumbnailUrl = mThumbnailUrl;

        Log.d("Test", "Step: Constructor");
    }

    public Step(Parcel parcel) {
        this.mIndex = parcel.readInt();
        this.mShortDescription = parcel.readString();
        this.mDescription = parcel.readString();
        this.mVideoURL = parcel.readString();
        this.mThumbnailUrl = parcel.readString();
    }

    public int getmIndex() {
        return mIndex;
    }

    public String getmShortDescription() {
        return mShortDescription;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmVideoURL() {
        return mVideoURL;
    }

    public String getmThumbnailUrl() {
        return mThumbnailUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mIndex);
        parcel.writeString(mShortDescription);
        parcel.writeString(mDescription);
        parcel.writeString(mVideoURL);
        parcel.writeString(mThumbnailUrl);
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel source) {
            return new Step(source);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

}
