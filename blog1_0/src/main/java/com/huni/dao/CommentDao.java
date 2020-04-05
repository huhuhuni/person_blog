package com.huni.dao;

import com.huni.entity.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentDao extends JpaRepository<Comment,Long> {
    List<Comment> findByBlogIdAndParentCommentNull(Long blogId,Sort sort);
}