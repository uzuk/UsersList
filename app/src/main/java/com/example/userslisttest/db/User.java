package com.example.userslisttest.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int uid;
    private String name;
    private Integer age;
    @ColumnInfo(name = "fav_color")
    private String favColor;
    @ColumnInfo(name = "avatar_uri")
    private String imageUriString;

    public User(String name, Integer age, String favColor, String imageUriString) {
        this.name = name;
        this.age = age;
        this.favColor = favColor;
        this.imageUriString = imageUriString;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getFavColor() {
        return favColor;
    }

    public String getImageUriString() { return imageUriString; }
}