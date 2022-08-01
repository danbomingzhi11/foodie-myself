package com.wyf.service.impl;

import com.wyf.mapper.CarouselMapper;
import com.wyf.popj.Carousel;
import com.wyf.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class CarouselServiceImpl implements CarouselService {

    @Autowired
    private CarouselMapper carouselMapper;


    // 获取所有轮播图
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Carousel> getCarousel(Integer isShow) {
        Example example = new Example(Carousel.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isShow", isShow);
        example.orderBy("sort").desc();

        List<Carousel> carousel = carouselMapper.selectByExample(example);

        return  carousel;
    }
}
