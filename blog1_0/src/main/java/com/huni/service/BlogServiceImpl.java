package com.huni.service;

import com.huni.NotFoundException;
import com.huni.dao.BlogDao;
import com.huni.entity.Blog;
import com.huni.entity.Type;
import com.huni.util.MarkdownUtils;
import com.huni.util.MyBeanUtils;
import com.huni.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogDao blogDao;


//    public  Blog getBlogByTitle(String title){
//        return blogDao.findByTitle();
//    }
    @Override
    public Blog getBlog(Long id) {
         return blogDao.findOne(id);
    }

    @Override
    public Page<Blog> selectBlog(Pageable pageable, BlogQuery blog) {
           return blogDao.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if(blog.getTitle()!=null && !"".equals(blog.getTitle())){
                         predicates.add(criteriaBuilder.like(root.<String>get("title"),"%"+blog.getTitle()+"%" ));
                }
                if(blog.getTypeId() != null)
                {
                    predicates.add(criteriaBuilder.equal(root.<Type>get("type").get("id"),+blog.getTypeId()));
                }
                if(blog.isRecommented()){
                    predicates.add(criteriaBuilder.equal(root.<Boolean>get("recommented"),blog.isRecommented()));
                }
                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);

    }

    @Override
    public Page<Blog> selectBlog(Pageable pageable, Long tagId) {
        return blogDao.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery cq, CriteriaBuilder cb) {
                  Join join = root.join("tags");
                  return cb.equal(join.get("id"),tagId);
            }
        },pageable);
    }

    @Override
    public Page<Blog> selectBlog(Pageable pageable) {
        return  blogDao.findAll(pageable);
    }

    @Override
    public Page<Blog> selectBlog(String query,Pageable pageable) {
        return blogDao.findByQuery("%"+query+"%",pageable);
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if(blog.getId()==null)
        {
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setView(0);
        }else{
            blog.setUpdateTime(new Date());
        }
          return blogDao.save(blog);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogDao.findGroupYear();
        Map<String,List<Blog>> map = new HashMap<>();
        for (String year : years) {
             map.put(year,blogDao.findByYear(year));
        }
        return map;
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
          Blog b = blogDao.findOne(id);
          if(b==null) {
              throw new NotFoundException("该博客不存在。");
          } else{
            BeanUtils.copyProperties(blog,b, MyBeanUtils.getNullPropertyNames(blog));
          }
         return blogDao.save(b);
    }
    @Transactional
    @Override
    public void deleteBlog(Long id) {
          blogDao.delete(id);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort =new Sort(Sort.Direction.DESC,"updateTime");
        Pageable pageable = new PageRequest(0,size,sort);
        List<Blog> ttt=blogDao.findTop(pageable);
        return blogDao.findTop(pageable);
    }

    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogDao.findOne(id);
        if(blog == null){
            throw new NotFoundException("该博客不存在");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog,b);
        String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        blogDao.updateView(id);
        return b;
    }

    @Override
    public Long countBlog() {
        return blogDao.count();
    }
}