package com.lee.yueftshare.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lee.yueftshare.model.IBodyData;


public class BodyItem implements IBodyData, Parcelable {
    public static final int BODY_HEADER =  0; //顶部显示
    public static final int BODY_WEIGH = 0x2; //体重
    public static final int BODY_BMI = 0x3;
    public static final int BOYT_FAT =0x4;
    public static final int MUSLE_RATE = 0x5;
    public static final int BODY_WATER = 0x6;
    public static final int NO_WATER_WEIGH = 0x7;
    public static final int BODY_BONE = 0x8;
    public static final int BODY_BASIC = 0x9;
    public static final int MUSL_RATE =0x10;
    public static final int INNER_FAT = 0x11;

    public static final int TYPE_STANDARD = 0x101;//标准型

    /**类型，体重、BMI、体脂等*/
    private int type;

    /**当前状态 ： 标准，不足等*/
    private int statue;
    private String title;
    private float value;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatue() {
        return statue;
    }

    public void setStatue(int statue) {
        this.statue = statue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public BodyItem(int type, int statue, String title, float value){
        this.type = type;
        this.statue = statue;
        this.title = title;
        this.value = value;
    }

    protected BodyItem(Parcel in) {
        this.type = in.readInt();
        this.statue = in.readInt();
        this.title = in.readString();
        this.value = in.readFloat();
    }

    public static final Creator<BodyItem> CREATOR = new Creator<BodyItem>() {
        @Override
        public BodyItem createFromParcel(Parcel in) {
            return new BodyItem(in);
        }

        @Override
        public BodyItem[] newArray(int size) {
            return new BodyItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeInt(statue);
        dest.writeString(title);
        dest.writeFloat(value);
    }
}
