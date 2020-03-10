package com.huni.Controller.admin;

import com.huni.entity.Blog;
import com.huni.entity.Tag;
import com.huni.entity.Type;
import com.huni.entity.User;
import com.huni.service.BlogService;
import com.huni.service.TagService;
import com.huni.service.TypeService;
import com.huni.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class BlogController {
    @Autowired
    BlogService blogService;
    @Autowired
    TypeService typeService;
    @Autowired
    TagService tagService;

    @GetMapping("/blogs")
    public String blogs(Model model, @PageableDefault(size =4,sort={"id"},direction = Sort.Direction.DESC ) Pageable pageable,BlogQuery blog){
        Page<Blog> page=blogService.selectBlog(pageable,blog);
        List<Type> types = typeService.listType();
        model.addAttribute("page",page);
        model.addAttribute("types",types);
        return "admin/blogs";
    }

    @PostMapping("/blogs/search")
    public String blogsSearch(Model model, @PageableDefault(size =4,sort={"updateTime"},direction = Sort.Direction.DESC ) Pageable pageable,BlogQuery blog){
        Page page=blogService.selectBlog(pageable,blog);
        model.addAttribute("page",page);
        return "admin/blogs :: blogList";
    }

    @GetMapping("/blog/input")
    public String blog_input(Model model){
        model.addAttribute("blog",new Blog());
        List<Type> types = typeService.listType();
        List<Tag>  tags = tagService.listTag();
        model.addAttribute("types",types);
        model.addAttribute("tags",tags);
        return "admin/blog-input";
    }

    @GetMapping("/blog/{id}/input")
    public String blog_edit(Model model,@PathVariable Long id){
       // System.out.print(id);
        Blog blog= blogService.getBlog(id);
        blog.init();//处理一下，把tags列表转化为tagsIds字符串
        model.addAttribute("blog",blog);
        List<Type> types = typeService.listType();
        List<Tag>  tags = tagService.listTag();
        model.addAttribute("types",types);
        model.addAttribute("tags",tags);
        return "admin/blog-input";
    }

    @PostMapping("/blogs")
    public String post(Blog blog, HttpSession session, RedirectAttributes redirectAttributes, BindingResult bindingResult){
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        Blog blog1 = null;
        if(blog.getId()!=null){
             blog1 = blogService.updateBlog(blog.getId(),blog);
        }
       else{
             blog1 = blogService.saveBlog(blog);
        }
        if(blog1 == null) {
            redirectAttributes.addFlashAttribute("message","博客提交失败了！");
        }else{
            redirectAttributes.addFlashAttribute("message","博客提交成功了！");
        }
        return "redirect:/admin/blogs";
    }
   @GetMapping("/blog/{id}/delete")
   public  String delete(@PathVariable Long id,RedirectAttributes redirectAttributes){
        blogService.deleteBlog(id);
        redirectAttributes.addFlashAttribute("message","删除成功");
       return "redirect:/admin/blogs";
   }
}