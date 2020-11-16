package com.jay.blog.utils;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jay.blog.vo.BlogVO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class PageUtil {
    public static Page generatePage(int pageNo, List<OrderItem> orderItems){
        Page page = new Page();
        page.setCurrent(pageNo);
        page.setOrders(orderItems);
        return page;
    }
    public static Page generatePage(int pageNo){
        Page page = new Page();
        page.setCurrent(pageNo);
        return page;
    }
    public static Page copyPage(Page oldPage){
        Page newPage = new Page();
        // 将 page 的值赋值给 resultPage
        // 指定字段不复制
        List<String> excludeFiled = new ArrayList<>();
        excludeFiled.add("records");
        BeanUtils.copyProperties(oldPage, newPage,excludeFiled.toArray(new String[excludeFiled.size()]));
        return newPage;
    }

}
