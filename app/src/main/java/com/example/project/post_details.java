package com.example.project;

public class post_details {
    int no_likes, no_comnts;
    String username, desc;

    public post_details(int no_likes, int no_comnts, String username, String desc) {
        this.no_likes = no_likes;
        this.no_comnts = no_comnts;
        this.username = username;
        this.desc = desc;
    }
}
