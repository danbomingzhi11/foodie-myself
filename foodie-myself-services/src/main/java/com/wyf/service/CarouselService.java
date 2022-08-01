package com.wyf.service;

import com.wyf.popj.Carousel;
import java.util.List;

public interface CarouselService {
    /***
     * 获取所有轮播图
     * @return
     */
    List<Carousel> getCarousel(Integer isShow);
}
