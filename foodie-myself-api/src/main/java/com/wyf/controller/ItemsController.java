package com.wyf.controller;

import com.wyf.popj.Items;
import com.wyf.popj.ItemsImg;
import com.wyf.popj.ItemsParam;
import com.wyf.popj.ItemsSpec;
import com.wyf.popj.vo.CommentLevelCountsVO;
import com.wyf.popj.vo.ItemInfoVO;
import com.wyf.popj.vo.ShopCartVO;
import com.wyf.service.ItemsService;
import com.wyf.utils.JsonResult;
import com.wyf.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "商品API", tags = "商品API")
@RestController
@RequestMapping("/items")
public class ItemsController {
    public static final Integer COMMENT_PAGE_SIZE = 10;

    @Autowired
    private ItemsService itemsService;

    // 查询商品详情信息
    @ApiOperation(value = "查询商品详情信息", notes = "查询商品详情信息", httpMethod = "GET")
    @RequestMapping("/info/{itemId}")
    public JsonResult selectAllItemByItemId(@PathVariable String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return JsonResult.errorMsg("缺少itemID");
        }

        // 新建VO
        ItemInfoVO itemInfoVO = new ItemInfoVO();
        // 获取商品信息
        Items itemsResult = itemsService.selectItemByItemId(itemId);
        // 获取商品图片信息
        List<ItemsImg> itemsImgList = itemsService.selectItemImgByItemId(itemId);
        // 获取商品标志信息
        List<ItemsSpec> itemsSpecList = itemsService.selectItemSpecByItemId(itemId);
        // 获取商品标签信息
        ItemsParam itemsParamResult = itemsService.selectItemParamByItemId(itemId);
        // 组装VO
        itemInfoVO.setItem(itemsResult);
        itemInfoVO.setItemImgList(itemsImgList);
        itemInfoVO.setItemSpecList(itemsSpecList);
        itemInfoVO.setItemParams(itemsParamResult);

        return JsonResult.ok(itemInfoVO);
    }

    @ApiOperation(value = "查询商品评论", notes = "查询商品评论", httpMethod = "GET")
    @GetMapping("/comments")
    public JsonResult comments(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @RequestParam String itemId,
            @ApiParam(name = "level", value = "评价等级", required = false)
            @RequestParam Integer level,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize) {

        if (StringUtils.isBlank(itemId)) {
            return JsonResult.errorMsg(null);
        }

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = COMMENT_PAGE_SIZE;
        }
        // 调用接口 以PagedGridResult 形式返回
        PagedGridResult pagedGridResult = itemsService.selectItemComment(itemId, level, page, pageSize);

        return JsonResult.ok(pagedGridResult);
    }

    @ApiOperation(value = "查询商品评价等级", notes = "查询商品评价等级", httpMethod = "GET")
    @GetMapping("/commentLevel")
    public JsonResult commentLevel(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @RequestParam String itemId) {

        if (StringUtils.isBlank(itemId)) {
            return JsonResult.errorMsg(null);
        }

        CommentLevelCountsVO countsVO = itemsService.queryCommentCounts(itemId);

        return JsonResult.ok(countsVO);
    }

    @ApiOperation(value = "搜索商品列表", notes = "搜索商品列表", httpMethod = "GET")
    @GetMapping("/search")
    public JsonResult search(
            @ApiParam(name = "keywords", value = "关键字", required = true)
            @RequestParam String keywords,
            @ApiParam(name = "sort", value = "排序", required = false)
            @RequestParam String sort,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize) {

        if (StringUtils.isBlank(keywords)) {
            return JsonResult.errorMsg(null);
        }

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = COMMENT_PAGE_SIZE;
        }

        PagedGridResult pagedGridResult = itemsService.selectCatsByKeywordsAndSort(keywords, sort, page, pageSize);

        return JsonResult.ok(pagedGridResult);
    }

    @ApiOperation(value = "通过分类id搜索商品列表", notes = "通过分类id搜索商品列表", httpMethod = "GET")
    @GetMapping("/catItems")
    public JsonResult catItems(
            @ApiParam(name = "catId", value = "三级分类id", required = true)
            @RequestParam Integer catId,
            @ApiParam(name = "sort", value = "排序", required = false)
            @RequestParam String sort,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize
    ) {
        if (catId == null) {
            return JsonResult.errorMsg(null);
        }

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = COMMENT_PAGE_SIZE;
        }

        PagedGridResult pagedGridResult = itemsService.selectCatsByCatIdAndSort(catId, sort, page, pageSize);

        return JsonResult.ok(pagedGridResult);
    }

    // 用于用户长时间未登录网站，刷新购物车中的数据（主要是商品价格），类似京东淘宝
    @ApiOperation(value = "根据商品规格ids查找最新的商品数据", notes = "根据商品规格ids查找最新的商品数据", httpMethod = "GET")
    @GetMapping("/refresh")
    public JsonResult refresh(
            @ApiParam(name = "itemSpecIds", value = "拼接的规格ids", required = true, example = "1001,1003,1005")
            @RequestParam String itemSpecIds) {

        if (StringUtils.isBlank(itemSpecIds)) {
            return JsonResult.ok();
        }

        List<ShopCartVO> list = itemsService.selectCartsBySpecId(itemSpecIds);

        return JsonResult.ok(list);
    }
}
