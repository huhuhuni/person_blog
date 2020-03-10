package com.huni.service;

import com.huni.NotFoundException;
import com.huni.dao.BlogDao;
import com.huni.entity.Blog;
import com.huni.entity.Type;
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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
}