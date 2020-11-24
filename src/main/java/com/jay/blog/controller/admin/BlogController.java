package com.jay.blog.controller.admin;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jay.blog.entity.*;
import com.jay.blog.search.mq.MqEsIndexMessage;
import com.jay.blog.search.mq.RabbitMqMessageProducer;
import com.jay.blog.service.Imp.BlogServiceImp;
import com.jay.blog.service.Imp.TagServiceImp;
import com.jay.blog.service.Imp.TypeServiceImp;
import com.jay.blog.service.Imp.UserServiceImp;
import com.jay.blog.vo.BlogQuery;
import com.jay.blog.vo.BlogVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: blog
 * @description:
 * @author: Jay
 * @create: 2020-04-03 17:18
 **/
@Slf4j
@Controller
@RequestMapping("/admin/blogs")
public class BlogController {

    private final static String LIST = "admin/blogs";
    private final static String INPUT = "admin/blogs-input";
    private final static String REDIRECT_LIST = "redirect:/admin/blogs";

    private final static String UPDATE="update";
    private final static String CREATE="create";


    @Autowired
    private BlogServiceImp blogService;

    @Autowired
    private UserServiceImp userService;

    @Autowired
    private TypeServiceImp typeService;

    @Autowired
    private TagServiceImp tagService;

    @Autowired
    private RabbitMqMessageProducer rabbitMqMessageProducer;

    @GetMapping
    public String blogs(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                        ModelMap model){
        // 默认 size = 10
        Page<Blog> page = new Page<>();
        List<OrderItem> orderItems = new ArrayList<>();
        // 按照创建时间来排序，最新的在前面。
        orderItems.add(new OrderItem().setColumn("update_time").setAsc(false));
        page.setOrders(orderItems);
        page.setCurrent(pageNo);
        // 获取博客
        Page<BlogVO> resultPage = blogService.listBlog(page, null);
        // 获取 type
        List<Type> types = typeService.listType();
        // 设置blogVO的类型
        setBlogVOTypeName(resultPage.getRecords(), types);

        //
        model.addAttribute("page", resultPage);
        model.addAttribute("types", types);

        log.info("【管理员blog】pageTotal={}, pages={}", resultPage.getTotal(), resultPage.getPages());

        return LIST;
    }
    private void setBlogVOTypeName(List<BlogVO> blogVOS, List<Type> types){
        // TODO 改用数据库多表查询来获取结果
        // 获取所有的分类信息
        for (BlogVO blogVO : blogVOS ) {
            for (Type type : types){
                if( blogVO.getType().getId() == type.getId() ){
                    blogVO.getType().setName(type.getName());
                    break;
                }
            }
        }
    }

    @PostMapping("/search")
    public String search(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                         BlogQuery blogQuery, ModelMap model){
        // TODO 查询不到指定的blog， 则提供一个查询失败的页面
        // 默认 size = 10
        Page<Blog> page = new Page<>();
        List<OrderItem> orderItems = new ArrayList<>();
        // 按照创建时间来排序，最新的在前面。
        orderItems.add(new OrderItem().setColumn("update_time").setAsc(false));
        page.setOrders(orderItems);
        page.setCurrent(pageNo);
        // 检查查询条件是否为空，如果为空则
        blogQuery = checkQuery(blogQuery);
//        if(blogQuery == null)
//            page.setCurrent(pageNo+1);

        log.info("【查询条件】 pageNo = {}, BlogQuery={}, blogQueryEmpty={}",pageNo, blogQuery,blogQuery == null);
        Page<BlogVO> resultPage = blogService.listBlog(page, blogQuery);
        // 获取 type
        List<Type> types = typeService.listType();
        // 设置blogVO的类型
        setBlogVOTypeName(resultPage.getRecords(), types);
        //
        log.info("【查询结果】result num={}",resultPage.getRecords().size());
        model.addAttribute("page", resultPage);
        model.addAttribute("types", types);

        return "admin/blogs :: blogList";
    }

    private BlogQuery checkQuery(BlogQuery blogQuery){
        if (blogQuery.getTitle().equals("")&&!blogQuery.isRecommend()&&blogQuery.getTypeId()==null) {
            return null;
        }
        return blogQuery;
    }

    @GetMapping("/input")
    public String input(ModelMap model){
        // 获取 tag 和 type
        setTypesAndTags(model);
        model.addAttribute("blog", new BlogVO());
        return INPUT;
    }
    /* 设置 Type 和 tag*/
    private void setTypesAndTags(ModelMap model){
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
    }

    // TODO 测试
    @GetMapping("/{id}/input")
    public String edit(@PathVariable Long id, ModelMap model){
        setTypesAndTags(model);
        BlogVO blogVO = blogService.getBlogVOById(id);
        // 获取博客的 tag。
        blogVO.setTagIds(tagService.getTagIds(blogVO.getId()));
        model.addAttribute("blog", blogVO);
        return INPUT;
    }

    /*新增和更新*/
    @PostMapping()
    public String post(@Valid BlogVO blogVO,
                       BindingResult result,
                       RedirectAttributes attributes, HttpSession session) throws NotFoundException {
        blogVO.setUser(new User( ((User)session.getAttribute("user")).getId() ) );
        //System.out.println(blogVO);
        int ok = -1;
        String operate = null;

        if (blogVO.getId() == null) {
            operate = CREATE;
            ok = blogService.saveOne(blogVO);
        } else {
            operate = UPDATE;
            ok = blogService.updateOne(blogVO);
        }

        if(ok != -1) {
            attributes.addAttribute("message", operate + " success");
            MqEsIndexMessage message = new MqEsIndexMessage(blogVO.getId(), MqEsIndexMessage.CREATE_OR_UPDATE);
            rabbitMqMessageProducer.publishMessage(message);
        }else {
            attributes.addAttribute("message", operate + " fail");
        }


        return REDIRECT_LIST;
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        int ok = blogService.deleteOne(id);
        if(ok != 0) {
            attributes.addAttribute("message","删除成功");
        } else {
            attributes.addAttribute("message", "删除失败");
        }
        return REDIRECT_LIST;
    }

    @RequestMapping("/preview/{id}")
    public String previewBlog(@PathVariable Long id, ModelMap model) throws NotFoundException {
        System.out.println(id);
        BlogVO blogVO = blogService.getBlogVObyIdToView(id);
        User user = userService.getOneById(blogVO.getUser().getId());
        user.setPassword(null);
        blogVO.setUser(user);
        blogVO.setTags(tagService.listTag(blogVO.getId()));
        model.addAttribute("blog", blogVO);
        return "blog";
    }
}
