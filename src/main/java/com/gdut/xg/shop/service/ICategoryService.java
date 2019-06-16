package com.gdut.xg.shop.service;

import com.gdut.xg.shop.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lele
 * @since 2019-06-11
 */
public interface ICategoryService extends IService<Category> {
    List<Category> getCategory();
    Boolean saveByList(List<Category> categoryList);
    Boolean deleteById(Integer categoryId);
}
