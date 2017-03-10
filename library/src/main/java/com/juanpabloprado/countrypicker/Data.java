package com.juanpabloprado.countrypicker;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Data implements Parcelable, Comparable<Data> {
  public String id;
  public String name;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeString(name);
  }

    public Data(){

    }
    public Data(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
    public Data createFromParcel(Parcel source) {
      Data data = new Data();
      data.id = source.readString();
      data.name = source.readString();
      return data;
    }

    public Data[] newArray(int size) {
      return new Data[size];
    }
  };

  @Override public int compareTo(@NonNull Data another) {
    return name.compareTo(another.name);
  }
}