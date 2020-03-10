package com.huni.Controller.admin;

import com.huni.entity.Type;
import com.huni.service.TypeService;
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
public class TypeController {
    @Autowired
    private TypeService typeService;

    @GetMapping("/types")
    public String types(@PageableDefault(size =4,sort={"id"},direction = Sort.Direction.DESC ) Pageable pageable, Model model){
        Page<Type> page=typeService.listType(pageable);
        model.addAttribute("page",page);
        return "admin/types";
    }
    @GetMapping("/type/input")
    public String input(Model model){
        model.addAttribute("type",new Type());
        return "admin/type-input";
    }
   @GetMapping("/type/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
       model.addAttribute("type",typeService.getType(id));
       return "admin/type-input";
    }

    @PostMapping("/types")
    public String type_input(@Valid Type type, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        Type type1 = typeService.getTypeByName(type.getName());
        if(type1!=null){
            bindingResult.rejectValue("name","nameError","提交失败，该分类名称已存在！");
        }
        if(bindingResult.hasErrors()){
            return "admin/type-input";
        }
       Type t= typeService.saveType(type);
       if(t == null){
            redirectAttributes.addFlashAttribute("message","新增失败了！");
       }else{
           redirectAttributes.addFlashAttribute("message","新增成功了！");
       }
        return "redirect:/admin/types";
    }

    @PostMapping("/types/{id}")
    public String type_editInput(@Valid Type type, BindingResult bindingResult,@PathVariable Long id, RedirectAttributes redirectAttributes){
        Type type1 = typeService.getTypeByName(type.getName());
        if(type1!=null){
            bindingResult.rejectValue("name","nameError","提交失败，该分类名称已存在！");
        }
        if(bindingResult.hasErrors()){
            return "admin/type-input";
        }
        Type t= typeService.updateType(id,type);
        if(t == null){
            redirectAttributes.addFlashAttribute("message","更新失败了！");
        }else{
            redirectAttributes.addFlashAttribute("message","更新成功了！");
        }
        return "redirect:/admin/types";
    }

    @GetMapping("/type/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes redirectAttributes){
        typeService.deleteType(id);
        redirectAttributes.addFlashAttribute("message","删除成功！");
        return "redirect:/admin/types";
    }
}