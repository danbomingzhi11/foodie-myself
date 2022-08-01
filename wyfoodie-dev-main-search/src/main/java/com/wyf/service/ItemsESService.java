package com.wyf.service;

import com.wyf.utils.PagedGridResult;

public interface ItemsESService {
    public PagedGridResult searhItems(String keywords,
                                      String sort,
                                      Integer page,
                                      Integer pageSize);

}
