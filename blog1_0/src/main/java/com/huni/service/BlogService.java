package com.huni.service;

import com.huni.entity.Blog;
import com.huni.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BlogService {
    Blog getBlog(Long id);
    Page<Blog> selectBlog(Pageable pageable,BlogQuery blog);
    Page<Blog> selectBlog(Pageable pageable);
    Page<Blog> selectBlog(String query,Pageable pageable);
    Blog saveBlog(Blog blog);
    Blog updateBlog(Long id,Blog blog);
    void deleteBlog(Long id);
    List<Blog> listRecommendBlogTop(Integer size);


    //Blog getBlogByTitle(String title);
}