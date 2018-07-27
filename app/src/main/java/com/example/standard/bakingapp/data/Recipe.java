package com.example.standard.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Recipe implements Parcelable {

    // Recipe Name
    private String mName;
    private int mId;

    // Todo: Id einfügen, done

    public Recipe(){};

    public void setmId(int mId) {
        this.mId = mId;
    }

    public Recipe(String mName, int mId) {
        this.mName = mName;
        this.mId = mId;

        Log.d("Test", "Recipe: Constructor");
    }

    public Recipe (Parcel parcel){
        this.mName = parcel.readString();
        this.mId = parcel.readInt();
    }

    public String getmName() {
        return mName;
    }

    public int getmId() {
        return mId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
        parcel.writeInt(mId);
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

}
