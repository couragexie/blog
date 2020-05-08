package com.jay.blog.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jay.blog.entity.Type;
import com.jay.blog.service.Imp.TypeServiceImp;
import com.jay.blog.service.TypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class TypeControllerTest {

    @Autowired
    TypeServiceImp service;

    @Test
    void types() {
        Integer pageNo = 2;
        Page<Type> page = new Page<>();
        ArrayList<OrderItem> orders = new ArrayList<>();
        orders.add(new OrderItem().setColumn("id"));
        page.setOrders(orders);
        page.setCurrent(pageNo);
        Page<Type> type = service.listType(page);

        List<Type> types = type.getRecords();

        types.forEach(System.out::println);
    }
}