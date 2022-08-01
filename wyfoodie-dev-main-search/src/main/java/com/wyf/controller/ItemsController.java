package com.wyf.controller;


import com.wyf.service.ItemsESService;
import com.wyf.utils.JsonResult;
import com.wyf.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("items")
public class ItemsController {

    @Autowired
    private ItemsESService itemsESService;
    @Autowired
    private ElasticsearchTemplate esTemplate;

    @GetMapping("/hello")
    public Object hello() {
        return "Hello Elasticsearch~";
    }

    @GetMapping("/es/search")
    public JsonResult search(
            String keywords,
            String sort,
            Integer page,
            Integer pageSize) {

        if (StringUtils.isBlank(keywords)) {
            return JsonResult.errorMsg(null);
        }

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = 20;
        }

        page --;

        PagedGridResult grid = itemsESService.searhItems(keywords,
                sort,
                page,
                pageSize);

        return JsonResult.ok(grid);
    }
}



