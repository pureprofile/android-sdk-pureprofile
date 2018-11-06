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
    @SerializedName("color_primary")
    public String colorPrimary;
    @SerializedName("color_primary_dark")
    public String colorPrimaryDark;
    @SerializedName("color_accent")
    public String colorAccent;
    @SerializedName("color_cta")
    public String colorCta;
    @SerializedName("logo_url")
    public String logoUrl;
    @SerializedName("font_color_title")
    public String fontColorTitle;
    @SerializedName("font_color_badge")
    public String fontColorBadge;
    @SerializedName("exchange_rate")
    public Float exchangeRate;

//    add selection variable
    public boolean isChecked = false;

    public Panel(Parcel in) {
        String[] stringValues = new String[9];
        in.readStringArray(stringValues);

        this.name = stringValues[0];
        this.guid = stringValues[1];
        this.colorPrimary = stringValues[2];
        this.colorPrimaryDark = stringValues[3];
        this.colorAccent = stringValues[4];
        this.colorCta = stringValues[5];
        this.logoUrl = stringValues[6];
        this.fontColorTitle = stringValues[7];
        this.fontColorBadge = stringValues[8];

        this.exchangeRate = in.readFloat();

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
                this.name,
                this.guid,
                this.colorPrimary,
                this.colorPrimaryDark,
                this.colorAccent,
                this.colorCta,
                this.logoUrl,
                this.fontColorTitle,
                this.fontColorBadge
        });

        parcel.writeFloat(this.exchangeRate);
        parcel.writeBooleanArray(new boolean[] { this.isChecked });
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
