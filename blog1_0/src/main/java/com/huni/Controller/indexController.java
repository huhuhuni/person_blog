package com.huni.Controller;

import com.huni.NotFoundException;
import com.huni.service.BlogService;
import com.huni.service.TagService;
import com.huni.service.TypeService;
import com.huni.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class indexController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;
    @GetMapping("/")
    public String blogs(Model model, @PageableDefault(size =4,sort={"id"},direction = Sort.Direction.DESC ) Pageable pageable){
              model.addAttribute("page",blogService.selectBlog(pageable));
              model.addAttribute("types",typeService.listTypeTop(6));
              model.addAttribute("tags",tagService.listTagTop(10));
              model.addAttribute("recommendBlogs",blogService.listRecommendBlogTop(8));
        System.out.println("-----------index--------------");
        return "index";
    }

    @GetMapping("/blog")
    public String blog() {
        System.out.println("-----------Blog--------------");
        return "Blog";
    }

    @GetMapping("/tags")
    public String trags() {
        System.out.println("-----------tags--------------");
        return "tags";
    }

    @GetMapping("/type")
    public String type() {
        System.out.println("-----------Blog--------------");
        return "Type";
    }

    @PostMapping("/search")
    public String search(Model model, @PageableDefault(size =5,sort={"id"},direction = Sort.Direction.DESC ) Pageable pageable,@RequestParam String query) {
        model.addAttribute("page",blogService.selectBlog(query,pageable));
        model.addAttribute("query",query);
        return "search";
    }

}
