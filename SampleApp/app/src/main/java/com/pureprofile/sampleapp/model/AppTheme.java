package com.pureprofile.sampleapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Model used by Volley for mapping Panel json
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class AppTheme implements Parcelable {
    @SerializedName("color_primary")
    public String colorPrimary;
    @SerializedName("color_secondary")
    public String colorSecondary;
    @SerializedName("color_cta")
    public String colorCta;
    public String colorCtaDis;
    @SerializedName("font_color_primary")
    public String fontColorPrimary;
    @SerializedName("font_color_secondary")
    public String fontColorSecondary;
    @SerializedName("font_color_cta")
    public String fontColorCta;
    @SerializedName("logo_url")
    public String logoUrl;

//    add selection variable
    public boolean isChecked = false;

    public AppTheme(Parcel in) {
        String[] stringValues = new String[8];
        in.readStringArray(stringValues);

        this.colorPrimary = stringValues[0];
        this.colorSecondary = stringValues[1];
        this.colorCta = stringValues[2];
        this.colorCtaDis = stringValues[3];
        this.fontColorPrimary = stringValues[4];
        this.fontColorSecondary = stringValues[5];
        this.fontColorCta = stringValues[6];
        this.logoUrl = stringValues[7];

        boolean[] boolValues = new boolean[1];
        in.readBooleanArray(boolValues);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {
                this.colorPrimary,
                this.colorSecondary,
                this.colorCta,
                this.colorCtaDis,
                this.fontColorPrimary,
                this.fontColorSecondary,
                this.fontColorCta,
                this.logoUrl
        });

        parcel.writeBooleanArray(new boolean[] { this.isChecked });
    }

    public static final Creator<AppTheme> CREATOR = new Creator<AppTheme>() {
        public AppTheme createFromParcel(Parcel in) {
            return new AppTheme(in);
        }

        public AppTheme[] newArray(int size) {
            return new AppTheme[size];
        }
    };
}
