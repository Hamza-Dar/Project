package com.example.project;

public class post {
    private String UID, Desc, image_uri, UserName;

    public post() {
    }

    public post(String UID, String desc, String image_uri, String userName) {
        this.UID = UID;
        Desc = desc;
        this.image_uri = image_uri;
        UserName = userName;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
