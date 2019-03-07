package com.pureprofile.sampleapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Model used by Volley for mapping Panel json
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Panel implements Parcelable {
    public String name;
    public String guid;
    @SerializedName("app_theme")
    public AppTheme appTheme;
    @SerializedName("exchange_rate")
    public Float exchangeRate;
    @SerializedName("currency_code")
    public String currencyCode;

    //    add selection variable
    public boolean isChecked = false;

    public Panel(Parcel in) {
        String[] stringValues = new String[3];
        in.readStringArray(stringValues);

        this.name = stringValues[0];
        this.guid = stringValues[1];
        this.currencyCode = stringValues[2];

        this.exchangeRate = in.readFloat();

        boolean[] boolValues = new boolean[1];
        in.readBooleanArray(boolValues);

        this.appTheme = in.readParcelable(AppTheme.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {
                this.name,
                this.guid,
                this.currencyCode
        });

        parcel.writeFloat(this.exchangeRate);
        parcel.writeBooleanArray(new boolean[] { this.isChecked });

        parcel.writeParcelable(this.appTheme, 0);
    }

    public static final Parcelable.Creator<Panel> CREATOR = new Parcelable.Creator<Panel>() {
        public Panel createFromParcel(Parcel in) {
            return new Panel(in);
        }

        public Panel[] newArray(int size) {
            return new Panel[size];
        }
    };
}
