package com.pureprofile.sampleapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Model used by Volley for mapping Panel json
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Instance implements Parcelable {
    public String code;
    public String key;
    public String name;
    public ArrayList<Panel> panels;

    public Instance(Parcel in) {
        String[] stringValues = new String[3];
        in.readStringArray(stringValues);
        this.code = stringValues[0];
        this.key = stringValues[1];
        this.name = stringValues[2];

        this.panels = new ArrayList<>();
        in.readTypedList(this.panels, Panel.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {
                this.code,
                this.key,
                this.name
        });

        parcel.writeTypedList(this.panels);
    }

    public static final Creator<Instance> CREATOR = new Creator<Instance>() {
        public Instance createFromParcel(Parcel in) {
            return new Instance(in);
        }

        public Instance[] newArray(int size) {
            return new Instance[size];
        }
    };
}
