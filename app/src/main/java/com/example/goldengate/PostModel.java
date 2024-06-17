package com.example.goldengate;

public class PostModel {
    private String imgUrl;
    private String description;
    private String key;
    private String username;
    private String user_profile;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_profile() {
        return user_profile;
    }

    public PostModel() {

    }
    public  PostModel(String imgUrl, String description, String username,String user_profile){
        this.imgUrl = imgUrl;
        this.description = description;
        this.username = username;
        this.user_profile = user_profile;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getKey() {
        return key;
    }

}
