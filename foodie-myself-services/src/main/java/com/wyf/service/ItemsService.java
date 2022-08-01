package com.wyf.service;

import com.wyf.popj.Items;
import com.wyf.popj.ItemsImg;
import com.wyf.popj.ItemsParam;
import com.wyf.popj.ItemsSpec;
import com.wyf.popj.bo.ShopcartBO;
import com.wyf.popj.vo.CommentLevelCountsVO;
import com.wyf.popj.vo.ShopCartVO;
import com.wyf.utils.PagedGridResult;
import io.swagger.models.auth.In;


import java.util.List;

public interface ItemsService {

    /**
     * 获取商品信息
     * @param itemId
     * @return
     */
    Items selectItemByItemId(String itemId);

    /**
     * 获取商品图片信息
     * @param itemId
     * @return
     */
    List<ItemsImg> selectItemImgByItemId(String itemId);

    /**
     * 获取商品Spec信息
     * @param itemId
     * @return
     */
    List<ItemsSpec> selectItemSpecByItemId(String itemId);

    /**
     * 获取商品Param信息
     * @param itemId
     * @return
     */
    ItemsParam selectItemParamByItemId(String itemId);

    /**
     * 获取商品评论
     * @param itemId
     * @param level
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult selectItemComment(String itemId, Integer level, Integer page, Integer pageSize);


    /**
     * 分局ItemId 查找所有评论
     * @param itemId
     * @return
     */
    CommentLevelCountsVO queryCommentCounts(String itemId);

    /**
     * 搜索商品列表 根据关键字 模糊查询 再根据动态sql进行排序
     * @param keyWords
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult selectCatsByKeywordsAndSort(String keyWords, String sort, Integer page, Integer pageSize);

    /**
     * 搜索商品列表 根据catId  再根据动态sql进行排序
     * @param catId
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult selectCatsByCatIdAndSort(Integer catId, String sort, Integer page, Integer pageSize);

    /**
     * 根据SpecID商品规格Id查询对应的购物车item数据
     * @param specId
     * @return
     */
    List<ShopCartVO> selectCartsBySpecId(String specId);


}
