package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class User {

    public String name;
    public long uid;
    public String username;
    public String profileImageUrl;

    public static User fromJson(JSONObject jsonObject) throws JSONException {

        User user = new User();
        user.name = jsonObject.getString("name");
        user.uid = jsonObject.getLong("id");
        user.username = jsonObject.getString("screen_name");
        user.profileImageUrl = jsonObject.getString("profile_image_url_https");

        return user;
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getUsername(){
        return username;
    }

}
