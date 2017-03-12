package com.muv.test;

import android.os.Parcel;
import android.os.Parcelable;

public class AnimalParcelable implements Parcelable
{
    public String image;
    public String description;

    public AnimalParcelable(String image, String description)
    {
        this.image = image;
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeString(description);
    }


    public static final Parcelable.Creator<AnimalParcelable> CREATOR = new Parcelable.Creator<AnimalParcelable>()
    {
        public AnimalParcelable createFromParcel(Parcel in) {
            return new AnimalParcelable(in);
        }

        public AnimalParcelable[] newArray(int size) {
            return new AnimalParcelable[size];
        }
    };

    private AnimalParcelable(Parcel parcel) {
        image = parcel.readString();
        description = parcel.readString();
    }
}
