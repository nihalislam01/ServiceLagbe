package com.javafest.ServiceLagbe.posts;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService implements IPostService{
    private static List<String> posts = new ArrayList<>();

    static {
        posts.add("Mason");
        posts.add("Carpenter");
        posts.add("Domestic Worker");
        posts.add("Electrician");
        posts.add("Babysitter");
        posts.add("Caretaker");
        posts.add("Driver");
        posts.add("Car Mechanic");
        posts.add("Home Tutor");
        posts.add("Private Nurse");
        posts.add("Private Spa");
        posts.add("Private Saloon");
        posts.add("Others");
    }

    public List<String> getPosts() {
        return posts;
    }

    @Override
    public void addPost(String post) {
        posts.add(post);
    }
}
