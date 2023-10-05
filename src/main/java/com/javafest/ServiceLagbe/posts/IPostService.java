package com.javafest.ServiceLagbe.posts;

import java.util.List;

public interface IPostService {
    public List<String> getPosts();
    public void addPost(String post);
}
