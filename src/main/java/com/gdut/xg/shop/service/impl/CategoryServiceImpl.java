package com.gdut.xg.shop.service.impl;

import com.gdut.xg.shop.entity.Category;
import com.gdut.xg.shop.dao.CategoryDao;
import com.gdut.xg.shop.service.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lele
 * @since 2019-06-11
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, Category> implements ICategoryService {
@Autowired
private Gson gson;
@Autowired
private StringRedisTemplate stringRedisTemplate;
     @Override
     public List<Category> getCategory() {
          List<Category> list=null;
          String cStr=stringRedisTemplate.opsForValue().get("category");
          if(!Objects.isNull(cStr)){
             list=gson.fromJson(cStr,new TypeToken<List<Category>>(){}.getType());
          }else{
               list=this.list();
               stringRedisTemplate.opsForValue().set("category",gson.toJson(list));
          }
          return list;
     }

     @Override
     public Boolean saveByList(List<Category> categoryList) {

          Boolean b= this.saveBatch(categoryList);
          stringRedisTemplate.delete("category");
          return b;
     }

     @Override
     public Boolean deleteById(Integer categoryId) {
          Boolean b=this.removeById(categoryId);
          stringRedisTemplate.delete("category");return b;
     }
}
