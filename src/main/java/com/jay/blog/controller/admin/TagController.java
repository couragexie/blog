package com.jay.blog.controller.admin;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jay.blog.entity.Tag;
import com.jay.blog.entity.Type;
import com.jay.blog.service.Imp.TagServiceImp;
import com.jay.blog.service.Imp.TypeServiceImp;
import com.jay.blog.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;

/**
 * @program: blog
 * @description:
 * @author: Jay
 * @create: 2020-04-04 14:42
 **/
@Slf4j
@Controller
@RequestMapping("/admin/tags")
public class TagController {

    @Autowired
    private TagServiceImp tagService;

    /*获取 tags*/
    @GetMapping
    public String tags(@RequestParam(value = "pageNo", defaultValue = "1")Integer pageNo,
                        ModelMap modelMap){
        // 默认 size = 10。
        Page<Tag> page = new Page<>();
        ArrayList<OrderItem> orders = new ArrayList<>();
        orders.add(new OrderItem().setColumn("id"));
        page.setOrders(orders);
        page.setCurrent(pageNo);
        modelMap.put("page", tagService.listTag(page));

        return "admin/tags";
    }



    /*进入新增页面*/
    @GetMapping("/input")
    public String input(ModelMap model){
        model.addAttribute("tag", new Tag());
        return "admin/tags-input";
    }

    /* 新增*/
    @PostMapping
    public String post(@Valid Tag tag, BindingResult result, RedirectAttributes attributes){
        Tag tag1 = tagService.getOneByName(tag.getName());
        if(tag1 != null){
            result.rejectValue("name", "nameError", "该标签已存在！");
        }
        if(result.hasErrors()){
            return "admin/tags-input";
        }

        int ok = tagService.saveOne(tag);
        if(ok != 0)
            attributes.addAttribute("message", "新增成功");
        else
            attributes.addAttribute("message", "添加失败");

        return "redirect:/admin/tags";
    }

    /* 删除分类*/
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        int ok = tagService.deleteOne(id);
        if (ok != 0)
            attributes.addAttribute("message", "删除成功");
        else
            attributes.addAttribute("message", "删除失败");

        return "redirect:/admin/tags";
    }

    /* 进入编辑页面*/
    @GetMapping("/{id}/input")
    public String edit(@PathVariable Long id, ModelMap modelMap){
        modelMap.addAttribute("tag", tagService.getOneById(id));
        return "admin/tags-input";
    }

    /* 更新页面*/
    @PostMapping("/{id}")
    public String update(@Valid Tag tag, BindingResult result,
                         @PathVariable Integer id, RedirectAttributes attributes){
        Tag tag1 = tagService.getOneByName(tag.getName());
        if (tag1 != null) {
            if (id.longValue() != tag1.getId())
                result.rejectValue("name", "nameError", "该分类已经存在！");
        }
        if (result.hasErrors())
            return "/admin/tags-input";

        int ok = tagService.updateOne(tag);
        if(ok != 0)
            attributes.addAttribute("message","更新成功！");
        else
            attributes.addAttribute("message", "更新失败");

        return "redirect:/admin/tags";
    }





}
