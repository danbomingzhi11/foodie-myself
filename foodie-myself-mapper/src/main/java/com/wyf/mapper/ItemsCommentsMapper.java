package com.wyf.mapper;

import com.sun.xml.internal.bind.v2.TODO;
import com.wyf.my.mapper.MyMapper;
import com.wyf.popj.ItemsComments;
import com.wyf.popj.vo.ItemCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsCommentsMapper extends MyMapper<ItemsComments> {
    // 获取商品评论信息
    List<ItemCommentVO> selectItemComment(@Param("paramsMap") Map<String, Object> paramsMap);

    // 根据itemId查询评论等级
    // TODO: 自己写的sql 一直报错 selectOne  未来一定搞定它
    //CommentLevelVo selectCommentLevelByItemId(String itemId);
}