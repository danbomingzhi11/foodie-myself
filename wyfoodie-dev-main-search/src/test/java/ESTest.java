import com.wyf.SearchApplication;
import com.wyf.es.pojo.Stu;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SearchApplication.class)
public class ESTest {

    @Autowired
    private ElasticsearchTemplate esTemplate;

    /**
     * 不建议使用 ElasticsearchTemplate 对索引进行管理（创建索引，更新映射，删除索引）
     * 索引就像是数据库或者数据库中的表，我们平时是不会是通过java代码频繁的去创建修改删除数据库或者表的
     * 我们只会针对数据做CRUD的操作
     * 在es中也是同理，我们尽量使用 ElasticsearchTemplate 对文档数据做CRUD的操作
     * 1. 属性（FieldType）类型不灵活
     * 2. 主分片与副本分片数无法设置
     */

    @Test
    public void createIndexStu() {
        Stu stu = new Stu();
        stu.setStuId(1001L);
        stu.setName("田晓帅");
        stu.setAge(20);
        stu.setMoney(18.8f);
        stu.setSign("i am spider major");
        stu.setDescription("i believe");
        IndexQuery indexQuery = new IndexQueryBuilder().withObject(stu).build();
        esTemplate.index(indexQuery);
    }


    @Test
    public void updateIndexStu() {
        Map<String, Object> sourceMap = new HashMap<>();
        sourceMap.put("sign", "xiugaile");

        IndexRequest indexRequest = new IndexRequest();
        indexRequest.source(sourceMap);

        UpdateQuery updateQuery = new UpdateQueryBuilder()
                .withClass(Stu.class)
                .withId("1001")
                .withIndexRequest(indexRequest)
                .build();

        esTemplate.update(updateQuery);
    }

    @Test
    public void searchStu() {

        Pageable pageable = PageRequest.of(0, 2);

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("description", "believe"))
                .withPageable(pageable)
                .build();
        AggregatedPage<Stu> aggregatedPage = esTemplate.queryForPage(searchQuery, Stu.class);

        System.out.println(aggregatedPage.getTotalPages());

        List<Stu> stuList = aggregatedPage.getContent();
        for (Stu i : stuList) {
            System.out.println(i.getAge());
        }
    }

    @Test
    public void highLightStu() {

        String preTag = "<font color='red'>";
        String postTag = "</font>";

        Pageable pageable = PageRequest.of(0, 2);

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("description", "believe"))
                .withPageable(pageable)
                .withHighlightBuilder(new HighlightBuilder().field("description")
                        .preTags(preTag)
                        .postTags(postTag)
                )
                .build();
        AggregatedPage<Stu> aggregatedPage = esTemplate.queryForPage(searchQuery, Stu.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {

                List<Stu> stuListHighlight = new ArrayList<>();

                SearchHits hits = searchResponse.getHits();
                for(SearchHit i : hits) {
                    HighlightField highlightField = i.getHighlightFields().get("description");
                    String description = highlightField.getFragments()[0].toString();

                    Object stuId = (Object) i.getSourceAsMap().get("stuId");
                    String name = (String) i.getSourceAsMap().get("name");
                    Integer age = (Integer) i.getSourceAsMap().get("age");
                    String sign = (String) i.getSourceAsMap().get("sign");
                    Float money = (Float) i.getSourceAsMap().get("money");

                    Stu stuHL = new Stu();
                    stuHL.setStuId(Long.valueOf(stuId.toString()));
                    stuHL.setDescription(description);
                    stuHL.setName(name);
                    stuHL.setAge(age);
                    stuHL.setSign(sign);
                    stuHL.setMoney(money);

                    stuListHighlight.add(stuHL);
                }

                if (stuListHighlight.size() > 0) {
                    return  new AggregatedPageImpl<>((List<T>)stuListHighlight);
                }

                return null;
            }
        });

        System.out.println(aggregatedPage.getTotalPages());

        List<Stu> stuList = aggregatedPage.getContent();
        for (Stu i : stuList) {
            System.out.println(i.getAge());
        }
    }
}
