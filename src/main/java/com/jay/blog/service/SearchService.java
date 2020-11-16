package com.jay.blog.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jay.blog.vo.BlogVO;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @Author: xiejie
 * @Date: 2020/11/16 15:30
 */
public interface SearchService {

    public Page<BlogVO> search(Integer pageNO, Integer pageSize, String keyword) throws IOException;

    public void createDoc(Long blogId) throws IOException;

    public void deleteDoc(Long blogId) throws IOException;
}
