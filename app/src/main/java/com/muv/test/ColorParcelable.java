package com.muv.test;

import android.os.Parcel;
import android.os.Parcelable;

public class ColorParcelable  implements Parcelable
{
    public int color;

    public ColorParcelable(int color)
    {
        this.color = color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(color);
    }


    public static final Parcelable.Creator<ColorParcelable> CREATOR = new Parcelable.Creator<ColorParcelable>()
    {
        public ColorParcelable createFromParcel(Parcel in) {
            return new ColorParcelable(in);
        }

        public ColorParcelable[] newArray(int size) {
            return new ColorParcelable[size];
        }
    };

    private ColorParcelable(Parcel parcel) {
        color = parcel.readInt();
    }
}
