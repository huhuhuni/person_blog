package com.huni.service;

import com.huni.entity.Blog;
import com.huni.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {
    Blog getBlog(Long id);
    Page<Blog> selectBlog(Pageable pageable,BlogQuery blog);
    Page<Blog> selectBlog(Pageable pageable,Long tagId);
    Page<Blog> selectBlog(Pageable pageable);
    Page<Blog> selectBlog(String query,Pageable pageable);
    Blog saveBlog(Blog blog);
    Map<String,List<Blog>> archiveBlog();
    Blog updateBlog(Long id,Blog blog);
    void deleteBlog(Long id);
    List<Blog> listRecommendBlogTop(Integer size);
    Blog getAndConvert(Long id);
    Long countBlog();

    //Blog getBlogByTitle(String title);
}