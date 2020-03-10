package com.huni.Controller.admin;

import com.huni.entity.Tag;
import com.huni.service.TagService;
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

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/tags")
    public String tags(@PageableDefault(size =4,sort={"id"},direction = Sort.Direction.DESC ) Pageable pageable, Model model){
        Page<Tag> page=tagService.listTag(pageable);
        model.addAttribute("page",page);
        return "admin/tags";
    }
    @GetMapping("/tag/input")
    public String input(Model model){
        model.addAttribute("tag",new Tag());
        return "admin/tag-input";
    }
    @GetMapping("/tag/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("tag",tagService.getTag(id));
        return "admin/tag-input";
    }

    @PostMapping("/tags")
    public String tag_input(@Valid Tag tag, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        Tag tag1 = tagService.getTagByName(tag.getName());
        if(tag1!=null){
            bindingResult.rejectValue("name","nameError","提交失败，该分类名称已存在！");
        }
        if(bindingResult.hasErrors()){
            return "admin/tag-input";
        }
        Tag t= tagService.saveTag(tag);
        if(t == null){
            redirectAttributes.addFlashAttribute("message","新增失败了！");
        }else{
            redirectAttributes.addFlashAttribute("message","新增成功了！");
        }
        return "redirect:/admin/tags";
    }

    @PostMapping("/tags/{id}")
    public String tag_editInput(@Valid Tag tag, BindingResult bindingResult,@PathVariable Long id, RedirectAttributes redirectAttributes){
        Tag tag1 = tagService.getTagByName(tag.getName());
        if(tag1!=null){
            bindingResult.rejectValue("name","nameError","提交失败，该分类名称已存在！");
        }
        if(bindingResult.hasErrors()){
            return "admin/tag-input";
        }
        Tag t= tagService.updateTag(id,tag);
        if(t == null){
            redirectAttributes.addFlashAttribute("message","更新失败了！");
        }else{
            redirectAttributes.addFlashAttribute("message","更新成功了！");
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("/tag/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes redirectAttributes){
        tagService.deleteTag(id);
        redirectAttributes.addFlashAttribute("message","删除成功！");
        return "redirect:/admin/tags";
    }
}