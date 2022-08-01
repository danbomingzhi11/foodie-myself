package com.wyf.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wyf.enums.LevelEnums;
import com.wyf.mapper.*;
import com.wyf.popj.*;
import com.wyf.popj.vo.CommentLevelCountsVO;
import com.wyf.popj.vo.ItemCommentVO;
import com.wyf.popj.vo.SearchItemsVO;
import com.wyf.popj.vo.ShopCartVO;
import com.wyf.service.ItemsService;
import com.wyf.utils.DesensitizationUtil;
import com.wyf.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemsServiceImpl implements ItemsService {

    @Autowired
    private ItemsImgMapper itemsImgMapper;

    @Autowired
    private ItemsMapper itemsMapper;

    @Autowired
    private ItemsSpecMapper itemsSpecMapper;

    @Autowired
    private ItemsParamMapper itemsParamMapper;

    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;


    //获取商品信息
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Items selectItemByItemId(String itemId) {
        Items items = itemsMapper.selectByPrimaryKey(itemId);
        return items;
    }

    //获取商品图片信息
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ItemsImg> selectItemImgByItemId(String itemId) {
        Example example = new Example(ItemsImg.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);

        List<ItemsImg> itemsImgList = itemsImgMapper.selectByExample(example);

        return itemsImgList;
    }

    //获取商品Spec信息
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ItemsSpec> selectItemSpecByItemId(String itemId) {
        Example example = new Example(ItemsSpec.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);

        List<ItemsSpec> ItemsSpecList = itemsSpecMapper.selectByExample(example);

        return ItemsSpecList;
    }

    // 获取商品Param信息
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public ItemsParam selectItemParamByItemId(String itemId) {
        Example example = new Example(ItemsParam.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);

        ItemsParam itemsParam = itemsParamMapper.selectOneByExample(example);

        return itemsParam;
    }

    // 获取商品评论信息
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult selectItemComment(String itemId, Integer level, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("itemId", itemId);
        map.put("level", level);

        // 创建PagedGridResult
        PagedGridResult pagedGridResult = new PagedGridResult();

        // 获取所有评论
        List<ItemCommentVO> itemCommentVOList = itemsCommentsMapper.selectItemComment(map);

        // 信息脱敏
        for (ItemCommentVO i : itemCommentVOList) {
            i.setNickname(DesensitizationUtil.commonDisplay(i.getNickname()));
        }

        // 开始分页
        PageHelper.startPage(page, pageSize);
        PageInfo<?> pageInfo = new PageInfo<>(itemCommentVOList);

        // 组装 PagedGridResult
        pagedGridResult.setRows(itemCommentVOList);
        pagedGridResult.setPage(page);
        pagedGridResult.setTotal(pageInfo.getPages());
        pagedGridResult.setRecords(pageInfo.getTotal());

        return pagedGridResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public CommentLevelCountsVO queryCommentCounts(String itemId) {
        CommentLevelCountsVO commentLevel = new CommentLevelCountsVO();
        Integer goodCommentLevelCounts = getCommentCounts(itemId, LevelEnums.good.type);
        Integer normalCommentLevelCounts = getCommentCounts(itemId, LevelEnums.normal.type);
        Integer badCommentLevelCounts = getCommentCounts(itemId, LevelEnums.bad.type);
        Integer totalCommentLevelCounts = goodCommentLevelCounts + normalCommentLevelCounts + badCommentLevelCounts;

        commentLevel.setGoodCounts(goodCommentLevelCounts);
        commentLevel.setNormalCounts(normalCommentLevelCounts);
        commentLevel.setBadCounts(badCommentLevelCounts);
        commentLevel.setTotalCounts(totalCommentLevelCounts);
        return commentLevel;

    }

    // 获取此等级的数量
    Integer getCommentCounts(String itemId, Integer level) {
        ItemsComments itemsComments = new ItemsComments();
        itemsComments.setItemId(itemId);

        if (level != null) {
            itemsComments.setCommentLevel(level);
        }

        return itemsCommentsMapper.selectCount(itemsComments);
    }

    //搜索商品列表 根据关键字 模糊查询 再根据动态sql进行排序
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult selectCatsByKeywordsAndSort(String keyWords, String sort, Integer page, Integer pageSize) {
        // 封装Map
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("keyWords", keyWords);
        paramsMap.put("sort", sort);

        //分页
        PageHelper.startPage(page, pageSize);
        PageInfo<?> pageInfo = new PageInfo<>();

        // 逻辑
        List<SearchItemsVO> searchItemsVO = itemsMapper.selectCatsByKeywordsAndSort(paramsMap);

        // 封装pagedGridResult
        PagedGridResult pagedGridResult = new PagedGridResult();
        pagedGridResult.setPage(page);
        pagedGridResult.setRecords(pageInfo.getTotal());
        pagedGridResult.setTotal(pageInfo.getPages());
        pagedGridResult.setRows(searchItemsVO);

        return pagedGridResult;
    }

    //搜索商品列表 根据catId  再根据动态sql进行排序
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult selectCatsByCatIdAndSort(Integer catId, String sort, Integer page, Integer pageSize) {
        // 封装Map
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("catId", catId);
        paramsMap.put("sort", sort);

        //分页
        PageHelper.startPage(page, pageSize);
        PageInfo<?> pageInfo = new PageInfo<>();

        // 逻辑
        List<SearchItemsVO> searchItemsVO = itemsMapper.selectCatsByCatIdAndSort(paramsMap);

        // 封装pagedGridResult
        PagedGridResult pagedGridResult = new PagedGridResult();
        pagedGridResult.setPage(page);
        pagedGridResult.setRecords(pageInfo.getTotal());
        pagedGridResult.setTotal(pageInfo.getPages());
        pagedGridResult.setRows(searchItemsVO);

        return pagedGridResult;
    }


    // 根据SpecID商品规格Id查询对应的购物车item数据
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ShopCartVO> selectCartsBySpecId(String specId) {
        List<ShopCartVO> shopCartVOList = itemsMapper.selectCartsBySpecId(specId);

        return  shopCartVOList;
    }
}
