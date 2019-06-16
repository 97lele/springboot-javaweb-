package com.gdut.xg.shop.web;


import com.gdut.xg.shop.VO.ResultVO;
import com.gdut.xg.shop.entity.Category;
import com.gdut.xg.shop.service.ICategoryService;
import com.gdut.xg.shop.util.ResultVOUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lele
 * @since 2019-06-11
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private Gson gson;

    @GetMapping
    public ResultVO getCategory() {
        List<Category> categorList = categoryService.getCategory();
        Map<String, Object> map = new HashMap();
        map.put("list", categorList);
        return ResultVOUtil.success(map);
    }

    @PostMapping("/admin/create")
    public ResultVO createCategory(@RequestParam("list")String listStr){
        List<Category> list=gson.fromJson(listStr,new TypeToken<List<Category>>() {
        }.getType());
        Boolean b=categoryService.saveByList(list);
        return ResultVOUtil.success(null,b);
    }
    @PostMapping("/admin/delete")
    public ResultVO deleteCategory(@RequestParam("id")Integer id){
        return ResultVOUtil.success(null,categoryService.deleteById(id));
    }

}

