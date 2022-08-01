package com.wyf.mapper;

import com.wyf.my.mapper.MyMapper;
import com.wyf.popj.Items;
import com.wyf.popj.vo.ItemCommentVO;
import com.wyf.popj.vo.SearchItemsVO;
import com.wyf.popj.vo.ShopCartVO;
import com.wyf.utils.PagedGridResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsMapper extends MyMapper<Items> {
    // 搜索商品列表 根据关键字 模糊查询 再根据动态sql进行排序
    List<SearchItemsVO> selectCatsByKeywordsAndSort(@Param("paramsMap") Map<String, String> paramsMap);

    //搜索商品列表 根据catId  再根据动态sql进行排序
    List<SearchItemsVO> selectCatsByCatIdAndSort(@Param("paramsMap") Map<String, Object> paramsMap);

    // 根据SpecId 查询对应的商品购物车数据
    List<ShopCartVO> selectCartsBySpecId(String specId);
}