package com.huni.Controller;

import com.huni.entity.Tag;
import com.huni.service.BlogService;
import com.huni.service.TagService;
import com.huni.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TagShowController {
    @Autowired
    private TagService tagService;
    @Autowired
    private BlogService blogService;
    @GetMapping("/tag/{id}")
    public String tag(Model model, @PageableDefault(size=4,sort={"updateTime"},direction = Sort.Direction.DESC ) Pageable pageable,@PathVariable Long id){
        List<Tag> tags=  tagService.listTagTop(1000);
        if(id == -1){
            id = tags.get(0).getId();
        }
        BlogQuery blogQuery = new BlogQuery();
        model.addAttribute("tags",tags);
        model.addAttribute("page",blogService.selectBlog(pageable,id));
        model.addAttribute("activeTagId",id);
        return "tags";
    }
}