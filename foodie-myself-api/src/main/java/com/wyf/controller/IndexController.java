package com.wyf.controller;

import com.wyf.enums.IsShow;
import com.wyf.popj.Carousel;
import com.wyf.popj.Category;
import com.wyf.popj.vo.SelectAllGrade;
import com.wyf.popj.vo.SixItemsVo;
import com.wyf.service.CarouselService;
import com.wyf.service.CategoryService;
import com.wyf.service.ItemsService;
import com.wyf.utils.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.util.List;

@Api(value = "首页相关接口", tags = {"首页相关接口"})
@RestController
@RequestMapping("/index")
public class IndexController {

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private CarouselService carouselService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ItemsService itemsService;

    // 首页轮播图
    @ApiOperation(value = "轮播图", notes = "轮播图", httpMethod = "GET")
    @RequestMapping("/carousel")
    public JsonResult carousel() {
        List<Carousel> carouselList = carouselService.getCarousel(IsShow.YES.isShow);

        return JsonResult.ok(carouselList);
    }

    // 查询所有一级分类
    @ApiOperation(value = "一级分类", notes = "查询所有一级分类", httpMethod = "GET")
    @RequestMapping("/cats")
    public JsonResult firstGradeCats() {
        List<Category> categoryList = categoryService.selectFirstGradeCarts();

        return JsonResult.ok(categoryList);
    }

    // 根据一级分类的id 查询商品信息 并且筛选前6条
    @ApiOperation(value = "查询每个一级分类下的6条商品数据", notes = "查询每个一级分类下的6条商品数据", httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public JsonResult selectSixNewItemsById(
            @ApiParam(name = "rootCatId", value = "一级分类id", required = true)
            @PathVariable Integer rootCatId) {
        List<SixItemsVo> sixItemsVo = categoryService.selectSixItemsByRootCatId(rootCatId);

        return JsonResult.ok(sixItemsVo);
    }

    // 根据一级分类的id 查询所有子分类内容
    @ApiOperation(value = "查询所有子分类内容", notes = "查询所有子分类内容", httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public JsonResult selectAllGradeById(
            @ApiParam(name = "rootCatId", value = "一级分类id", required = true)
            @PathVariable Integer rootCatId) {
        List<SelectAllGrade> selectAllGrade = categoryService.selectAllGradeById(rootCatId);

        return JsonResult.ok(selectAllGrade);
    }


}
