package com.jay.blog.controller.admin;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jay.blog.entity.Type;
import com.jay.blog.service.Imp.TypeServiceImp;
import com.jay.blog.vo.TypeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.jws.WebParam;
import javax.validation.Valid;
import java.util.ArrayList;

/**
 * @program: blog
 * @description:
 * @author: Jay
 * @create: 2020-04-03 17:17
 **/
@Slf4j
@Controller
@RequestMapping("/admin/types")
public class TypeController {

    @Autowired
    private TypeServiceImp typeService;

    /*获取 types*/
    @GetMapping
    public String types(@RequestParam(value = "pageNo", defaultValue = "1")Integer pageNo,
                        ModelMap modelMap){
        // 默认 size = 10。
        Page<Type> page = new Page<>();
        ArrayList<OrderItem> orders = new ArrayList<>();
        orders.add(new OrderItem().setColumn("id"));
        page.setOrders(orders);
        page.setCurrent(pageNo);
        modelMap.put("page", typeService.listType(page));

        return "admin/types";
    }


    /*进入新增页面*/
    @GetMapping("/input")
    public String input(ModelMap model){
        model.addAttribute("type", new Type());
        return "admin/types-input";
    }

    /* 新增*/
    @PostMapping
    public String post(@Valid Type type, BindingResult result, RedirectAttributes attributes){
        Type type1 = typeService.getOneByName(type.getName());
        if(type1 != null){
            result.rejectValue("name", "nameError", "该分类已存在！");
        }
        if(result.hasErrors()){
            return "admin/types-input";
        }

        int ok = typeService.saveOne(type);
        if(ok != 0)
            attributes.addAttribute("message", "新增成功");
        else
            attributes.addAttribute("message", "添加失败");

        return "redirect:/admin/types";
    }

    /* 删除分类*/
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        int ok = typeService.delete(id);
        if (ok != 0)
            attributes.addAttribute("message", "删除成功");
        else
            attributes.addAttribute("message", "删除失败");

        return "redirect:/admin/types";
    }

    /* 进入编辑页面*/
    @GetMapping("/{id}/input")
    public String edit(@PathVariable Long id, ModelMap modelMap){
        modelMap.addAttribute("type", typeService.getOneById(id));
        return "admin/types-input";
    }

    /* 更新页面*/
    @PostMapping("/{id}")
    public String update(@Valid Type type, BindingResult result,
                         @PathVariable Long id, RedirectAttributes attributes){
        Type type1 = typeService.getOneByName(type.getName());
        if (type1 != null) {
            if (id.equals(type1.getId()))
                result.rejectValue("name", "nameError", "该分类已经存在！");
        }
        if (result.hasErrors())
            return "/admin/types-input";

        int ok = typeService.update(type);
        if(ok != 0)
            attributes.addAttribute("message","更新成功！");
        else
            attributes.addAttribute("message", "更新失败");

        return "redirect:/admin/types";
    }


}
