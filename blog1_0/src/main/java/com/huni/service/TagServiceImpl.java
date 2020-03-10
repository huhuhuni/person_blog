package com.huni.service;

import com.huni.NotFoundException;
import com.huni.dao.TagDao;
import com.huni.entity.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagDao tagDao;
    @Transactional
    public Tag saveTag(Tag tag) {
        return tagDao.save(tag);
    }

    @Override
    public Tag getTag(Long id) {
        return tagDao.getOne(id);
    }

    @Override
    public Tag getTagByName(String name) {
        return tagDao.findByName(name);
    }


    public Page<Tag> listTag(Pageable pageable) {
        return tagDao.findAll(pageable);
    }
    @Transactional

    public Tag updateTag(Long id, Tag tag) {
        Tag t =tagDao.getOne(id);
        if(t==null) {
            throw new NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(tag,t);
        return tagDao.save(t);
    }


    public List<Tag> listTag() {
        return tagDao.findAll();
    }

    private List<Long> convertToList(String ids) {
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null) {
            String[] idarray = ids.split(",");
            for (int i=0; i < idarray.length;i++) {
                list.add(new Long(idarray[i]));
            }
        }
        return list;
    }

    @Override
    public List<Tag> listTag(String ids) {
        return tagDao.findAll(convertToList(ids));
    }

    @Override
    public void deleteTag(Long id) {
        tagDao.delete(id);

    }

    @Override
    public List<Tag> listTagTop(int size) {
        Sort sort =new Sort(Sort.Direction.DESC,"blogs.size");
        Pageable pageable = new PageRequest(0,size,sort);
        return tagDao.findTop(pageable);
    }

}